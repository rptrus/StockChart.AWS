package com.rohan.stockapp.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "stock", "dateAdded", "price", "numberOfUnits" })
public class Stock {

	@JsonProperty("stock")
	private String stock;
	@JsonProperty("dateAdded")
	private String dateAdded;
	@JsonProperty("price")
	private Float price;
	@JsonProperty("numberOfUnits")
	private Integer numberOfUnits;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Stock() {
	}

	/**
	 * 
	 * @param price
	 * @param stock
	 * @param dateAdded
	 */
	public Stock(String stock, String dateAdded, Float price, Integer numberOfUnits) {
		super();
		this.stock = stock;
		this.dateAdded = dateAdded;
		this.price = price;
		this.numberOfUnits = numberOfUnits;
	}

	@JsonProperty("stock")
	public String getStock() {
		return stock;
	}

	@JsonProperty("stock")
	public void setStock(String stock) {
		this.stock = stock;
	}

	public Stock withStock(String stock) {
		this.stock = stock;
		return this;
	}

	@JsonProperty("dateAdded")
	public String getDateAdded() {
		return dateAdded;
	}

	@JsonProperty("dateAdded")
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Stock withDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
		return this;
	}

	@JsonProperty("price")
	public Float getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(Float price) {
		this.price = price;
	}

	public Stock withPrice(Float price) {
		this.price = price;
		return this;
	}
	
	@JsonProperty("numberOfUnits")
	public Integer getNumberOfUnits() {
	return numberOfUnits;
	}

	@JsonProperty("numberOfUnits")
	public void setNumberOfUnits(Integer numberOfUnits) {
	this.numberOfUnits = numberOfUnits;
	}

	public Stock withNumberOfUnits(Integer numberOfUnits) {
	this.numberOfUnits = numberOfUnits;
	return this;
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this).append("stock", stock).append("dateAdded", dateAdded).append("price", price).append("numberOfUnits", numberOfUnits)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(price).append(stock).append(dateAdded).append(numberOfUnits).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Stock) == false) {
			return false;
		}
		Stock rhs = ((Stock) other);
		return new EqualsBuilder().append(price, rhs.price).append(stock, rhs.stock).append(dateAdded, rhs.dateAdded).append(numberOfUnits, rhs.numberOfUnits)
				.isEquals();
	}

}