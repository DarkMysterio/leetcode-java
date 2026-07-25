package patterns;

import java.util.*;

/**
 * ============================================================
 * SORTING PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Problem asks to process elements in a specific order
 *  - Merging/comparing ranges, intervals
 *  - Finding nearest neighbors by distance
 *  - Greedy algorithms that require sorted input
 *
 * Key Rule: ALWAYS use Integer.compare(a, b) NOT (a - b)
 *           Subtraction can overflow for large negative values!
 *
 * Time:  O(n log n) for all sorts below
 * Space: O(log n) in-place, O(n) if creating new structure
 */
public class SortingPatterns {

    public static void main(String[] args) {
        System.out.println("=== Sorting int[] ===");
        int[] arr = {5, 2, 8, 1, 9, 3};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr)); // [1, 2, 3, 5, 8, 9]

        System.out.println("\n=== Merge Intervals TODO ===");
        int[][] intervals1 = {{1,3},{2,6},{8,10},{15,18}};
        // int[][] merged = mergeIntervalsTODO(intervals1); // uncomment when ready
        int[][] merged = mergeIntervalsSolved(intervals1);
        System.out.println(Arrays.deepToString(merged)); // [[1,6],[8,10],[15,18]]

        System.out.println("\n=== K Closest Points TODO ===");
        int[][] points = {{1,3},{-2,2},{5,8},{0,1}};
        // int[][] closest = kClosestTODO(points, 2); // uncomment when ready
        int[][] closest = kClosestSolved(points, 2);
        System.out.println(Arrays.deepToString(closest));
    }

    // ─────────────────────────────────────────────
    // REUSABLE TEMPLATES
    // ─────────────────────────────────────────────

    // Template 1 — Sort int[]
    static void sortIntArray(int[] nums) {
        Arrays.sort(nums); // ascending, in-place, O(n log n)
    }

    // Template 2 — Sort String[]
    static void sortStringArray(String[] words) {
        Arrays.sort(words);                          // lexicographic
        Arrays.sort(words, (a, b) -> Integer.compare(a.length(), b.length())); // by length
    }

    // Template 3 — Sort int[][] by first column (intervals)
    static void sortIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    }

    // Template 4 — Sort List<int[]> by distance from origin
    static void sortByDistance(List<int[]> points) {
        points.sort((a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1]; // squared distance
            int distB = b[0] * b[0] + b[1] * b[1];
            return Integer.compare(distA, distB);   // ascending distance
        });
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: MERGE INTERVALS
    // ─────────────────────────────────────────────
    // LeetCode 56 — Medium
    // Given an array of intervals [start, end], merge all overlapping ones.
    //
    // Example: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
    //
    // Approach:
    //  1. Sort by start time
    //  2. Iterate: if current start <= last merged end → extend the end
    //              else → add new interval to result
    //
    // Time: O(n log n)  Space: O(n)

    // TODO VERSION — fill in the blanks
    static int[][] mergeIntervalsTODO(int[][] intervals) {
        // TODO: Sort intervals by start time
        // Arrays.sort(intervals, ???);

        List<int[]> result = new ArrayList<>();

        for (int[] curr : intervals) {
            // TODO: if result is empty OR no overlap with last interval
            //   → add curr to result
            // else
            //   → merge: extend the end of the last interval

        }

        return result.toArray(new int[0][]);
    }

    // SOLVED VERSION
    static int[][] mergeIntervalsSolved(int[][] intervals) {
        // Step 1: Sort by start time so overlapping intervals are adjacent
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> result = new ArrayList<>();

        for (int[] curr : intervals) {
            // If result is empty, or current interval doesn't overlap with last
            if (result.isEmpty() || curr[0] > result.get(result.size() - 1)[1]) {
                result.add(curr); // add as-is
            } else {
                // Overlap: extend the end of the last interval if needed
                result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], curr[1]);
            }
        }

        // Convert List<int[]> back to int[][]
        return result.toArray(new int[0][]);
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: K CLOSEST POINTS TO ORIGIN (Sorting version)
    // ─────────────────────────────────────────────
    // LeetCode 973 — Medium
    // Given points on a 2D plane, return the k closest to origin (0,0).
    // Distance formula: sqrt(x²+y²) — but we skip sqrt since it doesn't affect order
    //
    // Example: points=[[1,3],[-2,2]], k=1 → [[-2,2]]
    //
    // Approach (sort version):
    //  1. Sort points by squared distance
    //  2. Return first k
    //
    // Time: O(n log n)  Space: O(k)

    // TODO VERSION
    static int[][] kClosestTODO(int[][] points, int k) {
        // TODO: Sort points by (x²+y²) ascending
        // Arrays.sort(points, ???);

        // TODO: Return first k points
        return null;
    }

    // SOLVED VERSION
    static int[][] kClosestSolved(int[][] points, int k) {
        // Sort by squared Euclidean distance (no need for actual sqrt)
        Arrays.sort(points, (a, b) -> {
            int distA = a[0] * a[0] + a[1] * a[1]; // distance² of point a
            int distB = b[0] * b[0] + b[1] * b[1]; // distance² of point b
            return Integer.compare(distA, distB);   // ascending: closest first
        });

        // Return the first k elements (slice the sorted array)
        return Arrays.copyOfRange(points, 0, k);
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: SORT CUSTOM OBJECTS
    // ─────────────────────────────────────────────
    // Sort a list of Person objects by age ascending, then name lexicographically

    static class Person {
        String name;
        int age;
        Person(String name, int age) { this.name = name; this.age = age; }
        public String toString() { return name + "(" + age + ")"; }
    }

    static void sortCustomObjectsDemo() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Charlie", 30));
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 25));

        // Sort by age ascending, then by name lexicographically
        people.sort((a, b) -> {
            if (a.age != b.age) return Integer.compare(a.age, b.age); // primary: age
            return a.name.compareTo(b.name);                          // secondary: name
        });

        System.out.println(people); // [Alice(25), Bob(25), Charlie(30)]
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES IN SORTING
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  (a, b) -> a - b          ← can OVERFLOW if a is MIN_VALUE
     * ✅ RIGHT:  (a, b) -> Integer.compare(a, b)
     *
     * ❌ WRONG:  Arrays.sort(intArray, comparator)  ← doesn't compile for int[]
     * ✅ RIGHT:  Arrays.sort(intArray)              ← no comparator for primitives
     *            or use Integer[] / List<Integer>
     *
     * ❌ WRONG:  Collections.sort(array)            ← Collections.sort needs List
     * ✅ RIGHT:  Arrays.sort(array)
     *
     * ❌ WRONG:  Forgetting to convert result back   ← result.toArray(new int[0][])
     * ✅ RIGHT:  return result.toArray(new int[result.size()][]);
     */
}
