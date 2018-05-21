package com.muszek.microservice.httpRequestHandler;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import com.muszek.microservice.fieldsOfIMDB.Movie;
import com.muszek.microservice.shortestPathOperations.FindShortestPath;
import com.muszek.microservice.restConsumerIMDB.ActorConsumer;
import com.muszek.microservice.restConsumerIMDB.MovieConsumer;
import com.muszek.microservice.restConsumerIMDB.MoviesByActorConsumer;
import net.minidev.json.JSONArray;
import org.jgrapht.GraphPath;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class httpController {

    private final ActorConsumer actorConsumer;
    private final MovieConsumer movieConsumer;
    private final MoviesByActorConsumer moviesByActorConsumer;
    private final FindShortestPath findShortestPath;


    public httpController(ActorConsumer aC, MovieConsumer mC, MoviesByActorConsumer mBAC, FindShortestPath gC){
        this.actorConsumer =aC;
        this.movieConsumer =mC;
        this.moviesByActorConsumer = mBAC;
        this.findShortestPath = gC;
    }


    @RequestMapping(value = "path/{actorA}/{actorB}", method = RequestMethod.GET)
    public String getActorsShortestPath(@PathVariable("actorA") String actorA,
                                        @PathVariable("actorB") String actorB) {
        GraphPath<Actor,Movie> a = findShortestPath.shortestPath(actorConsumer.consume(actorA), actorConsumer.consume(actorB));
        if(a!=null){
            String jsonReturnValue = "";
            List<Movie> edges = a.getEdgeList();
            List<Actor> actors = a.getVertexList();
            for(int i = 0; i < actors.size(); ++i){
                if(i == actors.size()-1)
                 jsonReturnValue += actors.get(i);
                else
                    jsonReturnValue +=actors.get(i)+ " -> " + edges.get(i).toString() + " -> ";
            }
            return jsonReturnValue;
        }
        return "No path";
    }

    @RequestMapping(value = "path/{actorA}/{actorB}/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getActorsShortestPathJson(@PathVariable("actorA") String actorA,
                                                  @PathVariable("actorB") String actorB) {
        GraphPath<Actor,Movie> a = findShortestPath.shortestPath(actorConsumer.consume(actorA), actorConsumer.consume(actorB));
        if(a!=null){
            List<String> retval = new ArrayList<>();
            List<Movie> edges = a.getEdgeList();
            List<Actor> actors = a.getVertexList();
            for(int i = 0; i < actors.size()-1; ++i){
                retval.add(actors.get(i).toString()+ " -> " + edges.get(i).toString());
                retval.add(edges.get(i).toString()+ " <- " + actors.get(i+1).toString());
            }

            return retval;
        }
        return null;
    }

    @RequestMapping(value = "path/{actorA}/{actorB}/json2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getActorsShortestPathJson2(@PathVariable("actorA") String actorA,
                                                  @PathVariable("actorB") String actorB) {
        GraphPath<Actor,Movie> a = findShortestPath.shortestPath(actorConsumer.consume(actorA), actorConsumer.consume(actorB));
        if(a!=null){
            List<String> retval = new ArrayList<>();
            List<Movie> edges = a.getEdgeList();
            List<Actor> actors = a.getVertexList();
            for(int i = 0; i < actors.size(); ++i){
                if(i == actors.size()-1)
                retval.add(actors.get(i).toString());
                else{
                    retval.add(actors.get(i).toString());
                    retval.add(edges.get(i).toString());
                }

            }

            return retval;
        }
        return null;
    }

    @RequestMapping(value = "movies/{imdb}", method = RequestMethod.GET)
    public String getMovie(@PathVariable("imdb")String movieId) {

        return movieConsumer.consume(movieId).toString();
    }
    @RequestMapping(value = "actors/{imdb}", method = RequestMethod.GET)
    public String getActor(@PathVariable("imdb")String actorId) {

        return actorConsumer.consume(actorId).toString();
    }
    @RequestMapping(value = "actors/{imdb}/movies", method = RequestMethod.GET)
    public String getMoviesByActor(@PathVariable("imdb")String actorId) {
       String str = "";
        List<Movie> movies = moviesByActorConsumer.consume(actorId);
        for (Movie m:movies
             ) {
            str+= m.toString();
        }
        return str;
    }

}
