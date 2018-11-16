package com.rohan.stockapp.json;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "count", "status", "comments" })
public class Status {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("status")
	private String status;
	@JsonProperty("comments")
	private String comments;
	@JsonProperty("url")
	private String url;


	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Status() {
	}

	/**
	 * 
	 * @param count
	 * @param status
	 * @param comments
	 */
	public Status(Integer count, String status, String comments, String url) {
		super();
		this.count = count;
		this.status = status;
		this.comments = comments;
		this.url = url;
	}

	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	public Status withCount(Integer count) {
		this.count = count;
		return this;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public Status withStatus(String status) {
		this.status = status;
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

	public Status withComments(String comments) {
		this.comments = comments;
		return this;
	}
	
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public Status withUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("count", count).append("status", status).append("comments", comments).append("url", url)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(count).append(status).append(comments).append(url).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Status) == false) {
			return false;
		}
		Status rhs = ((Status) other);
		return new EqualsBuilder().append(count, rhs.count).append(status, rhs.status).append(comments, rhs.comments)
				.isEquals();
	}

}
