package com.stockmarket.datasheet.request;

import javax.validation.constraints.NotBlank;


public class DetailsRequest {
	@NotBlank
	private String companyname;

	@NotBlank
	private String fromDate;
	
	@NotBlank
	private String toDate;

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	
}
