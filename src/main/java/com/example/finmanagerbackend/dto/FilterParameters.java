package com.example.finmanagerbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterParameters {
    private String year;
    private String month;
    private String financialTransactionType;
    private String category;
}
