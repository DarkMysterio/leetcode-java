package exercises.trees;

public class TreeTraversals {

    public static void inOrder(TreeNode root){
        if(root == null){
            return;
        }
        inOrder(root.left);
        System.out.println(root.val +  " ");
        inOrder(root.right);
    }

    public static void main(String[] args) {
        /*
         * Constructing the following sample binary search tree:
         *
         *           4
         *          / \
         *         2   6
         *        / \ / \
         *       1  3 5  7
         */

        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        System.out.println("In-Order Traversal (Left -> Root -> Right):");
        inOrder(root);
        System.out.println(); // Output: 1 2 3 4 5 6 7
    }
}
