package com.muszek.microservice.restConsumerIMDB;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import com.muszek.microservice.fieldsOfIMDB.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Callable;

@Service
public class MoviesByActorConsumer implements Callable<AbstractMap.SimpleImmutableEntry<Actor,List<Movie>>> {
    Actor actor;
    List<Movie> movies;

    MoviesByActorConsumer(){

    }

    public MoviesByActorConsumer(Actor actor){
        this.actor= actor;
    }

    static ConsumerAndErrorChecker consumerAndErrorChecker = new ConsumerAndErrorChecker();
    public List<Movie> consume(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String path = "https://java.kisim.eu.org/actors/" + id + "/movies";
        List<Movie> movies = Arrays.asList(consumerAndErrorChecker.get(path,Movie[].class));
        return movies;
    }


    @Override
    public AbstractMap.SimpleImmutableEntry<Actor,List<Movie>> call() throws Exception {
        return new AbstractMap.SimpleImmutableEntry<Actor,List<Movie>>(actor,consume(actor.getId()));
    }
}
