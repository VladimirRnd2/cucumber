Feature: Создание и удаление автомобиля у пользователя

  Scenario: Создание и удаление машины у человека
    Given Переходим на существующего пользователя
    And Получаем список автомобилей пользователя
    When Делаем запрос на создания нового автомобиля
    Then получаем код ответа 201
    And получаем новый автомобиль в списке
    And удаляем автомобиль