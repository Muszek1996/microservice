package com.muszek.microservice.shortestPathOperations;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import com.muszek.microservice.fieldsOfIMDB.Movie;
import com.muszek.microservice.restConsumerIMDB.MovieConsumer;
import com.muszek.microservice.restConsumerIMDB.MoviesByActorConsumer;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FindShortestPath {
    private final MoviesByActorConsumer moviesByActorConsumer;
    private final MovieConsumer movieConsumer;
    private List<Movie> movies;
    private Map<Actor, List<Movie>> moviesOfActor;
    private List<Movie> aMovies;
    private List<Actor> actorsTmp;
    private Map<String, Actor> actorsToHandle;
    private Map<String, Actor> handledActors;
    List<GraphPath<Actor, Movie>> graphPaths;

    @Autowired
    public FindShortestPath(MoviesByActorConsumer moviesByActorConsumer, MovieConsumer movieConsumer) {
        this.moviesByActorConsumer = moviesByActorConsumer;
        this.movieConsumer = movieConsumer;
        actorsTmp = new LinkedList<>();
        graphPaths = new LinkedList<>();
        actorsToHandle = new TreeMap<>();
        aMovies = new LinkedList<>();
        movies = new LinkedList<>();
        handledActors = new TreeMap<>();
        moviesOfActor = new HashMap<Actor, List<Movie>>();

    }

    public GraphPath<Actor, Movie> shortestPath(Actor actorA, Actor actorB) {
        for (GraphPath<Actor, Movie> gP : graphPaths
                ) {
            if (BellmanFordAlgorithm.shortestPath(gP, actorA, actorB) != null)
                return gP;
        }


        Graph<Actor, Movie> g = new SimpleGraph<>(Movie.class);
        g.addVertex(actorA);
        g.addVertex(actorB);
        actorsToHandle.put(actorA.getId(), actorA);
        actorsToHandle.put(actorB.getId(), actorB);

        boolean pathFound = false;
        int distance = 6;

        while (!pathFound && --distance > 0) {
            ExecutorService executor = Executors.newCachedThreadPool();
            List<Future<AbstractMap.SimpleImmutableEntry<Actor, List<Movie>>>> futureMovieList = new LinkedList<>();
            List<Future<AbstractMap.SimpleImmutableEntry<Actor, Movie>>> futureActors = new LinkedList<>();
            for (Actor a : actorsToHandle.values()
                    ) {
                if (a == null) continue;
                futureMovieList.add(executor.submit(new MoviesByActorConsumer(a)));
                actorsToHandle.replace(a.getId(), null);
            }

            futureMovieList.forEach((callback) -> {
                try {
                    AbstractMap.SimpleImmutableEntry<Actor, List<Movie>> pair = callback.get();
                    moviesOfActor.put(pair.getKey(), pair.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.toString();
                }
            });


            moviesOfActor.forEach((actor, listOfActorMovies) -> {
                listOfActorMovies.forEach(movie -> {
                    futureActors.add(executor.submit(new MovieConsumer(actor, movie.getId())));
                });
            });


            futureActors.forEach((callback) -> {
                try {
                    AbstractMap.SimpleImmutableEntry<Actor, Movie> pair = callback.get();
                    pair.getValue().getActors().forEach((act) -> {
                        if (!act.equals(pair.getKey())) {
                            g.addVertex(pair.getKey());
                            g.addVertex(act);
                            actorsToHandle.put(act.getId(), act);
                            g.addEdge(pair.getKey(), act, pair.getValue());
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });

            GraphPath<Actor, Movie> path = BellmanFordAlgorithm.shortestPath(g, actorA, actorB);
            if (path != null) {
                pathFound = true;
                graphPaths.add(path);
                clean();
                return path;
            }

        }


        clean();
        return null;
    }

    private void clean() {
        movies.clear();
        moviesOfActor.clear();
        aMovies.clear();
        actorsTmp.clear();
        actorsToHandle.clear();
        handledActors.clear();
    }
}
