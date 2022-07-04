package com.example.stockexchangeapplication.service;

import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.model.Company;
import com.example.stockexchangeapplication.service.impl.SectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SectorServiceTest {
    @InjectMocks
    SectorService sectorService;

    @Mock
    SectorRepository sectorRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSectorsTest()
    {
        List<Sector> sectorList = new ArrayList<>();
        Sector one = new Sector(1l,"Automobile","Automobile Sector", new ArrayList<>());
        Sector two = new Sector(2l,"Health","Health Sector", new ArrayList<>());
        Sector three = new Sector(3l,"Finance","Finance Sector", new ArrayList<>());

        sectorList.add(one);
        sectorList.add(two);
        sectorList.add(three);

        when(sectorRepository.findAll()).thenReturn(sectorList);

        List<Sector> sectorListMock = sectorService.getAllSectors();

        assertEquals(3, sectorListMock.size());
        verify(sectorRepository, times(1)).findAll();
    }

    @Test
    public void addSectorTest() {
        Sector sector = new Sector(1l, "Automobile", "Automobile Sector", new ArrayList<>());
        sectorService.addSector(sector);
        verify(sectorRepository, times(1)).save(sector);
    }

    @Test
    public void getCompaniesBySectorIdTest() throws NotFoundException {
        Company company = new Company(1l, "MRF", 2000.0, "Mr. Ramamoorthy", "Mr. Ramamoorthy", "MRF is a leading tyre manufacturer",null,null, null);
        Sector sector = new Sector(1l, "Automobile", "Automobile Sector", Collections.singletonList(company));

        when(sectorRepository.existsById(1l)).thenReturn(true);
        when(sectorRepository.findById(1l)).thenReturn(Optional.of(sector));

        List<Company> companies = sectorService.getCompaniesBySectorId(1l);
        assertEquals(1, companies.size());
        assertEquals("MRF", companies.get(0).getCompanyName());
    }


    @Test
    public void getCompaniesBySectorIdTestException() throws NotFoundException {
        try {
            List<Company> companies = sectorService.getCompaniesBySectorId(1l);
        } catch (NotFoundException ex) {
            assertEquals("Sector with id 1 Not Found", ex.getMessage());
        }
    }

    @Test
    public void getSectorByIdTest() throws NotFoundException {
        when(sectorRepository.existsById(1l)).thenReturn(true);
        when(sectorRepository.findById(1l)).thenReturn(Optional.of(new Sector(1l, "Automobile", "Automobile Sector", new ArrayList<>())));

        Sector sector = sectorService.findSectorById(1l);

        assertEquals("Automobile", sector.getSectorName());
        assertEquals(0, sector.getCompanies().size());


    }

    @Test
    public void getSectorByIdTestException() throws NotFoundException {
        try {
            Sector sector = sectorService.findSectorById(1l);
        } catch (NotFoundException ex) {
            assertEquals("Sector with id 1 Not found", ex.getMessage());
        }
    }


}
