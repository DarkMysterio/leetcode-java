package exercises.graphs;

import java.util.*;

/**
 * ============================================================================
 * PROBLEM: Course Schedule II (LeetCode 210)
 * PATTERN: Topological Sort via Post-Order DFS
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & STRATEGY
 * ----------------------------------------------------------------------------
 * To find a valid ordering of courses, we must ensure that for every course,
 * all of its prerequisite dependencies are processed BEFORE the course itself.
 *
 * This natural ordering is achieved using POST-ORDER DFS:
 *   1. We dive deep into a course's prerequisites first.
 *   2. Once a course has no remaining prerequisites (a leaf node), it is "safe"
 *      to complete. We add it to our `orderedCourses` list.
 *   3. As recursion unwinds back up the call stack, parent courses are added
 *      AFTER their prerequisites are already in the list.
 *
 * To handle graph edge cases, three helper tracking structures are used:
 *   - `activePath` (Cycle Detection): Tracks nodes currently in the recursion stack.
 *     Re-visiting a node here means a circular dependency exists (e.g., A -> B -> A).
 *   - `addedCourses` (Deduplication): Ensures a course is appended to the final
 *     result list exactly ONCE, even if multiple other courses depend on it.
 *   - `prereqMap` mutation (Memoization): Clearing a course's prerequisite list
 *     once verified converts it into a base-case leaf node, preventing redundant
 *     re-traversals during future DFS calls.
 *
 * ----------------------------------------------------------------------------
 * 2. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let V = numCourses (vertices) and E = prerequisites.length (directed edges).
 *
 * TIME COMPLEXITY: O(V + E)
 *   - Graph Construction: Iterates over all E prerequisites once: O(E).
 *   - DFS Traversal: Every course (V) is evaluated, and every prerequisite
 *     dependency (E) is traversed at most once due to memoization and the
 *     `addedCourses` set.
 *   - Array Conversion: Iterates through the V elements of the result list: O(V).
 *   - Total Time: O(V + E).
 *
 * SPACE COMPLEXITY: O(V + E)
 *   - `prereqMap` (Adjacency List): Stores V keys and E total directed edges: O(V + E).
 *   - Tracking Sets (`activePath`, `addedCourses`, `orderedCourses`): Hold at
 *     most V elements each: O(V).
 *   - Recursion Call Stack: Can go up to O(V) deep in a linear graph (0 -> 1 -> 2 -> ...).
 *   - Total Auxiliary Space: O(V + E).
 * ============================================================================
 */
public class CourseScheduleII {

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // ---------------------------------------------------------------------
        // STEP 1: INITIALIZE ADJACENCY LIST
        // Pre-populate keys for ALL courses (0 to numCourses - 1) so that courses
        // with zero prerequisites still exist in the map to prevent NullPointerExceptions.
        // ---------------------------------------------------------------------
        HashMap<Integer, List<Integer>> prereqMap = new HashMap<>();
        for (int course = 0; course < numCourses; course++) {
            prereqMap.put(course, new ArrayList<>());
        }

        // Populate map: Course -> List of direct prerequisites required to take it
        for (int[] pair : prerequisites) {
            prereqMap.get(pair[0]).add(pair[1]);
        }

        // ---------------------------------------------------------------------
        // STEP 2: INITIALIZE DATA STRUCTURES FOR TRAVERSAL
        // ---------------------------------------------------------------------
        // Stores the final valid course sequence in post-order execution
        List<Integer> orderedCourses = new ArrayList<>();

        // Prevents duplicate additions of the same course to `orderedCourses`
        Set<Integer> addedCourses = new HashSet<>();

        // Tracks courses in the ACTIVE DFS stack to detect circular cycles
        HashSet<Integer> activePath = new HashSet<>();

        // ---------------------------------------------------------------------
        // STEP 3: RUN DFS FROM EVERY COURSE
        // The graph may be disconnected (forest of independent trees/components).
        // We must check every course to guarantee full coverage.
        // ---------------------------------------------------------------------
        for (int course = 0; course < numCourses; course++) {
            if (!dfs(course, prereqMap, activePath, addedCourses, orderedCourses)) {
                // If any DFS branch hits a cycle, a valid ordering is IMPOSSIBLE
                return new int[0];
            }
        }

        // ---------------------------------------------------------------------
        // STEP 4: CONVERT RESULT LIST TO PRIMITIVE ARRAY
        // Copy elements from `orderedCourses` into the required `int[]` return format.
        // ---------------------------------------------------------------------
        int[] finalOrder = new int[orderedCourses.size()];
        for (int i = 0; i < orderedCourses.size(); i++) {
            finalOrder[i] = orderedCourses.get(i);
        }
        return finalOrder;
    }

    /**
     * Depth-First Search helper method to validate dependency paths and record order.
     *
     * @param currentCourse  The course node currently under evaluation.
     * @param prereqMap      Adjacency list mapping courses to prerequisite lists.
     * @param activePath     Set of nodes in the CURRENT active call stack (cycle detection).
     * @param addedCourses   Set of nodes already added to the final result list (deduplication).
     * @param orderedCourses List building the topological order post-traversal.
     * @return `true` if currentCourse can be completed without cycles; `false` if a cycle is hit.
     */
    public boolean dfs(int currentCourse, HashMap<Integer, List<Integer>> prereqMap,
                       HashSet<Integer> activePath, Set<Integer> addedCourses, List<Integer> orderedCourses) {

        // ---------------------------------------------------------------------
        // BASE CASE 1: CYCLE DETECTED!
        // If `currentCourse` is already in `activePath`, we looped back onto a course
        // currently being evaluated higher up in our recursion stack (back-edge).
        // ---------------------------------------------------------------------
        if (activePath.contains(currentCourse)) {
            return false;
        }

        // ---------------------------------------------------------------------
        // BASE CASE 2: LEAF NODE / MEMOIZED SAFE NODE
        // If `currentCourse` has no prerequisites (or was previously cleared),
        // it can be completed immediately. Add it if not already in the result.
        // ---------------------------------------------------------------------
        if (prereqMap.get(currentCourse).isEmpty()) {
            if (!addedCourses.contains(currentCourse)) {
                orderedCourses.add(currentCourse);
                addedCourses.add(currentCourse);
            }
            return true;
        }

        // ---------------------------------------------------------------------
        // RECURSIVE STEP: EXPLORE PREREQUISITES FIRST (POST-ORDER)
        // 1. Mark current course as active on the current call stack path.
        // ---------------------------------------------------------------------
        activePath.add(currentCourse);

        List<Integer> prereqList = prereqMap.get(currentCourse);
        for (Integer prereq : prereqList) {
            // Traverse deeper into prerequisites before completing `currentCourse`
            if (!dfs(prereq, prereqMap, activePath, addedCourses, orderedCourses)) {
                return false; // Propagate cycle signal back up the call chain
            }
        }

        // ---------------------------------------------------------------------
        // BACKTRACKING & MEMOIZATION
        // 1. Remove node from `activePath` as we finish exploring all its branches.
        // 2. Clear its prerequisite list in `prereqMap` so future DFS runs treat
        //    it as a base-case leaf node instantly in O(1) time.
        // 3. Add node to `orderedCourses` now that ALL prerequisites are satisfied.
        // ---------------------------------------------------------------------
        activePath.remove(currentCourse);
        prereqMap.put(currentCourse, new ArrayList<>()); // Memoize as safe

        if (!addedCourses.contains(currentCourse)) {
            orderedCourses.add(currentCourse);
            addedCourses.add(currentCourse);
        }

        return true;
    }
}