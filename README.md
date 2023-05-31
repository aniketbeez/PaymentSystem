# PaymentSystem

## Components
### 1. Sql Server Database
To persist the data related to the payment system  
Setup:
* Configure Sql Server database on local or a remote server
* Use the scripts in the sql/ path to populate tables and the respective data data
* Configure spring datasource accordingly in application.yml file. Generally the credentials are stored in an encrypted store like secrets manager in aws. But for this POC, we are hard coding them with the data source in application.yml file

### 2. Kakfa  
A distributed queue where the payment rest api publishes create payment messages to be consumed by payment risk engine. For this POC, already created kafka image is used [Image](https://hub.docker.com/r/obsidiandynamics/kafka)
* Kafka - kafka broker
* Zookeeper - For leader election, health-check etc.
* KafDrop - A web UI for viewing Kafka topics and browsing consumer groups

Setup :  
* Configure the topic name in the application.yml file

### 3. Payment Web Service
This component is RESTful web API build in the Java Spring framework. It serves the following HTTP requests:  
 * Create payment  - publishes the create payment message to Kafka Queue
 * Get Payees for a user - fetches payees for the requester from the sql server database
 * Get Payment methods for a user -  fetches registered payment methods for the requester from the sql server database

This component has few added functionalities :
* Authentication using JWT and OAuth2.0 - A basic POC implementation (which can be extended further) of authentication by verifying user credentials and generating a JWT token with self-signed signature.
* Rate limiting - A simple POC rate limiting ability (which can be extended further) using Bucket4j
* Connection Pool for Sql server - Spring Boot in-built HicariCP connection pool
* Unit tests using Junit and Mockito  

Prerequisites:
* Sql server database is up and properly configured in application.yml file
* As this is a POC , we are using self signed signatures for JWTs instead of a dedicated authentication server. For authentication, please generate a private and a public key using OpenSSL and store it in /src/main/java/resources/certs/ directory as filenames 'private.pem' and 'public.pem'. The naming should be accurate as these keys are configured in application.yml  
* For simplicty, as we dont have an authentication service, we have configured an InMemoryUserDetailsManager with default user in Security Config class. Please set up the appropriate user credentials as these values will be used while testing the rest api

### 4. Payment Risk Engine
Consumes messages from the Kakfa queue, performs risk analysis and stores the messages in the Sql Server database. As this is a POC risk engine, it has a dummy logic for performing risk analysis and accepting only 70% of the messages.

## Pub/Sub:   
### Producer
![alt text](/images/producer.png)
### Consumer
![alt text](/images/consumer.png)

## Assumptions/Considerations
* Front end work is out of scope for this POC
* All other APIs such register user, register payment method, etc. are out-of-scope
* Encryption of secret information is not implemented but can be discussed during the interview
* Risk analysis requirements are out of scope and is replaced with a dummy logic of risk calculations
* Currently the Rest API does not serve HTTPS traffic but can be extended further

## Running the Project:

Prerequisites:
* Please go through prerequisites for each of the above components.
* Docker 23.0.5
* Java Corretto-17
* Spring Boot 3.1.0
* Ports needed - 8080, 8081, 9000, 9092, 2181

Steps:  
* Make sure the Sql server connection is working
* Navigate to paymentwebservice repository and package the solution in /target repository. (java -jar target/docker-payment-web-server-1.0.0.jar)
* Navigate to paymentriskengine repository and package the solution in /target repository. (java -jar target/docker-payment-risk-engine-server-1.0.0.jar)
* Navigate to root directory of this project where docker-compose.yml is present and run 'docker-compose up'  

Once all the configured containers are up and running you can test the application  

Testing
* You can use a web client like Postman
* Get the JWT - POST request to http://localhost:8081/token with 'Basic Auth' and user credentials in SecurityConfig class.
* Copy the JWT received in the response
* Perform POST/GET api resource requests using Authentication type as 'Bearer Token' and token value as the copied JWT
* If your are creating a payment, you shall verify the payment registered in database




