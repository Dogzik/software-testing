# Настройка тестов с Selenide + Selenoid

## Selenide

* Создать **Gradle** проект (в данной инструкции будет испльзоваться kotlin-dsl для конфигурации)
* Добавить зависимость для Selenide:
```kotlin
dependencies {
    ...
    testImplementation("com.codeborne:selenide:6.2.0")
}
```
* Добавить необходимые для работы Selenide параметры:
```kotlin
tasks.withType<Test> {
    ...
    systemProperty("selenide.headless", "true") // Для запуска тестов без запуска UI-браузера
    systemProperty("selenide.baseUrl", System.getProperty("selenide.baseUrl", "http://localhost:3000")) // Адрес нашего приложения
    systemProperty("selenide.driverManagerEnabled", "true") // Говорит селениду скачивать драйверы локально
    systemProperty("selenide.browser", System.getProperty("selenide.browser", "chrome")) // Используемый браузер
}
```
* При желании адрес приложения и используемый браузер можно переопределить с помощью аргументов запуска:
```
-Dselenide.baseUrl=<url> -Dselenide.browser=<browser>
```
* Теперь можно писать тесты с помощью selenide (например [AuthenticationTest](src/test/kotlin/com/example/AuthenticationTest.kt)) 
и запускать стандартными средствами **Gradle**: `./gradlew test`

## Selenoid

* Для поднятия selenoid локально достаточно проследовать [quick start guide](https://aerokube.com/selenoid/latest/) на
официальном сайте
* Для связки с selenide нужно добавть следующие параметры в конфигурацию:
```kotlin
tasks.withType<Test> {
    ...
    systemProperty("selenide.driverManagerEnabled", "false") // Теперь мы используем удалённые драйверы
    systemProperty("selenide.remote", System.getProperty("selenide.remote", "http://localhost:4444/wd/hub")) // Путь до selenium grid (в нашем случае его роль выполняет selenoid)
}
```
* Адрес selenium grid точно так же можно переопределить параметром запуска:
```
-Dselenide.remote=<remote-address>
```

## Allure
* Необходимо добавить плагин для работы для **Gradle**:
```kotlin
plugins {
    ...
    id("io.qameta.allure") version "2.9.6"
}
```
* Далее в конфигурации указать путь, по которому будут распологаться отчёты:
```kotlin
tasks.withType<Test> {
    ...
    systemProperty("allure.results.directory", "$projectDir/build/allure-results")
}
```
* Затем необходимо проаннотировать тесты с помощью `@Epic`, `@Feature` и так далее в соотвествии
с правилами Allure
* После этого тесты при запуске будут генерировать по указанному выше пути артефакты,
из которых можно собрать отчёт с помощью команды `./gradlew allureReport`
и затем посмотреть в браузере с помощью `./gradlew allureServe`

Полноценный конфиг можно подсмотреть [тут](build.gradle.kts)

## Github actions

По-хорошему в любых e2e тестах все необходимые приложения, сервисы и selenoid уже запущены где-то
и доступны извне, так что правильный конфиг для CI выглядит как-то так:
```yaml
selenide-tests:
  name: selenide-tests-${{ matrix.browser }}
  runs-on: ubuntu-latest
  matrix:
    browser: [ chrome, firefox ]

  steps:
    - name: Checkout
      uses: actions/checkout@v2

    - name: Run Selenide tests
      working-directory: selenide-tests
      run: ./gradlew test -Dselenide.browser=${{ matrix.browser }} -Dselenide.remote=<selenoid-host> -Dselenide.baseUrl=<app-host>
```
Далее результаты тестов подхватываются в Allure и публикуются аналогично тому как это
уже сдано в текущих настройках CI для тестов на бэкенд и фронтенд.

Однако из-за отсуствия возможности задеплоить сервисы удалёно приходится поднимать всё локально:
* Во-первых приходится поднимать selenoid прямо в тестах. Это делается следующим образом:
```yaml
- name: Start Selenoid
  uses: Xotabu4/selenoid-github-action@v2
  with:
    selenoid-start-arguments: |
      --args "-timeout 100s" --browsers="chrome;firefox"
```
* Во-вторых, в силу того, что selenoid запускает всё в отдельных контейнерах,
из них невозможно достучаться до приложений запущенных локально через имя `localhost`,
ибо в таком случае все адреса резовлятся внутрь контейнера и запросы не попадают на
поднятые приложения. Для решения этой проблемы все ссылки на `localhost` необходимо заменить на IP машины
в локальной сети, который можно получить командой `$(hostname -I | awk '{print $1}')`

Итоговый конфиг CI можно пронаблюдать по [ссылке](../.github/workflows/tests.yml)
