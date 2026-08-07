package exercises.graphs;

import java.util.ArrayList;
import java.util.List;

public class NumberofConnectedComponentsinanUndirectedGraph {
    public int countComponents(int n, int[][] edges) {
        // Build Adjacency List
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        boolean[] visited = new boolean[n];
        int count = 0;

        // Iterate through all nodes; every unvisited node starts a new component
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(i, graph, visited);
            }
        }

        return count;
    }

    private void dfs(int node, List<Integer>[] graph, boolean[] visited) {
        visited[node] = true;

        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited);
            }
        }
    }

}
