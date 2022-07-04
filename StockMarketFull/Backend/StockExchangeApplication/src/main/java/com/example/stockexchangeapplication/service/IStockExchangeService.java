package com.example.stockexchangeapplication.service;

import com.example.stockexchangeapplication.exceptions.AlreadyExistsException;
import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.response.StockExchangeResponse;
import com.example.stockexchangeapplication.model.StockExchange;

import java.util.List;

public interface IStockExchangeService {
    StockExchange addStockExchange(StockExchange stockExchange) throws AlreadyExistsException;
    StockExchange getStockExchangeByCode(String exchangeCode) throws NotFoundException;
    StockExchange getStockExchangeById(Long id) throws NotFoundException;
    List<StockExchangeResponse> getAllStockExchanges();
}
