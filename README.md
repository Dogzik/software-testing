# Software testing

## HW1

### Main

* В репозитории есть папка client, содержащая приложение на React
* Приложение содержит несколько страниц
* Приложение содержит сервис для общения с бэкендом на Spring
* Присутствуют unit, component и e2e тесты

### Advanced
* Приложение содержит авторизацию и не собрано из генераторов вида JHipster
* Написаны тесты для проверки авторизации
* Существует нескольно наборов тестов (несколько suites)

### Bonus
* Написаны тесты на [Jest](client/src/components/utils/Utils.jest.test.js)
  и [Mocha](client/src/components/utils/Utils.mocha.js)
* Написана [заметка](client/comparison.md) о сравнении этих фреймворков

---

## HW2

### Main
* Сервис на Spring + Postgres c 2 контроллерами
* Написаны различные тесты
    * Unit и Component с использованиме Mockk
    * Интеграционные с использование TestContainers
* Написан [TODO](server/TODO.md) с ненаписанными тестами

### Advanced
* Сделано взаимодействие клиента и сервера
* Написаны тесты на авторизацию
* Сделаны отдельные Spring TestConfiguration, которые можно переключать с помощью флага при запуске тестов
---

## HW3

### Main
* Добавлен [Github action](.github/workflows/tests.yml) для запуска тестов на бэкенд, фронтенд и E2E
---

## HW4

### Bonus
* Настроен [Allure report](https://dogzik.github.io/software-testing) для написанных unit, component, integration и e2e тестов

