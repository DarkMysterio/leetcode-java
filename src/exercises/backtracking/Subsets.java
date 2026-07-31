package exercises.backtracking;

import java.util.ArrayList;
import java.util.List;

public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        backtrack(results,new ArrayList<>(),0, nums);
        return results;
    }

    private void backtrack(List<List<Integer>> results,List<Integer> currentPath,int startIndex,int[] nums){
        // 1. RECORD/BASE CASE: Save the current solution state
        // Note: We MUST make a deep copy (new ArrayList) because currentPath is mutated.
        results.add(new ArrayList<>(currentPath));

        // Optional: If there's a specific goal size or stopping condition, return here.
        // if (currentPath.size() == target) return;

        // 2. EXPLORE CHOICES: Loop through available choices from startIndex onwards
        for (int i = startIndex; i < nums.length; i++) {

            // --- CHOICE ---
            currentPath.add(nums[i]);

            // --- RECURSE ---
            // Move to the next element (i + 1)
            backtrack(results, currentPath, i + 1, nums);

            // --- UNDO (BACKTRACK) ---
            // Remove the last added element to reset state for the next loop iteration
            currentPath.remove(currentPath.size() - 1);
        }
    }
}
