package com.stockmarket.datasheet.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stockmarket.datasheet.helper.ExcelHelper;
import com.stockmarket.datasheet.model.StockPrice;
import com.stockmarket.datasheet.repository.StockPriceRepository;

@Service
public class ExcelService {
  @Autowired
  StockPriceRepository repository;

  public List<StockPrice> save(MultipartFile file) {
	  System.out.println("hello 4");
    try {
    	System.out.println("hello 5");
      List<StockPrice> stock = ExcelHelper.excelToTutorials(file.getInputStream());
      repository.saveAll(stock);
      return stock;
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }

  public List<StockPrice> getAllCompanies() {
	System.out.println("hello");
	System.out.println(repository.findAll());
    return repository.findAll();
  }
  
  public Optional<StockPrice> getAllCompaniesById(Long Id) {
	    return repository.findById(Id);
	  }
  
  
  /*
  public List<StockPrice> getCompanyData(long id){
	 // return repository.fi);
  }
  */
}
