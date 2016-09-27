## user-manager

#### Overview ####

The application is an example of managing users over a rest api, with usages of
* [spring boot](http://projects.spring.io/spring-boot/) for exposing a rest api
* [swagger](http://swagger.io/) for documenting a rest api
* [rest assured](https://github.com/rest-assured/rest-assured) for testing a rest api
* [spring boot-actuator](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-actuator) for basic usage of application monitoring 
* [spring amqp](http://docs.spring.io/spring-amqp/reference/htmlsingle/) and [rabbit mq](https://www.rabbitmq.com/) for publising messages

#### Prerequisites ####
In order to run this application, [rabbit mq](https://www.rabbitmq.com/download.html) needs to be installed

#### Quick start ####
* execute the tests: ./gradlew clean test
* start the application: ./gradlew bootRun (the application will start on localhost:8087, see application.properties for changing the port)
