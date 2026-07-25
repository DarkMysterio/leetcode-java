package exercises;

import java.util.*;

public class CourseSchedule1 {
    /*
     * Complexitate Timp : O(V + E)
     * - V = numCourses (noduri), E = prerequisites.length (muchii).
     * - Construirea listei de adiacență durează O(V + E).
     * - Parcurgerea DFS vizitează fiecare nod și fiecare muchie o singură dată
     *   datorită optimizării cu tabloul 'vis'.
     *
     * Complexitate Spațiu: O(V + E)
     * - Lista de adiacență ocupă O(V + E) memorie.
     * - Tablourile 'vis' și 'path' ocupă O(V) memorie.
     * - Stiva de apeluri recursive DFS poate ajunge la adâncimea O(V) în cel mai rău caz.
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // Inițializăm lista de adiacență pentru fiecare curs (graf orientat)
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        // Populăm graful: prereq (pre[1]) -> curs dependent (pre[0])
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
        }

        // vis: evidență globală a nodurilor verificate complet (previne re-procesarea)
        boolean[] vis = new boolean[numCourses];

        // path: stiva de recursivitate curentă (detectează bucle pe drumul activ)
        boolean[] path = new boolean[numCourses];

        // Iterăm prin toate cursurile pentru a acoperi și componentele deconectate din graf
        for (int i = 0; i < numCourses; i++) {
            // Dacă cursul nu a fost verificat încă și DFS-ul găsește un ciclu -> imposibil
            if (!vis[i] && dfs(i, adj, vis, path)) {
                return false;
            }
        }

        // Nu s-a găsit niciun ciclu în tot graful -> toate cursurile pot fi terminate
        return true;
    }

    private static boolean dfs(int node, List<List<Integer>> adj, boolean[] vis, boolean[] path) {
        // Marcăm nodul ca vizitat global ȘI adăugat pe calea curentă
        vis[node] = path[node] = true;

        // Explorăm toți vecinii (cursurile care depind de nodul curent)
        for (int next : adj.get(node)) {
            // Caz 1: Vecin nevizitat -> continuăm explorarea DFS
            if (!vis[next] && dfs(next, adj, vis, path)) {
                return true;
            }
            // Caz 2: Vecinul este deja pe calea curentă -> Am găsit un ciclu!
            else if (path[next]) {
                return true;
            }
        }

        // BACKTRACKING: Scoatem nodul de pe calea curentă înainte de a ne întoarce
        path[node] = false;

        // Niciun ciclu găsit pe această ramură
        return false;
    }

    public static void main(String[] args) {
        int[][] v = new int[][]{{1,0},{2,0},{3,1},{3,2}};
        canFinish(4,v);
    }

}
