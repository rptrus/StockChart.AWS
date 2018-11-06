package com.rohan.stockapp.entity;

// a set of codes for one user in particular

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HOLDING")
public class Holding {

	public Holding(LocalDateTime dateAcquired, LocalDateTime dateDisposed, String code, BigDecimal price, Integer numberOfUnits) {
		super();
		this.dateAcquired = dateAcquired;
		this.dateDisposed = dateDisposed;
		this.code = code;
		this.price = price;
		this.numberOfUnits = numberOfUnits;
	}
	
	public Holding() {}

	@Id
	@GeneratedValue
	public Long id;
		
	LocalDateTime dateAcquired;
	
	LocalDateTime dateDisposed;
	
	String code;
		
	BigDecimal price;
	
	Integer numberOfUnits;
	
	@OneToOne(cascade = CascadeType.ALL)
	Quote quote;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
	User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	//@OneToOne(cascade = CascadeType.ALL)
	public Quote getQuote() {
		return quote;
	}
	
	public void setQuote(Quote quote) {
		this.quote = quote;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDateAcquired() {
		return dateAcquired;
	}

	public void setDateAcquired(LocalDateTime dateAcquired) {
		this.dateAcquired = dateAcquired;
	}

	public LocalDateTime getDateDisposed() {
		return dateDisposed;
	}

	public void setDateDisposed(LocalDateTime dateDisposed) {
		this.dateDisposed = dateDisposed;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getNumberOfUnits() {
		return numberOfUnits;
	}
	
	public void setNumberOfUnits(Integer numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

}
