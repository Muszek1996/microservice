package com.muszek.microservice.restConsumerIMDB;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import org.springframework.stereotype.Service;

@Service
public class ActorConsumer extends Thread implements Consume,Runnable {
        ConsumerAndErrorChecker consumerAndErrorChecker = new ConsumerAndErrorChecker();
    @Override
    public Actor consume(final String id) {
        String path = "https://java.kisim.eu.org/actors/" + id;
        Actor actor = consumerAndErrorChecker.get(path,Actor.class);
        return actor;
    }




}
