package exercises.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetsII {

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        // STEP 1: Sorting is MANDATORY.
        // It guarantees that all duplicate numbers are placed next to each other
        // (e.g., [2, 1, 2] -> [1, 2, 2]), allowing adjacent comparison with nums[i - 1].
        Arrays.sort(nums);

        // Start backtracking from index 0 with an empty path list
        backtrack(result, 0, new ArrayList<>(), nums);

        return result;
    }

    private void backtrack(List<List<Integer>> result, int startIndex, List<Integer> currentPath, int[] nums) {

        // Every state reached in the recursion tree represents a valid subset.
        // We create a deep copy (new ArrayList) because 'currentPath' is modified during backtracking.
        result.add(new ArrayList<>(currentPath));

        // Explore all possible remaining candidates starting from 'startIndex'
        for (int i = startIndex; i < nums.length; i++) {

            /*
             * =========================================================================
             * DUPLICATE SKIP LOGIC BREAKDOWN:
             * =========================================================================
             *
             * Condition 1: 'nums[i] == nums[i - 1]'
             * Checks if the current number is identical to the previous number.
             *
             * Condition 2: 'i > startIndex'
             * Distinguishes VERTICAL decisions (depth) from HORIZONTAL decisions (siblings):
             *
             * - CASE A: i == startIndex (First iteration of this loop)
             *   'i > startIndex' is FALSE -> DO NOT SKIP.
             *   The duplicate number (nums[i - 1]) was chosen by the PARENT call above.
             *   We are expanding VERTICALLY (going deeper) to form longer subsets
             *   like [2, 2] or [2, 2, 2].
             *
             * - CASE B: i > startIndex (Second or later iteration of this loop)
             *   'i > startIndex' is TRUE -> SKIP!
             *   We are moving HORIZONTALLY at the same depth level.
             *   We already completed exploring a sibling (nums[i - 1]) at this exact slot.
             *   Picking nums[i] now would create a redundant duplicate branch.
             * =========================================================================
             */
            if (i > startIndex && nums[i] == nums[i - 1]) {
                continue; // Skip identical sibling choices at this level
            }

            // 1. CHOOSE: Add candidate to the current subset
            currentPath.add(nums[i]);

            // 2. EXPLORE: Advance to the next depth.
            // Pass 'i + 1' so child calls only consider elements after index 'i'.
            backtrack(result, i + 1, currentPath, nums);

            // 3. UN-CHOOSE: Backtrack by removing the last element
            // so the next loop iteration gets a clean slate.
            currentPath.remove(currentPath.size() - 1);
        }
    }
}