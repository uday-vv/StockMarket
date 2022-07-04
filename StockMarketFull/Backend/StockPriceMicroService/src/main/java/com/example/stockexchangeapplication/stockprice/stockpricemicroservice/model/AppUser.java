package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "AppUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private boolean confirmed;

    private boolean admin;

    private String role;

}
