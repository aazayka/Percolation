public class Percolation {
    private Node[] tree;
    private int numOpen = 0;
    private int N;
    private boolean percolates;

    private class Node {
        int parent;
        int weight;

        boolean isOpen;
        boolean connectTop;
        boolean connectBottom;

        public Node(int i, int j) {
            this.parent = conrvertToScalar(i, j);
            this.weight = 1;
            connectTop = (i == 1);
            connectBottom = (i == N);
        }

        public void open() {
            isOpen = true;
        }

        public void setConnectors(Node otherNode) {
            if (this.connectBottom || otherNode.connectBottom) {
                this.connectBottom = true;
                otherNode.connectBottom = true;
            }
            if (this.connectTop || otherNode.connectTop) {
                this.connectTop = true;
                otherNode.connectTop = true;
            }
            percolates = connectTop & connectBottom;
        }
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be positive, but got " + n);
        tree = new Node[n * n];
        this.N = n;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                tree[conrvertToScalar(i, j)] = new Node(i, j);
            }
        }
    }

    private int conrvertToScalar(int row, int col) {
        return (row - 1) * N + col - 1;
    }

    //rows and cols are regular 1..N
    private void union(int row1, int col1, int row2, int col2) {
        if (col1 > N || col1 < 1) return;
        if (col2 > N || col2 < 1) return;
        if (row1 > N || row1 < 1) return;
        if (row2 > N || row2 < 1) return;

        final Node node1 = tree[conrvertToScalar(row1, col1)];
        final Node node2 = tree[conrvertToScalar(row2, col2)];

        if (!node1.isOpen || !node2.isOpen)
            return;
        int root1 = root(node1);
        int root2 = root(node2);

        Node rootNode1 = tree[root1];
        Node rootNode2 = tree[root2];

        if (rootNode1.weight > rootNode2.weight) {
            rootNode2.parent = root1;
            rootNode1.weight += rootNode2.weight;
        }
        else {
            rootNode1.parent = root2;
            rootNode2.weight += rootNode1.weight;
        }
        rootNode1.setConnectors(rootNode2);
        node1.setConnectors(rootNode1);
        node2.setConnectors(rootNode2);
    }


    private int root(Node node) {
        int i = node.parent;
        while (i != tree[i].parent) {
            i = tree[i].parent;
            tree[i].parent = tree[tree[i].parent].parent;
        }
        node.parent = i;
        node.setConnectors(tree[i]);
        return i;
    }

    private void checkArg(String procName, String fieldName, int val) {
        if (val < 1 || val > N) throw new IllegalArgumentException(
                "Invalid " + fieldName + " in " + procName + ". Expected 1.." + N
                        + " got " + val);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkArg("open", "row", row);
        checkArg("open", "col", col);
        int k = conrvertToScalar(row, col);
        if (!isOpen(k)) {
            tree[k].open();
            numOpen++;
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
        }
    }

    private boolean isOpen(int n2) {
        return tree[n2].isOpen;
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkArg("isOpen", "row", row);
        checkArg("isOpen", "col", col);
        return isOpen(conrvertToScalar(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkArg("isFull", "row", row);
        checkArg("isFull", "col", col);

        final Node node = tree[conrvertToScalar(row, col)];
        final boolean result = node.isOpen && (node.connectTop || tree[root(node)].connectTop);
        node.connectTop |= result;
        return result;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    // // test client (optional)
    // public static void main(String[] args)


}