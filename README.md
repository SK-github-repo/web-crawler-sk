# WEB Crawler

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Prerequirements](#prerequirements)
* [Setup](#setup)
* [Test](#test)
* [Future extension](#future-extension)

## General info
This project returns all found links on given by user website following all links related to website's domain. <br/>
Returned data is divided into three lists: domain links, external links (like google, facebook, twitter) and static content (like images, js and css files). 
	
## Technologies
Project is created with:
* SpringBoot: 2.2.0
* Java: 13
* JSoup: 1.12.1
* Jackson: 2.9.10
* JUnit: 5.5.2
* Mockito: 3.1

## Prerequirements
To download and run this project install below software:
* Java: 13
* Git: newer than 2.0
* Maven: newer than 3.3.9 (Optional)

To test the application:
* Postman

## Setup
To run this project, install it locally following below steps: 

#### 1. Download project from github
To copy project from github run below command in your terminal in location you want to save the project:

```
$ git clone <project link.git>
```

#### 2. Run Maven command to create \*.war file
*\*While running below commands you need to be in the same directory as the \*.war file* <br/>
Running below command will generate \.war file which can be locally deployed:
```
$ ./mvnw package
```
(Without local Maven): 
```
$ mvn package
```

If you want to build your \*.war file skipping tests run:
```
$ ./mvnw package -DskipTests
```
(Without local Maven): 
```
$ mvn package -DskipTests
```

#### 3. Deploy \*.war file
*\*While running below commands you need to be in the same directory as the \*.war file* <br/>
To quickly deploy this project you can run below command. 
```
$ java -jar crawler-0.0.1-SNAPSHOT.war
```

## Test

#### Run Automated Tests
*\*While running below commands you need to be in the same directory as the \*.war file* <br/>
To run all automated tests run below query:
```
$ ./mvnw test
```

#### Test locally deployed application with POSTMAN
To test deployed application follow below steps:

1. Open Postman 
2. Create *POST* request
3. Insert to requested URL: 
```
http://localhost:7070/crawler/pagesContent  ( port can be changed in application.properties )
```
4. Add *BODY* to created request 
5. Mark the option of *body type* to *RAW* and select *JSON* from the dropdown
6. To field for request body add below example (the value of url can be modified):
```
{
    "pageUrl": "http://www.mechanikasamochodowa.pl"
}
```
7. Press *SEND* button and wait for result

#### Test deployed on AWS application with POSTMAN (currently not active - I will activate it on demand)

1. Open Postman 
2. Create *POST* request
3. Insert to requested URL: 
```
http://3.17.14.74:7070/crawler/pagesContent
```
4. Add *BODY* to created request 
5. Mark the option of *body type* to *RAW* and select *JSON* from the dropdown
6. To field for request body add below example (the value of url can be modified):
```
{
    "pageUrl": "http://www.mechanikasamochodowa.pl"
}
```
7. Press *SEND* button and wait for result