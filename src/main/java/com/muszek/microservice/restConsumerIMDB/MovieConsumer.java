package com.muszek.microservice.restConsumerIMDB;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import com.muszek.microservice.fieldsOfIMDB.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.AbstractMap;
import java.util.concurrent.Callable;

@Service
public class MovieConsumer implements Consume, Callable<AbstractMap.SimpleImmutableEntry<Actor,Movie>> {
    ConsumerAndErrorChecker consumerAndErrorChecker = new ConsumerAndErrorChecker();
    String id;
    Actor act;
    MovieConsumer() {
    }

    public MovieConsumer(Actor actor,String id) {
        this.id = id;
        this.act = actor;
    }

    @Override
    public Movie consume(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String path = "https://java.kisim.eu.org/movies/" + id;
        Movie movie = consumerAndErrorChecker.get(path, Movie.class);
        return movie;
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<Actor,Movie> call() throws Exception {
        return new AbstractMap.SimpleImmutableEntry<Actor,Movie>(act,consume(id));
    }
}

