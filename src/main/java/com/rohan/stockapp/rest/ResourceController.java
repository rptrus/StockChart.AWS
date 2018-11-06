package com.rohan.stockapp.rest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mysql.jdbc.log.Log;
import com.rohan.stockapp.config.AmazonClient;
import com.rohan.stockapp.entity.Holding;
import com.rohan.stockapp.enums.SampleStocks;
import com.rohan.stockapp.enums.StatusFlags;
import com.rohan.stockapp.json.Status;
import com.rohan.stockapp.service.Processor;

@RestController
public class ResourceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AmazonClient amazonClient;
	
	@Autowired
	Processor processor;
	
	@GetMapping(value="/")
	public ModelAndView get() {
		 ModelAndView modelAndView = new ModelAndView();
		    modelAndView.setViewName("index.html");
		    return modelAndView;
	}
	
	@GetMapping(value="/get", produces = "application/json")
	public ResponseEntity<Status> get(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password) throws InterruptedException, ExecutionException {
		Status status = new Status();
		HttpStatus requestStatus = HttpStatus.OK;
		int num = processor.getStockPortfolio(username, password);
		status.setComments("Processed request successfully");
		return new ResponseEntity<Status>(status, requestStatus);
	}

	// add to existing holdings already saved
	@PostMapping(value="/add", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> add(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password, @RequestBody String json) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		Status status = new Status();
		boolean ok = processor.addStock(json, username, password);
		HttpStatus requestStatus = HttpStatus.OK;
		if (ok) {
			status.setComments("Processed added stock");
		} else {
			requestStatus = HttpStatus.BAD_REQUEST;
			status.setComments("Stock already exists!");
		}
		System.out.println(json);
		return new ResponseEntity<Status>(status, requestStatus);
	}
	
	// synchronise to the JSON provided
	@PostMapping(value="/addmulti", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> addmulti(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password, @RequestBody String json) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		Status status = new Status();
		HttpStatus requestStatus;
		status.setComments("Processed stock add request successfully");
		if (processor.checkIfUserExists(username)) {
			logger.error("The user already exists - must use PUT");
			status.setComments("User already Exists - send a PUT request instead");
			status.setStatus(StatusFlags.FAIL.name());
			requestStatus =  HttpStatus.BAD_REQUEST;			
		}
		else {
			int num = processor.addStockMulti(json, username, password);
			status.setCount(num);
			requestStatus =  HttpStatus.OK;
		} 
		System.out.println(json);
		return new ResponseEntity<Status>(status, requestStatus);
	}

	@PutMapping(value="/addmulti", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> modify(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password, @RequestBody String json) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		Status status = new Status();
		HttpStatus requestStatus = HttpStatus.OK;
		int num = processor.addStockMulti(json, username, password);
		status.setComments("Processed stock add request successfully");
		status.setCount(num);
		System.out.println(json);
		return new ResponseEntity<Status>(status, requestStatus);
		
	}
	
	@DeleteMapping(value="/delete", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> delete(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password, @RequestBody String json) throws JsonParseException, JsonMappingException, IOException {
		Status status = new Status();
		HttpStatus requestStatus = HttpStatus.OK;
		status.setComments("Processed delete request successfully [negative count permissable on delete]");
		processor.deleteStock(json, username, password);
		System.out.println(json);
		return new ResponseEntity<Status>(status, requestStatus);
		
	}
	
	@GetMapping(value="/random", produces = "application/json")
	public ResponseEntity<Status> random(@RequestHeader("X-username") String username, @RequestHeader("X-password") String password, @RequestParam(value="howmany", required=true) String howMany) throws JsonParseException, JsonMappingException, IOException, InterruptedException, ExecutionException {
		Status status = new Status();
		HttpStatus requestStatus = HttpStatus.OK;
		Set<Holding> holdingSet = new HashSet<>(); // use this detached from a database and inject it
		Random numUnits = new Random(System.currentTimeMillis());
		for (int i = 0; i < Integer.valueOf(howMany); i++) {
			SampleStocks theStock = SampleStocks.randomStock();
			Holding holding = new Holding();
			holding.setCode(theStock.name());
			holding.setDateAcquired(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
			holding.setDateDisposed(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
			holding.setNumberOfUnits(numUnits.nextInt(50));
			holding.setPrice(theStock.getSameplePrice());
			holdingSet.add(holding);
		}
		int num = processor.getStockPortfolio(username, password, holdingSet);		
		status.setComments("Processed request successfully");
		status.setCount(num);
		return new ResponseEntity<Status>(status, requestStatus);
	}
	
	@PostConstruct
	public void filler() {
		processor.fillTheDatabase();
	}

}
