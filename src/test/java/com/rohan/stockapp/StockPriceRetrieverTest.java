package com.rohan.stockapp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import com.rohan.stockapp.service.StockPriceRetriever;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
@EnableAsync
public class StockPriceRetrieverTest {
	
	@Autowired
	StockPriceRetriever stockPriceRetriever;

	@Test
	public void retrieverTest() {
		// need to poll 1 by 1
		Future<String> a = stockPriceRetriever.retrieveQuote("VTS");
		Future<String> b = stockPriceRetriever.retrieveQuote("BHP");
		Future<String> c = stockPriceRetriever.retrieveQuote("WBC");
		Future<String> d = stockPriceRetriever.retrieveQuote("KGN");
		Future<String> e = stockPriceRetriever.retrieveQuote("RHC");
		Future<String> f = stockPriceRetriever.retrieveQuote("FLT");

		try {
			System.out.println("VTS->"+ a.get());
			System.out.println("BHP->"+ b.get());		
			System.out.println("WBC->"+ c.get());
			System.out.println("KGN->"+ d.get());
			System.out.println("RHC->"+ e.get());
			System.out.println("FLT->"+ f.get());
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
	}


}
