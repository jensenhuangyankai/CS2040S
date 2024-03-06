/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        public int weight = 1;
        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        // TODO: Implement this
        int count = 1;
        if (node.left == null && node.right == null) return 0;
        if (child == Child.LEFT) {
            if (node.left != null) node = node.left;
            else return 0;
        }
        if (child == Child.RIGHT) {
            if (node.right != null) node = node.right;
            else return 0;
        }

        count += countNodes(node, Child.LEFT);
        count += countNodes(node, Child.RIGHT);

        return count;

    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        //return new TreeNode[0];
        int arrLength = countNodes(node, child);
        TreeNode[] array = new TreeNode[arrLength];
        if (node.left == null && node.right == null) return new TreeNode[0];
        if (child == Child.LEFT) {
            if (node.left != null) node = node.left;
            else return new TreeNode[0];
        }
        if (child == Child.RIGHT) {
            if (node.right != null) node = node.right;
            else return new TreeNode[0];
        }

        TreeNode[] left = enumerateNodes(node, Child.LEFT);
        TreeNode[] right = enumerateNodes(node, Child.RIGHT);

        if (left.length > 0) System.arraycopy(left, 0, array, 0, left.length);
        array[left.length] = node;
        if (right.length > 0) System.arraycopy(right, 0, array, left.length+1, right.length);
        return array;
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        //System.out.println("-!-");
        //for (TreeNode t : nodeList) System.out.println(t.key);

        //if (Arrays.equals(nodeList, new TreeNode[0])) return null;
        int len = nodeList.length;
        int mid = len / 2;

        if (len == 1){
            TreeNode last = nodeList[0];
            last.left = null;
            last.right = null;
            return last;
        }
        //System.out.println("mid");
        //System.out.println(nodeList[mid].key);
        TreeNode root = nodeList[mid];
        TreeNode left;
        if (mid > 0){
            TreeNode[] tempLeft = new TreeNode[mid];
            System.arraycopy(nodeList, 0, tempLeft, 0, mid);
            left = buildTree(tempLeft);
        }
        else{
            left = null;
        }

        TreeNode right;
        if (len - mid - 1 > 0){
            TreeNode[] tempRight = new TreeNode[len - mid - 1];
            System.arraycopy(nodeList, mid+1, tempRight, 0, len - mid - 1);
            right = buildTree(tempRight);
        }
        else {
            right = null;
            //System.out.println("null");
        }

        root.left = left;
        root.right = right;
        return root;

    }
    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this
        if (node == null) return true;
        double leftWeight = 0;
        double rightWeight = 0;
        if (node.left != null) leftWeight = node.left.weight;
        if (node.right != null) rightWeight = node.right.weight;
        double nodeWeight = node.weight;


        if (leftWeight > ((double) 2 /3)*(nodeWeight) || rightWeight > ((double) 2/3)*(nodeWeight)) return false;
        else return true;
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        fixWeights(newChild, Child.LEFT);
        fixWeights(newChild, Child.RIGHT);

        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    public void fixWeights(TreeNode node, Child child) {
        if (child == Child.LEFT) {
            node = node.left;
        } else if (child == Child.RIGHT) {
            node = node.right;
        }
        if (node == null) return;
        if (node.left == null && node.right == null) return;
        if (node.left != null){
            fixWeights(node, Child.LEFT);
            node.weight += node.left.weight;
        }
        if (node.right != null){
            fixWeights(node, Child.RIGHT);
            node.weight += node.right.weight;
        }
        return;

    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;
        String[] path = new String[root.weight+1];
        int pointer = 0;
        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node.weight++;
                node = node.left;
                path[pointer++] = "L";

            } else {
                if (node.right == null) break;
                node.weight++;
                node = node.right;
                path[pointer++] = "R";
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
        node.weight++;

        node = root;
        for (String s: path) {
            if (s == null) return;
            if (s == "L") {
                if (checkBalance(node.left) == false){
                    rebuild(node, Child.LEFT);
                    return;
                }
                if (node.left != null) node = node.left;
            }
            else if (s == "R"){
                if (checkBalance(node.right) == false) {
                    rebuild(node, Child.RIGHT);
                    return;
                }
                if (node.right != null) node = node.right;
            }
        }
        return;



    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
