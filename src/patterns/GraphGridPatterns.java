package patterns;

import java.util.*;

/**
 * ============================================================
 * GRAPH & GRID PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Problem involves a 2D grid (matrix)
 *  - "Count connected regions", "spread from source", "reachability"
 *  - Island counting, flood fill, shortest path in grid
 *
 * Key building blocks:
 *  1. Directions array — defines 4 neighbors (up, down, left, right)
 *  2. Visited matrix   — avoids revisiting cells
 *  3. DFS (recursive or stack-based) — explore each connected region fully
 *  4. BFS (queue-based) — explore level by level (useful for min steps)
 *
 * Common pattern:
 *  for each cell in grid:
 *    if cell is unvisited and is a "start":
 *      call DFS/BFS from that cell
 *      count++ (if counting regions)
 *
 * Time: O(rows * cols)  Space: O(rows * cols) for visited matrix
 */
public class GraphGridPatterns {

    // ─────────────────────────────────────────────
    // DIRECTIONS ARRAY — the 4 cardinal neighbors
    // ─────────────────────────────────────────────
    static final int[][] DIRS = {
        {-1,  0},  // up
        { 1,  0},  // down
        { 0, -1},  // left
        { 0,  1}   // right
    };

    public static void main(String[] args) {
        System.out.println("=== Number of Islands ===");
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        System.out.println(numIslandsSolved(grid1)); // 1

        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        System.out.println(numIslandsSolved(grid2)); // 3

        System.out.println("\n=== Flood Fill ===");
        int[][] image = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] filled = floodFillSolved(image, 1, 1, 2);
        System.out.println(Arrays.deepToString(filled)); // [[2,2,2],[2,2,0],[2,0,1]]

        System.out.println("\n=== Rotting Oranges ===");
        int[][] oranges = {{2,1,1},{1,1,0},{0,1,1}};
        System.out.println(rottingOrangesSolved(oranges)); // 4
    }

    // ─────────────────────────────────────────────
    // HELPER: Bounds check
    // ─────────────────────────────────────────────
    static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE A — DFS Grid
    // ─────────────────────────────────────────────
    static void dfsGrid(char[][] grid, boolean[][] visited, int r, int c) {
        int rows = grid.length, cols = grid[0].length;

        // Base cases: out of bounds, already visited, or invalid cell
        if (!inBounds(r, c, rows, cols)) return;
        if (visited[r][c]) return;
        if (grid[r][c] == '0') return; // not a land cell

        visited[r][c] = true; // mark as visited

        // Explore all 4 neighbors
        for (int[] dir : DIRS) {
            dfsGrid(grid, visited, r + dir[0], c + dir[1]);
        }
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE B — BFS Grid
    // ─────────────────────────────────────────────
    static int bfsGrid(int[][] grid, int startR, int startC) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{startR, startC}); // start position
        visited[startR][startC] = true;
        int steps = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // BFS level = one "step" away

            for (int i = 0; i < levelSize; i++) {
                int[] cell = queue.poll();
                int r = cell[0], c = cell[1];

                // Process cell — record result, etc.

                for (int[] dir : DIRS) {
                    int nr = r + dir[0], nc = c + dir[1];
                    if (inBounds(nr, nc, rows, cols) && !visited[nr][nc]) {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
            steps++; // one more level = one more step
        }

        return steps;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: NUMBER OF ISLANDS
    // ─────────────────────────────────────────────
    // LeetCode 200 — Medium
    // Count number of islands in a 2D grid ('1' = land, '0' = water).
    // Connected lands (horizontally/vertically) form one island.
    //
    // Example:
    //   1 1 0 0 0
    //   1 1 0 0 0
    //   0 0 1 0 0
    //   0 0 0 1 1
    //   → 3 islands
    //
    // Approach:
    //  - Iterate every cell
    //  - If land ('1') and not visited → new island found, DFS to mark it all visited
    //  - Count++ each time we start a new DFS
    //
    // Time: O(r*c)  Space: O(r*c)

    // TODO VERSION
    static int numIslandsTODO(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // TODO: if grid[r][c] == '1' and not visited:
                //   count++
                //   DFS from (r, c) to mark the whole island as visited
            }
        }

        return count;
    }

    // SOLVED VERSION
    static int numIslandsSolved(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // New unvisited land cell = new island
                if (grid[r][c] == '1' && !visited[r][c]) {
                    count++;
                    dfsIsland(grid, visited, r, c, rows, cols); // mark entire island
                }
            }
        }

        return count;
    }

    // DFS helper: mark all connected land as visited
    static void dfsIsland(char[][] grid, boolean[][] visited, int r, int c, int rows, int cols) {
        if (!inBounds(r, c, rows, cols)) return; // out of grid
        if (visited[r][c]) return;               // already seen
        if (grid[r][c] == '0') return;           // water, stop

        visited[r][c] = true; // mark as part of current island

        for (int[] dir : DIRS) {
            dfsIsland(grid, visited, r + dir[0], c + dir[1], rows, cols);
        }
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: FLOOD FILL
    // ─────────────────────────────────────────────
    // LeetCode 733 — Easy
    // Fill connected region of same color with a new color.
    //
    // Example: image=[[1,1,1],[1,1,0],[1,0,1]], sr=1, sc=1, color=2
    //       → [[2,2,2],[2,2,0],[2,0,1]]
    //
    // Approach:
    //  - Record original color at (sr, sc)
    //  - DFS: if cell has original color → paint it, recurse to neighbors
    //  - Watch out: if newColor == originalColor → skip (avoid infinite loop)
    //
    // Time: O(r*c)  Space: O(r*c)

    // TODO VERSION
    static int[][] floodFillTODO(int[][] image, int sr, int sc, int color) {
        int originalColor = image[sr][sc];
        if (originalColor == color) return image; // no-op guard

        // TODO: call a DFS helper to fill starting from (sr, sc)
        return image;
    }

    static void floodFillDFSTODO(int[][] image, int r, int c, int originalColor, int newColor) {
        // TODO: bounds check
        // TODO: if cell is not originalColor, return
        // TODO: paint cell with newColor
        // TODO: recurse in all 4 directions
    }

    // SOLVED VERSION
    static int[][] floodFillSolved(int[][] image, int sr, int sc, int color) {
        int originalColor = image[sr][sc];
        if (originalColor == color) return image; // already the target color → no infinite loop

        floodFillDFS(image, sr, sc, originalColor, color, image.length, image[0].length);
        return image;
    }

    static void floodFillDFS(int[][] image, int r, int c, int origColor, int newColor, int rows, int cols) {
        if (!inBounds(r, c, rows, cols)) return;      // out of grid
        if (image[r][c] != origColor) return;         // wrong color, stop

        image[r][c] = newColor; // paint this cell

        for (int[] dir : DIRS) {
            floodFillDFS(image, r + dir[0], c + dir[1], origColor, newColor, rows, cols);
        }
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: ROTTING ORANGES
    // ─────────────────────────────────────────────
    // LeetCode 994 — Medium
    // Grid: 0=empty, 1=fresh, 2=rotten.
    // Each minute, rotten oranges spread to adjacent fresh ones.
    // Return minutes to rot all oranges, or -1 if impossible.
    //
    // Example: [[2,1,1],[1,1,0],[0,1,1]] → 4
    //
    // Approach (multi-source BFS):
    //  - Start BFS from ALL rotten oranges simultaneously
    //  - Each BFS level = 1 minute
    //  - Count fresh oranges; if any remain after BFS → -1
    //
    // Time: O(r*c)  Space: O(r*c)

    // TODO VERSION
    static int rottingOrangesTODO(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Deque<int[]> queue = new ArrayDeque<>();
        int freshCount = 0;

        // TODO: iterate all cells:
        //   if cell == 2 → add to queue (start of BFS)
        //   if cell == 1 → increment freshCount

        int minutes = 0;

        while (!queue.isEmpty() /* TODO: && freshCount > 0 */) {
            // TODO: process one BFS level (one minute)
            //   for each cell in current level:
            //     try all 4 neighbors
            //     if neighbor is fresh (==1) → rot it (set to 2), add to queue, freshCount--
            // minutes++
        }

        // TODO: return freshCount > 0 ? -1 : minutes
        return -1;
    }

    // SOLVED VERSION
    static int rottingOrangesSolved(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Deque<int[]> queue = new ArrayDeque<>();
        int freshCount = 0;

        // Initialize: find all rotten oranges and count fresh ones
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c}); // rotten → BFS start
                if (grid[r][c] == 1) freshCount++;                  // fresh → must be reached
            }
        }

        int minutes = 0;

        // Multi-source BFS: spread rot level by level (1 level = 1 minute)
        while (!queue.isEmpty() && freshCount > 0) {
            int levelSize = queue.size(); // oranges rotting this minute

            for (int i = 0; i < levelSize; i++) {
                int[] cell = queue.poll();
                int r = cell[0], c = cell[1];

                for (int[] dir : DIRS) {
                    int nr = r + dir[0], nc = c + dir[1];

                    if (inBounds(nr, nc, rows, cols) && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2;               // rot the neighbor
                        freshCount--;                    // one less fresh orange
                        queue.offer(new int[]{nr, nc}); // add to next BFS level
                    }
                }
            }

            minutes++; // finished one minute of spreading
        }

        // If any fresh oranges remain → impossible
        return freshCount > 0 ? -1 : minutes;
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH GRIDS
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  grid[r][c] without bounds check → ArrayIndexOutOfBoundsException
     * ✅ RIGHT:  always check inBounds(r, c, rows, cols) first
     *
     * ❌ WRONG:  int rows = grid[0].length; cols = grid.length (swapped!)
     * ✅ RIGHT:  int rows = grid.length; int cols = grid[0].length;
     *
     * ❌ WRONG:  not using a visited matrix or modifying original → revisit cells
     * ✅ RIGHT:  use boolean[][] visited or mark cell as visited in-place
     *
     * ❌ WRONG:  Flood Fill: if originalColor == newColor → infinite recursion
     * ✅ RIGHT:  if (originalColor == color) return image; at the start
     *
     * ❌ WRONG:  Rotting Oranges: starting BFS from only one rotten orange
     * ✅ RIGHT:  multi-source BFS — add ALL initial rotten oranges to queue first
     *
     * ❌ WRONG:  queue.offer(new int[]{r, c}) after processing, not all 4 dirs
     * ✅ RIGHT:  loop over DIRS array, check bounds, then offer
     */
}
