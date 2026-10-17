package org.example.mst;
import org.example.graph.Graph;
import org.example.dsu.DSU;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class Kruskal {
    public static class Result {
        public final long totalWeight;
        public final List<Graph.Edge> selectedEdges;
        public final boolean connected;
        public final long edgeExaminations;
        public final long dsuOperations;
        public Result(
                long totalWeight,
                List<Graph.Edge> selectedEdges,
                boolean connected,
                long edgeExaminations,
                long dsuOperations
        ) {
            this.totalWeight = totalWeight;
            this.selectedEdges = selectedEdges;
            this.connected = connected;
            this.edgeExaminations = edgeExaminations;
            this.dsuOperations = dsuOperations;
        }
    }
    public static Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        if (vertices == 0) {
            return new Result(
                    0, new ArrayList<>(), false, 0, 0
            );
        }
        List<Graph.Edge> edges =
                new ArrayList<>(graph.getEdges());
        edges.sort(
                Comparator.comparingInt(edge -> edge.weight)
        );
        DSU dsu = new DSU(vertices);
        List<Graph.Edge> selectedEdges = new ArrayList<>();
        long totalWeight = 0;
        long edgeExaminations = 0;
        long dsuOperations = 0;
        for (Graph.Edge edge : edges) {
            edgeExaminations++;
            dsuOperations += 2;
            if (dsu.union(edge.source, edge.destination)) {
                selectedEdges.add(edge);
                totalWeight += edge.weight;
            }
            if (selectedEdges.size() == vertices - 1) {
                break;
            }
        }
        boolean connected =
                selectedEdges.size() == vertices - 1;
        return new Result(
                totalWeight,
                selectedEdges,
                connected,
                edgeExaminations,
                dsuOperations
        );
    }
}
