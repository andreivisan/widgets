# Widgets
------------------------------------
REST API for CRUD operation on widgets. The project is built with
* Java 11
* Spring Boot 2.3.3
* H2 in memory DB

## Build and run
------------------------------------
* Run Unit and Integration tests
```
mvn clean test
```

* Build the project
```
mvn clean install
```

* Launch the server
```
mvn spring:boot run
```
Server is launched on port 8080

## REST API documentation
------------------------------------
### Create a new widget
For testing the API I used Postman. Alternatively ```curl```command works as well.

POST Request ```http://localhost:8080/widgets```

### Get widget by identifier
GET Request ```http://localhost:8080/widgets/{id}```

### Update widget by ID
PUT Request ```http://localhost:8080/widgets/{id}```

### Delete widget by ID
DELETE Request ```http://localhost:8080/widgets/{id}```

## Solution approach
------------------------------------

