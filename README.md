## Example of solving a test task
This is a simple REST API (Java 11, Spring Boot, Spring Data Jpa) with a PostgreSQL database.

### Features
Using http requests, you can do the following:
- _Creating records (batched);_
- _Getting the list of records in JSON format;_
- _Editing records;_
- _Delete all records (batched)._

#### Table Structure
| ID  | DATE                 | VALUE   |
|-----|----------------------|---------|
| 1   | 2023-01-15 06:20:00  | Value 1 |
| 2   | 2023-01-15 06:21:00  | Value 2 |
| ... | ...                  | ...     |
| n   | yyyyy-mm-dd hh:mm:ss | Value n |

#### Creating records
The "number" parameter takes an integer value greater than 0.  
The query creates the number of records in the table specified in the "number" parameter.
```http request
POST /api/values?number=99 
```
The following fields will be populated in the record being created:  
- _DATE_ - the current date will be written;  
- _VALUE_ - generated from a combination of the string "Value " and the sequence number for each string, you get, for example, "Value 99".

#### Delete all records
Clears the VALUES table of all records.
```http request
DELETE /api/values
```
#### Editing records
Accepts the record ID in the request and JSON in the request body.  
```http request
PATCH /api/values/:id
```
```json
{
  "value" : "Updated value 1"
}
```
When the query is executed, the record with the identifier specified in the query is edited.  
Sets a new value in the "VALUE" field specified in the body of the query and sets the date of change in the "DATE" field.
#### Getting the list of records
Get the whole table "VALUES" in JSON format with the possibility to filter and sort values by "ID", "DATE" and "VALUE" fields using query parameters.
```http request
GET /api/values
```
Available parameters for GET request:

- filter.value.ctn - get records containing a string fragment;
- filter.value.eq - get records containing a string;
- filter.date.eq - get records with date in format "yyyyy-MM-dd HH:mm:ss" (2022-12-04 16:14:29);
- filter.date.gt - get records with a date greater than the one specified;
- filter.date.lt - get entries with a date earlier than specified;
- filter.id.eq - get entries with specified ID;
- filter.id.in - get entries with ID in range (10-20);
- filter.id.gt - get entries with ID greater than specified;
- filter.id.lt - get records with ID less than specified;
- order - sorting (order=desc.date).  

Valid values for the "order" parameter:
- asc.id
- asc.date
- asc.value
- desc.id
- desc.date
- desc.value

### Installation
Create a table and sequence.
```sql
create sequence values_seq minvalue 1;

create table Values (
    id bigint NOT NULL DEFAULT nextval('values_seq') PRIMARY KEY,
    date timestamp(0) NOT NULL,
    value text NOT NULL
);
```

### Configuration
Edit the application.properties file if necessary.
```properties
server.port=80

spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/rest_app_db?cachePrepStmts=true&useServerPrepStmts=true&rewriteBatchedStatements=true
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.jdbc.batch_size=30
spring.jpa.properties.hibernate.generate_statistics=true
spring.jpa.properties.hibernate.jdbc.batch_versioned_data = true
```

### Create and run the .jar file
```shell
%Path%\ValuesRestApi>mvn install
%Path%\ValuesRestApi>cd target
%Path%\ValuesRestApi\target>java -jar ValueRestApi-0.0.1-SNAPSHOT.jar 
```
_I'm a novice java developer. If you've any comments about the program, please email me at_ nzhxdev@gmail.com. _Thank you!_