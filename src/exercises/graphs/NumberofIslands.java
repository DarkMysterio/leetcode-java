package exercises.graphs;

/**
 * ============================================================================
 * PROBLEM: Number of Islands (LeetCode 200)
 * PATTERN: Grid-Based Connected Components via Depth-First Search (DFS)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * Imagine the 2D grid as a map of water ('0') and land ('1'). An island is a
 * group of connected land cells reachable by moving horizontally or vertically.
 *
 * To count the total number of distinct islands:
 *   1. Iterate through every cell in the grid using nested loops.
 *   2. When we encounter unvisited land ('1'), we know we have discovered a
 *      BRAND NEW island, so we increment our `count`.
 *   3. We then immediately trigger a Depth-First Search (DFS) starting from
 *      that cell to traverse and "explore" the entire connected component.
 *   4. During DFS, we mark every land cell belonging to this island in our
 *      `visited` array so that future loop iterations won't re-count it as a
 *      new island.
 *
 * ----------------------------------------------------------------------------
 * 2. KEY CHANGE: VISITED ARRAY VS. IN-PLACE "SINKING"
 * ----------------------------------------------------------------------------
 * Instead of mutating the original grid (overwriting '1' with '0' or '2'), we
 * allocate an explicit auxiliary matrix: `boolean[][] visited`.
 *
 * WHY USE A VISITED ARRAY?
 *   - Immutability & Safety: In production software, mutating input parameters
 *     can cause side-effects for callers who still need the original grid.
 *   - Clear Separation of State: Keeps the input data untouched while cleanly
 *     tracking algorithmic search state separately.
 *
 * RECAP ON ALGORITHM IDENTITY (DFS vs BFS):
 *   - Calling this method `dfs` reflects its true nature: recursive function
 *     calls utilize the system call stack to traverse down a branch as deeply
 *     as possible before backtracking. (BFS would require an explicit Queue).
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $M$ be the number of rows (`grid.length`) and $N$ be the number of columns (`grid[0].length`).
 *
 * TIME COMPLEXITY: $O(M \times N)$
 *   - The nested loops examine every cell $(i, j)$ in the matrix.
 *   - Each cell is marked in `visited` at most once and traversed at most once
 *     by DFS. Thus, the total work across all calls is linear relative to the
 *     total number of cells in the grid.
 *
 * SPACE COMPLEXITY: $O(M \times N)$
 *   - Auxiliary Matrix: The `boolean[][] visited` array takes $O(M \times N)$ space.
 *   - Recursion Stack: In the worst-case (e.g., an entire grid filled with '1's
 *     in a winding snake shape), the call stack can grow up to $M \times N$ frames deep.
 *   - Total Space: $O(M \times N)$.
 * ============================================================================
 */
public class NumberofIslands {

    public int numIslands(char[][] grid) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Edge Case Safety
        // INTUITION: If the grid is null, has 0 rows, or has 0 columns, no
        // islands can exist. Checking `grid[0].length` prevents array out of bounds.
        // ---------------------------------------------------------------------
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // ---------------------------------------------------------------------
        // AUXILIARY STATE TRACKING
        // INTUITION: `visited[i][j]` will be true if cell (i, j) has already
        // been explored by a DFS search. Defaults to false for all cells.
        // ---------------------------------------------------------------------
        boolean[][] visited = new boolean[rows][cols];

        // ---------------------------------------------------------------------
        // GRID SCANNING LOOP
        // INTUITION: Traverse every cell row-by-row, column-by-column.
        // ---------------------------------------------------------------------
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // CONDITIONAL TRIGGER:
                // We only start a search if:
                //   1. The current cell is land ('1').
                //   2. The current cell has NOT been visited yet.
                if (grid[i][j] == '1' && !visited[i][j]) {
                    // Found a new unvisited island! Increment counter.
                    count++;

                    // Explore and mark all connected land belonging to this island
                    dfs(grid, visited, i, j);
                }
            }
        }

        return count;
    }

    /**
     * Helper method to perform Depth-First Search on adjacent grid cells.
     *
     * @param grid    The original input 2D character matrix.
     * @param visited The state matrix tracking explored coordinates.
     * @param i       Current row coordinate.
     * @param j       Current column coordinate.
     */
    private void dfs(char[][] grid, boolean[][] visited, int i, int j) {

        // ---------------------------------------------------------------------
        // BASE CASE GUARD CLAUSE (FLATTENED BOUNDARY & STATE CHECK)
        // INTUITION: Immediately return and stop recursion if ANY apply:
        //   1. `i < 0 || i >= grid.length`    -> Out of vertical bounds
        //   2. `j < 0 || j >= grid[0].length` -> Out of horizontal bounds
        //   3. `grid[i][j] == '0'`            -> Cell is water (not land)
        //   4. `visited[i][j] == true`        -> Cell was already processed
        // ---------------------------------------------------------------------
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length
                || grid[i][j] == '0' || visited[i][j]) {
            return;
        }

        // ---------------------------------------------------------------------
        // MARK VISITED STATE
        // INTUITION: State tracking replacing "land sinking". Mark this cell
        // as processed so future DFS traversals or loop iterations skip it.
        // ---------------------------------------------------------------------
        visited[i][j] = true;

        // ---------------------------------------------------------------------
        // RECURSIVE CARDINAL EXPLORATION
        // INTUITION: Explore all 4 orthogonal neighbors (Down, Up, Right, Left).
        // The base case at the top of the function handles stopping when hitting
        // boundaries or water.
        // ---------------------------------------------------------------------
        dfs(grid, visited, i + 1, j); // Explore Down
        dfs(grid, visited, i - 1, j); // Explore Up
        dfs(grid, visited, i, j + 1); // Explore Right
        dfs(grid, visited, i, j - 1); // Explore Left
    }
}