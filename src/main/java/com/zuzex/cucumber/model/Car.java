package com.zuzex.cucumber.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private Long id;
    private String company;
    private String model;
    @JsonIgnore
    private Person person;
}
