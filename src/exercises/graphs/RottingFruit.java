package exercises.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ============================================================================
 * PROBLEM: Rotting Oranges (LeetCode 994 / NeetCode)
 * PATTERN: Level-by-Level Multi-Source BFS on 2D Grids
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * Rot spreads simultaneously in all 4 directions from EVERY rotten orange (`2`)
 * at a constant speed of 1 step per minute.
 *
 * This natural multi-point expansion model is a textbook fit for Multi-Source BFS:
 *   1. All initial rotten oranges (`2`) are pushed into the Queue at t = 0.
 *   2. We process the queue layer by layer (level snapshotting), where each
 *      layer processed represents 1 elapsed minute.
 *   3. Fresh oranges (`1`) turn rotten (`2`) upon contact and join the queue
 *      to infect their own neighbors in the NEXT minute.
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP OF KEY PITFALLS & UNCERTAINTIES RESOLVED
 * ----------------------------------------------------------------------------
 *  - PITFALL 1: Decrementing `totalCleanFruits` upon `queue.poll()`.
 *    -> UNCERTINTY REASON: Why can't we decrement when popping from the queue?
 *    -> EXPLANATION: Polled elements are ALREADY rotten (either initially or
 *       infected previously). Decrementing on pop subtracts from the fresh count
 *       for oranges that were never fresh to begin with!
 *    -> FIX: Decrement `totalCleanFruits--` ONLY inside the neighbor check block
 *       at the exact moment `grid[nx][ny] == 1` is transformed into `2`.
 *
 *  - PITFALL 2: Correct Level Snapshotting (`queue.size()`).
 *    -> UNCERTINTY REASON: How do we track discrete minutes accurately?
 *    -> EXPLANATION: Capturing `int currentRottenFruits = queue.size()` before
 *       the inner loop freezes the batch of oranges rotting during the current minute.
 *       `minutes++` is then executed once after processing that entire batch.
 *
 *  - PITFALL 3: Starting with Zero Fresh Oranges (`totalCleanFruits == 0`).
 *    -> EXPLANATION: If the grid contains no fresh oranges (e.g., `[[2]]` or `[[0]]`),
 *       0 minutes are required. Checking this upfront prevents redundant loop cycles
 *       or off-by-one minute returns.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $M$ be the number of rows and $N$ be the number of columns in the grid.
 *
 * TIME COMPLEXITY: $\mathcal{O}(M \times N)$
 *   - Grid Scan: Initial double loop inspects all $M \times N$ cells once.
 *   - Queue Operations: Every orange is offered and polled at most once.
 *   - Neighbor Checks: 4 direction vectors evaluated per cell ($\mathcal{O}(1)$ work).
 *   - Overall time scales strictly linearly with total grid elements.
 *
 * SPACE COMPLEXITY: $\mathcal{O}(M \times N)$
 *   - Queue Storage: In the worst-case scenario (e.g., grid full of rotten oranges),
 *     the queue holds up to $M \times N$ elements.
 *   - In-Place Mutation: We mutate `grid[nx][ny] = 2` directly, eliminating
 *     the need for a separate `boolean[][] visited` matrix.
 * ============================================================================
 */
public class RottingFruit {

    public int orangesRotting(int[][] grid) {
        // ---------------------------------------------------------------------
        // GUARD CLAUSE: Handle empty or malformed grids
        // ---------------------------------------------------------------------
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int totalCleanFruits = 0;
        int minutes = 0;
        Queue<int[]> queue = new ArrayDeque<>();

        // ---------------------------------------------------------------------
        // STEP 1: MULTI-SOURCE INITIALIZATION & FRESH ORANGE COUNTING
        // ---------------------------------------------------------------------
        // INTUITION: Scan the grid to:
        // 1. Seed the Queue with ALL rotten oranges (`2`) up front (t = 0).
        // 2. Track the total count of fresh oranges (`1`) to check termination.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                }
                if (grid[i][j] == 1) {
                    totalCleanFruits++;
                }
            }
        }

        // ---------------------------------------------------------------------
        // EDGE CASE GUARD: No fresh oranges to rot
        // INTUITION: If there are 0 fresh oranges initially, 0 minutes are needed.
        // ---------------------------------------------------------------------
        if (totalCleanFruits == 0) {
            return 0;
        }

        // 4-directional offsets: {row, col} -> Up, Down, Right, Left
        int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

        // ---------------------------------------------------------------------
        // STEP 2: LEVEL-BY-LEVEL MULTI-SOURCE BFS
        // ---------------------------------------------------------------------
        // Continue while there are rotten oranges to process AND fresh oranges remain
        while (!queue.isEmpty() && totalCleanFruits > 0) {

            // LEVEL SNAPSHOT: Freeze the number of rotten oranges at the current minute layer.
            // This ensures we process ONLY this layer before incrementing `minutes`.
            int currentRottenFruits = queue.size();

            for (int i = 0; i < currentRottenFruits; i++) {
                int[] currentFruit = queue.poll();
                int x = currentFruit[0];
                int y = currentFruit[1];

                // Explore 4 adjacent neighbors
                for (int[] direction : directions) {
                    int nx = x + direction[0];
                    int ny = y + direction[1];

                    // ---------------------------------------------------------
                    // CONDITIONAL CHECK:
                    // 1. Boundary check: ensure (nx, ny) is inside grid limits.
                    // 2. Target check: cell MUST be a fresh orange (`1`).
                    //    (Walls `-1`, empty cells `0`, and rotten `2` are ignored).
                    // ---------------------------------------------------------
                    if (nx >= 0 && nx < rows && ny >= 0
                            && ny < cols && grid[nx][ny] == 1) {

                        // INTUITION FOR THIS PLACEMENT:
                        // 1. `grid[nx][ny] = 2`: Infect the fresh orange (in-place visited marker).
                        // 2. `totalCleanFruits--`: EXACT MATCH — decrement fresh count ONLY
                        //    when a fresh orange converts to rotten.
                        // 3. `queue.offer(...)`: Add newly infected orange so it can
                        //    infect its own neighbors during the NEXT minute layer.
                        grid[nx][ny] = 2;
                        totalCleanFruits--;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }

            // Incremented once AFTER processing the complete batch for the current minute layer
            minutes++;
        }

        // ---------------------------------------------------------------------
        // STEP 3: FINAL VERIFICATION
        // ---------------------------------------------------------------------
        // If fresh oranges remain unreachable (e.g., isolated by walls), return -1.
        // Otherwise, return total elapsed minutes.
        if (totalCleanFruits > 0) {
            return -1;
        }

        return minutes;
    }
}