package exercises.graphs;

/**
 * ============================================================================
 * PROBLEM: Surrounded Regions (LeetCode 130 / NeetCode)
 * PATTERN: Reverse Boundary DFS / In-Place State Mutation
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION
 * ----------------------------------------------------------------------------
 * A region of 'O's is captured ONLY IF it is completely surrounded by 'X's.
 * This implies that any 'O' connected to the outer border of the grid can
 * NEVER be captured.
 *
 * Rather than searching inward from every 'O' (which requires complex tracking
 * to prove a region hits no edges), we work BACKWARDS from the 4 borders:
 *   1. Find all border 'O's and run DFS to mark their connected components
 *      as safe using a temporary character 'H' (Haven).
 *   2. Sweep the matrix: Any remaining 'O' was unreachable from borders and is
 *      thus captured ('O' -> 'X'). Any 'H' is restored to 'O' ('H' -> 'O').
 *
 * ----------------------------------------------------------------------------
 * 2. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let M be the number of rows and N be the number of columns in the grid.
 *
 * TIME COMPLEXITY: O(M x N)
 *   - Boundary Scans: Inspecting the 4 edges takes O(M + N).
 *   - DFS Traversals: Each cell is visited at most once across all DFS calls
 *     because mutating 'O' to 'H' acts as an in-place visited set: O(M x N).
 *   - Matrix Sweep: A final nested loop visits all M x N cells once: O(M x N).
 *   - Total Time: Linear O(M x N).
 *
 * SPACE COMPLEXITY: O(M x N)
 *   - Auxiliary Space: O(1) extra space because we mutate the board in-place
 *     with 'H' instead of allocating a separate boolean[][] visited matrix.
 *   - Call Stack: In the worst-case scenario (e.g., a grid filled entirely
 *     with 'O' in a spiral shape), recursion stack depth reaches O(M x N).
 * ============================================================================
 */
public class SurroundedRegions {

    public void solve(char[][] board) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle null or empty grids immediately
        // ---------------------------------------------------------------------
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        int rows = board.length;
        int cols = board[0].length;

        // ---------------------------------------------------------------------
        // STEP 1: BOUNDARY SEARCH (Top & Bottom Rows)
        // ---------------------------------------------------------------------
        // Iterate through all columns along Row 0 (top) and Row (rows - 1) (bottom).
        // If a border cell is 'O', DFS will flood-fill all connected 'O's to 'H'.
        for (int c = 0; c < cols; c++) {
            dfs(0, c, board);          // Top border edge
            dfs(rows - 1, c, board);   // Bottom border edge
        }

        // ---------------------------------------------------------------------
        // STEP 1 (Cont.): BOUNDARY SEARCH (Left & Right Columns)
        // ---------------------------------------------------------------------
        // Iterate through all rows along Column 0 (left) and Column (cols - 1) (right).
        for (int r = 0; r < rows; r++) {
            dfs(r, 0, board);          // Left border edge
            dfs(r, cols - 1, board);   // Right border edge
        }

        // ---------------------------------------------------------------------
        // STEP 2: FULL GRID MATRIX SWEEP (State Resolution)
        // ---------------------------------------------------------------------
        // After boundary DFS finishes, the grid contains three character states:
        //   - 'X': Original water/walls (unchanged)
        //   - 'O': Isolated fresh regions that couldn't reach any edge -> CAPTURED
        //   - 'H': Safe border-connected regions -> RESTORED TO 'O'
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') {
                    board[r][c] = 'X'; // Enclosed region captured!
                } else if (board[r][c] == 'H') {
                    board[r][c] = 'O'; // Safe border region restored!
                }
            }
        }
    }

    /**
     * Helper method to perform Depth-First Search for border reachability.
     *
     * @param r     Current row index
     * @param c     Current column index
     * @param board The 2D character matrix
     */
    private void dfs(int r, int c, char[][] board) {
        // ---------------------------------------------------------------------
        // BASE CASE / PRUNING CONDITIONS:
        // 1. Boundary Checks: Prevent ArrayIndexOutOfBoundsException.
        // 2. Cell Type Check: Process ONLY 'O' cells.
        //    - If cell is 'X', it acts as a wall (stop DFS).
        //    - If cell is 'H', it was already visited by DFS (stop infinite loops).
        // ---------------------------------------------------------------------
        if (r < 0 || c < 0 || r >= board.length || c >= board[0].length || board[r][c] != 'O') {
            return;
        }

        // ---------------------------------------------------------------------
        // STATE MUTATION:
        // Mark current cell as 'H' (Haven). This fulfills two goals:
        //   1. Saves this cell from being flipped to 'X' later.
        //   2. Acts as our 'visited' marker so we never re-evaluate this cell.
        // ---------------------------------------------------------------------
        board[r][c] = 'H';

        // ---------------------------------------------------------------------
        // RECURSIVE STEP: Explore all 4 cardinal directions
        // ---------------------------------------------------------------------
        dfs(r + 1, c, board); // Down
        dfs(r - 1, c, board); // Up
        dfs(r, c + 1, board); // Right
        dfs(r, c - 1, board); // Left
    }
}