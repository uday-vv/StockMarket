package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.repository;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    List<StockPrice> findByCompanyId(Long companyId);
    List<StockPrice> findByCompanyCodeOrderByTimeStampDesc(String companyCode);

    @Query("select s from StockPrice s where s.companyCode = :companyCode and s.timeStamp >= :startDate and s.timeStamp <= :endDate")
    List<StockPrice> findByCompanyCodeInDateRange(@Param("companyCode") String companyCode, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
