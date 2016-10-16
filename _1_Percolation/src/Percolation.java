import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private static final int TOP = 0;
    private final int bottom;

    private boolean[][] matrix;
    private WeightedQuickUnionUF uf;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        matrix = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = false;
            }
        }
        bottom = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        if (!validate(i, j))
            throw new IndexOutOfBoundsException();

        int n = matrix.length;
        int lineIndex = (i - 1) * n + j;
        if (!isOpen(i, j)) {
            matrix[i - 1][j - 1] = true;
            if (i == 1) { // top row
                uf.union(TOP, lineIndex);
            } else if (i == n) { // bottom row
                uf.union(lineIndex, bottom);
            }
            if (i > 1 && isOpen(i - 1, j)) {
                uf.union(lineIndex - n, lineIndex);
            }
            if (i < n && isOpen(i + 1, j)) {
                uf.union(lineIndex, lineIndex + n);
            }
            if (j > 1 && isOpen(i, j - 1)) {
                uf.union(lineIndex - 1, lineIndex);
            }
            if (j < n && isOpen(i, j + 1)) {
                uf.union(lineIndex + 1, lineIndex);
            }
        }
    }

    private boolean validate(int i, int j) {
        return i > 0 && i <= matrix.length && j > 0 && j <= matrix[0].length;
    }

    public boolean isOpen(int i, int j) {
        if (!validate(i, j))
            throw new IndexOutOfBoundsException();
        return matrix[i-1][j-1];
    }

    public boolean isFull(int i, int j) {
        if (!validate(i, j))
            throw new IndexOutOfBoundsException();
        int lineIndex = (i - 1) * matrix.length + j;
        return uf.connected(TOP, lineIndex);
    }

    public boolean percolates() {
        return uf.connected(TOP, bottom);
    }
}
