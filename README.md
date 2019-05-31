# Trading bot

### Description
A very basic Trading Bot that tracks the price of a certain product and will execute a pre-defined trade in said 
product when it reaches a given price. After the product has been bought the Trading Bot will keep on tracking the 
prices and execute a sell order when a certain price is hit.

### Configuration
Trading bot has multiple configuration properties which consumed by Spring using `@ConfigurationProperties` classes.

You have to provide the next properties using any way of providing externalized properties for Spring  
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html   
For example, you can create an `application.properties` file and place it with executable jar of the bot.

Properties are divided in two main groups: 
- Connection properties.  
Here you can specify product feed and  websocket urls and trading api urls as well as security 
and header parameters.  
By default bot will use `https://api.dev.getbux.com`environment.

```properties
# Authorization header used in both websocket and rest api connections
trading.bot.connection.authorization=
# Accepted language header used in both websocket and rest api connections
trading.bot.connection.language=
# Websocket product feed connection url
trading.bot.connection.product-feed-url=
# API url for buy trading requests
trading.bot.connection.buy-api-url=
# API url for close trading requests
trading.bot.connection.close-api-url=
# Content type header used in both websocket and rest api connections
trading.bot.connection.content-type=
```

- Product properties.   
These are the properties for bot's product configuration.

```properties
# Product id to trade
trading.bot.product.product-id=
# Maximum buying price. Bot will buy provided product when current quote is equal to this price
trading.bot.product.buying-price=
# The price when bot will close a position with profit.
trading.bot.product.lower-limit-price=
# The price when bot will close a position with loss.
trading.bot.product.upper-limit-price=
```

##### Notes 
The relation between prices prices is validated by the next rule:  
lower-limit-price < buying-price < upper-limit-price
    price.

  
    
  

### Technologies
- Spring Boot
- Slf4j
- Maven
- Lombok        
- JUnit
- Mockito

### Build 

Create an executable Spring Boot jar  
`./mvnw clean package spring-boot:repackage`

### Run
`./target/trading_bot-0.0.1-SNAPSHOT-exec.jar`
   
Application runs on 8077 port. 
