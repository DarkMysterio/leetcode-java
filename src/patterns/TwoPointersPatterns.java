package patterns;

import java.util.*;

/**
 * ============================================================
 * TWO POINTERS PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Array/string is sorted (or can be sorted)
 *  - Looking for a pair/triplet that satisfies a condition
 *  - Need to check characters from both ends (palindrome)
 *  - Partitioning / removing elements in-place
 *
 * Two flavors:
 *  A) OPPOSITE DIRECTION — left starts at 0, right starts at end
 *     → Close in toward each other
 *  B) SAME DIRECTION (slow/fast) — both start at 0, move at different speeds
 *     → Used for removing duplicates, moving zeros, etc.
 *
 * Time: O(n)  Space: O(1)
 */
public class TwoPointersPatterns {

    public static void main(String[] args) {
        System.out.println("=== Valid Palindrome ===");
        System.out.println(validPalindromeSolved("A man, a plan, a canal: Panama")); // true
        System.out.println(validPalindromeSolved("race a car"));                      // false

        System.out.println("\n=== Two Sum II ===");
        int[] res = twoSumIISolved(new int[]{2, 7, 11, 15}, 9);
        System.out.println(Arrays.toString(res)); // [1, 2]

        System.out.println("\n=== Container With Most Water ===");
        System.out.println(maxWaterSolved(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})); // 49

        System.out.println("\n=== 3Sum ===");
        List<List<Integer>> triples = threeSumSolved(new int[]{-1, 0, 1, 2, -1, -4});
        System.out.println(triples); // [[-1,-1,2],[-1,0,1]]
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE A — Opposite Direction
    // ─────────────────────────────────────────────
    static void oppositeDirectionTemplate(int[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            // Do something with arr[left] and arr[right]
            // Decide which pointer to move:
            if (arr[left] + arr[right] < /* target */ 0) {
                left++;   // need larger value → move left forward
            } else if (arr[left] + arr[right] > 0) {
                right--;  // need smaller value → move right backward
            } else {
                // found the answer
                left++;
                right--;
            }
        }
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATE B — Same Direction (slow/fast)
    // ─────────────────────────────────────────────
    static int sameDirectionTemplate(int[] arr) {
        int slow = 0; // "write" pointer — where next valid element goes

        for (int fast = 0; fast < arr.length; fast++) {
            if (/* arr[fast] is valid */ arr[fast] != 0) {
                arr[slow] = arr[fast]; // write valid element to slow position
                slow++;
            }
            // fast always moves forward; slow only moves when we write
        }

        return slow; // length of valid portion
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: VALID PALINDROME
    // ─────────────────────────────────────────────
    // LeetCode 125 — Easy
    // A phrase is a palindrome if, after keeping only alphanumeric chars
    // and lowercasing, it reads the same forward and backward.
    //
    // Example: "A man, a plan, a canal: Panama" → true
    //          "race a car"                     → false
    //
    // Approach:
    //  - Two pointers from both ends
    //  - Skip non-alphanumeric characters
    //  - Compare lowercased chars; if mismatch → false
    //
    // Time: O(n)  Space: O(1)

    // TODO VERSION
    static boolean validPalindromeTODO(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            // TODO: skip non-alphanumeric on the left
            // while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;

            // TODO: skip non-alphanumeric on the right

            // TODO: compare chars (lowercase both)
            // if not equal → return false
            // else → move both pointers inward
        }

        return true;
    }

    // SOLVED VERSION
    static boolean validPalindromeSolved(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            // Skip non-alphanumeric chars from the left
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            // Skip non-alphanumeric chars from the right
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;

            // Compare lowercased characters
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false; // mismatch → not a palindrome
            }

            left++;  // move inward
            right--;
        }

        return true; // all chars matched
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: TWO SUM II (Sorted Array)
    // ─────────────────────────────────────────────
    // LeetCode 167 — Medium
    // Given a 1-indexed sorted array, find two numbers that sum to target.
    //
    // Example: numbers=[2,7,11,15], target=9 → [1,2]
    //
    // Approach:
    //  - Sorted → opposite two pointers work perfectly
    //  - If sum < target → need larger → move left right
    //  - If sum > target → need smaller → move right left
    //  - If sum == target → found it
    //
    // Time: O(n)  Space: O(1)

    // TODO VERSION
    static int[] twoSumIITODO(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            // TODO: if sum == target → return 1-indexed positions
            // TODO: if sum < target → move left
            // TODO: if sum > target → move right
        }

        return new int[]{};
    }

    // SOLVED VERSION
    static int[] twoSumIISolved(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1}; // +1 because problem is 1-indexed
            } else if (sum < target) {
                left++;  // need a bigger number → move left pointer right
            } else {
                right--; // need a smaller number → move right pointer left
            }
        }

        return new int[]{}; // guaranteed not reached per problem constraints
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: CONTAINER WITH MOST WATER
    // ─────────────────────────────────────────────
    // LeetCode 11 — Medium
    // Given height[], find two lines that form a container with max water.
    // Water = min(height[left], height[right]) * (right - left)
    //
    // Example: height=[1,8,6,2,5,4,8,3,7] → 49
    //
    // Approach:
    //  - Start with widest container (left=0, right=end)
    //  - Always move the shorter side inward (can only improve by taller wall)
    //  - Track max area seen
    //
    // Time: O(n)  Space: O(1)

    // TODO VERSION
    static int maxWaterTODO(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            // TODO: compute current area = min(height[left], height[right]) * width
            // TODO: update maxArea
            // TODO: move the pointer with the shorter height inward
        }

        return maxArea;
    }

    // SOLVED VERSION
    static int maxWaterSolved(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            int width = right - left;                              // horizontal distance
            int h = Math.min(height[left], height[right]);        // water level = shorter wall
            int area = h * width;
            maxArea = Math.max(maxArea, area);                    // update best

            // Move the shorter wall inward — moving taller wall can only hurt or stay same
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 4: 3SUM
    // ─────────────────────────────────────────────
    // LeetCode 15 — Medium
    // Given int[] nums, return all unique triplets [a,b,c] where a+b+c=0.
    //
    // Example: [-1,0,1,2,-1,-4] → [[-1,-1,2],[-1,0,1]]
    //
    // Approach:
    //  1. Sort the array
    //  2. Fix one element (i), use two pointers for the other two
    //  3. Skip duplicates carefully to avoid repeat triplets
    //
    // Time: O(n²)  Space: O(1) excluding output

    // TODO VERSION
    static List<List<Integer>> threeSumTODO(int[] nums) {
        Arrays.sort(nums); // MUST sort first
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            // TODO: skip duplicate values for i (if i>0 && nums[i]==nums[i-1], continue)

            // TODO: set left = i+1, right = nums.length - 1
            // TODO: two-pointer loop while left < right:
            //   compute sum = nums[i] + nums[left] + nums[right]
            //   if sum == 0 → add triplet, skip duplicates, move both pointers
            //   if sum < 0 → move left
            //   if sum > 0 → move right
        }

        return result;
    }

    // SOLVED VERSION
    static List<List<Integer>> threeSumSolved(int[] nums) {
        Arrays.sort(nums); // sort so two-pointer approach works
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate values for the fixed element
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    // Skip duplicates for left pointer
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    // Skip duplicates for right pointer
                    while (left < right && nums[right] == nums[right - 1]) right--;

                    left++;  // move both inward after recording triplet
                    right--;
                } else if (sum < 0) {
                    left++;  // sum too small → need bigger value
                } else {
                    right--; // sum too large → need smaller value
                }
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH TWO POINTERS
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  while (left <= right)    in palindrome → compares char to itself
     * ✅ RIGHT:  while (left < right)
     *
     * ❌ WRONG:  return [left+1, right+1] for 0-indexed problems
     * ✅ RIGHT:  check whether problem is 0-indexed or 1-indexed!
     *
     * ❌ WRONG:  forgetting to skip duplicates in 3Sum → duplicate triplets in output
     * ✅ RIGHT:  if (i > 0 && nums[i] == nums[i-1]) continue;
     *           and inner duplicate skip after finding a triplet
     *
     * ❌ WRONG:  not sorting first in 3Sum / TwoSumII
     * ✅ RIGHT:  Arrays.sort(nums); at the top of the method
     */
}
