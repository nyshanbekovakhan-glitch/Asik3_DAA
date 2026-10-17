package org.example.mst;
import org.example.graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
public class Prim {
    public static Kruskal.Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        if (vertices == 0) {
            return new Kruskal.Result(
                    0, new ArrayList<>(), false, 0, 0
            );
        }
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Graph.Edge> pq =
                new PriorityQueue<>(
                        (a, b) -> Integer.compare(
                                a.weight, b.weight
                        )
                );
        List<Graph.Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeExaminations = 0;
        visited[0] = true;
        for (Graph.Edge edge : graph.getAdjacentEdges(0)) {
            pq.offer(edge);
            edgeExaminations++;
        }
        while (!pq.isEmpty()
                && selectedEdges.size() < vertices - 1) {

            Graph.Edge edge = pq.poll();
            if (visited[edge.destination]) {
                continue;
            }
            visited[edge.destination] = true;
            selectedEdges.add(edge);
            totalWeight += edge.weight;
            for (Graph.Edge next :
                    graph.getAdjacentEdges(edge.destination)) {

                edgeExaminations++;
                if (!visited[next.destination]) {
                    pq.offer(next);
                }
            }
        }
        boolean connected =
                selectedEdges.size() == vertices - 1;
        return new Kruskal.Result(
                totalWeight,
                selectedEdges,
                connected,
                edgeExaminations,
                0
        );
    }
}
