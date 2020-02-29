public class Percolation {
    private boolean[][] fields;
    private int[] tree;
    private final int N;
    private int numOpen = 0;
    private int[] weights;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n<=0) throw new IllegalArgumentException("n should be positive, but got " + n);
        fields = new boolean[n][n];
        tree = new int[n*n+2];
        weights = new int[n*n+2];
        tree[0] = 0; tree[n*n+1] = n*n+1;
        weights[0] = 1; weights[n*n+1] = 1;
        this.N = n;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                fields[i-1][j-1] = false;
                int k = conrvertToScalar(i, j);
                tree[k] = k;
                weights[k] = 1;
            }
        }
    }

    private int conrvertToScalar(int row, int col){
        if(row == 0) return 0;
        if(row == N+1) return N*N + 1;
        return (row-1) * N + col;
    }

    private void union(int row1, int col1, int row2, int col2){
        if(col1 > N || col1 < 1) return;
        if(col2 > N || col2 < 1) return;
        if((row1 >= 1 && row1 <= N && !isOpen(row1, col1))
           ||(row2 >= 1 && row2 <= N && !isOpen(row2, col2))) return;
        int n1 = root(conrvertToScalar(row1, col1));
        int n2 = root(conrvertToScalar(row2, col2));
        if(weights[n1] > weights[n2]){
            tree[n2] = n1;
            weights[n1] += weights[n2];
        } else {
            tree[n1] = n2;
            weights[n2] += weights[n1];
        }
    }

    private int root(int i){
        while(i != tree[i]){
            i = tree[i];
            tree[i] = tree[tree[i]];
        }
        return i;
    }

    private void checkArg(String procName, String fieldName, int val){
        if(val < 1 || val > N) throw  new IllegalArgumentException("Invalid " + fieldName + " in " + procName + ". Expected 1.." + N
                                                                           + " got " + val);
    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        checkArg("open", "row", row);
        checkArg("open", "col", col);

        if(!fields[row-1][col-1]){
            fields[row-1][col-1] = true;
            numOpen++;
            union(row, col, row-1, col);
            union(row, col, row+1, col);
            union(row, col, row, col-1);
            union(row, col, row, col+1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        checkArg("isOpen", "row", row);
        checkArg("isOpen", "col", col);
        return fields[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        checkArg("isFull", "row", row);
        checkArg("isFull", "col", col);

        return root(0) == root(conrvertToScalar(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates(){
        return root(0) == root(N*N+1);
    }

    // // test client (optional)
    // public static void main(String[] args)


}