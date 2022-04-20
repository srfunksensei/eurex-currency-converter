# eurex-brankovic-m

## Task
Write a library to exchange money from one currency into another, using the ECB reference exchange rate for a particular day (within the last 90 days).
You can use any of the following languages, based on your preference and what you think would be the best tool for the task: Go, Java, Kotlin, C#, C, C++, Python.
Example: convert 14.50 USD to CHF on July 8th

ECB reference rates as XML files [https://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html](https://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html)
90 days history [https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml](https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml)

## Implementation
The task has been done with the use of Spring Boot and Java, as these are the primary technologies I use. I assume that Python would be better choice if we aim for simple library.

On startup historical records are fetched and stored inside a map. Users can then try and exchange money from one currency to another.
The service is built in MVP manner with the following parts: 
* configuration properties - for configuring which currencies and url should be used for the app
* restTemplate - for fetching data
* parser - for parsing xml
* service - for combining everything into one logic

The logic for exchanging is implemented in the following way:
* exchange cannot be done for the future
* only available currencies can be used
* if date passed is weekend day, the closest working day will be used
* if there are no data for a given date, conversion will not happen

## Prerequisites

1. Java 8 (or higher)
2. Maven

### Building and Running

```bash
$ ./mvnw clean spring-boot:run
```
or alternatively using your installed maven version

```bash
$ mvn clean spring-boot:run
```

### Testing 

To see the application in action you can use check swagger collection [provided](http://localhost:8080/swagger-ui/)
or alternatively using 

```bash
$ curl -X POST "http://localhost:8080/api/ecb/exchange" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"amount\": 1,  \"date\": \"2022-02-8\",  \"fromCurrency\": \"USD\",  \"toCurrency\": \"CHF\"}"
```

### Dockerizing app

```bash
$ mvn clean package
$ docker build -t ecb:latest .
```

running docker image

```bash
$ docker run -it -p 8080:8080 ecb
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## TODOs
- use implicit unmarshalling instead of xml parser 
- improve error handling
- use db instead of concurrent map
- add scheduler to fetch daily changes
- use reactive programming
