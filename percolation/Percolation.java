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

    private final boolean[] isFull;

    public Percolation(int size) {
        this.size = size;
        weightedQuickUnionUF = new WeightedQuickUnionUF(size * size + 1 + size);
        isOpen = new boolean[size * size + 1 + size];
        isFull = new boolean[size * size + 1 + size];
        initArrays();
    }

    public void open(int row, int col) {
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

        recomputeFullArray();
    }

    public boolean isOpen(int row, int col) {
        return isOpen[convertRowColToIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        return isFull[convertRowColToIndex(row, col)];
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
            weightedQuickUnionUF.union(convertRowColToIndex(row1, col1), convertRowColToIndex(row2, col2));
    }
    
    private int convertRowColToIndex(int row, int col) {
        return row + size * (col - 1);
    }

    private int[] convertIndexToRowCol(int index) {
        int[] result = new int[2];
        result[0] = index / size;
        result[1] = index % size;
        return result;
    }

    private void initArrays() {
        isOpen[0] = true;
        for (int i = 0; i < size; i++) {
            connectCell(0, 1, 1, i + 1);
        }
        isFull[0] = true;
    }

    private void recomputeFullArray() {
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (isFull(i, j)) continue;
                if (!isOpen(i, j)) continue;
                if (i > 1 && isFull(i - 1, j)) {
                    isFull[convertRowColToIndex(i, j)] = true;
                }
                if (j > 1 && isFull(i, j - 1)) {
                    isFull[convertRowColToIndex(i, j)] = true;
                }
                if (i < size && isFull(i + 1, j)) {
                    isFull[convertRowColToIndex(i, j)] = true;
                }
                if (j < size && isFull(i, j + 1)) {
                    isFull[convertRowColToIndex(i, j)] = true;
                }
            }
        }
        for (int i = size * size + 1; i < size * size + size + 1; i++) {
            if (isFull(convertIndexToRowCol(i)[0] - 1, convertIndexToRowCol(i)[1])) {
                isFull[i] = true;
            }
        }
    }


}
