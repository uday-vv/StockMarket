package com.example.stockexchangeapplication.service;


import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.request.CompanyRequest;
import com.example.stockexchangeapplication.model.Company;

import java.util.List;

public interface ICompanyService {
    Company addCompany(Company company);
    Company getCompany(Long id) throws NotFoundException;
    List<Company> getAllCompanies();
    void deleteCompany(Long id) throws NotFoundException;
    Company editCompany(CompanyRequest company, Long id) throws NotFoundException;
}
