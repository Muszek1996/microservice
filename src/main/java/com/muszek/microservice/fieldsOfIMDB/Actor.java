package com.muszek.microservice.fieldsOfIMDB;

import com.muszek.microservice.restConsumerIMDB.MoviesByActorConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Actor implements Comparable {
    static int iterator = 0;

    public int getNumber() {
        return number;
    }

    int number;
    private String name, id;
    private List<Movie> movies;

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Actor actor = (Actor) obj;
        return Objects.equals(id, actor.id);
    }

    public boolean sameActor(Object obj) {
        if (this.getId().equals(((Actor)obj).getId())) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Actor actor = (Actor) obj;
        return Objects.equals(id, actor.id);
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        String ret = getName() + "(" + getId() + ")";
    /*    if (movies != null){
            ret+="\n{";
            for (Movie m : movies
                    ) {
                ret += m.toString();
            }
            ret += "}";
        }*/

        return ret;
    }


    public Actor() {
        number = ++iterator;
    }

    @Override
    public int compareTo(Object o) {
        if (number > ((Actor) o).number) return -1;
        if (equals(o)) return 0;
        return 1;
    }
}
