package exercises.backtracking;

/**
 * PROBLEM: Word Search (LeetCode 79)
 *
 * =============================================================================
 * INTUITION & APPROACH
 * =============================================================================
 * We need to determine if a target word can be constructed by traversing adjacent
 * cells (up, down, left, right) on a 2D character grid.
 *
 * 1. DFS (Depth-First Search):
 *    At any matching cell, we explore down a path character-by-character.
 *
 * 2. Backtracking:
 *    If a path reaches a dead-end (boundary, mismatched character, or visited cell),
 *    we MUST "undo" our choice by un-marking the current cell as visited. This ensures
 *    that other starting points or alternative path branches can reuse this cell.
 *
 * =============================================================================
 * COMPLEXITY ANALYSIS
 * =============================================================================
 * - Time Complexity: O(N * M * 3^L)
 *   * N = rows, M = columns, L = length of the target word.
 *   * In the worst case, we trigger a search from every single cell on the board (N * M).
 *   * From the starting cell, we have 4 directions to try. For every subsequent character
 *     in the path, we have at most 3 choices (since we cannot step backward onto the cell
 *     we just came from).
 *
 * - Space Complexity: O(L)
 *   * The max recursion stack depth is equal to L (the length of the word).
 *   * Modifying the board matrix in-place avoids needing an auxiliary O(N * M)
 *     boolean matrix for visited tracking, keeping extra space usage at O(1).
 */
public class WordSearch {

    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        // Iterate through every cell on the grid as a potential starting letter
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // If a path starting at board[i][j] completes the word, return true immediately
                if (backtrack(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean backtrack(char[][] board, String word, int index, int x, int y) {
        // =========================================================================
        // BASE CASE 1: SUCCESS
        // =========================================================================
        // If 'index' equals word.length(), we have successfully matched all characters
        // from index 0 to word.length() - 1.
        if (index == word.length()) {
            return true;
        }

        // =========================================================================
        // BASE CASE 2: BOUNDARY CHECKS & MISMATCH PRUNING
        // -------------------------------------------------------------------------
        // PREVIOUS MISTAKES ADDRESSED HERE:
        // 1. Array Out-Of-Bounds (Off-By-One):
        //    - Arrays are 0-indexed. Valid row indices are 0 to board.length - 1.
        //      Using 'x >= board.length' (instead of x > board.length) prevents crash.
        // 2. Column Bounds Bug:
        //    - 'y' represents columns, so compare 'y' to 'board[0].length', NOT 'board.length'.
        // 3. String Index Out-Of-Bounds:
        //    - Checking 'index == word.length()' BEFORE this block guarantees 'index' is
        //      always a valid index for 'word.charAt(index)'.
        // 4. Visited Cell Match:
        //    - If board[x][y] was previously marked as '#' (visited), board[x][y] != charAt(index)
        //      automatically evaluates to true, preventing infinite loops.
        // =========================================================================
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != word.charAt(index)) {
            return false;
        }

        // =========================================================================
        // STEP 1: CHOOSE & MARK VISITED
        // -------------------------------------------------------------------------
        // PREVIOUS MISTAKE ADDRESSED HERE:
        // - Missing visited tracking causes infinite recursion (e.g., bouncing
        //   back and forth between adjacent matching letters), leading to a StackOverflowError.
        // - We mutate board[x][y] in-place to '#' so downstream recursive calls know it's taken.
        // =========================================================================
        char temp = board[x][y];
        board[x][y] = '#';

        // =========================================================================
        // STEP 2: EXPLORE (4 Directions)
        // -------------------------------------------------------------------------
        // Use logical OR (||) short-circuiting: if any direction succeeds (returns true),
        // Java skips evaluating the rest of the directions for maximum efficiency.
        // =========================================================================
        boolean found = backtrack(board, word, index + 1, x + 1, y) || // Down
                backtrack(board, word, index + 1, x - 1, y) || // Up
                backtrack(board, word, index + 1, x, y + 1) || // Right
                backtrack(board, word, index + 1, x, y - 1);   // Left

        // =========================================================================
        // STEP 3: BACKTRACK (RESTORE STATE)
        // -------------------------------------------------------------------------
        // CRITICAL STEP: Reset the cell back to its original character before returning.
        // This allows other paths or subsequent starting cells in the main loop to use
        // this cell.
        // =========================================================================
        board[x][y] = temp;

        return found;
    }
}