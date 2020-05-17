import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF tree;
    private final boolean[] isOpen;
    private final boolean[] connectTop;
    private final boolean[] connectBottom;
    private final int N;
    private boolean percolates;
    private int numOpen;

    private void setConnectors(int thisNode, int otherNode) {
        if (connectBottom[thisNode] || connectBottom[otherNode]) {
            this.connectBottom[thisNode] = true;
            connectBottom[otherNode] = true;
        }
        if (connectTop[thisNode] || connectTop[otherNode]) {
            connectTop[thisNode] = true;
            connectTop[otherNode] = true;
        }
        percolates = percolates || (connectTop[thisNode] & connectBottom[otherNode]);
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be positive, but got " + n);
        N = n;
        final int arraySize = N * N;
        tree = new WeightedQuickUnionUF(arraySize);
        connectTop = new boolean[arraySize];
        connectBottom = new boolean[arraySize];
        isOpen = new boolean[arraySize];
        for (int i = 0; i < N; i++) {
            connectTop[i] = true;
            connectBottom[arraySize - i - 1] = true;
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

        final int node1 = conrvertToScalar(row1, col1);
        final int node2 = conrvertToScalar(row2, col2);
        if (!isOpen(node1) || !isOpen(node2))
            return;

        final int rootNode1 = tree.find(node1);
        final int rootNode2 = tree.find(node2);

        tree.union(node1, node2);

        setConnectors(rootNode1, rootNode2);
        setConnectors(node1, rootNode1);
        setConnectors(node2, rootNode2);
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

        if (!isOpen[k]) {
            isOpen[k] = true;
            numOpen++;
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
        }
        percolates = percolates || (connectTop[k] && connectBottom[k]);
    }

    private boolean isOpen(int n2) {
        return isOpen[n2];
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

        final int k = conrvertToScalar(row, col);
        final boolean result = isOpen(k) && (connectTop[k] || connectTop[tree.find(k)]);
        connectTop[k] |= result;
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