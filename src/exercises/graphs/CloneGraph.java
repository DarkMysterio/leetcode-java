package exercises.graphs;

import java.util.HashMap;

/**
 * ============================================================================
 * PROBLEM: Clone Graph (LeetCode 133 / NeetCode)
 * PATTERN: Imperative Graph DFS with HashMap Cycle Tracking
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * A "Deep Copy" of a graph requires recreating every single node in a completely
 * new memory location, along with replicating all interconnected edges.
 *
 * Unlike Trees, Graphs contain cycles (e.g., Node 1 <-> Node 2) and shared
 * neighbors (diamond paths: 1 -> 2 -> 4 and 1 -> 3 -> 4).
 *
 * To solve this without falling into infinite recursion or duplicating nodes:
 *   1. We maintain a `HashMap<Node, Node>` to map each [Original Node -> Cloned Node].
 *   2. As soon as a node is created, we put it into the map BEFORE exploring neighbors.
 *   3. When exploring neighbors, we check if the neighbor is already in the map:
 *      - IF NOT VISITED: Create a new clone, link it, and recurse into it.
 *      - IF VISITED (Cycle Detected): Retrieve the existing clone from the map
 *        and link it to the current node's neighbor list.
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP OF PAST PITFALLS SOLVED IN THIS IMPLEMENTATION
 * ----------------------------------------------------------------------------
 *  - PITFALL 1: Returning `new Node()` on empty input.
 *    -> Returning a dummy node creates a graph with `val = 0` instead of `null` (`[]`).
 *    -> FIX: Directly return `null` when `node == null`.
 *
 *  - PITFALL 2: Using a `HashSet<Node>` instead of a `HashMap<Node, Node>`.
 *    -> `HashSet` tells you IF a node was visited, but cannot give you the
 *       reference to its ALREADY CLONED copy during a cycle!
 *    -> FIX: `HashMap<Node, Node>` preserves original-to-clone object mapping.
 *
 *  - PITFALL 3: Parent-Pointer Tree DFS (`neighbor != parent`).
 *    -> Tracking only `parent` prevents 1-step back-tracking, but fails on
 *       cycles > 2 nodes (e.g., 1 -> 2 -> 3 -> 1) causing StackOverflowError.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $V$ be the number of vertices (nodes) and $E$ be the number of edges.
 *
 * TIME COMPLEXITY: $\mathcal{O}(V + E)$
 *   - Each vertex (node) is created and processed exactly once via `dfs()`.
 *   - Every edge is traversed at most twice (once from each direction in an
 *     undirected graph).
 *
 * SPACE COMPLEXITY: $\mathcal{O}(V)$
 *   - Hash Map State: Stores $V$ key-value pairs (mapping original to clone).
 *   - Call Stack: In the worst case (a linear path graph), recursion goes $V$ frames deep.
 * ============================================================================
 */
public class CloneGraph {

    public Node cloneGraph(Node node) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle Null Graphs
        // INTUITION: An empty input (`node == null`) must return `null` directly.
        // Returning `new Node()` would create a dummy node with default val = 0.
        // ---------------------------------------------------------------------
        if (node == null || node.neighbors == null) {
            return null;
        }

        // 1. Instantiating the clone of the root starting node
        Node startNode = new Node(node.val);

        // 2. HashMap to store [Original Node -> Cloned Node] mappings
        HashMap<Node, Node> visited = new HashMap<>();

        // 3. Initiate imperative DFS traversal
        dfs(startNode, node, visited);

        // Return the reference to the root clone of the graph
        return startNode;
    }

    /**
     * Imperative Recursive Helper to populate neighbors across the graph.
     *
     * @param startNode     The newly created clone node being populated.
     * @param originalNode  The corresponding node in the original graph.
     * @param visited       Map maintaining references to all instantiated clones.
     */
    public void dfs(Node startNode, Node originalNode, HashMap<Node, Node> visited) {
        // ---------------------------------------------------------------------
        // REGISTER IN MAP IMMEDIATELY
        // INTUITION: Store the current mapping before entering the loop.
        // If a cycle loops back to `originalNode` during neighbor exploration,
        // the child call can look up `visited.get(originalNode)` right away!
        // ---------------------------------------------------------------------
        visited.put(originalNode, startNode);

        // Iterate through all neighbors of the current original node
        for (Node neighbor : originalNode.neighbors) {

            // -----------------------------------------------------------------
            // CASE 1: UNVISITED NEIGHBOR (New Node)
            // INTUITION: If the neighbor isn't in the map, instantiate its clone,
            // connect it to `startNode`, and recursively explore its connections.
            // -----------------------------------------------------------------
            if (!visited.containsKey(neighbor)) {
                Node deepNeighbor = new Node(neighbor.val);
                startNode.neighbors.add(deepNeighbor);

                // Recurse to populate `deepNeighbor`
                dfs(deepNeighbor, neighbor, visited);
            }
            // -----------------------------------------------------------------
            // CASE 2: VISITED NEIGHBOR (Cycle / Shared Edge Detected)
            // INTUITION: The neighbor clone ALREADY exists in memory. Do NOT
            // create a `new Node()`. Retrieve its existing clone from the map
            // and attach it to close the cycle safely.
            // -----------------------------------------------------------------
            else {
                startNode.neighbors.add(visited.get(neighbor));
            }
        }
    }
}