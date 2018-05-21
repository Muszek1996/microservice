package com.muszek.microservice.shortestPathOperations;

import com.muszek.microservice.fieldsOfIMDB.Actor;
import com.muszek.microservice.fieldsOfIMDB.Movie;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;

import java.util.List;

abstract class BellmanFordAlgorithm {

    public static GraphPath<Actor,Movie> shortestPath(Graph<Actor,Movie> graph,Actor actorA,Actor actorB){
        BellmanFordShortestPath<Actor,Movie> bfsp = new BellmanFordShortestPath<Actor,Movie>(graph);
        GraphPath<Actor, Movie> shortestPath = bfsp.getPath(actorA,actorB);
        if(shortestPath==null) return null;
        List<Movie> edges = shortestPath.getEdgeList();
        List<Actor> actors = shortestPath.getVertexList();
        for(int i = 0; i < actors.size(); ++i){
            if(i == actors.size()-1)
                System.out.print(actors.get(i));
            else
                System.out.print(actors.get(i) + " -> " + edges.get(i).toString() + " -> ");
        }

        return shortestPath;
    }

    public static GraphPath<Actor,Movie> shortestPath(GraphPath<Actor,Movie> graph,Actor actorA,Actor actorB){

        boolean a,b,c,d;
        a=graph.getStartVertex().sameActor(actorA);
        b=graph.getEndVertex().sameActor(actorB);
        c=graph.getStartVertex().sameActor(actorB);
        d=graph.getEndVertex().sameActor(actorA);
        if((a&&b)||(c&&d)){
            return graph;
        }
        return null;
    }

}
