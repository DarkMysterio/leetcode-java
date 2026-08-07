package exercises.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * ============================================================================
 * PROBLEM: Course Schedule (LeetCode 207 / NeetCode)
 * PATTERN: Topological Sort / Cycle Detection in Directed Graphs (DFS)
 * ============================================================================
 *
 * ----------------------------------------------------------------------------
 * 1. HIGH-LEVEL INTUITION & DESIGN
 * ----------------------------------------------------------------------------
 * This problem asks if it is possible to take all courses given dependency pairs.
 * We can model this as a DIRECTED GRAPH where:
 *   - Vertices (Nodes) = Courses (0 to numCourses - 1)
 *   - Directed Edges   = Prerequisites (e.g., Course A -> Prerequisites)
 *
 * A set of courses CANNOT be finished IF AND ONLY IF there is a CYCLE in the graph!
 * For example:
 *   Course 0 requires Course 1
 *   Course 1 requires Course 0
 *   -> This forms a circular dependency (0 -> 1 -> 0), making it impossible to start.
 *
 * To detect cycles using Depth-First Search (DFS), we track three states for nodes:
 *   1. UNVISITED: Node has not been processed yet.
 *   2. VISITING (Current DFS Path): Node is currently in the active recursion call stack.
 *      If DFS encounters a node that is already in "VISITING" state, A CYCLE IS DETECTED!
 *   3. VISITED & SAFE (Fully Processed): Node and all its sub-dependencies have been
 *      checked and confirmed to have NO cycles. We memoize this by clearing its
 *      prerequisite list so future DFS traversals skip it instantly in O(1) time.
 *
 * ----------------------------------------------------------------------------
 * 2. RECAP OF PAST PITFALLS & BUGS SOLVED
 * ----------------------------------------------------------------------------
 *  - PITFALL 1: NullPointerException on `courses.get(currentCourse)`.
 *    -> REASON: If Course B has no prerequisites, it was never added as a KEY in
 *       the HashMap. Calling `.get(B)` returned `null`, crashing `.isEmpty()`.
 *    -> FIX: Pre-populate the HashMap with empty lists for ALL courses `0` to `numCourses - 1`
 *       before building edges.
 *
 *  - PITFALL 2: Skipping Isolated / Independent Nodes.
 *    -> REASON: Iterating only over `courses.keySet()` missed courses that had no
 *       outgoing/incoming dependencies if they weren't in the initial map keys.
 *    -> FIX: Iterate through a standard integer loop `for (int c = 0; c < numCourses; c++)`.
 *
 *  - PITFALL 3: Time Limit Exceeded (TLE) without Memoization.
 *    -> REASON: Re-traversing subgraphs that were already proven safe in previous DFS calls.
 *    -> FIX: Clear `preMap` (`courses.put(currentCourse, new ArrayList<>())`) after
 *       a course is verified cycle-free.
 *
 * ----------------------------------------------------------------------------
 * 3. COMPLEXITY ANALYSIS
 * ----------------------------------------------------------------------------
 * Let $V$ be the number of courses (`numCourses`) and $E$ be the number of prerequisite
 * pairs (`prerequisites.length`).
 *
 * TIME COMPLEXITY: $\mathcal{O}(V + E)$
 *   - Building the graph takes $\mathcal{O}(V + E)$ time.
 *   - Each vertex/course is entered into `dfs()` at most once for full traversal.
 *   - Because of memoization (clearing `preMap`), each directed edge is traversed
 *     at most ONCE across the entire algorithm execution.
 *
 * SPACE COMPLEXITY: $\mathcal{O}(V + E)$
 *   - Adjacency List Map: Stores $V$ keys and a total of $E$ directed edge entries.
 *   - Recursion Stack & `visited` Set: Holds up to $\mathcal{O}(V)$ nodes in the worst-case
 *     linear path graph (e.g., $0 \rightarrow 1 \rightarrow 2 \rightarrow \dots \rightarrow V-1$).
 * ============================================================================
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // ---------------------------------------------------------------------
        // STEP 1: PRE-POPULATE ADJACENCY LIST FOR ALL COURSES
        // INTUITION: Pre-initializing keys 0 through (numCourses - 1) ensures
        // every single course exists in the map. This prevents NullPointerException
        // when querying courses that have zero prerequisites.
        // ---------------------------------------------------------------------
        HashMap<Integer, List<Integer>> courses = new HashMap<>();
        for (int i = 0; i < numCourses; i++) {
            courses.put(i, new ArrayList<>());
        }

        // ---------------------------------------------------------------------
        // STEP 2: BUILD DIRECTED GRAPH EDGES
        // Mapping: course -> list of prerequisites required to take it
        // ---------------------------------------------------------------------
        for (int[] pre : prerequisites) {
            int course = pre[0];
            int mustTake = pre[1];
            courses.get(course).add(mustTake);
        }

        // ---------------------------------------------------------------------
        // STEP 3: RECURSION STACK TRACKER (Cycle Detector)
        // Tracks nodes currently active in the execution stack of the CURRENT DFS path.
        // ---------------------------------------------------------------------
        HashSet<Integer> visited = new HashSet<>();

        // ---------------------------------------------------------------------
        // STEP 4: RUN DFS FROM EVERY COURSE
        // INTUITION: The graph may be disconnected (consisting of multiple separate
        // component trees/graphs). We must launch DFS from every course 0 to numCourses - 1
        // to guarantee no isolated cycles exist anywhere in the forest.
        // ---------------------------------------------------------------------
        for (int c = 0; c < numCourses; c++) {
            if (!dfs(c, visited, courses)) {
                return false; // Cycle detected somewhere -> impossible to finish all courses!
            }
        }

        return true; // All courses scanned with zero cycles detected!
    }

    /**
     * Depth-First Search helper method to detect cycles and validate dependencies.
     *
     * @param currentCourse The node currently being evaluated.
     * @param visited       Set of nodes in the CURRENT active DFS recursion stack.
     * @param courses       Adjacency list mapping courses to prerequisite lists.
     * @return `true` if currentCourse can be finished safely; `false` if a cycle is found.
     */
    public boolean dfs(int currentCourse, HashSet<Integer> visited,
                       HashMap<Integer, List<Integer>> courses) {
        // ---------------------------------------------------------------------
        // BASE CASE 1: CYCLE DETECTED!
        // INTUITION: If `visited` already contains `currentCourse`, it means we
        // looped back onto a node that is currently being evaluated higher up
        // in the active call stack (a back-edge in graph theory).
        // ---------------------------------------------------------------------
        if (visited.contains(currentCourse)) {
            return false;
        }

        List<Integer> preMap = courses.get(currentCourse);

        // ---------------------------------------------------------------------
        // BASE CASE 2: NO PREREQUISITES / ALREADY VERIFIED SAFE (Memoized)
        // INTUITION: If `preMap` is empty, this course has no requirements
        // (or was previously cleared after being proven safe). It can be taken immediately.
        // ---------------------------------------------------------------------
        if (preMap.isEmpty()) {
            return true;
        }

        // ---------------------------------------------------------------------
        // BACKTRACKING - STEP 1: MARK AS CURRENTLY VISITING
        // Add currentCourse to active recursion stack before exploring children.
        // ---------------------------------------------------------------------
        visited.add(currentCourse);

        // Recursively check all prerequisites for currentCourse
        for (Integer pre : preMap) {
            if (!dfs(pre, visited, courses)) {
                return false; // Cycle detected down this dependency branch
            }
        }

        // ---------------------------------------------------------------------
        // BACKTRACKING - STEP 2: REMOVE FROM CURRENT PATH
        // INTUITION: We finished exploring all paths emanating from `currentCourse`.
        // Remove it from `visited` so sibling branches in other DFS traversals
        // do not incorrectly trigger a cycle error.
        // ---------------------------------------------------------------------
        visited.remove(currentCourse);

        // ---------------------------------------------------------------------
        // MEMOIZATION / PRUNING:
        // INTUITION: Since all child prerequisite branches returned `true` without
        // finding a cycle, `currentCourse` is completely safe! By setting its
        // list to empty, any FUTURE DFS traversal encountering `currentCourse`
        // will instantly hit BASE CASE 2 and return `true` in O(1) time.
        // ---------------------------------------------------------------------
        courses.put(currentCourse, new ArrayList<>());

        // Return true: All dependencies verified; course can be finished safely!
        return true;
    }
}