package org.example.experiments;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;
import java.util.Random;
public class GraphGenerator {
    public static Graph connectedGraph(
            int vertices,
            int extraEdges,
            long seed
    ) {
        if (vertices < 1 || extraEdges < 0) {
            throw new IllegalArgumentException(
                    "Invalid graph parameters"
            );
        }
        long maxEdges =
                (long) vertices * (vertices - 1) / 2;
        if ((long) vertices - 1 + extraEdges > maxEdges) {
            throw new IllegalArgumentException(
                    "Too many edges for a simple graph"
            );
        }
        Graph graph = new Graph(vertices);
        Random random = new Random(seed);
        for (int vertex = 1; vertex < vertices; vertex++) {
            int parent = random.nextInt(vertex);
            int weight = random.nextInt(100) + 1;
            graph.addEdge(vertex, parent, weight);
        }
        int added = 0;
        while (added < extraEdges) {
            int source = random.nextInt(vertices);
            int destination = random.nextInt(vertices);
            if (source == destination
                    || hasEdge(graph, source, destination)) {
                continue;
            }
            int weight = random.nextInt(100) + 1;
            graph.addEdge(source, destination, weight);
            added++;
        }
        return graph;
    }
    private static boolean hasEdge(
            Graph graph,
            int source,
            int destination
    ) {
        for (Graph.Edge edge : graph.getEdges()) {
            if ((edge.source == source
                    && edge.destination == destination)
                    || (edge.source == destination
                    && edge.destination == source)) {
                return true;
            }
        }
        return false;
    }
    public static DirectedGraph dag(
            int vertices,
            int edgeCount,
            long seed
    ) {
        if (vertices < 0 || edgeCount < 0) {
            throw new IllegalArgumentException(
                    "Invalid graph parameters"
            );
        }
        long maxEdges =
                (long) vertices * (vertices - 1) / 2;
        if (edgeCount > maxEdges) {
            throw new IllegalArgumentException(
                    "Too many edges for a DAG"
            );
        }
        DirectedGraph graph =
                new DirectedGraph(vertices);
        Random random = new Random(seed);
        boolean[][] exists =
                new boolean[vertices][vertices];
        int added = 0;
        while (added < edgeCount) {
            int source = random.nextInt(vertices);
            int destination = random.nextInt(vertices);
            if (source >= destination
                    || exists[source][destination]) {
                continue;
            }
            int weight = random.nextInt(100) + 1;
            graph.addEdge(source, destination, weight);
            exists[source][destination] = true;
            added++;
        }
        return graph;
    }
}
