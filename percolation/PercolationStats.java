/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int gridSize;

    private final int trialsNumber;

    private final double[] openSitesToPercolate;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int gridSize, int trialsNumber) {
        if (gridSize <= 0 || trialsNumber <= 0) {
            throw new IllegalArgumentException();
        }
        this.gridSize = gridSize;
        this.trialsNumber = trialsNumber;
        this.openSitesToPercolate = new double[trialsNumber];

        compute();
    }

    private void compute() {
        for (int i = 0; i < trialsNumber; i++) {
            Percolation percolation = new Percolation(gridSize);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(gridSize) + 1;
                int col = StdRandom.uniform(gridSize) + 1;
                percolation.open(row, col);
            }
            double partOfOpenSites = (double) percolation.numberOfOpenSites()/(gridSize * gridSize);
            openSitesToPercolate[i] = partOfOpenSites;
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSitesToPercolate);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSitesToPercolate);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (stddev() * 1.96/Math.sqrt(trialsNumber));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (stddev() * 1.96/Math.sqrt(trialsNumber));
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        System.out.println("Mean = " + stats.mean());
        System.out.println("Standard deviation = " + stats.stddev());
        System.out.println("95% confidence interval = {" + stats.confidenceLo() + ", " + stats.confidenceHi() + "}");
    }
}
