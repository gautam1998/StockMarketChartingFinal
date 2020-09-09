package com.stockmarket.login.security.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockmarket.login.repository.UserRepository;
import com.stockmarket.login.models.User;

@Service
public class UserHelper {
	@Autowired
    UserRepository dataRepository;
	
	
	@Transactional
    public User updateDataProfile(User data,String username) {
        User user = dataRepository.findByUsername(username).orElse(null);
        System.out.println(user.getUsername());
        System.out.println(data.getUsername());
        if(user == null) {
        	System.out.println("null");
        }
        if (data.getPassword().length() != 0) {
        	System.out.println('1');
            user.setPassword(data.getPassword());
        }
        if (data.getEmail().length() != 0) {
        	System.out.println('2');
            user.setEmail(data.getEmail());
        }
        if (data.getUsername().length() != 0) {
        	System.out.println('3');
            user.setUsername(data.getUsername());
        }
        System.out.println(user.getUsername());
        dataRepository.saveAndFlush(user);
        return user;
    }
	
	@Transactional(readOnly = true)
    public User retrieveDataProfile(String dataname) {
		System.out.println(dataname.length());
		dataname = dataname.replaceAll("\\s", "");
		System.out.println(dataname.length());
        User dataEntity = dataRepository.findByUsername(dataname).orElse(null);
        if (dataEntity != null) {
        	
            //dataInfo = new Data();
            //BeanUtils.copyProperties(dataEntity, dataInfo);
        	return dataEntity;
        }
        return null;
        
    }

}
