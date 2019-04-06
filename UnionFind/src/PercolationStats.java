import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int n;
    private final int trials;
    private final double[] x;
    private double mean = -1;
    private double stddev = -1;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.n = n;
        this.trials = trials;
        this.x = new double[trials];

        for (int i = 0; i < trials; i++) {
            x[i] = (double) experiments() / (n * n);
        }
    }

    public double mean() {
        if (mean < 0) {
            mean = StdStats.mean(x);
        }
        return mean;
    }

    public double stddev() {
        if (stddev < 0) {
            stddev = StdStats.stddev(x);
        }
        return stddev;
    }

    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    private int experiments() {
        Percolation percolation = new Percolation(n);

        do {
            int row;
            int col;
            do {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
            } while (percolation.isOpen(row, col));
            percolation.open(row, col);
        } while (!percolation.percolates());
        return percolation.numberOfOpenSites();
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats p = new PercolationStats(n, t);

        System.out.printf("mean                    = %f\n", p.mean());
        System.out.println(String.format("stddev                  = %f", p.stddev()));
        System.out.println(String.format("95%% confidence interval = [%f, %f]", p.confidenceLo(), p.confidenceHi()));
    }
}