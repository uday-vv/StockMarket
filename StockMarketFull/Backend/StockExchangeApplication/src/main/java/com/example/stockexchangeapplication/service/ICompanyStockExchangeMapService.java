package com.example.stockexchangeapplication.service;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.model.Company;
import com.example.stockexchangeapplication.model.CompanyStockExchangeMap;

public interface ICompanyStockExchangeMapService {
    Company getCompanyByCompanyCode(String companyCode) throws NotFoundException;
    boolean checkIfCompanyExistsInExchange(Long companyId, Long exchangeId);
    boolean checkIfCompanyCodeExistsInExchange(String companyCode, Long exchangeId);
    void save(CompanyStockExchangeMap map);
    Integer getNumberOfCompaniesInExchange(Long id);
    void deleteByCompanyId(Long id);
}
