package com.example.stockexchangeapplication.util;

import com.example.stockexchangeapplication.model.Company;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    public IPODetails copyObject(IPODetails source, IPODetails objectToBeCopied) {
        source.setOpenDateTime(objectToBeCopied.getOpenDateTime());
        source.setPricePerShare(objectToBeCopied.getPricePerShare());
        source.setTotalNumberOfShares(objectToBeCopied.getTotalNumberOfShares());

        return source;
    }

    public Company copyObject(Company source, Company objectToBeCopied) {
        source.setCompanyName(objectToBeCopied.getCompanyName());
        source.setSector(objectToBeCopied.getSector());
        source.setCeo(objectToBeCopied.getCeo());
        source.setCwebsite(objectToBeCopied.getCwebsite());
        source.setCeo(objectToBeCopied.getCeo());
        source.setTurnover(objectToBeCopied.getTurnover());
        source.setCceo(objectToBeCopied.getCceo());
        source.setSector(objectToBeCopied.getSector());
        return source;
    }
}
