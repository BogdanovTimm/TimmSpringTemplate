package com.example.hibernatetest2.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ClassBean {

    @Value("Default text")
    private String string;

    private final String finalString = "ANUS";

}
