package org.example.dsu;

public class DSU {

    private final int[] parent;
    private final int[] size;
    private int components;

    public DSU(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(
                    "Size cannot be negative"
            );
        }

        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        checkElement(x);

        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        components--;

        return true;
    }

    public int getComponents() {
        return components;
    }

    private void checkElement(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException(
                    "Invalid element: " + x
            );
        }
    }
}
