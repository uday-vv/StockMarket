package com.example.stockexchangeapplication.controller;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.request.CompanyRequest;
import com.example.stockexchangeapplication.model.*;
import com.example.stockexchangeapplication.service.impl.CompanyService;
import com.example.stockexchangeapplication.service.impl.CompanyStockExchangeService;
import com.example.stockexchangeapplication.service.impl.SectorService;
import com.example.stockexchangeapplication.service.impl.StockExchangeService;
import com.example.stockexchangeapplication.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    StockExchangeService stockExchangeService;

    @Autowired
    CompanyStockExchangeService mapService;

    @Autowired
    SectorService sectorService;

    @Autowired
    Mapper mapper;

    @PostMapping(
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Object> createCompany(@RequestBody CompanyRequest companyRequest) throws NotFoundException {
        Company company = mapper.getModelMapper().map(companyRequest, Company.class);
        companyService.addCompany(company);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(company.getId())
                .toUri();

        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/mapExchange",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<String> mapCompanyToExchange(@RequestBody Map<String,String> map) throws NotFoundException {
        if ((!map.containsKey("companyId")) || (! map.containsKey("exchangeCode") || (!map.containsKey("companyCode")))) {
            return new ResponseEntity<>("Require all values", HttpStatus.BAD_REQUEST);
        }

        if (mapService.checkIfCompanyExistsInExchange(Long.parseLong(map.get("companyId")),stockExchangeService.getStockExchangeByCode(map.get("exchangeCode")).getId())) throw new NotFoundException("Company Already Exists in Exchange");


        Company company = companyService.getCompany(Long.parseLong(map.get("companyId")));
        StockExchange stockExchange = stockExchangeService.getStockExchangeByCode(map.get("exchangeCode"));

        CompanyStockExchangeMap companyStockExchangeMap = new CompanyStockExchangeMap();
        companyStockExchangeMap.setCompany(company);
        companyStockExchangeMap.setStockExchange(stockExchange);
        companyStockExchangeMap.setCompanyCode(map.get("companyCode"));

        mapService.save(companyStockExchangeMap);

        return new ResponseEntity<>("Mapped Successfully", HttpStatus.OK);
    }

    @GetMapping(
            value = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Company> getCompany(@PathVariable Long id) throws NotFoundException {
        Company company = companyService.getCompany(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping(
            value = "/getId",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Long> getCompany(@PathParam("companyCode") String companyCode) throws NotFoundException {
        Company company = mapService.getCompanyByCompanyCode(companyCode);
        return new ResponseEntity<>(company.getId(), HttpStatus.OK);
    }

    @GetMapping(
            value = "/checkMapping",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<String> checkMapping(@PathParam("companyCode") String companyCode, @PathParam("exchangeCode") String exchangeCode) throws NotFoundException {
        long exchangeId = 0;
        try {
            exchangeId = stockExchangeService.getStockExchangeByCode(exchangeCode).getId();
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("Not Found", HttpStatus.OK);
        }
        Company company = mapService.getCompanyByCompanyCode(companyCode);
        if(mapService.checkIfCompanyExistsInExchange(company.getId(), exchangeId) && mapService.checkIfCompanyCodeExistsInExchange(companyCode, exchangeId)) return new ResponseEntity(company.getId()+"", HttpStatus.OK);
        return new ResponseEntity("Not Found", HttpStatus.OK);
    }

    @GetMapping(
            value = "/all",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(),HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Company> editCompany(@RequestBody CompanyRequest companyDto, @PathVariable Long id)
            throws NotFoundException
    {
        Company updatedCompanyDto = companyService.editCompany(companyDto,id);
        if(updatedCompanyDto == null) {
            throw new NotFoundException("Company with id "+id+" Not found");
        }
        return ResponseEntity.ok(updatedCompanyDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) throws NotFoundException {
        companyService.deleteCompany(id);
        return new ResponseEntity<>("Deleted Company "+id,HttpStatus.OK);
    }



}
