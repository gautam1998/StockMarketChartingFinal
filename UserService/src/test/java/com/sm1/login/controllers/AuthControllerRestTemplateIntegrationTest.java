package com.sm1.login.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.stockmarket.login.controllers.AuthController;
import com.stockmarket.login.models.User;
import com.stockmarket.login.payload.request.LoginRequest;
import com.stockmarket.login.payload.request.SignupRequest;
import com.stockmarket.login.payload.response.MessageResponse;


public class AuthControllerRestTemplateIntegrationTest {
	@Autowired
	private AuthController authController; 
	
    
    @Test
    public void register_new_user() {
    	
    	// when
    	ResponseEntity<?> regiterResponse = authController.registerUser(new SignupRequest("gautams818","inagakis@gamil.com",null, "Ginger123$"));

        // then
    	assertEquals(HttpStatus.OK, regiterResponse.getStatusCode());
    }
    
    @Test
    public void sigin_user() {
    	
    	// when
    	ResponseEntity<?> regiterResponse = authController.authenticateUser(new LoginRequest("gautams818","Ginger123$"));

        // then
    	assertEquals(HttpStatus.OK, regiterResponse.getStatusCode());
    }
    
    


}
