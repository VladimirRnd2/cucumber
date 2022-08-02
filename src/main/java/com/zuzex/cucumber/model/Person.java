package com.zuzex.cucumber.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private List<Car> cars;

}
