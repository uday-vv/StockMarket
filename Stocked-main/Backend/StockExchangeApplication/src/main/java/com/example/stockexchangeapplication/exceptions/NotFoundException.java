package com.example.stockexchangeapplication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotFoundException extends Throwable {
    private String message;
}
