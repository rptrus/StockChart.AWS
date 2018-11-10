package com.rohan.stockapp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rohan.stockapp.dto.StockReportElement;
import com.rohan.stockapp.entity.Holding;
import com.rohan.stockapp.entity.Quote;
import com.rohan.stockapp.entity.User;
import com.rohan.stockapp.json.Status;
import com.rohan.stockapp.json.Stock;
import com.rohan.stockapp.json.StockAdd;
import com.rohan.stockapp.json.StockSet;
import com.rohan.stockapp.repository.HoldingRepository;
import com.rohan.stockapp.repository.UserRepository;

@RestController
public class Processor {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${filename}")
	private String fileName;
	
	@Value("${bucket:noworriesmate}")
	private String bucketName;
	
	@Autowired
	private ObjectMapper objectmapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ChartConstruction chart;
	
	@Autowired
	private StockPriceRetriever stockPriceRetriever;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HoldingRepository holdingRepository;

	public boolean addStock(Status status, String json, String username, String password) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		StockAdd stockAdd = objectmapper.readValue(json, StockAdd.class);
		User theUser = userService.getUser(username);
		Set<Holding> userHoldings = userService.getUserHoldings(theUser); // A
		for (Holding holding: userHoldings) {
			if (holding.getCode().equals(stockAdd.getStock())) {				
				logger.error("Stock code {} already exists!");
				return false;
			}
		}
		System.out.println(stockAdd);
		Holding newHolding = new Holding();
		newHolding.setCode(stockAdd.getStock());
		newHolding.setDateAcquired(LocalDateTime.ofInstant(Instant.parse(stockAdd.getDateAdded()), ZoneId.systemDefault()));
		newHolding.setPrice(new BigDecimal(stockAdd.getPrice()));
		newHolding.setNumberOfUnits(stockAdd.getNumberOfUnits());
		newHolding.setUser(theUser);
		userHoldings.add(newHolding);
		constructChart(status, toStockElementList(userHoldings, getLatestPrices(userHoldings)), fileName);
		return true;
	}
	
	public void addStockMulti(Status status, String json, String username, String password) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		int numberOfHoldings = 0;
		StockSet stockSet = objectmapper.readValue(json, StockSet.class);
		String email = stockSet.getComments();
		System.out.println(stockSet);
		User theUser = userService.getUser(username);
		Set<Holding> userHoldings = userService.getUserHoldings(theUser);
		// filter out the things that don't match, leaving the things that are left=new O(N^2) but OK for small porfolios
		List<Stock> myNewList = stockSet.getStocks().stream().filter(jStock-> userHoldings.stream().noneMatch(dbStock -> dbStock.getCode().equals(jStock.getStock()))  ).collect(Collectors.toList());
		List<Holding> myRemList = userHoldings.stream().filter(jStock-> stockSet.getStocks().stream().noneMatch(dbStock -> dbStock.getStock().equals(jStock.getCode()))  ).collect(Collectors.toList());

		for (Stock newStock : myNewList) {
			Instant addDate = Instant.parse(newStock.getDateAdded());
			LocalDateTime newDate = LocalDateTime.ofInstant(addDate, ZoneId.systemDefault());
			Holding newHolding = new Holding();
			newHolding.setCode(newStock.getStock());
			newHolding.setDateAcquired(newDate);
			newHolding.setPrice(new BigDecimal(newStock.getPrice()));
			newHolding.setNumberOfUnits(newStock.getNumberOfUnits());
			newHolding.setUser(theUser);
			userHoldings.add(newHolding);
			holdingRepository.save(newHolding);
			holdingRepository.flush();
			numberOfHoldings++;
		}
		for (Holding deleteStock : myRemList) {
			java.util.Iterator<Holding> userHoldingsIterator = userHoldings.iterator();
			while (userHoldingsIterator.hasNext()) {
				Holding holding = userHoldingsIterator.next();
				if (holding.getCode().equals(deleteStock.getCode())) {
					System.out.println("We have a match - now we can delete "+holding.getCode());
					userHoldingsIterator.remove();
					holdingRepository.save(holding);
					numberOfHoldings--;
				}
			}			
		}
		List<StockReportElement> stockElementList = toStockElementList(userHoldings, getLatestPrices(userHoldings));
		constructChart(status, stockElementList, fileName, email);
		status.setCount(numberOfHoldings);		
	}
	
	// We will rectify the DB elements (that have been sync'd by JSON together with the pulled stock prices  
	private List<StockReportElement> toStockElementList(Set<Holding> userHoldings, Map<String, BigDecimal> latestStockPrices) {
		List<StockReportElement> stockList = new ArrayList<>();
		for (Holding holding : userHoldings) {
			StockReportElement stockReportElement = new StockReportElement();
			stockReportElement.setCode(holding.getCode());
			stockReportElement.setAcquiredPrice(holding.getPrice());
			stockReportElement.setDateAcquired(holding.getDateAcquired());
			stockReportElement.setNumberOfUnits(holding.getNumberOfUnits());
			stockReportElement.setCurrentPrice(latestStockPrices.get(holding.getCode()));
			stockList.add(stockReportElement);
		}
		return stockList;
	}
	
	public boolean checkIfUserExists(String username, String password) {
		User aUser = userService.getUser(username);
		return (aUser != null && StringUtils.isNotEmpty(aUser.getPassword())
				&&
				aUser.getPassword().equals(Utils.md5Hash(password)));
	}
	
	private void constructChart(Status status, List<StockReportElement> stockElementList, String fullPathFilename) {
		constructChart(status, stockElementList, fullPathFilename, "r.truscott@gmail.com"); // fix later
	}
	
	private void constructChart(Status status, List<StockReportElement> stockElementList, String fullPathFilename, String email) {
		synchroniseCurrentPrices(stockElementList); // can remove this
		int size = stockElementList.size();
		if ( size > 8 ) // we will silently excise more than 8 items
		    stockElementList.subList(8, size).clear();
		chart.makePDFChart(status, stockElementList, fullPathFilename, bucketName, email);
	}
	
	// If we don't have a latest price, then what we will do is set it to the current price and flag an alert
	private void synchroniseCurrentPrices(List<StockReportElement> stockElementList) {
			for (StockReportElement stock : stockElementList) {
					if (stock.getCurrentPrice() == null) 
							stock.setCurrentPrice(stock.getAcquiredPrice()); // hack. 0% growth
			}
	}

	// Populate the database for testing - move to junit later
	public void fillTheDatabase() {
		List<Map<String, Object>> holdingList = jdbcTemplate.queryForList("select * from holding");
		System.out.println(holdingList);
		
		User user = new User();
		user.setBio("The founding user");
		user.setDateJoined(LocalDateTime.of(2018, 10, 1, 12, 0));
		user.setUsername("rohan");
		user.setPassword(Utils.md5Hash("Pa55w0rd"));

		// 1
		Holding holding1 = new Holding(LocalDateTime.now(), LocalDateTime.now(), "VAS", new BigDecimal(1.23),10);
		Quote quote1 = new Quote("VAS", new BigDecimal(1.99));
		holding1.setQuote(quote1);
		holding1.setUser(user);
		quote1.setHolding(holding1);
		
		// 2
		Holding holding2 = new Holding(LocalDateTime.now(), LocalDateTime.now(), "WBC", new BigDecimal(26.99),20);
		Quote quote2 = new Quote("WBC", new BigDecimal(28.99));
		holding2.setQuote(quote2);
		holding2.setUser(user);
		quote2.setHolding(holding2);
		
		// 3
		Holding holding3 = new Holding(LocalDateTime.now(), LocalDateTime.now(), "JBH", new BigDecimal(16.99),20);
		Quote quote3 = new Quote("JBH", new BigDecimal(20.99));
		holding3.setQuote(quote3);
		holding3.setUser(user);
		quote3.setHolding(holding3);
		
		Set<Holding> stockSet = new HashSet<>();
		stockSet.add(holding1);
		stockSet.add(holding2);
		stockSet.add(holding3);
		user.setHoldings(stockSet);
		
		userRepository.save(user);
		
		User user2 = new User();
		user2.setBio("Test user");
		user2.setDateJoined(LocalDateTime.of(2018, 11, 11, 12, 0));
		user2.setUsername("testuser");
		user2.setPassword(Utils.md5Hash("testpass"));
		
		userRepository.save(user2);

	}
	
	@Transactional
	public void deleteStock(String json, String username, String password) throws JsonParseException, JsonMappingException, IOException {
		User theUser = userService.getUser(username); //DB
		Set<Holding> userHoldings = userService.getUserHoldings(theUser);
		int size = userHoldings.size();
		if (size >= 1) {
			StockSet stockSet = objectmapper.readValue(json, StockSet.class);
			Stock stock = stockSet.getStocks().get(0); // we only have one
			Holding holding2Delete = holdingRepository.findByCode(stock.getStock());
			if (userHoldings.remove(holding2Delete))
				holdingRepository.save(holding2Delete);
			logger.info("Delete operation performed");
		} else if (size == 0) {
			logger.error("Nothing to delete!");
		}
	}
	
	public void getStockPortfolio(Status status, String username, String password) throws InterruptedException, ExecutionException {
		getStockPortfolio(status, username, password, null);
	}

	public void getStockPortfolio(Status status, String username, String password, Set<Holding> userHoldings) throws InterruptedException, ExecutionException {
		int num = 0; 
		if (userHoldings == null) {
			User theUser = userService.getUser(username);
			userHoldings = userService.getUserHoldings(theUser);
		}
		num = userHoldings.size();
		List<StockReportElement> stockElementList = toStockElementList(userHoldings, getLatestPrices(userHoldings));
		constructChart(status, stockElementList, fileName);
		status.setCount(num);
	}
	
	private Map<String, BigDecimal> getLatestPrices(Set<Holding> holdings) throws InterruptedException, ExecutionException {
		Map<String, BigDecimal> returnedPrices = new HashMap<String, BigDecimal>();
		Map<String, Future<String>> waitForPrices = new HashMap<String, Future<String>>();
		for (Holding stock: holdings) {
			Future<String> currentPrice = stockPriceRetriever.retrieveQuote(stock.getCode());
			waitForPrices.put(stock.getCode(), currentPrice);
		}
		// now we just get all our values that have been computed in parallel 
		for (Map.Entry<String, Future<String>> entry : waitForPrices.entrySet())
		{			
			if (NumberUtils.isCreatable(entry.getValue().get())) 
					returnedPrices.put(entry.getKey(), new BigDecimal(entry.getValue().get()));
			else { // "unknown" or any other non numeric value
				logger.error("Non numeric price returned. Skipped."); // Good enough measure for now.
				continue;
			}
		}
		return returnedPrices;
	}
	
}
