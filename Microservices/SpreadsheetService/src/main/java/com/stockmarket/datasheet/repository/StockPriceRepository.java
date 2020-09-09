package com.stockmarket.datasheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.datasheet.model.StockPrice;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
}
