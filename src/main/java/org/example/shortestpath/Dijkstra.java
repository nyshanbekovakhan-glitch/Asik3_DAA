package org.example.shortestpath;

import org.example.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {

    private static final long INF = Long.MAX_VALUE;

    public static class Node {
        public final int vertex;
        public final long distance;

        public Node(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public static class Result {
        public final long[] distances;
        public final int[] previous;
        public final long edgeExaminations;

        public Result(
                long[] distances,
                int[] previous,
                long edgeExaminations
        ) {
            this.distances = distances;
            this.previous = previous;
            this.edgeExaminations = edgeExaminations;
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
            Graph graph,
            int source
    ) {
        int vertices = graph.getVertices();

        if (source < 0 || source >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid source vertex"
            );
        }

        long[] distances = new long[vertices];
        int[] previous = new int[vertices];

        java.util.Arrays.fill(distances, INF);
        java.util.Arrays.fill(previous, -1);

        PriorityQueue<Node> queue =
                new PriorityQueue<>(
                        (a, b) -> Long.compare(
                                a.distance,
                                b.distance
                        )
                );

        distances[source] = 0;
        queue.offer(new Node(source, 0));

        long edgeExaminations = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.distance
                    != distances[current.vertex]) {
                continue;
            }

            for (Graph.Edge edge :
                    graph.getAdjacentEdges(current.vertex)) {

                edgeExaminations++;

                int next = edge.destination;

                long newDistance =
                        distances[current.vertex]
                                + edge.weight;

                if (newDistance < distances[next]) {
                    distances[next] = newDistance;
                    previous[next] = current.vertex;

                    queue.offer(
                            new Node(next, newDistance)
                    );
                }
            }
        }

        return new Result(
                distances,
                previous,
                edgeExaminations
        );
    }
}
