/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUF;

    private final int size;

    private final boolean[] isOpen;

    private int numberOfOpenSites;

    private final boolean[] isFull;

    public Percolation(int size) {
        this.size = size;
        numberOfOpenSites = 0;
        weightedQuickUnionUF = new WeightedQuickUnionUF(size * size + 1 + size);
        isOpen = new boolean[size * size + 1 + size];
        isFull = new boolean[size * size + 1 + size];
        initArrays();
    }

    public void open(int row, int col) {
        if (!isArgumentsLegal(row, col)) {
            throw new IllegalArgumentException();
        }

        if (row > 1 && isOpen(row - 1, col)) {
            connectCell(row - 1, col, row, col);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            connectCell(row, col - 1, row, col);
        }
        if (row < size && isOpen(row + 1, col)) {
            connectCell(row + 1, col, row, col);
        }
        if (col < size && isOpen(row, col + 1)) {
            connectCell(row, col + 1, row, col);
        }

        isOpen[convertRowColToIndex(row, col)] = true;
        numberOfOpenSites += 1;

        recomputeFullArray();
    }

    public boolean isOpen(int row, int col) {
        if (!isArgumentsLegal(row, col)) {
            throw new IllegalArgumentException();
        }

        return isOpen[convertRowColToIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (!isArgumentsLegal(row, col)) {
            throw new IllegalArgumentException();
        }

        return isFull[convertRowColToIndex(row, col)];
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        boolean percolates;
        for (int i = 0; i < size; i++) {
            percolates = weightedQuickUnionUF.connected(1, convertRowColToIndex(size + 1, i + 1));
            if (percolates) {
                return true;
            }
        }
        return false;
    }

    private void connectCell(int row1, int col1, int row2, int col2) {
        weightedQuickUnionUF
                .union(convertRowColToIndex(row1, col1), convertRowColToIndex(row2, col2));
    }

    private int convertRowColToIndex(int row, int col) {
        return size * (row-1) + col;
    }

    private int[] convertIndexToRowCol(int index) {
        int[] result = new int[2];
        result[0] = index / size;
        result[1] = index % size + 1;
        return result;
    }

    private boolean isArgumentsLegal(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            return false;
        }

        return true;
    }

    private void initArrays() {
        isOpen[0] = true;
        for (int i = 0; i < size; i++) {
            connectCell(1, 0, 1, i + 1);
            connectCell(size, i + 1, size + 1, i + 1);
        }
        isFull[0] = true;
    }

    private void recomputeFullArray() {
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (isFull(i, j)) {
                    continue;
                }
                if (!isOpen(i, j)) {
                    continue;
                }
                if (weightedQuickUnionUF.connected(0, convertRowColToIndex(i, j))) {
                    isFull[convertRowColToIndex(i, j)] = true;
                }
            }
            for (int k = size * size + 1; k < size * size + size + 1; k++) {
                if (isFull(convertIndexToRowCol(k)[0] - 1, convertIndexToRowCol(k)[1])) {
                    isFull[k] = true;
                }
            }
        }
    }
}
