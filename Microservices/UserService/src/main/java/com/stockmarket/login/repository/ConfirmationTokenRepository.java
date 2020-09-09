package com.stockmarket.login.repository;

import org.springframework.data.repository.CrudRepository;

import com.stockmarket.login.models.ConfirmationToken;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}