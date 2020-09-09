package com.stockmarket.datasheet.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "StockPrice2")
public class StockPrice {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "companyid")
	private long companyid;
	
	@Column(name = "stockexchange")
	private String stockexchange;
	
	@Column(name = "price")
	private long price;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "time")
	private String time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(long companyname) {
		this.companyid = companyname;
	}

	public String getStockexchange() {
		return stockexchange;
	}

	public void setStockexchange(String stockexchange) {
		this.stockexchange = stockexchange;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public StockPrice(long id, long companyid, String stockexchange, long price, Date date, String time) {
		super();
		this.id = id;
		this.companyid = companyid;
		this.stockexchange = stockexchange;
		this.price = price;
		this.date = date;
		this.time = time;
	}

	public StockPrice() {
		super();
	}
	
	
	
}
