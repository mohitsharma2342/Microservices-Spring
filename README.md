# Microservices-Spring
All Microservices Repo with Cloud Config Server,Api Gateway


Microservice :- 
First create services
and service registery
then replaced port with service name


Cloud Config Server :- 
First we create User service and DEPARTMENT-SERVICE
we will register them on service registery

in application properties we duplicate code for registering service 

we will manage this Cloud Config Server 

We created another microservice cloud config server
in its main class we added below annotation

@EnableConfigServer

Next Step 2 we will create git repo 
in that repo created one file application.yml
 and added Common configuration in file
  eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      default-zone: http://localhost:8761/eureka
	  
	 
Next Step 3	 
	to get the info from git repo we will do the configuration in cloud config microservice 
	spring:
  application:
    name: CONFIG-SERVER
  cloud:
    config:
      server:
        git:
          uri: https://github.com/mohitsharma2342/config-server/
          clone-on-start: true

Next 4
 and will add one more dependency in all pom to talk with cloud config server
   <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>
Next Step 5
 now to bootsrap the configuration we will add bootstrap.yml file in user and department
 by which microservecies will communicate with cloud config server
 spring:
  cloud:
    config:
      enabled: true
      uri: http://localhost:9001
	  
next step 6 we will remove the common service registeration configuration from all microservices





Api Gatway :- 
  in which we defined routes first in application.yml file
  spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:
        - id: USER-SERVICE
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user/**   
        - id: DEPARTMENT-SERVICE
          uri: lb://DEPARTMENT-SERVICE
          predicates:
            - Path=/department/**
			
			
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
         
Now  when there is chances that our one of the microservice is not working Then circuit breaker comes in picture
So what circuit breaker does it identifies which of the microservice is not running So it will run the available fallback methods 

Fot that we will use hystrix liberay as circuit breaker server to see which all services running which all service are not running

To implement sytrix we use below annotation on api-gateway main class
@EnableHystrix
So now we will create fallBackController in api-gateway So whenever our service is down it will redirected to that perticuler controller
 In that conroller create two method for our two microservices user and department

So To call this method we will do the configuration in application.yml file . we will add the filters along with our patterns

spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:
        - id: USER-SERVICE
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user/**
          filters:
            - name: CircuitBreaker
              args:
                name: USER-SERVICE
                fallbackuri: forward:/userServiceFallBack
        - id: DEPARTMENT-SERVICE
          uri: lb://DEPARTMENT-SERVICE
          predicates:
            - Path=/department/**
          filters:
            - name: CircuitBreaker
              args:
                name: DEPARTMENT-SERVICE
                fallbackuri: forward:/departmentServiceFallBack
				
So Now we will define in how many seconds our this fallback will invoke if our services doesn't work 

hystrix:
  command:
    fallbackcmd:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 4000

Hystrix dashboard pending :-

