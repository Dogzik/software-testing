# Jest vs Mocha

В целом синтаксис обоих фрейморков достаточно схож. Настолько, что можно почти полностью копировать код, написанный для
одного фреймворка, для миграции на второй. Оба поддеживают работа с произвольными фрейморками для мокирования и
ассёртов.

Также они позволяют достаточно комфортно работать с ансихронными функциями, в силу того, что тесты могут быть
асинхронными лямбдами, что использование `await` в теле теста.

## Jest

### Плюсы

* Поставляется вместе с шаблоном react-приложния, так что нет необходимости добавлять его отедельно, потенциально
  напарываясь на конфликты с зависимостями
* Имеет встроенные (и удобные) инструменты для мокирования и ассёртов
* Поддерживает работу с ES6 модулями и JSX
* Поддерживает лёгкий параллельный запуск тестов

### Минусы

* Не очень удобно мокироать целые классы, в отличие от функции

## Mocha

### Плюсы

* В силу "взрослости" фреймворка, довльно легко найти необходимую информацию
* По схожей причине достаточно хорошо развит тулинг

### Минусы

* Нет втсроенных инструментов для моккирования и ассёртов
* Нет стабильной поддержки ES6 модулей
* Нет (по крайней мере я не нашёл) поддережки JSX синтаксиса
* Работа с параллеьными тестами содержит больше подводных камней, чем хотелось бы