package org.example.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    public static class Edge {
        public int source;
        public int destination;
        public int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    private final int vertices;
    private final List<Edge> edges;
    private final List<List<Edge>> adjacencyList;

    public Graph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("Vertices cannot be negative");
        }

        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyList = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        checkVertex(source);
        checkVertex(destination);

        if (weight < 0) {
            throw new IllegalArgumentException(
                    "Edge weight cannot be negative"
            );
        }

        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);

        adjacencyList.get(source).add(edge);

        adjacencyList.get(destination).add(
                new Edge(destination, source, weight)
        );
    }

    private void checkVertex(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException(
                    "Invalid vertex: " + vertex
            );
        }
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        checkVertex(vertex);
        return adjacencyList.get(vertex);
    }
}
