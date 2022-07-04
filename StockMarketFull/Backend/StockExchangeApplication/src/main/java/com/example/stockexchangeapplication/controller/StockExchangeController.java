package com.example.stockexchangeapplication.controller;

import com.example.stockexchangeapplication.exceptions.AlreadyExistsException;
import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.response.StockExchangeResponse;
import com.example.stockexchangeapplication.model.StockExchange;
import com.example.stockexchangeapplication.service.impl.StockExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stockexchange")
@CrossOrigin(origins = "*")
public class StockExchangeController {

    @Autowired
    StockExchangeService stockExchangeService;

    @PostMapping(
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Object> createStockExchange(@RequestBody StockExchange exchange) throws AlreadyExistsException {
        System.out.println("Stock"+exchange.getName());
        StockExchange stockExchange = stockExchangeService.addStockExchange(exchange);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stockExchange.getId())
                .toUri();
        System.out.println("Loc"+location);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @GetMapping(
            value = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public StockExchange getStockExchange(@PathVariable String id) throws NotFoundException {
        Long exchangeId = Long.parseLong(id);

        return stockExchangeService.getStockExchangeById(exchangeId);
    }

    @GetMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<StockExchangeResponse>> getStockExchange() throws NotFoundException {
        return new ResponseEntity<>(stockExchangeService.getAllStockExchanges(),HttpStatus.OK);
    }
}
