package exercises.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ============================================================================
 * PROBLEM: Islands and Treasure (Walls and Gates / LeetCode 286 / NeetCode)
 * PATTERN: Multi-Source Breadth-First Search (BFS) on 2D Grids
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION: WHY MULTI-SOURCE BFS?
 * ----------------------------------------------------------------------------
 * The goal is to find the shortest distance from every land cell (`INF`) to its
 * CLOSEST treasure chest (`0`).
 *
 * Traditional Single-Source BFS:
 *   Starting from each land cell to search for the nearest chest is redundant.
 *   Starting from each chest individually also forces multiple grid scans.
 *
 * Multi-Source BFS Solution:
 *   Instead of running separate BFS calls for each treasure chest, we push ALL
 *   chests into a single Queue UPFRONT.
 *
 *   Think of it like dropping multiple pebbles into a pond simultaneously.
 *   Concentric ripples expand outward from every chest at the exact same rate
 *   (1 step per layer). Whichever ripple hits an unvisited land cell first is
 *   GUARANTEED to be from the nearest chest.
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP OF PAST PITFALLS SOLVED IN THIS IMPLEMENTATION
 * ----------------------------------------------------------------------------
 *  - PITFALL 1: Per-Node Nested BFS Calls (Time Limit Exceeded - TLE).
 *    -> Running `bfs(grid, i, j)` inside a nested loop causes $\mathcal{O}(K \times M \times N)$
 *       runtime because each chest re-traverses the grid over and over.
 *    -> FIX: Collect all chest coordinates first, then execute ONE global BFS traversal.
 *
 *  - PITFALL 2: Hardcoded Initial Queue Positions.
 *    -> Adding dummy coordinates like `new int[]{1, 2}` instead of dynamic variables.
 *    -> FIX: Seed queue directly with loop coordinates `new int[]{i, j}`.
 *
 *  - PITFALL 3: Expanding Static Outer-Loop Coordinates (`i`, `j`).
 *    -> Using `i - 1` and `j + 1` inside the inner BFS loop instead of the polled
 *       cell's coordinates `x` and `y`.
 *    -> FIX: Compute adjacent cells using polled queue coordinates: `x + dir[0]`, `y + dir[1]`.
 *
 *  - PITFALL 4: Incorrect Distance Counter Increment (`distance++`).
 *    -> Incrementing distance once per polled node instead of once per BFS level.
 *    -> FIX: Derive neighbor distance directly from parent cell state: `grid[nx][ny] = grid[x][y] + 1`.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $M$ be the number of rows and $N$ be the number of columns in the grid.
 *
 * TIME COMPLEXITY: $\mathcal{O}(M \times N)$
 *   - Initial matrix scan to locate chests takes $\mathcal{O}(M \times N)$.
 *   - Every cell (land, water, chest) is entered into the queue AT MOST ONCE.
 *   - Edges (4 directional moves per cell) are processed in constant time $\mathcal{O}(1)$.
 *   - Total operations scale strictly linearly with grid dimensions.
 *
 * SPACE COMPLEXITY: $\mathcal{O}(M \times N)$
 *   - Queue Storage: In the worst-case scenario (e.g., a grid full of chests),
 *     the queue will hold up to $M \times N$ elements.
 *   - Memory Optimization: No auxiliary `boolean[][] visited` array is required!
 *     The grid itself tracks visited status (`grid[nx][ny] == Integer.MAX_VALUE`),
 *     maintaining $\mathcal{O}(1)$ extra space beyond the queue.
 * ============================================================================
 */
public class IslandsandTreasure {

    public void islandsAndTreasure(int[][] grid) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle Empty or Malformed Input Grids
        // ---------------------------------------------------------------------
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new ArrayDeque<>();

        // ---------------------------------------------------------------------
        // STEP 1: MULTI-SOURCE INITIALIZATION (Seeding the Queue)
        // ---------------------------------------------------------------------
        // INTUITION: Iterate through the grid to identify all starting points (0s).
        // By adding all sources to the Queue BEFORE starting traversal, the BFS
        // will naturally process distances level-by-level across ALL chests concurrently.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                }
            }
        }

        // Standard 4-directional offsets: {rowOffset, colOffset} -> Up, Down, Left, Right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // ---------------------------------------------------------------------
        // STEP 2: SINGLE-PASS MULTI-SOURCE BFS TRAVERSAL
        // ---------------------------------------------------------------------
        while (!queue.isEmpty()) {
            // Poll current processing node coordinates
            int[] pair = queue.poll();
            int x = pair[0]; // Current Row
            int y = pair[1]; // Current Column

            // Explore all 4 orthogonal neighbors
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                // -------------------------------------------------------------
                // CONDITIONAL VALIDATION:
                // 1. Boundary Check: Ensure (nx, ny) is inside grid dimensions.
                // 2. Unvisited Check: `grid[nx][ny] == Integer.MAX_VALUE`
                //    - Water (-1): Evaluates to false (Ignored).
                //    - Chest (0): Evaluates to false (Ignored).
                //    - Already Visited Land (dist < INF): Evaluates to false (Ignored).
                //    - Unvisited Land (INF / 2147483647): Evaluates to true.
                // -------------------------------------------------------------
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols
                        && grid[nx][ny] == Integer.MAX_VALUE) {

                    // INTUITION: The shortest distance to neighbor (nx, ny) is
                    // exactly 1 unit greater than current cell (x, y).
                    grid[nx][ny] = grid[x][y] + 1;

                    // Push newly processed land cell into queue to continue wave expansion
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
    }
}