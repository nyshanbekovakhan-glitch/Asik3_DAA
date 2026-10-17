package org.example.connectivity;

import org.example.graph.DirectedGraph;
import org.example.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class SCC {
    public static List<List<Integer>> findComponents(
            DirectedGraph graph
    ) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        List<Integer> finishOrder = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfsOrder(graph, i, visited, finishOrder);
            }
        }
        List<List<Integer>> reversed =
                new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            reversed.add(new ArrayList<>());
        }
        for (Graph.Edge edge : graph.getEdges()) {
            reversed.get(edge.destination).add(edge.source);
        }
        Collections.reverse(finishOrder);
        java.util.Arrays.fill(visited, false);
        List<List<Integer>> components =
                new ArrayList<>();
        for (int vertex : finishOrder) {
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                dfsReversed(
                        vertex,
                        reversed,
                        visited,
                        component
                );
                components.add(component);
            }
        }
        return components;
    }
    private static void dfsOrder(
            DirectedGraph graph,
            int vertex,
            boolean[] visited,
            List<Integer> finishOrder
    ) {
        visited[vertex] = true;
        for (Graph.Edge edge :
                graph.getAdjacentEdges(vertex)) {
            if (!visited[edge.destination]) {
                dfsOrder(
                        graph,
                        edge.destination,
                        visited,
                        finishOrder
                );
            }
        }
        finishOrder.add(vertex);
    }
    private static void dfsReversed(
            int vertex,
            List<List<Integer>> reversed,
            boolean[] visited,
            List<Integer> component
    ) {
        visited[vertex] = true;
        component.add(vertex);
        for (int next : reversed.get(vertex)) {
            if (!visited[next]) {
                dfsReversed(
                        next,
                        reversed,
                        visited,
                        component
                );
            }
        }
    }
}
