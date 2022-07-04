package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.repository;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    @Query(value = "select u from AppUser u where u.email = :email and u.password = :password")
    Optional<AppUser> findUser(@Param("email")String email, @Param("password") String password);

    @Query(value = "select u from AppUser u where u.email = :email")
    Optional<AppUser> findUser(@Param("email")String email);
}
