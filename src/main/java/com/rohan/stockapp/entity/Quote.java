package com.rohan.stockapp.entity;

// a quote for a particular code. Rest data will be populated in to this class

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "QUOTE")
public class Quote {
	
	public Quote(String code, BigDecimal nowPrice) {
		super();
		this.code = code;
		this.nowPrice = nowPrice;
	}
	
	public Quote() {};

	@Id
	@GeneratedValue
	Long id;
	
	String code;
	
	BigDecimal nowPrice;
	
	@OneToOne(mappedBy="quote")
	//@JoinColumn(name = "fkid")
	Holding holding;
	
	public Holding getHolding() {
		return holding;
	}
	
	public void setHolding(Holding holding) {
		this.holding = holding;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
	}


}
