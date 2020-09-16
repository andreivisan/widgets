# Widgets

REST API for CRUD operation on widgets. The project is built with
* Java 11
* Spring Boot 2.3.3
* H2 in memory DB

## Build and run

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
* I used Spring HATEOAS project library in order to create a REST representation that follows the principle of HATEOAS.

* For the in memory storage approach (non H2) I chose to represent the data in a TreeMap data structure.
  Reasoning behind my choice:
    * I used zIndex as a key for the TreeMap implementation in order to have the widgets sorted by zIndex.
    * Regarding time-space complexity I weighed in the possibility of using an extra map which would have the Widget ID as key. That implies more space for better time for operations like widget lookups. I assumed that read/write operations are not as many as widgets are being created so I chose to use less space for a slight increase in lookup time. 
    
## Complexities implemented:
1. Pagination
4. SQL Database - for this I chose H2 in memory database. The configuration to switch between in memory storage using a TreeMap or H2 database is done by setting ```storage.h2```inside ```application.properties``` to ```true``` for using H2 DB or ```false``` for using TreeMap in memory storage.
  To access the H2 console use ```http://localhost:8080/h2-console``` username ```sa``` and no password. Data source URL is ```jdbc:h2:mem:widget```.

