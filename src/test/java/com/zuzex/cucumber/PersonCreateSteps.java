package com.zuzex.cucumber;

import com.zuzex.cucumber.model.Person;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonCreateSteps {
    private static final String BASE_URL = "http://localhost:8080/api/person/";

    private final RestTemplate restTemplate;
    private List<Person> beforePersonList;
    private ResponseEntity<Person> personResponseEntity;


    @Autowired
    public PersonCreateSteps( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Given("Получаем актуальные персоны из БД")
    public void getBeforeAllPersons() {
        ResponseEntity<Person[]> persons = restTemplate.getForEntity(BASE_URL, Person[].class);
        Assertions.assertEquals(persons.getStatusCode().value(), 200);
        beforePersonList = Arrays.asList(Objects.requireNonNull(persons.getBody()));
        System.out.println(beforePersonList);
    }

    @When("создаем персону в БД")
    public void createPerson() {
        Person createdPerson = Person.builder()
                .login("death")
                .firstName("Death")
                .lastName("Angry")
                .build();

        personResponseEntity = restTemplate.postForEntity(BASE_URL + "create", createdPerson, Person.class);
        Assertions.assertNotNull(personResponseEntity);
    }

    @Then("получаем статус {int}")
    public void getStatus(int value) {
        assertEquals(personResponseEntity.getStatusCode().value(), value);
    }

    @And("получаем новый спискок персон и сравниваем с первоначальным, отлавливаем разницу")
    public void getNewPersonListAndCompare() {
        ResponseEntity<Person[]> persons = restTemplate.getForEntity(BASE_URL, Person[].class);
        Assertions.assertEquals(persons.getStatusCode().value(), 200);
        List<Person> afterPersonList = Arrays.asList(Objects.requireNonNull(persons.getBody()));
        Assertions.assertEquals(beforePersonList.size() + 1, afterPersonList.size());
        Person createdPerson = afterPersonList.get(afterPersonList.size() - 1);
        Assertions.assertEquals(Objects.requireNonNull(personResponseEntity.getBody()).getLogin(), createdPerson.getLogin());
        Assertions.assertEquals(personResponseEntity.getBody().getFirstName(), createdPerson.getFirstName());
        Assertions.assertEquals(personResponseEntity.getBody().getLastName(), createdPerson.getLastName());
    }

    @And("удаляем тестовую сущность")
    public void deleteTestPerson() {
        restTemplate.delete(BASE_URL + Objects.requireNonNull(personResponseEntity.getBody()).getId());
//        personService.deletePersonById(personService.findByLogin(Objects.requireNonNull(personResponseEntity.getBody()).getLogin()).getId());
    }
}
