package patterns;

import java.util.*;

/**
 * ============================================================
 * BINARY SEARCH PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Array is SORTED (or monotonic answer space)
 *  - Looking for a specific value, lower/upper bound
 *  - "Search on Answer": can I achieve X? → binary search on X
 *    (e.g., minimum capacity, maximum possible value)
 *
 * Core idea: Each iteration eliminates HALF the search space.
 *
 * Three variations:
 *  A) Classic: find exact target
 *  B) Lower bound: find first index where nums[i] >= target
 *  C) Upper bound: find first index where nums[i] > target
 *  D) Search on answer: binary search on a "feasibility" function
 *
 * ⚠️ Key danger: off-by-one errors in while condition and mid calculation
 *
 * Time: O(log n)  Space: O(1)
 */
public class BinarySearchPatterns {

    public static void main(String[] args) {
        System.out.println("=== Binary Search ===");
        System.out.println(binarySearchSolved(new int[]{-1,0,3,5,9,12}, 9));  // 4
        System.out.println(binarySearchSolved(new int[]{-1,0,3,5,9,12}, 2));  // -1

        System.out.println("\n=== Search Insert Position ===");
        System.out.println(searchInsertSolved(new int[]{1,3,5,6}, 5)); // 2
        System.out.println(searchInsertSolved(new int[]{1,3,5,6}, 2)); // 1
        System.out.println(searchInsertSolved(new int[]{1,3,5,6}, 7)); // 4

        System.out.println("\n=== Search in Rotated Sorted Array ===");
        System.out.println(searchRotatedSolved(new int[]{4,5,6,7,0,1,2}, 0)); // 4
        System.out.println(searchRotatedSolved(new int[]{4,5,6,7,0,1,2}, 3)); // -1

        System.out.println("\n=== Koko Eating Bananas ===");
        System.out.println(kokoSolved(new int[]{3,6,7,11}, 8)); // 4
        System.out.println(kokoSolved(new int[]{30,11,23,4,20}, 5)); // 30
    }

    // ─────────────────────────────────────────────
    // TEMPLATE A — Classic Binary Search (exact target)
    // ─────────────────────────────────────────────
    static int classicBinarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {                            // ← <= because both ends are inclusive
            int mid = left + (right - left) / 2;          // ← avoids integer overflow vs (l+r)/2

            if (nums[mid] == target) {
                return mid;                                // found!
            } else if (nums[mid] < target) {
                left = mid + 1;                           // target is in right half
            } else {
                right = mid - 1;                          // target is in left half
            }
        }

        return -1; // not found
    }

    // ─────────────────────────────────────────────
    // TEMPLATE B — Lower Bound (first index >= target)
    // ─────────────────────────────────────────────
    static int lowerBound(int[] nums, int target) {
        int left = 0, right = nums.length; // right = length (one past last valid index)

        while (left < right) {             // ← strict < because right is exclusive
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;            // mid is too small, search right
            } else {
                right = mid;              // mid could be the answer, but check left too
            }
        }

        return left; // first index where nums[left] >= target
    }

    // ─────────────────────────────────────────────
    // TEMPLATE C — Upper Bound (first index > target)
    // ─────────────────────────────────────────────
    static int upperBound(int[] nums, int target) {
        int left = 0, right = nums.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] <= target) {
                left = mid + 1;           // mid is target or smaller, search right
            } else {
                right = mid;              // mid is too big, keep it as candidate
            }
        }

        return left; // first index where nums[left] > target
    }

    // ─────────────────────────────────────────────
    // TEMPLATE D — Search on Answer
    // ─────────────────────────────────────────────
    // Binary search on the answer space [lo, hi]
    // canAchieve(mid) is a monotonic boolean function
    static int searchOnAnswer(int[] nums, int h) {
        int lo = 1, hi = Arrays.stream(nums).max().getAsInt(); // answer range

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (canAchieve(nums, mid, h)) {  // mid works → try smaller
                hi = mid;
            } else {
                lo = mid + 1;               // mid doesn't work → need larger
            }
        }

        return lo; // smallest valid answer
    }

    // Helper: can we eat all bananas at speed 'k' within 'h' hours?
    private static boolean canAchieve(int[] piles, int k, int h) {
        int hours = 0;
        for (int pile : piles) {
            hours += (pile + k - 1) / k; // ceiling division: how many hours for this pile
        }
        return hours <= h;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: BINARY SEARCH
    // ─────────────────────────────────────────────
    // LeetCode 704 — Easy
    // Given sorted int[] nums and target, return index or -1.
    //
    // Example: nums=[-1,0,3,5,9,12], target=9 → 4
    //          nums=[-1,0,3,5,9,12], target=2 → -1

    // TODO VERSION
    static int binarySearchTODO(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            // TODO: compute mid without overflow
            // int mid = ???;

            // TODO: if nums[mid] == target → return mid
            // TODO: if nums[mid] < target  → search right half
            // TODO: else                   → search left half
        }

        return -1;
    }

    // SOLVED VERSION
    static int binarySearchSolved(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // safe mid (no overflow)

            if (nums[mid] == target) {
                return mid;            // exact match
            } else if (nums[mid] < target) {
                left = mid + 1;        // target is to the right
            } else {
                right = mid - 1;       // target is to the left
            }
        }

        return -1; // not found
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: SEARCH INSERT POSITION
    // ─────────────────────────────────────────────
    // LeetCode 35 — Easy
    // Given sorted array and target, return index where target is or would be inserted.
    //
    // Example: [1,3,5,6], target=5 → 2
    //          [1,3,5,6], target=2 → 1
    //          [1,3,5,6], target=7 → 4
    //
    // This is exactly the lower bound pattern.

    // TODO VERSION
    static int searchInsertTODO(int[] nums, int target) {
        int left = 0, right = nums.length; // right = length (could insert at end)

        while (left < right) {
            // TODO: compute mid
            // TODO: if nums[mid] < target → search right
            // TODO: else → narrow right bound
        }

        return left; // insertion position
    }

    // SOLVED VERSION
    static int searchInsertSolved(int[] nums, int target) {
        int left = 0, right = nums.length; // inclusive: target could insert at index n

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;  // target is larger → look right
            } else {
                right = mid;     // nums[mid] >= target → mid could be the insert point
            }
        }

        return left; // first position where nums[left] >= target (insert here)
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: SEARCH IN ROTATED SORTED ARRAY
    // ─────────────────────────────────────────────
    // LeetCode 33 — Medium
    // Array was sorted then rotated. Find target, return index or -1.
    // No duplicates.
    //
    // Example: [4,5,6,7,0,1,2], target=0 → 4
    //          [4,5,6,7,0,1,2], target=3 → -1
    //
    // Approach:
    //  - One half of the array is ALWAYS sorted after any rotation
    //  - Determine which half is sorted, check if target is in it
    //  - Binary search accordingly
    //
    // Key insight: if nums[left] <= nums[mid], the LEFT half is sorted

    // TODO VERSION
    static int searchRotatedTODO(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;

            // TODO: determine which half is sorted:
            //   if nums[left] <= nums[mid] → left half is sorted
            //     check if target is in [left, mid] range
            //     if yes → right = mid - 1
            //     else   → left = mid + 1
            //   else → right half is sorted
            //     check if target is in [mid, right] range
            //     if yes → left = mid + 1
            //     else   → right = mid - 1
        }

        return -1;
    }

    // SOLVED VERSION
    static int searchRotatedSolved(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) return mid; // found it

            if (nums[left] <= nums[mid]) {
                // LEFT half [left..mid] is sorted
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1; // target is in the sorted left half
                } else {
                    left = mid + 1;  // target must be in the right half
                }
            } else {
                // RIGHT half [mid..right] is sorted
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;  // target is in the sorted right half
                } else {
                    right = mid - 1; // target must be in the left half
                }
            }
        }

        return -1;
    }

    // ─────────────────────────────────────────────
    // PROBLEM 4: KOKO EATING BANANAS (Search on Answer)
    // ─────────────────────────────────────────────
    // LeetCode 875 — Medium
    // Koko can eat k bananas/hour. With h hours, find minimum k to eat all piles.
    //
    // Example: piles=[3,6,7,11], h=8 → 4
    //          piles=[30,11,23,4,20], h=5 → 30
    //
    // Approach:
    //  - Binary search on k (eating speed), range [1, max(piles)]
    //  - For each candidate k, compute hours needed
    //  - Find smallest k where hours <= h
    //
    // Key: ceiling division for partial pile: (pile + k - 1) / k

    // TODO VERSION
    static int kokoTODO(int[] piles, int h) {
        int lo = 1;
        int hi = 0;
        for (int p : piles) hi = Math.max(hi, p); // max pile = max possible speed needed

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            // TODO: compute total hours at speed mid (ceiling division per pile)
            // TODO: if hours <= h → mid might work, try smaller: hi = mid
            // TODO: else → mid too slow, need faster: lo = mid + 1
        }

        return lo;
    }

    // SOLVED VERSION
    static int kokoSolved(int[] piles, int h) {
        int lo = 1;
        int hi = 0;
        for (int p : piles) hi = Math.max(hi, p); // max speed = largest pile (eat in 1 hour)

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            // Count hours needed at speed 'mid'
            int hours = 0;
            for (int pile : piles) {
                hours += (pile + mid - 1) / mid; // ceiling: Math.ceil(pile/mid)
            }

            if (hours <= h) {
                hi = mid;       // speed 'mid' is sufficient → try slower (smaller k)
            } else {
                lo = mid + 1;   // speed 'mid' not enough → need faster
            }
        }

        return lo; // minimum speed that works
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH BINARY SEARCH
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  int mid = (left + right) / 2;   → overflow if left+right > Integer.MAX_VALUE
     * ✅ RIGHT:  int mid = left + (right - left) / 2;
     *
     * ❌ WRONG:  while (left < right)  for classic search (misses single-element arrays)
     * ✅ RIGHT:  while (left <= right)  for classic; while (left < right)  for bounds
     *
     * ❌ WRONG:  right = mid (in classic search) → infinite loop if target not found
     * ✅ RIGHT:  right = mid - 1 in classic; right = mid in lower/upper bound
     *
     * ❌ WRONG:  Forgetting ceiling division in "Search on Answer" problems
     * ✅ RIGHT:  (pile + k - 1) / k  or  (int) Math.ceil((double) pile / k)
     *
     * ❌ WRONG:  int hi = max(piles) needing streams when simple loop is cleaner
     * ✅ RIGHT:  for (int p : piles) hi = Math.max(hi, p);
     */
}
