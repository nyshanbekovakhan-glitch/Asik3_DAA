package org.example;

import org.example.connectivity.SCC;
import org.example.connectivity.TopologicalSort;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;
import org.example.mst.Kruskal;
import org.example.mst.Prim;
import org.example.shortestpath.DAGShortestPath;
import org.example.shortestpath.Dijkstra;

import java.util.List;

public class AlgorithmTests {

    public static void runAll() {
        testSingleVertex();
        testDisconnectedGraph();
        testMultipleMST();
        testDuplicateWeights();
        testTopologicalSort();
        testCycleDetection();
        testSCC();
        testDijkstraUnreachable();
        testDijkstraShortestPath();
        testDAGShortestPath();
        testDAGNegativeEdge();
        testNegativeWeightRejected();

        System.out.println("All algorithm tests passed.");
    }

    private static void testSingleVertex() {
        Graph graph = new Graph(1);

        Kruskal.Result kruskal =
                Kruskal.findMST(graph);

        Kruskal.Result prim =
                Prim.findMST(graph);

        check(kruskal.connected, "Single vertex Kruskal");
        check(prim.connected, "Single vertex Prim");
        check(kruskal.totalWeight == 0, "Single vertex weight");
        check(prim.totalWeight == 0, "Single vertex Prim weight");
    }

    private static void testDisconnectedGraph() {
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 3);
        graph.addEdge(2, 3, 5);

        Kruskal.Result kruskal =
                Kruskal.findMST(graph);

        Kruskal.Result prim =
                Prim.findMST(graph);

        check(!kruskal.connected, "Disconnected Kruskal");
        check(!prim.connected, "Disconnected Prim");
    }

    private static void testMultipleMST() {
        Graph graph = new Graph(3);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(0, 2, 1);

        Kruskal.Result kruskal =
                Kruskal.findMST(graph);

        Kruskal.Result prim =
                Prim.findMST(graph);

        check(kruskal.connected, "Multiple MST Kruskal");
        check(prim.connected, "Multiple MST Prim");
        check(kruskal.totalWeight == 2, "Multiple MST weight");
        check(prim.totalWeight == 2, "Multiple MST Prim weight");
    }

    private static void testDuplicateWeights() {
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 2);
        graph.addEdge(0, 3, 2);
        graph.addEdge(0, 2, 2);

        Kruskal.Result kruskal =
                Kruskal.findMST(graph);

        Kruskal.Result prim =
                Prim.findMST(graph);

        check(kruskal.totalWeight == 6, "Duplicate weights Kruskal");
        check(prim.totalWeight == 6, "Duplicate weights Prim");
    }

    private static void testTopologicalSort() {
        DirectedGraph graph = new DirectedGraph(4);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        TopologicalSort.Result result =
                TopologicalSort.sort(graph);

        check(!result.hasCycle, "DAG cycle check");
        check(result.order.size() == 4, "DAG order size");
        check(isBefore(result.order, 0, 1), "Topological 0 before 1");
        check(isBefore(result.order, 0, 2), "Topological 0 before 2");
        check(isBefore(result.order, 1, 3), "Topological 1 before 3");
        check(isBefore(result.order, 2, 3), "Topological 2 before 3");
    }

    private static void testCycleDetection() {
        DirectedGraph graph = new DirectedGraph(3);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TopologicalSort.Result result =
                TopologicalSort.sort(graph);

        check(result.hasCycle, "Cycle detection");
    }

    private static void testSCC() {
        DirectedGraph graph = new DirectedGraph(5);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        List<List<Integer>> components =
                SCC.findComponents(graph);

        check(components.size() == 2, "SCC component count");
    }

    private static void testDijkstraUnreachable() {
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 2);
        graph.addEdge(2, 3, 4);

        Dijkstra.Result result =
                Dijkstra.findShortestPaths(graph, 0);

        check(result.distances[1] == 2, "Dijkstra reachable");
        check(
                result.distances[2] == Long.MAX_VALUE,
                "Dijkstra unreachable"
        );
        check(
                result.getPath(2).isEmpty(),
                "Dijkstra unreachable path"
        );
    }

    private static void testDijkstraShortestPath() {
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 3);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 8);

        Dijkstra.Result result =
                Dijkstra.findShortestPaths(graph, 0);

        check(result.distances[3] == 6, "Dijkstra shortest distance");
        check(
                result.getPath(3).equals(List.of(0, 2, 1, 3)),
                "Dijkstra shortest path"
        );
    }

    private static void testDAGShortestPath() {
        DirectedGraph graph = new DirectedGraph(4);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, -2);
        graph.addEdge(2, 3, 3);

        DAGShortestPath.Result result =
                DAGShortestPath.findShortestPaths(graph, 0);

        check(result.distances[3] == 5, "DAG shortest distance");
    }

    private static void testDAGNegativeEdge() {
        DirectedGraph graph = new DirectedGraph(3);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, -2);

        DAGShortestPath.Result result =
                DAGShortestPath.findShortestPaths(graph, 0);

        check(result.distances[2] == 2, "DAG negative edge");
    }

    private static void testNegativeWeightRejected() {
        Graph graph = new Graph(2);

        boolean rejected = false;

        try {
            graph.addEdge(0, 1, -1);
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }

        check(rejected, "Negative weight rejection");
    }

    private static boolean isBefore(
            List<Integer> order,
            int first,
            int second
    ) {
        return order.indexOf(first) < order.indexOf(second);
    }

    private static void check(
            boolean condition,
            String testName
    ) {
        if (!condition) {
            throw new IllegalStateException(
                    "Test failed: " + testName
            );
        }
    }
    public static void main(String[] args) {
        runAll();
    }
}
