package com.stockmarket.datasheet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockmarket.datasheet.dto.ObjectDTO;

public interface ObjectRepository extends JpaRepository<ObjectDTO, Long> {
}
