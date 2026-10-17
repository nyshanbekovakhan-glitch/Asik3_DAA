package org.example.graph;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph {

    private final int vertices;
    private final List<List<Graph.Edge>> adjacencyList;
    private final List<Graph.Edge> edges;

    public DirectedGraph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException(
                    "Vertices cannot be negative"
            );
        }

        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>();
        this.edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        checkVertex(source);
        checkVertex(destination);
        Graph.Edge edge =
                new Graph.Edge(source, destination, weight);

        edges.add(edge);
        adjacencyList.get(source).add(edge);
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

    public List<Graph.Edge> getEdges() {
        return edges;
    }

    public List<Graph.Edge> getAdjacentEdges(int vertex) {
        checkVertex(vertex);
        return adjacencyList.get(vertex);
    }
}
