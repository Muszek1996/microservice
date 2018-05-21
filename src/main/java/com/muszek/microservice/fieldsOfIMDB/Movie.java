package com.muszek.microservice.fieldsOfIMDB;

import com.muszek.microservice.restConsumerIMDB.MovieConsumer;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;


public class Movie extends DefaultEdge{
    private String title,id;
    private List<Actor> actors;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        String returnval = getTitle()+"("+getId()+")";
      /*  if(actors != null){
           returnval+=  "\n{";
                for (Actor a:actors) {
                    returnval += a.toString()+"\n";
                }
                returnval += "}\n";
        }*/


        return returnval;
    }

}
