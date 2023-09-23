package com.example.hibernatetest2.security.event;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserEvent {

    private Long id;

    private String type;

    private String description;

    private String device;

    private String ipAddress;

    private LocalDateTime createdAt;
    
}