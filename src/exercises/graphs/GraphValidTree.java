package exercises.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Graph Valid Tree (LeetCode 261 / NeetCode)
 * PATTERN: Graph Theory Theorem (N - 1 Edges + Full Connectivity)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * By definition in Graph Theory, an UNDIRECTED graph with N nodes is a VALID TREE
 * IF AND ONLY IF it satisfies two fundamental properties simultaneously:
 *   1. It is FULLY CONNECTED (contains exactly 1 connected component).
 *   2. It contains NO CYCLES.
 *
 * KEY GRAPH THEOREM:
 *   An undirected graph with N nodes is a Tree <===> It has EXACTLY (N - 1) edges
 *   AND is fully connected.
 *
 * WHY THIS SIMPLIFIES OUR ALGORITHM:
 *   - If E > N - 1: There are too many edges; a cycle is MATHEMATICALLY GUARANTEED.
 *   - If E < N - 1: There are too few edges; the graph is GUARANTEED DISCONNECTED.
 *   - If E == N - 1: The graph can only fail to be a tree if it is disconnected!
 *
 * Therefore, by enforcing `edges.length == n - 1` up front, we completely eliminate
 * the need for complex cycle detection (like parent tracking in DFS or Union-Find).
 * We only need to launch a single DFS from node 0 and verify if all N nodes are reached!
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP OF PAST PITFALLS & BUGS SOLVED
 * ----------------------------------------------------------------------------
 *  - PITFALL: Guard Clause Bug `if (n <= 1) return true;`
 *    -> REASON: If n = 1 and input contains a self-loop edge like `edges = [[0, 0]]`,
 *       an early `return true;` bypasses the `edges.length != n - 1` check and
 *       incorrectly accepts an invalid graph.
 *    -> FIX: Remove the special guard clause. The `edges.length != n - 1` condition
 *       and DFS traversal naturally evaluate n = 1 (or any n) with 100% mathematical accuracy.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let V = n (number of nodes/vertices) and E = edges.length (number of edges).
 *
 * TIME COMPLEXITY: O(V + E) -> Simplifies to O(V)
 *   - Edge check `edges.length != n - 1`: O(1) time.
 *   - Adjacency List Construction: Iterates through V nodes and E edges: O(V + E).
 *   - DFS Traversal: Visits each node at most once and explores its edges: O(V + E).
 *   - Since we know E == V - 1, total time simplifies strictly to linear time: O(V).
 *
 * SPACE COMPLEXITY: O(V + E) -> Simplifies to O(V)
 *   - Adjacency List (`adjList`): Stores V keys and 2E directed edge pairs: O(V + E).
 *   - Visited Set (`visited`): Stores at most V node integers: O(V).
 *   - Recursion Call Stack: Maximum call depth reaches O(V) in a linear line graph.
 *   - Total Auxiliary Space simplifies to O(V).
 * ============================================================================
 */
public class GraphValidTree {

    public boolean validTree(int n, int[][] edges) {
        // ---------------------------------------------------------------------
        // STEP 1: EULERIAN TREE EDGE COUNT CHECK (O(1) Pruning)
        // INTUITION: A valid tree with N nodes MUST have exactly N - 1 edges.
        // - Too many edges (edges.length > n - 1) -> Must contain at least one cycle.
        // - Too few edges (edges.length < n - 1) -> Must be broken into disconnected islands.
        // ---------------------------------------------------------------------
        if (edges.length != n - 1) {
            return false;
        }

        // ---------------------------------------------------------------------
        // STEP 2: BUILD UNDIRECTED ADJACENCY LIST
        // INTUITION: Pre-allocate an empty list for every node 0 to n - 1 so that
        // isolated nodes still exist safely in memory without NullPointerExceptions.
        // ---------------------------------------------------------------------
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        // Because the graph is UNDIRECTED, an edge between A and B means
        // A can reach B AND B can reach A. We populate bidirectional connections.
        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }

        // Tracks all unique nodes reached during our traversal
        HashSet<Integer> visited = new HashSet<>();

        // ---------------------------------------------------------------------
        // STEP 3: MEASURE REACHABILITY VIA DFS
        // INTUITION: Arbitrarily start DFS from node 0. If the graph is fully
        // connected (a single tree component), DFS will naturally traverse through
        // every single neighbor and visit all N nodes in a single run.
        // ---------------------------------------------------------------------
        dfs(-1,0, adjList, visited);

        // ---------------------------------------------------------------------
        // STEP 4: VERIFY FULL CONNECTIVITY
        // INTUITION: If `visited.size() == n`, every node was reachable from node 0.
        // Combined with STEP 1 (edges == n - 1), this proves the graph IS A VALID TREE!
        // ---------------------------------------------------------------------
        return visited.size() == n;
    }

    /**
     * Depth-First Search helper method to explore connected graph components.
     *
     * @param node     The current node being visited.
     * @param adjList  The bidirectional adjacency list.
     * @param visited  HashSet recording all nodes visited so far.
     */
    private void dfs(int previous,int node, List<List<Integer>> adjList, HashSet<Integer> visited) {
        // ---------------------------------------------------------------------
        // BASE CASE / PRUNING CONDITION:
        // INTUITION: If `visited` already contains `node`, stop exploring!
        // This fulfills two critical roles:
        //   1. Prevents infinite recursion back-and-forth between undirected neighbors (A <-> B).
        //   2. Prevents re-visiting nodes in cyclic paths.
        // ---------------------------------------------------------------------
        if (visited.contains(node)) {
            return;
        }

        // Mark the current node as visited
        visited.add(node);

        // ---------------------------------------------------------------------
        // RECURSIVE STEP: Explore all adjacent neighbors
        // ---------------------------------------------------------------------
        for (Integer neighbor : adjList.get(node)) {
            if(neighbor != previous) {
                dfs(node,neighbor, adjList, visited);
            }
        }
    }
}