package com.rohan.stockapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
 
//URI("https://finance.yahoo.com/quote/DDR.AX");

@Component
public class StockPriceRetriever {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RegexManager regexManager;
	
	@Value("${writefile:true}")
	Boolean writeFile;
	
	public StockPriceRetriever(RegexManager regexManager) {
		this.regexManager = regexManager;
	}
	
	@Async
	public Future<String> retrieveQuote(String code) {
		
		StopWatch stopwatch = new StopWatch();
		String aQuotePage = null;
		String quote = null;
		
		try {
		UriComponents uri = UriComponentsBuilder.newInstance()
			      .scheme("https").host("finance.yahoo.com").path("/quote/").path(code).path(".AX").build();
		stopwatch.start();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity(uri.toString(), String.class);
		logger.debug(result.getBody());
		aQuotePage = result.getBody();
		quote = regexManager.getLastPrice(aQuotePage, code);
		stopwatch.stop();
		System.out.println("["+code+"] Seconds: "+stopwatch.getTotalTimeSeconds());
		if (writeFile)
			Files.write(Paths.get("c:\\temp\\output"+code+".txt"), aQuotePage.getBytes());
		} catch (IOException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AsyncResult<String>(quote);
	}
}
