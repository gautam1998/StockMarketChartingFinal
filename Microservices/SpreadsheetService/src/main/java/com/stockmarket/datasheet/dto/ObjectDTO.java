package com.stockmarket.datasheet.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock_price")
public class ObjectDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_code")
    private String companyCode;
    @Column(name = "stock_exchange")
    private String stockExchange;
    @Column(name = "price")
    private Double price;
    @Column(name = "change_date")
    private Date changeDate;
    @Column(name = "change_timestamp")
    private Date changeTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Date getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Date changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }
}
