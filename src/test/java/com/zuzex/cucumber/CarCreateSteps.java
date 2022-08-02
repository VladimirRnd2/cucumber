package com.zuzex.cucumber;

import com.zuzex.cucumber.model.Car;
import com.zuzex.cucumber.model.CreateCarDto;
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

public class CarCreateSteps {

    private static final String BASE_URL = "http://192.168.2.40:8080/api/person/";

    private final RestTemplate restTemplate;
    private Person beforePerson;
    private List<Car> beforeCarList;
    private ResponseEntity<Car> carResponseEntity;

    @Autowired
    public CarCreateSteps(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Given("Переходим на существующего пользователя")
    public void getExistedPerson() {
        ResponseEntity<Person[]> persons = restTemplate.getForEntity(BASE_URL, Person[].class);
        beforePerson = Objects.requireNonNull(persons.getBody())[0];
        Assertions.assertNotNull(beforePerson);
    }

    @And("Получаем список автомобилей пользователя")
    public void getAllCarByPerson() {
        beforeCarList = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(BASE_URL + beforePerson.getId() + "/cars", Car[].class))).toList();
        Assertions.assertNotNull(beforeCarList);
    }

    @When("Делаем запрос на создания нового автомобиля")
    public void createNewCarInPersonCars() {
        CreateCarDto carDto = CreateCarDto.builder().company("Audi").model("A7").build();
        carResponseEntity = restTemplate.postForEntity(BASE_URL + beforePerson.getId() + "/createcar", carDto, Car.class);
        Assertions.assertNotNull(carResponseEntity);
    }

    @Then("получаем код ответа {int}")
    public void checkStatusCode(int statusCode) {
        Assertions.assertEquals(carResponseEntity.getStatusCode().value(), statusCode);
    }

    @And("получаем новый автомобиль в списке")
    public void getNewCarInList() {
        ResponseEntity<Car[]> carlist = restTemplate.getForEntity(BASE_URL + beforePerson.getId() + "/cars", Car[].class);
        Assertions.assertEquals(carlist.getStatusCode().value(), 200);
        Assertions.assertEquals(Objects.requireNonNull(carlist.getBody())[carlist.getBody().length - 1], carResponseEntity.getBody());
    }

    @And("удаляем автомобиль")
    public void deleteCar() {
        restTemplate.delete(BASE_URL + beforePerson.getId() + "/" + Objects.requireNonNull(carResponseEntity.getBody()).getId());
    }
}
