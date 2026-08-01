package exercises.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Permutations (LeetCode 46)
 * PATTERN: Backtracking (Ordering / Position Placement Decision Tree)
 * ============================================================================
 *
 * HIGH-LEVEL INTUITION:
 * 1. COMBINATIONS VS. PERMUTATIONS (The Core Mental Pivot):
 *    - Combinations ([1, 2] == [2, 1]): Order DOES NOT matter. We use a `startIndex`
 *      to force the loop to only look forward to avoid duplicate orderings.
 *    - Permutations ([1, 2] != [2, 1]): Order DOES matter. Every single position in
 *      the result can pick ANY remaining unused number. Therefore, we ALWAYS loop
 *      from index `0` instead of `startIndex`!
 *
 * 2. DECISION TREE STRUCTURE:
 *    - Level 0 (First Slot):  We have N choices to place in position 0.
 *    - Level 1 (Second Slot): We have N - 1 choices left to place in position 1.
 *    - Level K (Slot K):      We have N - K choices left.
 *    - Total Leaf Nodes = N * (N - 1) * (N - 2) * ... * 1 = N! (N Factorial).
 *
 * 3. TRACKING USED ELEMENTS:
 *    - Since we loop from index `0` at every level, we must prevent reusing an element
 *      that is ALREADY in our current branch (`currentPath`).
 *    - `!currentPath.contains(nums[i])` ensures each element is used at most once per path.
 *    - TRAP / OPTIMIZATION NOTE: `List.contains()` is an O(N) lookup. In production /
 *      interviews, using a `boolean[] visited` array reduces element check time to O(1)!
 *
 * ============================================================================
 * COMPLEXITY ANALYSIS:
 * ============================================================================
 * TIME COMPLEXITY: O(N * N!)  [or O(N^2 * N!) due to `currentPath.contains()`]
 *   - There are N! total unique permutations (leaves of the recursion tree).
 *   - Building each permutation requires N steps.
 *   - Creating a snapshot copy `new ArrayList<>(currentPath)` takes O(N) time.
 *   - Overall Time Bound: O(N * N!).
 *
 * SPACE COMPLEXITY: O(N)
 *   - Call Stack: The recursion tree goes down to depth N (one level per slot).
 *   - Current Path: `currentPath` holds at most N elements.
 *   - (Output list `result` space is excluded per standard algorithmic conventions).
 * ============================================================================
 */
public class Permutations {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        // Start recursion from an empty path.
        backtrack(result, new ArrayList<>(), nums);

        return result;
    }

    public void backtrack(List<List<Integer>> result, List<Integer> currentPath, int[] nums) {

        // ---------------------------------------------------------------------
        // BASE CASE: PERMUTATION COMPLETE
        // INTUITION: When `currentPath` length equals `nums.length`, we have filled
        // every available slot in this permutation branch.
        // ---------------------------------------------------------------------
        if (currentPath.size() == nums.length) {
            // DEEP COPY REQUIRED: Save a snapshot of 'currentPath' so subsequent
            // backtracking mutations do not destroy our saved result!
            result.add(new ArrayList<>(currentPath));
            return;
        }

        // ---------------------------------------------------------------------
        // FOR-LOOP BLUEPRINT FOR PERMUTATIONS
        // INTUITION: Always start from index 0 because any unused number in `nums`
        // can fill the current position, regardless of where it appears in `nums`.
        // ---------------------------------------------------------------------
        for (int i = 0; i < nums.length; i++) {

            // -----------------------------------------------------------------
            // ELEMENT SELECTION CHECK
            // INTUITION: Only pick `nums[i]` if it hasn't been chosen earlier in
            // this specific branch path.
            // -----------------------------------------------------------------
            if (!currentPath.contains(nums[i])) {

                // 1. CHOOSE
                // Place nums[i] into the current available slot of our permutation.
                currentPath.add(nums[i]);

                // 2. RECURSE
                // Move to the next slot position. Notice we pass the same parameters—
                // the child call will loop from index 0 again to find the next unused number.
                backtrack(result, currentPath, nums);

                // 3. UN-CHOOSE (BACKTRACK)
                // Remove the last added element to restore 'currentPath' state.
                // This frees up 'nums[i]' so other parallel branches can use it!
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }
}