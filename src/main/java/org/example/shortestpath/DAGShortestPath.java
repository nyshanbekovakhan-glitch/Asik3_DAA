package org.example.shortestpath;

import org.example.connectivity.TopologicalSort;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DAGShortestPath {

    private static final long INF = Long.MAX_VALUE;

    public static class Result {
        public final long[] distances;
        public final int[] previous;

        public Result(long[] distances, int[] previous) {
            this.distances = distances;
            this.previous = previous;
        }

        public List<Integer> getPath(int destination) {
            if (destination < 0
                    || destination >= distances.length) {
                throw new IllegalArgumentException(
                        "Invalid destination"
                );
            }

            if (distances[destination] == INF) {
                return Collections.emptyList();
            }

            List<Integer> path = new ArrayList<>();
            int current = destination;

            while (current != -1) {
                path.add(current);
                current = previous[current];
            }

            Collections.reverse(path);
            return path;
        }
    }

    public static Result findShortestPaths(
            DirectedGraph graph,
            int source
    ) {
        int vertices = graph.getVertices();

        if (source < 0 || source >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid source vertex"
            );
        }

        TopologicalSort.Result topo =
                TopologicalSort.sort(graph);

        if (topo.hasCycle) {
            throw new IllegalArgumentException(
                    "Graph must be acyclic"
            );
        }

        long[] distances = new long[vertices];
        int[] previous = new int[vertices];

        java.util.Arrays.fill(distances, INF);
        java.util.Arrays.fill(previous, -1);

        distances[source] = 0;

        for (int vertex : topo.order) {
            if (distances[vertex] == INF) {
                continue;
            }

            for (Graph.Edge edge :
                    graph.getAdjacentEdges(vertex)) {

                long newDistance =
                        distances[vertex] + edge.weight;

                if (newDistance < distances[edge.destination]) {
                    distances[edge.destination] = newDistance;
                    previous[edge.destination] = vertex;
                }
            }
        }

        return new Result(distances, previous);
    }
}
