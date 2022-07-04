package com.example.stockexchangeapplication.repository;

import com.example.stockexchangeapplication.model.CompanyStockExchangeMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyStockExchangeMapRepository extends JpaRepository<CompanyStockExchangeMap, Long> {
    Optional<CompanyStockExchangeMap> findByCompanyCode(String companyCode);

    @Query(value = "select count(*) from COMPANY_STOCK_EXCHANGE_MAP c where c.STOCK_EXCHANGE_ID=:exchangeId", nativeQuery = true)
    Integer getNumberOfCompaniesByExchange(@Param("exchangeId") Long exchangeId);

    @Query(value = "select case when count(*) = 1 then TRUE else FALSE END from COMPANY_STOCK_EXCHANGE_MAP c where c.COMPANY_ID = :companyId and c.STOCK_EXCHANGE_ID = :stockExchangeId",nativeQuery = true)
    boolean checkIfCompanyExistsInExchange(@Param("companyId") Long companyId, @Param("stockExchangeId") Long stockExchangeId);

    @Query(value = "select case when count(*) = 1 then TRUE else FALSE END from COMPANY_STOCK_EXCHANGE_MAP c where c.COMPANY_CODE = :companyCode and c.STOCK_EXCHANGE_ID = :stockExchangeId",nativeQuery = true)
    boolean checkIfCompanyExistsInExchange(@Param("companyCode") String companyCode, @Param("stockExchangeId") Long stockExchangeId);

    @Query(value = "delete from COMPANY_STOCK_EXCHANGE_MAP c where c.company_id=:companyId", nativeQuery = true)
    void deleteByCompanyId(@Param("companyId") Long companyId);
}
