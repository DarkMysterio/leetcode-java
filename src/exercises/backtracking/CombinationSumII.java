package exercises.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Combination Sum II (LeetCode 40)
 * PATTERN: Backtracking with Duplicate Pruning & Early Termination
 * ============================================================================
 *
 * HIGH-LEVEL INTUITION:
 * 1. DECISION TREE:
 *    At every step, we build a candidate combination element-by-element. This
 *    creates a decision tree where each level represents selecting a number
 *    to add to our path.
 *
 * 2. WHY WE MUST SORT:
 *    - Duplicate Prevention: Sorting brings duplicate values next to each other
 *      (e.g., [1, 2, 1, 2] -> [1, 1, 2, 2]). This lets us identify duplicate
 *      choices at the same level in O(1) time.
 *    - Early Pruning: Because the array is sorted in non-decreasing order, if
 *      a candidate exceeds our target, ALL subsequent candidates in the loop
 *      will also exceed it. We can stop searching immediately!
 *
 * 3. THE "SAME-LEVEL" DUPLICATE TRICK (`i > startIndex`):
 *    - `i == startIndex`: First choice at this tree level. We MUST try it
 *      (allows using multiple duplicate numbers along a single branch depth,
 *      e.g., [1, 1, 6]).
 *    - `i > startIndex`: Second or later choice at the SAME tree level. If
 *      candidates[i] == candidates[i - 1], we ALREADY explored a branch
 *      starting with this value at this level. We skip it to prevent generating
 *      duplicate combinations in our final result.
 *
 * ============================================================================
 * COMPLEXITY ANALYSIS:
 * ============================================================================
 * TIME COMPLEXITY: O(2^N * N)
 *   - so we can have up to two branches at each step and we have n steps
 *   - results in 2*2*2... n times => 2^n , we also have a sorting in nlog n but
 *   - 2^n is the dominant factor
 *   - In the worst case, every element can either be included or excluded in a
 *     combination, generating up to 2^N state evaluations.
 *   - Copying a valid path to the result list takes O(N) time.
 *   - Sorting takes O(N log N), which is dominated by O(2^N * N).
 *   - Note: In practice, pruning (early exit + duplicate skips) dramatically
 *     reduces runtime below the theoretical O(2^N) upper bound.
 *
 * SPACE COMPLEXITY: O(N)
 *   - Call Stack: The recursion tree reaches a maximum depth of N (when all
 *     elements are picked sequentially).
 *   - Path Storage: `currentPath` holds at most N elements at any given time.
 *   - (Result list space is excluded as standard in output complexity bounds).
 * ============================================================================
 */
public class CombinationSumII {

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();

        // INTUITION: Sorting is the foundation of this algorithm. It groups identical
        // numbers together so we can easily skip duplicate branches and prune early.
        Arrays.sort(candidates);

        // Start DFS / Backtracking from index 0 with an empty combination path and sum = 0.
        backtrack(result, new ArrayList<>(), target, 0, 0, candidates);

        return result;
    }

    private void backtrack(List<List<Integer>> result, List<Integer> currentPath,
                           int target, int currentSum, int startIndex, int[] candidates) {

        // ---------------------------------------------------------------------
        // BASE CASE 1: PRUNING (Exceeded target)
        // INTUITION: Since all numbers are positive, once currentSum > target,
        // adding more numbers will only increase the sum further. Stop exploring.
        // ---------------------------------------------------------------------
        if (currentSum > target) {
            return;
        }

        // ---------------------------------------------------------------------
        // BASE CASE 2: SUCCESS MATCH
        // INTUITION: We hit our target sum!
        // WHY `new ArrayList<>(currentPath)`? Java passes objects by reference.
        // `currentPath` is constantly mutated during backtracking. If we append
        // `currentPath` directly, future updates/pops will alter or clear our
        // saved answers! We must save an independent snapshot (deep copy).
        // WHY `return`? No need to add more numbers; any extra positive number
        // will exceed target.
        // ---------------------------------------------------------------------
        if (currentSum == target) {
            result.add(new ArrayList<>(currentPath));
            return;
        }

        // ---------------------------------------------------------------------
        // FOR-LOOP RECURSION BLUEPRINT
        // INTUITION: `startIndex` prevents picking earlier elements, avoiding
        // duplicate permutations (e.g., forces [1, 2, 5] instead of re-trying [2, 1, 5]).
        // ---------------------------------------------------------------------
        for (int i = startIndex; i < candidates.length; i++) {

            // -----------------------------------------------------------------
            // DUPLICATE PRUNING RULE
            // INTUITION:
            // - `i > startIndex` means we are looking at the 2nd, 3rd, etc., choice
            //   at the CURRENT recursion depth.
            // - If `candidates[i] == candidates[i - 1]`, we ALREADY tested a branch
            //   that starts with this identical value at this exact tree level.
            // - We skip it to avoid producing identical duplicate combination outputs.
            // -----------------------------------------------------------------
            if (i > startIndex && candidates[i] == candidates[i - 1]) {
                continue; // Skip duplicate candidate at the current level
            }

            // -----------------------------------------------------------------
            // EARLY TERMINATION OPTIMIZATION
            // INTUITION: Since `candidates` is sorted, if adding `candidates[i]`
            // exceeds our remaining target, every subsequent element (`candidates[i+1]`,
            // `candidates[i+2]`, etc.) is guaranteed to be >= `candidates[i]` and
            // will ALSO exceed the target. We can safely `break` out of the loop
            // entirely rather than wasting time checking remaining iterations!
            // -----------------------------------------------------------------
            if (currentSum + candidates[i] > target) {
                break;
            }

            // -----------------------------------------------------------------
            // STEP 1: CHOOSE
            // INTUITION: Temporarily add the current candidate to test if it leads
            // to a valid solution path.
            // -----------------------------------------------------------------
            currentPath.add(candidates[i]);

            // -----------------------------------------------------------------
            // STEP 2: RECURSE
            // INTUITION: Move forward in the tree.
            // WHY `i + 1`? Problem constraint specifies each array index can be
            // used AT MOST ONCE per combination. Passing `i + 1` guarantees the
            // recursive child call cannot re-use element at index `i`.
            // -----------------------------------------------------------------
            backtrack(result, currentPath, target, currentSum + candidates[i], i + 1, candidates);

            // -----------------------------------------------------------------
            // STEP 3: UN-CHOOSE (BACKTRACK)
            // INTUITION: Remove the candidate we just tested. This restores
            // `currentPath` to its exact prior state, allowing the loop to cleanly
            // try the NEXT candidate at the current level without path pollution.
            // -----------------------------------------------------------------
            currentPath.remove(currentPath.size() - 1);
        }
    }
}