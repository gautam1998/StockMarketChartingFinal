package com.stockmarket.datasheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import com.stockmarket.datasheet.dto.ObjectDTO;
import com.stockmarket.datasheet.helper.ExcelHelper;
import com.stockmarket.datasheet.message.ResponseMessage;
import com.stockmarket.datasheet.model.Header;
import com.stockmarket.datasheet.model.StockPrice;
import com.stockmarket.datasheet.service.ExcelService;
import com.stockmarket.datasheet.service.MainService;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/excel")
public class MainController {
    @Autowired
    MainService mainService;
    
    @Autowired
    ExcelService fileService;

    
    @GetMapping("/all")
    @LoadBalanced
	public String allAccess() {
	  System.out.println("hello1");
		return "Public Content.";
	}

    @PostMapping("/upload")
    @LoadBalanced
    public ResponseEntity<Header> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        mainService.uploadExcel(file);
        List<ObjectDTO> stockPriceList = mainService.importExcelData2DB(file);
        return new ResponseEntity<>(mainService.getUploadSummary(stockPriceList), HttpStatus.OK);
    }
    
    @PostMapping("/upload2")
    @LoadBalanced
    public ResponseEntity<List<StockPrice>> uploadFile(@RequestParam("file") MultipartFile file) {
    	System.out.println("hello 1");
      String message = "";

      if (ExcelHelper.hasExcelFormat(file)) {
    	  System.out.println("hello 2");
        try {
        	System.out.println("hello 3");
        	List<StockPrice> stock = fileService.save(file);
        	
          message = "Uploaded the file successfully: " + stock.toString();
          //return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
          return ResponseEntity.status(HttpStatus.OK).body(stock);
        } catch (Exception e) {
          message = "Could not upload the file: " + file.getOriginalFilename() + "!";
          //return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
          return null;
        }
      }

      message = "Please upload an excel file!";
      //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
      return null;
    }
    
    @GetMapping("/getAll")
    @LoadBalanced
	public ResponseEntity<List<StockPrice>> allDetails() {
    	//ResponseEntity.status(HttpStatus.OK).body(fileService.getAllCompanies());
    	return ResponseEntity.status(HttpStatus.OK).body(fileService.getAllCompanies());
	}
    
    @GetMapping("/getById")
    @LoadBalanced
    public ResponseEntity<Optional<StockPrice>> idDetails(@PathVariable Long id) {
    	//ResponseEntity.status(HttpStatus.OK).body(fileService.getAllCompaniesById(id));
    	return ResponseEntity.status(HttpStatus.OK).body(fileService.getAllCompaniesById(id));
	}

}
