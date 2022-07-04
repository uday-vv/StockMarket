package com.example.stockexchangeapplication.service;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.request.CompanyRequest;
import com.example.stockexchangeapplication.model.Company;
import com.example.stockexchangeapplication.repository.CompanyRepository;
import com.example.stockexchangeapplication.service.impl.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deleteCompanyTest() throws NotFoundException {
        when(companyRepository.existsById(1l)).thenReturn(true);
        companyService.deleteCompany(1l);
        verify(companyRepository, times(1)).deleteById(1l);
    }

    @Test
    public void deleteCompanyTestException() throws NotFoundException {
        try {
            companyService.deleteCompany(1l);
        } catch (NotFoundException ex) {
            assertEquals("Company doesn't exists", ex.getMessage());
        }
    }


    @Test
    public void getCompanyByIdTestException() throws NotFoundException {
        try {
            Company company = companyService.getCompany(1l);
        } catch (NotFoundException ex) {
            assertEquals("Company with id 1 Not Found", ex.getMessage());
        }
    }

    @Test
    public void getAllCompaniesTest() {
        List<Company> companyList = new ArrayList<>();

        when(companyRepository.findAll()).thenReturn(companyList);

        List<Company> companies = companyService.getAllCompanies();

        assertEquals(2, companies.size());
        assertEquals("Mrs. Sneha Reddy", companies.get(1).getCeo());
    }

    @Test(expected = NullPointerException.class)
    public void editCompanyTest() throws NotFoundException {
        when(companyRepository.existsById(1l)).thenReturn(true);
        CompanyRequest companyRequest = new CompanyRequest("GGL", "Google", "Sundar P", 987.34, "Apollo.com", "LA");

        Company company = companyService.editCompany(companyRequest, 1l);

    }

    @Test
    public void editCompanyTestException() throws NotFoundException {
        CompanyRequest companyRequest = new CompanyRequest("Apl", "Apple", "Tim Cook", 356.4, "Apple.com", "LA");

        try {
            Company company = companyService.editCompany(companyRequest, 1l);
        }catch (NotFoundException ex) {
            assertEquals("Company with id 1 Not Found", ex.getMessage());
        }

    }
}
