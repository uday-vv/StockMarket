package com.example.stockexchangeapplication.service;

import com.example.stockexchangeapplication.exceptions.AlreadyExistsException;
import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.model.StockExchange;
import com.example.stockexchangeapplication.repository.CompanyStockExchangeMapRepository;
import com.example.stockexchangeapplication.repository.StockExchangeRepository;
import com.example.stockexchangeapplication.service.impl.CompanyStockExchangeService;
import com.example.stockexchangeapplication.service.impl.StockExchangeService;
import com.example.stockexchangeapplication.util.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockExchangeServiceTest {
    @InjectMocks
    StockExchangeService stockExchangeService;

    @Mock
    CompanyStockExchangeMapRepository mapRepository;

    @InjectMocks
    CompanyStockExchangeService mapService;

    @Autowired
    Mapper mapper;

    @Mock
    StockExchangeRepository stockExchangeRepository;

    @Test
    public void addStockExchangeTest() throws AlreadyExistsException {
        StockExchange exchange = new StockExchange(1l, "National Stock Exchange", "NSE", new ArrayList<>());

        stockExchangeService.addStockExchange(exchange);

        verify(stockExchangeRepository, times(1)).save(exchange);
    }

    @Test
    public void addStockExchangeTestException() throws AlreadyExistsException {
        StockExchange exchange = new StockExchange(1l, "National Stock Exchange", "NSE", new ArrayList<>());

        when(stockExchangeRepository.save(exchange)).thenThrow(EntityExistsException.class);

        try {
            stockExchangeService.addStockExchange(exchange);
        } catch (AlreadyExistsException e) {
            assertEquals("Stock Exchange with Name National Stock Exchange already exists", e.getMessage());
        }

    }

    @Test
    public void getExchangeByCodeTest() throws NotFoundException {
        when(stockExchangeRepository.findByCode("NSE")).thenReturn(Optional.of(new StockExchange(1l, "National Stock Exchange", "NSE", new ArrayList<>())));

        StockExchange exchange = stockExchangeService.getStockExchangeByCode("NSE");

        assertEquals("National Stock Exchange", exchange.getName());
        assertEquals("NSE", exchange.getCode());
    }

    @Test
    public void getExchangeByCodeTestException(){
        try {
            StockExchange exchange = stockExchangeService.getStockExchangeByCode("NSE");
        } catch (NotFoundException ex) {
            assertEquals("Exchange NSE Not Found", ex.getMessage());
        }
    }

    @Test
    public void getExchangeByIdTest() throws NotFoundException {
        when(stockExchangeRepository.findById(1l)).thenReturn(Optional.of(new StockExchange(1l, "National Stock Exchange", "NSE", new ArrayList<>())));

        StockExchange exchange = stockExchangeService.getStockExchangeById(1l);

        assertEquals("National Stock Exchange", exchange.getName());
        assertEquals("NSE", exchange.getCode());
    }

    @Test
    public void getExchangeByIdTestException(){
        try {
            StockExchange exchange = stockExchangeService.getStockExchangeById(1l);
        } catch (NotFoundException ex) {
            assertEquals("Exchange with id 1 Not Found", ex.getMessage());
        }
    }
}
