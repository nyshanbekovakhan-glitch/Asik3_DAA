package org.example.connectivity;
import org.example.graph.DirectedGraph;
import org.example.graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class TopologicalSort {
    public static class Result {
        public final List<Integer> order;
        public final boolean hasCycle;
        public Result(List<Integer> order, boolean hasCycle) {
            this.order = order;
            this.hasCycle = hasCycle;
        }
    }
    public static Result sort(DirectedGraph graph) {
        int vertices = graph.getVertices();
        int[] indegree = new int[vertices];
        for (Graph.Edge edge : graph.getEdges()) {
            indegree[edge.destination]++;
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < vertices; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);
            for (Graph.Edge edge :
                    graph.getAdjacentEdges(current)) {
                indegree[edge.destination]--;
                if (indegree[edge.destination] == 0) {
                    queue.offer(edge.destination);
                }
            }
        }
        boolean hasCycle = order.size() != vertices;
        return new Result(order, hasCycle);
    }
}
