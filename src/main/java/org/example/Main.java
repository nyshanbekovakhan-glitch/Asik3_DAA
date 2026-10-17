package org.example;

import org.example.connectivity.SCC;
import org.example.connectivity.TopologicalSort;
import org.example.experiments.Benchmark;
import org.example.experiments.GraphGenerator;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;
import org.example.mst.Kruskal;
import org.example.mst.Prim;
import org.example.shortestpath.DAGShortestPath;
import org.example.shortestpath.Dijkstra;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        AlgorithmTests.runAll();
        Benchmark.runAll();
        testMST();
        testTopologicalSort();
        testSCC();
        testDijkstra();
        testDAGShortestPath();

        System.out.println();
        System.out.println("Starting MST benchmark");

        Benchmark.runMSTBenchmark("mst_results.csv");
    }

    private static void testMST() {
        System.out.println("\n=== MST ===");

        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 7);

        Kruskal.Result kruskal =
                Kruskal.findMST(graph);

        Kruskal.Result prim =
                Prim.findMST(graph);

        System.out.println("Kruskal weight: "
                + kruskal.totalWeight);

        System.out.println("Prim weight: "
                + prim.totalWeight);

        System.out.println("Connected: "
                + kruskal.connected);

        System.out.println("Same weight: "
                + (kruskal.totalWeight == prim.totalWeight));
    }

    private static void testTopologicalSort() {
        System.out.println("\n=== Topological Sort ===");

        DirectedGraph graph = new DirectedGraph(5);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);

        TopologicalSort.Result result =
                TopologicalSort.sort(graph);

        System.out.println("Order: " + result.order);
        System.out.println("Has cycle: " + result.hasCycle);

        DirectedGraph cyclicGraph =
                new DirectedGraph(3);

        cyclicGraph.addEdge(0, 1, 1);
        cyclicGraph.addEdge(1, 2, 1);
        cyclicGraph.addEdge(2, 0, 1);

        TopologicalSort.Result cycleResult =
                TopologicalSort.sort(cyclicGraph);

        System.out.println(
                "Cycle detected: " + cycleResult.hasCycle
        );
    }

    private static void testSCC() {
        System.out.println("\n=== Strongly Connected Components ===");

        DirectedGraph graph = new DirectedGraph(5);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        List<List<Integer>> components =
                SCC.findComponents(graph);

        System.out.println("Components: " + components);
        System.out.println("Component count: "
                + components.size());
    }

    private static void testDijkstra() {
        System.out.println("\n=== Dijkstra ===");

        Graph graph = new Graph(5);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);

        Dijkstra.Result result =
                Dijkstra.findShortestPaths(graph, 0);

        for (int vertex = 0;
             vertex < graph.getVertices();
             vertex++) {

            System.out.print("To " + vertex + ": ");

            if (result.distances[vertex] == Long.MAX_VALUE) {
                System.out.println("Unreachable");
            } else {
                System.out.println(
                        "distance = " + result.distances[vertex]
                                + ", path = " + result.getPath(vertex)
                );
            }
        }

        System.out.println(
                "Edge examinations: "
                        + result.edgeExaminations
        );
    }

    private static void testDAGShortestPath() {
        System.out.println("\n=== DAG Shortest Path ===");

        DirectedGraph graph =
                new DirectedGraph(5);

        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 6);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);

        DAGShortestPath.Result result =
                DAGShortestPath.findShortestPaths(
                        graph,
                        0
                );

        for (int vertex = 0;
             vertex < graph.getVertices();
             vertex++) {

            System.out.print("To " + vertex + ": ");

            if (result.distances[vertex] == Long.MAX_VALUE) {
                System.out.println("Unreachable");
            } else {
                System.out.println(
                        "distance = " + result.distances[vertex]
                                + ", path = " + result.getPath(vertex)
                );
            }
        }
    }
}
