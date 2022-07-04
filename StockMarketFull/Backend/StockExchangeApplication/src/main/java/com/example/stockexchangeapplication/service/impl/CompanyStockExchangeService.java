package com.example.stockexchangeapplication.service.impl;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.model.Company;
import com.example.stockexchangeapplication.model.CompanyStockExchangeMap;
import com.example.stockexchangeapplication.repository.CompanyStockExchangeMapRepository;
import com.example.stockexchangeapplication.repository.StockExchangeRepository;
import com.example.stockexchangeapplication.service.ICompanyStockExchangeMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyStockExchangeService implements ICompanyStockExchangeMapService {

    @Autowired
    CompanyStockExchangeMapRepository mapRepository;

    @Autowired
    StockExchangeRepository stockExchangeRepository;

    @Override
    public Company getCompanyByCompanyCode(String companyCode) throws NotFoundException {
        Optional<CompanyStockExchangeMap> companyMapOptional = mapRepository.findByCompanyCode(companyCode);

        if (companyMapOptional.isEmpty()) throw new NotFoundException("Company doesn't exist");

        return companyMapOptional.get().getCompany();
    }

    @Override
    public boolean checkIfCompanyExistsInExchange(Long companyId, Long exchangeId) {
        boolean existsInExchange = mapRepository.checkIfCompanyExistsInExchange(companyId, exchangeId);
        return existsInExchange;
    }

    @Override
    public boolean checkIfCompanyCodeExistsInExchange(String companyCode, Long exchangeId) {
        boolean existsInExchange = mapRepository.checkIfCompanyExistsInExchange(companyCode, exchangeId);
        return existsInExchange;
    }

    @Override
    public void save(CompanyStockExchangeMap map) {
        mapRepository.save(map);
    }

    @Override
    public Integer getNumberOfCompaniesInExchange(Long id) {
        return mapRepository.getNumberOfCompaniesByExchange(id);
    }

    @Override
    public void deleteByCompanyId(Long id) {
        mapRepository.deleteByCompanyId(id);
    }


}
