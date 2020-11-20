# The Account Application

  Table of contents

  * [**About the application**](#about-the-application)
  * [**Update history**](#update-history)
  * [**Assumptions**](#assumptions)
  * [**Instructions**](#instructions)
    + [**Pre-requsites**](#pre-requsites)
    + [**Running the binaries**](#running-the-binaries)
    + [**Running the docker image**](#running-the-binaries)
    + [**RESTful API endpoints**](#restful-api-endpoints)
        * [**Get a list of accounts with customer ID**](#get-a-list-of-accounts-with-customer-id)
        * [**Get a list of transactions with account number**](#get-a-list-of-transactions-with-account-number)
  * [**Architecture**](#architecture)
    + [**Model layer**](#model-layer)
        * [**Database**](#database)
        * [**Tables**](#tables)
            * [**ACCOUNT**](#account)
            * [**TRANSACTION**](#transaction)
        * [**Hibernate**](#hibernate)
    + [**Controller layer**](#controller-layer)
    + [**Logging**](#logging)
  * [**Technology stack**](#technology-stack)

## **About the application**

  The Account application serves as the backend of a web application which allows customers to view their accounts and the transactions of any account that they hold. It is written in Java and Spring Boot.

  It provides the following endpoints:
  * Get a list of accounts with customer ID 
  * Get a list of transactions with account number

## **Update history**

  | Version| Date | Description | 
  | --- | --- | --- | 
  | 1.0.0 | 20 Nov 2020 | Initial release |
## **Assumptions**

The following assumptions are being made for the application:
* There is a front-end application (outside of scope) handling the view layer which consumes the APIs.
* Customer would perform a login operation (outside of scope) and would be able to provide a customer ID for account list enquiry.
* There are constraints on the customer ID, account number and other fields.
* The application connects to a H2 in-memory relation database.

## **Instructions**

  ### Pre-requsites

  * Git
  * Maven
  * JDK8
  * Docker (if you would like to run a pre-packaged application directly)
  * An API testing tool (e.g. Postman, or just use the Swagger UI)

  ### **Running the binaries**

  Clone the "main" branch of the account repository from GitHub
  
  Git repository location:<br/>
  https://github.com/patrickpycheung/account

  
    git clone https://github.com/patrickpycheung/account

  The applcation can be started directly by running the following command

    mvn spring-boot:run

  If you would like to create a JAR file from the source, run the following command

    mvn clean package

  <img src="https://bn1301files.storage.live.com/y4mvMNVcM9YErZFL7rHxBApB2k5tBCNE3TI9gOfPwwfGLojMVIAc8SfM64mtECabRUJaxYsOY_yVou1VmAayhKJnV4_ixc-XZGfLpb08a6K_P9WSHbqdb3WwWB0d_vwNTa2Rj-En34Dip6ASTUqnEGwfdHsMUN50cjp9kGPkd-mi0oSqb2HUCzt5ukRXATyQRkH?width=2876&height=316&cropmode=none" title="Build_JAR_01" alt="Build_JAR_01">

  <img src="https://bn1301files.storage.live.com/y4mmLQjTMn_tGC0yL08hlUGuEEz4meeHIE_Ly3TdxKmq_y5P4FNM_fOJHAgRn9dYyMQYawjcisloSEUGwdZ2oMB7e3rfhzzu_7lAwYjGInKchMiLipvvzYqWaq_-s2Ddln55qqEGUpuw683PSgO-PXXirVmjf1eLSCNIESYHdUu_Bgs90xFNG9CHINDXeZqZuhy?width=2876&height=842&cropmode=none" title="Build_JAR_02" alt="Build_JAR_02">

  A JAR file will be created in the "target" folder and can be executed by the following command

    java -jar account-<artifact version>.jar

  ### **Running the docker image**

  A pre-packaged container image has been created for use readily.

  Dockerhub repository location:<br/>
  https://hub.docker.com/r/patrick888/account

  Pull the lastest docker image (docker tag "latest") from dockerhub with the following command

    docker pull patrick888/account:latest

  Run the docker image in detached mode within in a container using the following command:
    
    docker run --rm -d -p [host port]:[container port] -t patrick888/account:latest

  e.g.
    
    docker run --rm -d -p 8080:8080 -t patrick888/account:latest

  ### **RESTful API endpoints**

  The following API endpoints are available:

  | Verb | Path | URL param  | Request param | Request body | Description 
  | --- | --- | --- | --- | --- |  --- |
  | GET | /api/account | N/A | Customer ID | N/A | Get a list of accounts with customer ID 
  | GET | /api/account/transaction | N/A | Account number | N/A | Get a list of transactions with account number

  The required parameters and returns are described in the Swagger UI page.

  <img src="https://bn1301files.storage.live.com/y4mJDVNW6rszftXoitckE6hK23LFDTsZTRDFgOayRc0r7TTkdyyNP2cl2qDeGOi5hynHF08IulrKs2lSE05eb77hh7I2z4-2FKPjkjZLLnx_Ynm9q3pZ8CmAp_n2vDGE7wwOzxNPLr5ANBEl0iiGBvE_CqSbWSDnXnqncJ_VKb2T-cLDAw7wAOAsibZW9n76L_A?width=5118&height=1314&cropmode=none" title="Swagger_UI" alt="Swagger_UI">

  Swagger UI path (assuming you are running the application in local environment):<br/>
  http://localhost:8080/swagger-ui.html#/account-controller

  Sample call:

  #### **Get a list of accounts with customer ID**

  <img src="https://bn1301files.storage.live.com/y4mTFcFRWHjtalE2MDSRecgP4lrL4u0vPtfKMqGyzKqzlqXi9zZGeT7UmLjsioUzYA9n_MF-G0k_HsdFBSGEz7M7edTOb4H56_Th1OX31Rq3H4UY6LxdMy5dK1ujEDKWuXMJPH8tTsUZTceymeKhD5EqLcUbJDEU4xoXsvOMYCia4nuhBcPQfBxQAbud-wdTE2-?width=2856&height=2344&cropmode=none" title="Get_Account_List_01" alt="Get_Account_List_01">
  
  <img src="https://bn1301files.storage.live.com/y4mhnbsAqqRU8oyO9r2OUA_2hXhklFUzct9lJe5q3Q4go1lNoKhfPxqxRJI6wGnpjHmEUhRT6khMYJDOgIFAaBH2FLYKYsMHFDntItYSc35svQ_gHeI3xHfEw4VT4rG3GBkUhalQZKdLNqek3OAo6Q-qLujzUzxHJ4Dj0nG9KPhxYGJEUcnE7g-GZY9cktt59Z3?width=2840&height=1604&cropmode=none" title="Get_Account_List_02" alt="Get_Account_List_02">

  #### **Get a list of transactions with account number**

  <img src="https://bn1301files.storage.live.com/y4mqv4LrvWGdI6bOWxbk46IC_o9AD4phtvSPDZnIwv3LToiQNuxKqzAKnW0fo9EAwua336B3pCOd7ki-scW5Oymuh-KO2gcgnQ9D4AC7Mlt1ft2BQm-ScF8KPxYwYfQXkSox_di7l1pHXLRkgI1Ks7PgdZHy5ui9JaEytQpc7kqj0n57VAWPXnFDkdVF2UrAwPz?width=2838&height=2530&cropmode=none" title="Get_Transaction_List_01" alt="Get_Transaction_List_01">

  <img src="https://bn1301files.storage.live.com/y4mlYiu4PGLdUxOXEXXfWQfvJs2B0NfZXwWQ4qRytPQMEcEmhzmfjIHVP-GwYZW16CWEMHQWrqRmGlTuzhQw-laUx8ebkjyPGcMxOUtPfX6PQDEArgA31SrZwhK4ARBZpglYheiTa9Uql8lKuTkOjv4wJgBTNlsQAjnbuyg13cheqKTIFEBuahFr71kTf8nuYQi?width=2838&height=1714&cropmode=none" title="Get_Transaction_List_02" alt="Get_Transaction_List_02">

## **Architecture**

### Model layer

#### Database

The database is assumed to be a H2 in-memory database.

This can be modifed to other DB by configuring the Spring data source.

  <img src="https://bn1301files.storage.live.com/y4mf-MAwOYKQXvf1maJ12-ben9Z3eNTNyhEAxZYXws-riHF-uNxCLI0PPS16pM4u5-VireYTbXkGDzWjWQ5FWwvQOs92rruphZDL3o_6qDPmoFzaxibs6QkXxI9xTUV9VjqCYzo2uaQSQIhtKo0uJaJWCi5V9AhEgG33gHSmLxpBxZVaPmcy6U9D-F4nvwz9sOA?width=1240&height=688&cropmode=none" title="Spring_data_source" alt="Spring_data_source">

#### Tables

There are 2 tables in the database, "ACCOUNT" and "TRANSACTION".

They have the following structures, as induced by Hibernate from the java entities "Account" and "Transaction":

##### ACCOUNT

  <img src="https://bn1301files.storage.live.com/y4mU_b3pIN01byMjycZT1D4OKrDxCFFBqduWRbqg-X6u0uzg9HkGG4KrL5wqqoYBAAWB_-qejDi0akb0c9ubPsLlULeJthctPgDaKgok8_-4eAHDwICoWquyY14RVKsGgN-7mUcGA3FCFFLEwCDQgjwE6WGPHZoXjcVR9c5CVODb2s5nHD_YqF2Btj3OzBzdbdu?width=870&height=404&cropmode=none" title="Account_table_structure" alt="Account_table_structure">

##### TRANSACTION

  <img src="https://bn1301files.storage.live.com/y4mLnSFpP7Xu_H6bXUKTBc_j7sFOhZgIkYg2MKeEsxad2hx45Dg-EP6xSI_WKz7_zzNPL3-ZfBF3w5mPDbRDVPd6sHbGPPQNk3MGTrVPX6qt-TUeYsXR5GqrdkwNNOodXIbGE-apHy50lhJN5XcsD43rTVGqArPKfz1_h_6qRqCaosVpt4szStfrukbBX-lJuQl?width=820&height=442&cropmode=none" title="Transaction_table_structure" alt="Transaction_table_structure">

#### Hibernate

Hibernate is used for the object relational mapping (ORM) between the java entity model and the DB table.

When the application starts up, the "spring.jpa.hibernate.ddl-auto: create-drop" configuration will instruct hibernate to create the database and the tables (based on the entity model). When the application stops, the database and tables will be destroyed as well.

  <img src="https://bn1301files.storage.live.com/y4mhBhw3iSgmKaYUdWdTkLiCNBj4yu8-5EcqgYyYU4CUJPDOxnrF4J3wae01gueXSDOpCwPFkkW4XZymE5Uh6kW0dMFt6sS89AElrpc6Yst0DjJWnO2CgHga-9hiY1PBVQUg9NTvvg6VzxQJS6ZLvvrKFqEHvCknZZ5oGPb9Bagl7n4grgzCHz1Ps5hf5NuWW6s?width=784&height=50&cropmode=none" title="spring.jpa.hibernate.ddl-auto-create-drop" alt="spring.jpa.hibernate.ddl-auto-create-drop">

It will also create some initial data specified in data.sql. You can edit the the initial data by modifying this file.

You can view/edit the created tables and data in the H2 console.

  <img src="https://bn1301files.storage.live.com/y4myOKfZZx8NrWIDXwln9rlNd0IGGHioYlAp245GOJzxXPAiwzRmPJc6T2-D8uUXg_9-ii9ePth5XrHufyDc5kZqjPO325gn39LE-5PkBeMlqABzXR_EXh-bUNlGfwD8xOMaGCMQuYXD41NzOYKeCaIoQ65g1RSvEHIPh8NXL-8u-jZNntNRsqj7HdFwz23vfyM?width=3014&height=942&cropmode=none" title="H2_console" alt="H2_console">

  H2 console path (assuming you are running the application in local environment):<br/>
  http://localhost:8080/h2-console

Note: In production settings, the "spring.jpa.hibernate.ddl-auto" should be set to "none", as any tables should be managed by the DBA

### Controller layer

The endpoints are hosted by the controller AccountController. It listens for API calls.
When one is received, it triggers the corresponding method to handle the request.

It will then perform field validation (for GET request on request params and URL params) and bean validation (for PUT/POST request on request body which will be translated a a java object).

A custom exception handler CustomGlobalExceptionHandler is created to handle the error response arised from problematic requests. If any validation on the request fails, it will return a response with a HTTP 400 BAD REQUEST staus, together with the error message. This provides a response with more meaningful details.

  <img src="https://bn1301files.storage.live.com/y4me4MKrobFHclcd7G_s_JUPejG7e1gH5lQu0WBqcWq4H0RAHvrCgt7VYOI1m350azdt3N7On934Kxu3rJYsLdqbETQlIeIl9KkRDOQ6B_QZ7FdrtVbu7QorX0p6JmR2V_VMpinYJHmvsqQaPjxJspmU89Y93z_3RGAtzmzmtzaxkpOztDQ-eulg44K9pr7tqSk?width=2844&height=2210&cropmode=none" title="Validation_failed" alt="Validation_failed">

When validation is passed, it creates the input object and delegates to the service AccountService to perform the actual operation.

In AccountService, an instance of "JpaRepository" (a data access object (DAO)) will be injected. The service will create a query object (of "Example" class) from the controller input and supply it to the DAO. Hibernate will translate this to a SQL statement, fetch the matching results from DB, and convert it to java objects with property values.

The result will be passed back the contoller and a ResponseEntity object will be returned. It will be translated to a JSON response to the API caller.

### Logging

Logging has been configured at INFO level which will publish informational logs, warnings as well as errors to both the console and the log file.

**Console log**

<img src="https://bn1301files.storage.live.com/y4m6KWwA8-4uilP-uMI2OlMTnrmctMZsntLjMm2lOOcUEzHepVA4h-L1jvNX2mB1uowANWxUC7tEhQ71hv_u8YZ_LbfjmHFJAWT9PmDNkSAKV9AekJ7hN_KKuN7FUCvbpmt1j0Rvu8S9-n2C0dlY5AD56NXWWjr_mhv-zFOrZV_1XhdxK2TMOxkKsYvAUqYEnnm?width=2878&height=1638&cropmode=none" title="Console_log" alt="Console_log">

**Log file**

<img src="https://bn1301files.storage.live.com/y4mk5CibLcrlRE-vCjgmTddWhNG2UGnJxonmDaLuK24J73oSHj5owR8bZ6TLjtnw1QxGfcPhIQmyOfz5zqe6j2Kxx0907M41bPtPB3hsGb79uLL-qpPbhNc3c5AaGXZcmFpVaKNWBYZA7zVUv6-XpOqZ6UEX1NK8Js27zfwKwRfNzGVek2N8Es1JJeDDFWw-HJY?width=1226&height=810&cropmode=none" title="Log_file_location" alt="Log_file_location">

<img src="https://bn1301files.storage.live.com/y4mD-vNUm8Jr9I2hWUG0lnh2sgtDWAb9KgcJJgwMbLVx9fiaaGpdStY3vi1543R71--751yVtfltfDGUG-OT4h3L5MxmjuL88NV0CrL0ouVSeJrs76xdzEzCOYaT2C4uW1FYija2x41vH63kpwLNHsvyQI2ATQpY8KQKjTO_UC3X0dYtq7wbg-9ypUQy6dlTbAI?width=2180&height=1518&cropmode=none" title="Log_file" alt="Log_file">

The log file is saved at the same location where the applciation is launched.
In addition, it is set to archive daily or whenever the file reaches the file size limit.

These can be configured in the logback-spring.xml.

To change the log level (e.g. in order to see the Hibernate SQL statements):

* Change the property "logging.level.root" to "debug" in application.yml
* Amend the "springProfile" property (for the default profile, i.e. !dev) in logback-spring.xml such that it will write logs at debug level

## **Technology stack**

* Java 8
* Spring Boot v2.4.0
  * Dependencies:
    * spring-boot-starter-web (Proivdes a Tomcat web container)
    * lombok (Provides getters/setters to entity/model class properties, and provides slf4j support for logging)
    * spring-boot-starter-test (Provides JUnit support)
    * springfox-swagger2 (Provides Swagger support)
    * springfox-swagger-ui (Provides Swager UI)
    * spring-boot-maven-plugin (Provides Maven support)
    * h2 (Provides H2 in-memory DB)
    * spring-boot-starter-data-jpa (Provides Hibernate and JPA support)
    * spring-boot-starter-validation (Provides validation support)
* Docker
* GitHub