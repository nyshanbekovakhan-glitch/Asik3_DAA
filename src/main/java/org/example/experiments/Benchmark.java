package org.example.experiments;

import org.example.connectivity.SCC;
import org.example.connectivity.TopologicalSort;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;
import org.example.mst.Kruskal;
import org.example.mst.Prim;
import org.example.shortestpath.DAGShortestPath;
import org.example.shortestpath.Dijkstra;

import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;

public class Benchmark {

    private static final int REPETITIONS = 5;

    private static final int[][] CONFIGURATIONS = {
            {10, 30},
            {100, 500},
            {1000, 5000}
    };

    public static void runAll() throws Exception {
        File directory = new File("results");
        directory.mkdirs();

        runMSTBenchmark("results/mst_results.csv");
        runConnectivityBenchmark(
                "results/connectivity_results.csv"
        );
        runShortestPathBenchmark(
                "results/shortest_path_results.csv"
        );
    }

    public static void runMSTBenchmark(String filename)
            throws Exception {

        try (PrintWriter writer =
                     new PrintWriter(new File(filename))) {

            writer.println(
                    "algorithm,vertices,edges,averageTimeNs,"
                            + "operations,totalWeight,connected"
            );

            for (int[] configuration : CONFIGURATIONS) {
                int vertices = configuration[0];
                int edges = configuration[1];

                long kruskalTime = 0;
                long primTime = 0;
                long kruskalOperations = 0;
                long primOperations = 0;
                long kruskalWeight = 0;
                long primWeight = 0;
                boolean connected = false;

                for (int r = 0; r < REPETITIONS; r++) {
                    Graph graph =
                            GraphGenerator.connectedGraph(
                                    vertices,
                                    edges - vertices + 1,
                                    42L + r
                            );

                    long start = System.nanoTime();
                    Kruskal.Result kruskal =
                            Kruskal.findMST(graph);
                    kruskalTime += System.nanoTime() - start;

                    start = System.nanoTime();
                    Kruskal.Result prim =
                            Prim.findMST(graph);
                    primTime += System.nanoTime() - start;

                    kruskalOperations +=
                            kruskal.edgeExaminations
                                    + kruskal.dsuOperations;

                    primOperations += prim.edgeExaminations;

                    kruskalWeight = kruskal.totalWeight;
                    primWeight = prim.totalWeight;
                    connected = kruskal.connected;

                    if (kruskal.totalWeight != prim.totalWeight
                            || !kruskal.connected
                            || !prim.connected) {
                        throw new IllegalStateException(
                                "MST algorithms disagree"
                        );
                    }
                }

                writer.printf(
                        Locale.US,
                        "Kruskal,%d,%d,%d,%d,%d,%b%n",
                        vertices,
                        edges,
                        kruskalTime / REPETITIONS,
                        kruskalOperations / REPETITIONS,
                        kruskalWeight,
                        connected
                );

                writer.printf(
                        Locale.US,
                        "Prim,%d,%d,%d,%d,%d,%b%n",
                        vertices,
                        edges,
                        primTime / REPETITIONS,
                        primOperations / REPETITIONS,
                        primWeight,
                        connected
                );
            }
        }

        System.out.println("MST benchmark completed.");
    }

    private static void runConnectivityBenchmark(
            String filename
    ) throws Exception {

        try (PrintWriter writer =
                     new PrintWriter(new File(filename))) {

            writer.println(
                    "algorithm,vertices,edges,averageTimeNs,operations"
            );

            for (int[] configuration : CONFIGURATIONS) {
                int vertices = configuration[0];
                int edges = configuration[1];

                long topoTime = 0;
                long sccTime = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    DirectedGraph graph =
                            GraphGenerator.dag(
                                    vertices,
                                    edges,
                                    100L + r
                            );

                    long start = System.nanoTime();
                    TopologicalSort.Result topo =
                            TopologicalSort.sort(graph);
                    topoTime += System.nanoTime() - start;

                    if (topo.hasCycle) {
                        throw new IllegalStateException(
                                "Generated DAG has a cycle"
                        );
                    }

                    start = System.nanoTime();
                    SCC.findComponents(graph);
                    sccTime += System.nanoTime() - start;
                }

                writer.printf(
                        Locale.US,
                        "TopologicalSort,%d,%d,%d,%d%n",
                        vertices,
                        edges,
                        topoTime / REPETITIONS,
                        vertices + edges
                );

                writer.printf(
                        Locale.US,
                        "SCC,%d,%d,%d,%d%n",
                        vertices,
                        edges,
                        sccTime / REPETITIONS,
                        vertices + edges
                );
            }
        }

        System.out.println("Connectivity benchmark completed.");
    }

    private static void runShortestPathBenchmark(
            String filename
    ) throws Exception {

        try (PrintWriter writer =
                     new PrintWriter(new File(filename))) {

            writer.println(
                    "algorithm,vertices,edges,averageTimeNs,operations"
            );

            for (int[] configuration : CONFIGURATIONS) {
                int vertices = configuration[0];
                int edges = configuration[1];

                long dijkstraTime = 0;
                long dagTime = 0;
                long dijkstraOperations = 0;

                for (int r = 0; r < REPETITIONS; r++) {
                    Graph graph =
                            GraphGenerator.connectedGraph(
                                    vertices,
                                    edges - vertices + 1,
                                    200L + r
                            );

                    long start = System.nanoTime();
                    Dijkstra.Result dijkstra =
                            Dijkstra.findShortestPaths(graph, 0);
                    dijkstraTime += System.nanoTime() - start;

                    dijkstraOperations +=
                            dijkstra.edgeExaminations;

                    DirectedGraph directedGraph =
                            GraphGenerator.dag(
                                    vertices,
                                    edges,
                                    300L + r
                            );

                    start = System.nanoTime();
                    DAGShortestPath.findShortestPaths(
                            directedGraph,
                            0
                    );
                    dagTime += System.nanoTime() - start;
                }

                writer.printf(
                        Locale.US,
                        "Dijkstra,%d,%d,%d,%d%n",
                        vertices,
                        edges,
                        dijkstraTime / REPETITIONS,
                        dijkstraOperations / REPETITIONS
                );

                writer.printf(
                        Locale.US,
                        "DAGShortestPath,%d,%d,%d,%d%n",
                        vertices,
                        edges,
                        dagTime / REPETITIONS,
                        vertices + edges
                );
            }
        }

        System.out.println("Shortest path benchmark completed.");
    }
}
