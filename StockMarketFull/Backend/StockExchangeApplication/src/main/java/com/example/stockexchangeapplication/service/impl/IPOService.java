package com.example.stockexchangeapplication.service.impl;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.model.StockExchange;
import com.example.stockexchangeapplication.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IPOService implements IIPOService {

    @Autowired
    IPORepository ipoRepository;

    @Autowired
    Mapper mapper;

    @Autowired
    StockExchangeService stockExchangeService;

    @Override
    public IPODetails createIPO(IPODetails ipoDetails) {
        return ipoRepository.save(ipoDetails);
    }

    @Override
    public IPODetails getIPOById(Long id) throws Exception {
        if (! ipoRepository.existsById(id)) {
            throw new Exception("IPO Not Found");
        }

        return ipoRepository.findById(id).get();
    }

    @Override
    public IPODetails editIPO(Long id, IPORequest ipoDetails) throws NotFoundException {
        if (!ipoRepository.existsById(id)) {
            throw new NotFoundException("IPO with id "+id+" Not Found");
        }
        IPODetails source = ipoRepository.findById(id).get();
        IPODetails objectToBeUpdated = mapper.getModelMapper().map(ipoDetails, IPODetails.class);
        source = mapper.copyObject(source, objectToBeUpdated);

        for(String exchangeCode: ipoDetails.getExchangesList()) {
            StockExchange exchange = stockExchangeService.getStockExchangeByCode(exchangeCode);
            if (!source.getStockExchanges().contains(exchange)) {
                source.getStockExchanges().add(exchange);
            }
        }

        source.getStockExchanges().removeIf(exchange -> !ipoDetails.getExchangesList().contains(exchange.getName()));

        return ipoRepository.save(source);
    }

    @Override
    public List<IPOResponse> getIPOsByChronology(LocalDateTime today) {
        List<IPODetails> ipoDetails = ipoRepository.getAllByChronologicalOrder(today);
        return ipoDetails.stream().map(ipo -> {
            IPOResponse ipoResponse = mapper.getModelMapper().map(ipo, IPOResponse.class);
            ipoResponse.setCompanyName(ipo.getCompany().getCompanyName());
            return ipoResponse;
        }).collect(Collectors.toList());
    }
}
