package exercises.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Combination Sum (LeetCode 39)
 * PATTERN: Backtracking (For-Loop / Multi-Way Decision Tree)
 * ============================================================================
 *
 * TIME COMPLEXITY: O(N^(T / M))
 *   - N = total elements in 'nums' array.
 *   - T = target value.
 *   - M = minimum element value in 'nums'.
 *   - The max depth of the recursion tree is (T / M) (e.g., target = 7, min = 1 -> max depth 7).
 *   - At each level, the loop can branch up to N times.
 *
 * SPACE COMPLEXITY: O(T / M)
 *   - The maximum depth of the call stack and 'currentPath' list is bounded by (T / M).
 *   - (Excluding the space used to store the output 'result').
 * ============================================================================
 */
public class CombinationSum {

    public List<List<Integer>> combinationSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), 0, target, 0, nums);
        return result;
    }

    public void backtrack(List<List<Integer>> result, List<Integer> currentPath,
                          int startIndex, int target, int currentSum, int[] nums) {

        // ---------------------------------------------------------------------
        // BASE CASE 1: PRUNING (Exceeded target)
        // If currentSum exceeds target, stop exploring this branch immediately.
        // ---------------------------------------------------------------------
        if (currentSum > target) {
            return;
        }

        // ---------------------------------------------------------------------
        // BASE CASE 2: SUCCESS MATCH
        // ⚠️ TRAP ALERT (FIXED): Always include 'return;' here!
        // If you forget 'return;', execution falls into the loop below, trying
        // to add more positive numbers to an already completed sum.
        // ---------------------------------------------------------------------
        if (currentSum == target) {
            // Must pass a shallow copy (new ArrayList<>), NOT currentPath directly!
            result.add(new ArrayList<>(currentPath));
            return;
        }

        // ---------------------------------------------------------------------
        // FOR-LOOP BACKTRACKING BLUEPRINT
        // Loop iterates over candidate choices starting from 'startIndex'.
        // Using 'startIndex' guarantees combination ordering ([2,3] vs [3,2])
        // to eliminate duplicate results.
        // ---------------------------------------------------------------------
        for (int i = startIndex; i < nums.length; i++) {

            // 1. MAKE A CHOICE (Choose nums[i])
            currentPath.add(nums[i]);

            // 2. RECURSE / EXPLORE
            // ⚠️ YOUR PREVIOUS BIG TRAP (NEVER DO THIS):
            // DO NOT put two backtrack calls (e.g., backtrack(i) AND backtrack(i+1))
            // inside a for-loop!
            //
            // WHY? The 'for' loop ALREADY handles moving to i+1, i+2, etc.
            // Calling both inside a loop duplicates work exponentially and creates false permutations.
            //
            // CHEAT SHEET FOR THE 'index' ARGUMENT:
            // - Pass 'i'   -> Elements CAN be reused infinitely (Combination Sum I).
            // - Pass 'i+1' -> Elements CANNOT be reused (Subsets, Combination Sum II, Permutations).
            backtrack(result, currentPath, i, target, currentSum + nums[i], nums);

            // 3. UNDO CHOICE (Backtrack / Un-choose nums[i])
            // Remove the last added element so currentPath is restored for the next loop iteration.
            currentPath.remove(currentPath.size() - 1);
        }
    }
}