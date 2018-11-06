package com.rohan.stockapp.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

// This may need to change if Yahoo changes their page data!
@Component
public class RegexManager {
	
	public String getLastPrice(String page, String code) {
		Pattern pattern = Pattern.compile(".*?"+code+"\\.AX.*?regularMarketPrice.*?fmt\":\"(\\d+\\.\\d+)\"}.*",Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(page);
		boolean matches = matcher.matches();
		if (matches) {			
			return matcher.group(1);
			
		}
		return "unknown";		
	}
	
	public String getIndustry(String page) {
		Pattern pattern = Pattern.compile(".*?\"industry\":\"(.*)\"[,]?.*recommendationTrend\".*?",Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(page);
		boolean matches = matcher.matches();
		if (matches) {
			return matcher.group(1);
		}
		return "unknown";
	}


}
