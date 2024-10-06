package com.example.Practice4Client.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Tournament {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double prizePool;
}
