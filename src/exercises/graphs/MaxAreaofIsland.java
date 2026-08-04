package exercises.graphs;

/**
 * ============================================================================
 * PROBLEM: Max Area of Island (LeetCode 695)
 * PATTERN: Grid-Based Connected Components via Additive Depth-First Search (DFS)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * This problem expands on "Number of Islands". Instead of simply counting how
 * many distinct islands exist, we need to calculate the total surface area
 * (number of connected land cells) for each island and track the maximum area.
 *
 * To achieve this:
 *   1. Iterate through every cell in the matrix using nested loops.
 *   2. When we find an unvisited land cell (`grid[i][j] == 1`), we trigger a DFS.
 *   3. Rather than returning `void`, the DFS function returns an `int` representing
 *      the total size of the island connected to that starting cell.
 *   4. The area of any node is calculated recursively:
 *          1 (the current cell) + area of Down + area of Up + area of Right + area of Left
 *   5. We use `Math.max(maxCount, dfs(...))` to keep track of the largest island seen so far.
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP: DATA TYPE PITFALL (int[][] vs char[][])
 * ----------------------------------------------------------------------------
 *  - In "Number of Islands" (LeetCode 200), the grid is `char[][]` containing `'1'` and `'0'`.
 *  - In "Max Area of Island" (LeetCode 695), the grid is `int[][]` containing `1` and `0`.
 *  - Trap: Checking `grid[i][j] == '1'` against an `int[][]` evaluates `1 == 49`
 *    (ASCII value of '1'), which is ALWAYS false! We must compare against integer `1`.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $M$ be the number of rows (`grid.length`) and $N$ be the number of columns (`grid[0].length`).
 *
 * TIME COMPLEXITY: $O(M \times N)$
 *   - The outer loops scan all $M \times N$ cells in the grid once.
 *   - Each cell is visited at most once by DFS thanks to the `visited` array check.
 *   - Total operations across all calls are linear relative to grid size: $O(M \times N)$.
 *
 * SPACE COMPLEXITY: $O(M \times N)$
 *   - Auxiliary State Matrix: `boolean[][] visited` takes $O(M \times N)$ memory.
 *   - Call Stack: In the worst-case (an island filling the entire grid in a snake-like path),
 *     the call stack goes $M \times N$ frames deep.
 *   - Total Space: $O(M \times N)$.
 * ============================================================================
 */
public class MaxAreaofIsland {

    public int maxAreaOfIsland(int[][] grid) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle Null or Empty Grids
        // INTUITION: If the grid is null, has 0 rows, or 0 columns, no area exists.
        // ---------------------------------------------------------------------
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int maxCount = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // Explicit state tracking array to preserve original grid data immutability
        boolean[][] visited = new boolean[rows][cols];

        // ---------------------------------------------------------------------
        // GRID TRAVERSAL LOOP
        // INTUITION: Scan every cell to locate unvisited land components.
        // ---------------------------------------------------------------------
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // CRITICAL CHECK:
                // 1. Is this cell land? (`grid[i][j] == 1`) -> Using integer 1, NOT char '1'!
                // 2. Has this land cell NOT been visited yet? (`!visited[i][j]`)
                if (grid[i][j] == 1 && visited[i][j] == false) {

                    // Trigger DFS to calculate island size and update max area encountered
                    maxCount = Math.max(maxCount, dfs(grid, visited, i, j));
                }
            }
        }

        return maxCount;
    }

    /**
     * Recursive helper method that explores a land component and computes its area.
     *
     * @return The total number of connected land cells reachable from (i, j).
     */
    public int dfs(int[][] grid, boolean[][] visited, int i, int j) {

        // ---------------------------------------------------------------------
        // BASE CASE / BOUNDARY GUARD CLAUSES
        // INTUITION: Stop exploring and return area 0 if:
        //   1. Row or column coordinates are out of grid bounds.
        //   2. The cell is water (`grid[i][j] == 0`).
        //   3. The cell was already visited in a previous call (`visited[i][j] == true`).
        // ---------------------------------------------------------------------
        if (i >= grid.length || i < 0 || j < 0 || j >= grid[i].length
                || grid[i][j] == 0 || visited[i][j] == true) {
            return 0;
        }

        // ---------------------------------------------------------------------
        // MARK VISITED STATE
        // INTUITION: Immediately flag current land cell as visited before
        // recursing so we avoid infinite recursion loops across neighboring cells.
        // ---------------------------------------------------------------------
        visited[i][j] = true;

        // ---------------------------------------------------------------------
        // ADDITIVE BOTTOM-UP RECURSION
        // INTUITION:
        //   - Current cell contributes 1 to the area count.
        //   - Recurse in 4 cardinal directions: Down (i+1), Up (i-1), Right (j+1), Left (j-1).
        //   - Each recursive call returns the sum of land cells found down its path.
        // ---------------------------------------------------------------------
        return 1 + dfs(grid, visited, i + 1, j)
                + dfs(grid, visited, i - 1, j)
                + dfs(grid, visited, i, j + 1)
                + dfs(grid, visited, i, j - 1);
    }
}