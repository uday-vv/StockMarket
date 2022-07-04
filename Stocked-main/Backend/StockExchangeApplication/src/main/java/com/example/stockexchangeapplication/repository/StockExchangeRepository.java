package com.example.stockexchangeapplication.repository;

import com.example.stockexchangeapplication.model.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {
    @Query(value = "select * from STOCK_EXCHANGE where code=:code", nativeQuery = true)
    Optional<StockExchange> findByCode(@Param("code") String code);
}
