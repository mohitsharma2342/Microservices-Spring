# Microservices-Spring


1.Microservice Architecture                                                                                                                                     
2.Service Registery(Netflix Eureka)                                                                                                                             
3.Cloud Config Server                                                                                                                                           
4.Api-Gateway(Zuul)                                                                                                                                             
5.Circuit Breaker(hystrix)                                                                                                                                       
6.Hystrix DashBoard                                                                                                                                             
7.ZipKin                                                                                                                                                         


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
Zipkin pending



Micro service design pattern 
https://www.linkedin.com/posts/hinaaroraa_microservicepdf-activity-7223182895543152641-W2hh?utm_source=share&utm_medium=member_android

**Saga design pattern**

This pattern is used to manage and maintain data consistency across multiple microservices
It is responsible for managing the overall transaction and coordinating the compensating actions required in case of any failures. It is useful when dealing with complex business processes that involve multiple services, such as order processing, shipping, and billing. 

**Circuit Breaker Pattern**
Circuit Breaker pattern in microservices is a fault-tolerance mechanism that monitors and controls interactions between services
Main purpose is stop the request and response process if a service is not working, as the name suggests.

**1. Closed State**
In the Closed state, the circuit breaker operates normally, allowing requests to flow through between services.
During this phase, the circuit breaker monitors the health of the downstream service by collecting and analyzing metrics such as response times, error rates, or timeouts.
If the monitored metrics remain within acceptable thresholds, indicating that the downstream service is healthy, the circuit breaker stays in the Closed state and continues to forward requests.
**2. Open State**
When the monitored metrics breach predetermined thresholds, signaling potential issues with the downstream service, the circuit breaker transitions to the Open state.
In the Open state, the circuit breaker immediately stops forwarding requests to the failing service, effectively isolating it.
Instead of allowing requests to reach the failing service and potentially exacerbate the issue, the circuit breaker provides a predefined fallback response or an error message to the caller.
This helps prevent cascading failures and maintains system stability by ensuring that clients receive timely feedback, even when services encounter issues.
**3. Half-Open State**
After a specified timeout period in the Open state, transitions to Half-Open state.
Allows a limited number of trial requests to pass through to the downstream service.
Monitors responses to determine service recovery.
If trial requests succeed, indicating service recovery, transitions back to Closed state.
If trial requests fail, service issues persist.
May transition back to Open state or remain in Half-Open state for further evaluation.
https://www.geeksforgeeks.org/what-is-circuit-breaker-pattern-in-microservices/

**API gateway**
An API gateway is an API management tool . The API Gateway is a server. that sits between a client and a collection of backend services.
which will be a single entry point into a system.
Which is responsible for request routing, composition, and protocol translation.
All the requests made by the client go through the API Gateway. After that, the API Gateway routes requests to the appropriate microservice
It also has other responsibilities such as authentication, monitoring, load balancing, caching, request shaping and management, and static response handling.

**Load Balancer**

Load balancing refers to efficiently distributing the incoming network traffic 
across a group of backend servers (multiple instances of the service).In

Server side load balancing is distributing the incoming requests towards multiple instances of the service.

Client side load balancing is distributing the outgoing request from the client itself.


**1. Server-side Load Balancing**
In Server-side load balancing, the instances of the service are deployed on multiple servers and then a load balancer is put in front of them. It is generally a hardware load balancer. All the incoming requests traffic firstly comes to this load balancer acting as a middle component. It then decides to which server a particular request must be directed to based on some algorithm.

Server side load balancing

Disadvantages of Server-side load balancing
Server side load balancer acts as a single point of failure as if it fails, all the instances of the microservice becomes inaccessible as only load balancer has the list of servers.

Since each microservice will have a separate load balancer, the overall complexity of the system increases and it becomes hard to manage.

The network latency increases as the number of hops for the request increases from one to two with the load balancer, one to the load balancer and then another from load balancer to the microservice.

**2. Client-side Load Balancing**
The instances of the service are deployed on multiple servers. 
Load balancer's logic is part of the client itself,
 it holds the list of servers and decides to which server a particular request must be directed .

Spring client side load balancing
Spring Netflix Eureka has a built-in client side load balancer called Ribbon.
Ribbon can automatically be configured by registering RestTemplate as a bean and annotating it with @LoadBalanced.
In this example we will focus on how to access a microservice instance transparently using RestTemplate and @LoadBalance.



