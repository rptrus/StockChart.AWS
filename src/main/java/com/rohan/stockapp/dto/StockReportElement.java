package com.rohan.stockapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rohan.stockapp.entity.Holding;

public class StockReportElement extends BaseReportElement {
	
	public StockReportElement() {
		super();
	}
	
	public StockReportElement(LocalDateTime dateAcquired, String code, BigDecimal acquiredPrice, BigDecimal nowPrice, Integer numberOfUnits) {
		super(dateAcquired, code, acquiredPrice, nowPrice, numberOfUnits);
	}

	// set to true if the stock has already been sold
	public boolean disposed() {
		return false;
	}
	
	public boolean active() {
		return true;
	}

}
