## Install local environment
- Install docker
- Run rabbitmq container
```
docker run -it --name rabbitmq --restart always -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### Check the rabbitmq server
```
http://localhost:15672/
user: guest
pass: guest
```


#### Compile
``
javac -cp amqp-client-5.7.1.jar Consumer/src/Receive.java
``

#### Execute
``
java -cp .:amqp-client-5.7.1.jar:slf4j-api-1.7.26.jar:slf4j-simple-1.7.26.jar Receive
``