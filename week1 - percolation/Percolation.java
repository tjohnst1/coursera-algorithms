/******************************************************************************
*  Compilation:  javac-algs4 Percolation.java
*  Execution:    java-algs4 Percolation sample-input.txt
*
*  Week 1 Assignment - Percolation
*  Princeton/Coursera Algorithms
*  Prompt: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
*
******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF uf;
  private boolean[] grid;
  private int boardSize;
  private int virtualTopSite;
  private int virtualBottomSite;

  // create n-by-n grid, with all sites blocked
  public Percolation(int n) {
    if (n < 1) throw new IllegalArgumentException("the board must be at least 1x1");

    boardSize = n;
    virtualTopSite = n * n + 1;
    virtualBottomSite = n * n + 2;
    uf = new WeightedQuickUnionUF(n * n + 2);
    grid = new boolean[n * n];
  }

  // open site (row, col) if it is not open already (and connect to the open sites around it)
  public void open(int row, int col) {
    validateCoordinates(row, col);

    // exit early if the space is already open
    if (isOpen(row, col)) {
      return;
    }

    openSpace(row, col);
    connectWithAdjacentOpenSpaces(row, col);
    connectToVirtualSitesIfRelevant(row, col);
  }

  // is site (row, col) open?
  public boolean isOpen(int row, int col) {
    validateCoordinates(row, col);

    int index = toIndex(row, col);

    return grid[index];
  }

  // is site (row, col) full?
  public boolean isFull(int row, int col) {
    validateCoordinates(row, col);

    int index = toIndex(row, col);

    return uf.connected(index, virtualTopSite) || uf.connected(index, virtualBottomSite);
  }

  // number of open sites
  public int numberOfOpenSites() {
    int openSites = 0;
    for (int i = 0; i < boardSize * boardSize; i++) {
      if (grid[i]) {
        openSites += 1;
      }
    }

    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.connected(virtualTopSite, virtualBottomSite);
  }

  // convert coordinates to a number (to find elements in our board array)
  private int toIndex(int row, int col) {
    if (col == 1) {
      return row - 1;
    }

    return (col - 1) * boardSize + row - 1;
  }

  // check to see if the supplied coordinates are not in the grid
  private boolean isOutOfBounds(int row, int col) {
    return (row < 1 || col < 1 || row > boardSize || col > boardSize);
  }

  // make sure the supplied coordinates are valid, i.e. on the board
  private void validateCoordinates(int row, int col) {
    if (isOutOfBounds(row, col)) {
      throw new IllegalArgumentException("the supplied coordinates are not valid");
    }
  }

  // mark the space as open (i.e. true) in the grid
  private void openSpace(int row, int col) {
    if (!isOutOfBounds(row, col)) {
      int index = toIndex(row, col);
      grid[index] = true;
    }
  }

  // connect two spaces via "union"
  private void connectOpenSpaces(int row1, int col1, int row2, int col2) {
    if (!isOutOfBounds(row1, col1) && !isOutOfBounds(row2, col2) && isOpen(row1, col1) && isOpen(row2, col2)) {
      int index1 = toIndex(row1, col1);
      int index2 = toIndex(row2, col2);

      uf.union(index1, index2);
    }
  }

  // connect with the spaces to the top, bottom, left and right of the specified coordinates
  private void connectWithAdjacentOpenSpaces(int row, int col) {
    connectOpenSpaces(row, col, row - 1, col);
    connectOpenSpaces(row, col, row + 1, col);
    connectOpenSpaces(row, col, row, col - 1);
    connectOpenSpaces(row, col, row, col + 1);
  }

  private void connectToVirtualSitesIfRelevant(int row, int col) {
    if (!isOutOfBounds(row, col)) {
      int index = toIndex(row, col);

      if (row == 1) {
        uf.union(index, virtualTopSite);
      }

      if (row == boardSize) {
        uf.union(index, virtualBottomSite);
      }
    }
  }

  // test client (optional)
  public static void main(String[] args) {
  }
}
