package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.AppUser;

public interface IUserService {
    String saveUser(AppUser user) throws Exception;
    AppUser getUserById(Long id) throws Exception;
    AppUser getUser(String email, String password);
    AppUser getUserByMail(String mail);
    String updateUser(AppUser user, String token) throws Exception;
}
