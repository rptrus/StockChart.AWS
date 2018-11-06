package com.rohan.stockapp.json;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "stocks", "comments" })
public class StockSet {

	@JsonProperty("stocks")
	private List<Stock> stocks = null;
	@JsonProperty("comments")
	private String comments;

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public StockSet() {
	}

	/**
	 * 
	 * @param stocks
	 * @param comments
	 */
	public StockSet(List<Stock> stocks, String comments) {
		super();
		this.stocks = stocks;
		this.comments = comments;
	}

	@JsonProperty("stocks")
	public List<Stock> getStocks() {
		return stocks;
	}

	@JsonProperty("stocks")
	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public StockSet withStocks(List<Stock> stocks) {
		this.stocks = stocks;
		return this;
	}

	@JsonProperty("comments")
	public String getComments() {
		return comments;
	}

	@JsonProperty("comments")
	public void setComments(String comments) {
		this.comments = comments;
	}

	public StockSet withComments(String comments) {
		this.comments = comments;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("stocks", stocks).append("comments", comments).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(stocks).append(comments).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof StockSet) == false) {
			return false;
		}
		StockSet rhs = ((StockSet) other);
		return new EqualsBuilder().append(stocks, rhs.stocks).append(comments, rhs.comments).isEquals();
	}

}
