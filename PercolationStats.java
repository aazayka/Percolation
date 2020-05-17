import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] experiments;
    private final int N;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Incorrect n = " + n + " or trials = " + trials);
        experiments = new double[trials];
        N = n;
        Percolation p;
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            runTests(p);
            experiments[i] = (double) p.numberOfOpenSites() / (n * n);
            p = null;
        }
        mean = StdStats.mean(experiments);
        stddev = StdStats.stddev(experiments);
    }

    private void runTests(Percolation p) {
        while(!p.percolates()){
            p.open(StdRandom.uniform(N) + 1, StdRandom.uniform(N) + 1);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean - 1.96 * stddev / Math.sqrt(experiments.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean + 1.96 * stddev / Math.sqrt(experiments.length);
    }

    // test client (see below)
    public static void main(String[] args){
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}