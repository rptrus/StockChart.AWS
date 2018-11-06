package com.rohan.stockapp.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SampleStocks {
	BHP(32.00),
	WOW(24.00),
	ALL(4.00),
	CBA(67.00),
	JBH(12.00),
	WES(33.00),
	RIO(46.00),
	TAH(4.00),
	RHC(50.00),
	DDR(5.00),
	ANZ(20.00),
	NAB(21.00),
	FLT(13.00),
	AMP(1.00),
	BOQ(19.00),
	TLS(2.00),
	DEG(1.00),
	HIG(1.00),
	COH(102.00),
	CPU(20.00),
	CSR(99.00),
	STO(2.00),
	WOR(4.00),
	WPL(12.00);
	
	SampleStocks(double samplePrice) {
		this.setSameplePrice(new BigDecimal(samplePrice));
	}
		
	private BigDecimal sameplePrice;
	
	private static final List<SampleStocks> VALUES = Arrays.asList(values());
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static SampleStocks randomStock()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	}
	
	public SampleStocks randomStock2()  {		
	    Collections.shuffle(VALUES);
		return VALUES.get(0);
	}


	public BigDecimal getSameplePrice() {
		return sameplePrice;
	}

	public void setSameplePrice(BigDecimal sameplePrice) {
		this.sameplePrice = sameplePrice;
	}
}
