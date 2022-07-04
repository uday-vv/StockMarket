package com.example.stockexchangeapplication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlreadyExistsException extends Throwable{
    private String message;
}
