import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final boolean[] isOpen;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;
    private final int virtualStart;
    private final int virtualEnd;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.n = n;
        virtualStart = n * n;
        virtualEnd = virtualStart + 1;
        numberOfOpenSites = 0;
        isOpen = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                isOpen[rowColToN(i, j)] = false;
            }
        }
    }

    public boolean isOpen(int row, int col) {
        outOfRange(row, col);
        return isOpen[rowColToN(row, col)];
    }

    public void open(int row, int col) {
        outOfRange(row, col);
        int nOfOpen = rowColToN(row, col);
        if (!isOpen[nOfOpen]) {
            isOpen[nOfOpen] = true;
            numberOfOpenSites++;

            if (row == 1) {
                uf.union(nOfOpen, virtualStart);
                uf2.union(nOfOpen, virtualStart);
            }

            if (row == this.n) {
                uf.union(nOfOpen, virtualEnd);
            }

            union(row - 1, col, nOfOpen);
            union(row + 1, col, nOfOpen);
            union(row, col - 1, nOfOpen);
            union(row, col + 1, nOfOpen);
        }
    }

    private void union(int row, int col, int nOfUnion) {
        if (isLegalRowCol(row, col) && isOpen(row, col)) {
            uf.union(rowColToN(row, col), nOfUnion);
            uf2.union(rowColToN(row, col), nOfUnion);
        }
    }

    public boolean percolates() {
        return uf.connected(virtualStart, virtualEnd);
    }

    public boolean isFull(int row, int col) {
        outOfRange(row, col);
        return uf2.connected(rowColToN(row, col), virtualStart);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    private int rowColToN(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    private void outOfRange(int row, int col) {
        if (row > n || col > n || row < 1 || col < 1) throw new java.lang.IllegalArgumentException();
    }

    private boolean isLegalRowCol(int row, int col) {
        return !(row > n || col > n || row < 1 || col < 1);
    }
}