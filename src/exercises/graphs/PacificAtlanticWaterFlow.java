package exercises.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Pacific Atlantic Water Flow (LeetCode 417 / NeetCode)
 * PATTERN: Reverse Boundary DFS / Reachability Search
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION: REVERSE FLOW VS. FORWARD FLOW
 * ----------------------------------------------------------------------------
 *  - FORWARD FLOW (Naive):
 *    Start at EVERY single cell in the grid and simulate water flowing downhill
 *    (High -> Low) to see if it reaches both the Pacific and Atlantic oceans.
 *    -> Time Complexity: O((M x N)^2) due to redundant path recalculations.
 *
 *  - REVERSE FLOW (Optimal - Implemented Here):
 *    Instead of watching water fall DOWN to the ocean, stand at the ocean borders
 *    and climb UPHILL (Low -> High / Equal) into the island!
 *    -> If you can walk uphill from the Pacific to cell X, it guarantees that
 *       water at cell X can flow downhill to the Pacific.
 *    -> Running 2 separate traversals (one from Pacific edges, one from Atlantic edges)
 *       reduces the time complexity to linear time: O(M x N).
 *
 * ----------------------------------------------------------------------------
 * 2. CRITICAL PITFALLS & UNCERTAINTIES RESOLVED
 * ----------------------------------------------------------------------------
 *  - PITFALL 1: HashSet<int[]> Failure in Java
 *    -> WHY IT FAILS: Arrays in Java use object identity for reference equality,
 *       not content equality. Thus, new int[]{1,2}.equals(new int[]{1,2}) is FALSE.
 *       Using HashSet<int[]> causes .contains() to fail every time.
 *    -> FIX: Use 2D boolean matrices (boolean[][] pac, boolean[][] atl). This is
 *       faster (O(1) direct indexing) and avoids primitive reference hash bugs.
 *
 *  - PITFALL 2: Guard Clause Evaluation Order
 *    -> WHY IT MATTERS: In Java, logical conditions evaluate left-to-right (short-circuit).
 *       Checking `visited[r][c]` or `heights[r][c]` BEFORE verifying that `r` and `c`
 *       are within grid bounds will throw an `ArrayIndexOutOfBoundsException`.
 *    -> FIX: Always place index boundary checks (`r < 0 || r >= rows ...`) FIRST.
 *
 *  - PITFALL 3: Why Uphill Check (`heights[r][c] < prevHeight`)?
 *    -> EXPLANATION: Since we are walking backwards FROM the ocean TO the mountains,
 *       we can only move to a cell if it is HIGHER THAN OR EQUAL TO the cell we just left.
 *       If `heights[r][c] < prevHeight`, the terrain drops, meaning water couldn't have
 *       flowed down from (r, c) to prevHeight.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let M be the number of rows and N be the number of columns in the grid.
 *
 * TIME COMPLEXITY: O(M x N)
 *   - Pacific DFS visits each cell at most once: O(M x N).
 *   - Atlantic DFS visits each cell at most once: O(M x N).
 *   - Final matrix comparison scans each cell once: O(M x N).
 *   - Total Time: O(M x N).
 *
 * SPACE COMPLEXITY: O(M x N)
 *   - Auxiliary Matrices: `pac` and `atl` matrices consume 2 x (M x N) space.
 *   - Recursion Stack: In the worst-case scenario (a long snake-like grid path),
 *     the call stack depth can reach O(M x N).
 *   - Output List: Holds up to O(M x N) result pairs.
 * ============================================================================
 */
public class PacificAtlanticWaterFlow {

    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();

        // Guard Clause: Handle empty or invalid grid inputs
        if (heights == null || heights.length == 0 || heights[0].length == 0) {
            return result;
        }

        int rows = heights.length;
        int cols = heights[0].length;

        // Boolean matrices track reachability from each ocean:
        // pac[r][c] == true means cell (r, c) can flow into the Pacific
        // atl[r][c] == true means cell (r, c) can flow into the Atlantic
        boolean[][] pac = new boolean[rows][cols];
        boolean[][] atl = new boolean[rows][cols];

        // ---------------------------------------------------------------------
        // STEP 1: SEED DFS FROM TOP AND BOTTOM BORDERS
        // ---------------------------------------------------------------------
        // Top Row (r = 0) touches Pacific.
        // Bottom Row (r = rows - 1) touches Atlantic.
        for (int c = 0; c < cols; c++) {
            dfs(0, c, pac, heights[0][c], heights);               // Top edge -> Pacific
            dfs(rows - 1, c, atl, heights[rows - 1][c], heights); // Bottom edge -> Atlantic
        }

        // ---------------------------------------------------------------------
        // STEP 2: SEED DFS FROM LEFT AND RIGHT BORDERS
        // ---------------------------------------------------------------------
        // Left Column (c = 0) touches Pacific.
        // Right Column (c = cols - 1) touches Atlantic.
        for (int r = 0; r < rows; r++) {
            dfs(r, 0, pac, heights[r][0], heights);               // Left edge -> Pacific
            dfs(r, cols - 1, atl, heights[r][cols - 1], heights); // Right edge -> Atlantic
        }

        // ---------------------------------------------------------------------
        // STEP 3: FIND OVERLAPPING REACHABLE CELLS
        // ---------------------------------------------------------------------
        // If a cell is reachable from BOTH oceans, add its coordinates to result.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pac[r][c] && atl[r][c]) {
                    result.add(Arrays.asList(r, c));
                }
            }
        }

        return result;
    }

    /**
     * Helper method to perform Depth-First Search moving UPHILL from ocean edges.
     *
     * @param r          Current row index
     * @param c          Current column index
     * @param visited    Boolean matrix marking ocean reachability (acts as visited set)
     * @param prevHeight Height of the previous cell we stepped from
     * @param heights    Input 2D grid containing cell elevation values
     */
    private void dfs(int r, int c, boolean[][] visited, int prevHeight, int[][] heights) {
        // ---------------------------------------------------------------------
        // BASE CASES / PRUNING CONDITIONS:
        // 1. Boundary Check: Ensure (r, c) is inside grid limits.
        // 2. Visited Check: Skip if cell was already explored by this ocean's DFS.
        // 3. Elevation Check: Since we walk UPHILL from ocean to land, the current
        //    cell must be >= prevHeight. If heights[r][c] < prevHeight, water
        //    cannot flow downhill from (r, c) to prevHeight!
        // ---------------------------------------------------------------------
        if (r < 0 || r >= heights.length || c < 0 || c >= heights[0].length
                || visited[r][c] || heights[r][c] < prevHeight) {
            return;
        }

        // Mark cell as reachable from the target ocean
        visited[r][c] = true;

        // ---------------------------------------------------------------------
        // RECURSIVE STEP: Explore all 4 cardinal neighbors
        // Pass current cell's height (heights[r][c]) as `prevHeight` for the next step
        // ---------------------------------------------------------------------
        dfs(r + 1, c, visited, heights[r][c], heights); // Down
        dfs(r - 1, c, visited, heights[r][c], heights); // Up
        dfs(r, c + 1, visited, heights[r][c], heights); // Right
        dfs(r, c - 1, visited, heights[r][c], heights); // Left
    }
}