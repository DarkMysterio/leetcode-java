package patterns;

import java.util.*;

/**
 * ============================================================
 * INTERVALS PATTERNS — Interview Reference
 * ============================================================
 * When to use:
 *  - Problem involves ranges [start, end] that may overlap
 *  - "Merge overlapping ranges", "Insert new range", "Count meetings"
 *
 * Core operations:
 *  1. Sort by start time
 *  2. Overlap check:  a.end >= b.start  (when sorted, a.start <= b.start)
 *  3. Merge:  new end = max(a.end, b.end)
 *
 * Overlap condition (after sorting):
 *   intervals[i][0] <= intervals[i-1][1]  → they overlap
 *   i.e. current start <= previous end
 *
 * Time: O(n log n) for sort + O(n) to process = O(n log n)
 * Space: O(n) for result list
 */
public class IntervalsPatterns {

    public static void main(String[] args) {
        System.out.println("=== Merge Intervals ===");
        int[][] merged = mergeIntervalsSolved(new int[][]{{1,3},{2,6},{8,10},{15,18}});
        System.out.println(Arrays.deepToString(merged)); // [[1,6],[8,10],[15,18]]

        System.out.println("\n=== Insert Interval ===");
        int[][] after = insertIntervalSolved(new int[][]{{1,3},{6,9}}, new int[]{2,5});
        System.out.println(Arrays.deepToString(after)); // [[1,5],[6,9]]

        int[][] after2 = insertIntervalSolved(
            new int[][]{{1,2},{3,5},{6,7},{8,10},{12,16}}, new int[]{4,8});
        System.out.println(Arrays.deepToString(after2)); // [[1,2],[3,10],[12,16]]

        System.out.println("\n=== Meeting Rooms (Can Attend All?) ===");
        System.out.println(canAttendMeetingsSolved(new int[][]{{0,30},{5,10},{15,20}})); // false
        System.out.println(canAttendMeetingsSolved(new int[][]{{7,10},{2,4}}));          // true
    }

    // ─────────────────────────────────────────────
    // OVERLAP CHECK HELPER
    // ─────────────────────────────────────────────
    // Two intervals [a, b] and [c, d] overlap iff: a <= d AND c <= b
    // After sorting by start: [a, b] and [c, d] where a <= c
    //   → they overlap iff c <= b  (current start <= prev end)
    static boolean overlaps(int[] a, int[] b) {
        // Assumes a.start <= b.start (sorted order)
        return b[0] <= a[1]; // b starts before a ends
    }

    // ─────────────────────────────────────────────
    // PROBLEM 1: MERGE INTERVALS
    // ─────────────────────────────────────────────
    // LeetCode 56 — Medium
    // Merge all overlapping intervals.
    //
    // Example: [[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
    //
    // Approach:
    //  1. Sort by start time
    //  2. Initialize result with first interval
    //  3. For each interval:
    //     - If it overlaps with last result interval → extend the end
    //     - Else → add as a new interval

    // TODO VERSION
    static int[][] mergeIntervalsTODO(int[][] intervals) {
        // TODO: sort by start time
        // Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> result = new ArrayList<>();
        // TODO: add first interval to result

        for (int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
            int[] last = result.get(result.size() - 1); // last merged interval

            // TODO: if curr.start <= last.end → overlap → extend last.end
            // TODO: else → no overlap → add curr to result
        }

        return result.toArray(new int[0][]);
    }

    // SOLVED VERSION
    static int[][] mergeIntervalsSolved(int[][] intervals) {
        // Step 1: sort by start time so overlapping intervals are adjacent
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> result = new ArrayList<>();
        result.add(intervals[0]); // start with first interval

        for (int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
            int[] last = result.get(result.size() - 1); // last interval in result

            if (curr[0] <= last[1]) {
                // Overlap: current start is within (or touching) last interval
                // Extend the end of the last interval if needed
                last[1] = Math.max(last[1], curr[1]);
            } else {
                // No overlap: start a fresh interval
                result.add(curr);
            }
        }

        return result.toArray(new int[0][]);
    }

    // ─────────────────────────────────────────────
    // PROBLEM 2: INSERT INTERVAL
    // ─────────────────────────────────────────────
    // LeetCode 57 — Medium
    // Given non-overlapping sorted intervals, insert a new interval and merge.
    //
    // Example: intervals=[[1,3],[6,9]], newInterval=[2,5] → [[1,5],[6,9]]
    //          intervals=[[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval=[4,8]
    //                  → [[1,2],[3,10],[12,16]]
    //
    // Approach (3-phase):
    //  Phase 1: Add all intervals that END before newInterval starts (no overlap)
    //  Phase 2: Merge all intervals that OVERLAP with newInterval
    //  Phase 3: Add all remaining intervals (start after newInterval ends)

    // TODO VERSION
    static int[][] insertIntervalTODO(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        // Phase 1: TODO — add intervals that end before newInterval starts
        // while (i < n && intervals[i][1] < newInterval[0]) { result.add(intervals[i]); i++; }

        // Phase 2: TODO — merge overlapping intervals into newInterval
        // while (i < n && intervals[i][0] <= newInterval[1]) {
        //   newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        //   newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
        //   i++;
        // }
        // result.add(newInterval);

        // Phase 3: TODO — add remaining intervals
        return result.toArray(new int[0][]);
    }

    // SOLVED VERSION
    static int[][] insertIntervalSolved(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        // Phase 1: intervals that are completely BEFORE newInterval (no overlap)
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]); // ends before newInterval starts → no merge needed
            i++;
        }

        // Phase 2: merge all overlapping intervals with newInterval
        while (i < n && intervals[i][0] <= newInterval[1]) {
            // current interval overlaps → expand newInterval to cover both
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval); // add the merged interval

        // Phase 3: intervals that are completely AFTER newInterval (no overlap)
        while (i < n) {
            result.add(intervals[i]); // starts after newInterval ends → add as-is
            i++;
        }

        return result.toArray(new int[0][]);
    }

    // ─────────────────────────────────────────────
    // PROBLEM 3: MEETING ROOMS (Can Attend All?)
    // ─────────────────────────────────────────────
    // LeetCode 252 — Easy (Premium, but common interview question)
    // Given meeting intervals, determine if a person can attend ALL meetings.
    // → Impossible if any two meetings overlap.
    //
    // Example: [[0,30],[5,10],[15,20]] → false (0-30 overlaps with 5-10)
    //          [[7,10],[2,4]]          → true  (no overlaps)
    //
    // Approach:
    //  1. Sort by start time
    //  2. Check consecutive pairs: if prev.end > curr.start → conflict

    // TODO VERSION
    static boolean canAttendMeetingsTODO(int[][] intervals) {
        // TODO: sort by start time
        // TODO: check each consecutive pair for overlap
        //   if intervals[i-1][1] > intervals[i][0] → return false
        return true;
    }

    // SOLVED VERSION
    static boolean canAttendMeetingsSolved(int[][] intervals) {
        // Sort by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        for (int i = 1; i < intervals.length; i++) {
            // If previous meeting ends AFTER current one starts → conflict
            if (intervals[i - 1][1] > intervals[i][0]) {
                return false; // overlap detected
            }
        }

        return true; // no overlaps → can attend all
    }

    // ─────────────────────────────────────────────
    // COMMON JAVA MISTAKES WITH INTERVALS
    // ─────────────────────────────────────────────
    /*
     * ❌ WRONG:  Forgetting to sort first → incorrect overlap detection
     * ✅ RIGHT:  Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
     *
     * ❌ WRONG:  Overlap check: curr[0] < last[1]  (misses touching intervals like [1,3],[3,5])
     * ✅ RIGHT:  curr[0] <= last[1]  (use <=, touching intervals usually should merge)
     *            (depends on problem — check if [1,3] and [3,5] should merge)
     *
     * ❌ WRONG:  last[1] = curr[1]  (always overwrite end)
     * ✅ RIGHT:  last[1] = Math.max(last[1], curr[1])  (keep the larger end)
     *            Example: [[1,10],[2,3]] — wrong would give end=3, right gives end=10
     *
     * ❌ WRONG:  result.toArray() without type → returns Object[]
     * ✅ RIGHT:  result.toArray(new int[0][])  ← correct return for int[][]
     *
     * ❌ WRONG:  In Insert Interval, not expanding newInterval[0] in phase 2
     * ✅ RIGHT:  Both start and end must be updated:
     *            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
     *            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
     */
}
