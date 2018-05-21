package com.muszek.microservice.restConsumerIMDB;

import com.muszek.microservice.fieldsOfIMDB.Movie;

public interface Consume {
     Object consume(String id);
}
