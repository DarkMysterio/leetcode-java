package exercises.trees;



public class InvertBinaryTree {

    /**
     * COMPLEXITATE:
     *
     * Complexitate de Timp: O(N)
     * - Trecem prin fiecare nod din arbore o singură dată pentru a-i inversa copiii.
     * - unde N = numărul total de noduri din arbore.
     *
     * Complexitate de Spațiu: O(H)
     * - Spațiul este ocupat de stiva de apeluri recursive (call stack).
     * - În cel mai rău caz (arbore complet dezechilibrat / ca o listă), H = N -> O(N).
     * - În cel mai bun caz (arbore perfect echilibrat), H = log(N) -> O(log N).
     * - unde H = înălțimea arborelui.
     */
    public static TreeNode invertTree(TreeNode root) {
        // 1. CASUL DE BAZĂ (Guard Clause)
        // Oprirea recursivității: dacă am ajuns la un nod inexistente (null), returnăm null.
        // Această verificare ne scapă și de erori de tip NullPointerException
        // și elimină nevoia de a folosi blocuri 'else' în restul metodei.
        if (root == null) {
            return null;
        }

        // 2. SCHIMBUL (SWAP)
        // Inversăm legăturile stânga și dreapta ale nodului curent.
        // Folosim o variabilă auxiliară 'aux' ca să nu pierdem referința către root.right.
        // NOTĂ: Funcționează corect chiar dacă un copil este 'null' sau dacă ambii sunt 'null' (frunză)!
        TreeNode aux = root.right;
        root.right = root.left;
        root.left = aux;

        // 3. APELURILE RECURSIVE
        // Nu mai avem nevoie de 'if (root.left != null)' înainte de apel.
        // Dacă root.left este null, apelul de mai jos va fi interceptat
        // de primul 'if (root == null)' de la începutul funcției și va returna direct.
        invertTree(root.left);
        invertTree(root.right);

        // 4. RETURNAREA REZULTATULUI
        // Returnăm nodul curent modificat înapoi către apelant.
        // Deși este ultima instrucțiune, AICI RETURN-UL ESTE OBLIGATORIU
        // deoarece metoda are tipul de retur 'TreeNode' (nu mai este 'void').
        return root;
    }



}
