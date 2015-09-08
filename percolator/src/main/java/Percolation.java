


public class Percolation {
    private final Tree tree;
    private boolean[] open;
    private final int n;
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = N;
        this.tree = new Tree(N*N + 2);        // 2 synthetic sites for upper and bottom lines
        this.open = new boolean[N*N + 1];
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        checkBoundary(i, j);
        int pos = calculatePos(i, j);
        open[pos] = true;
        
        if (isOpen0(i-1, j)) {
            connect(i, j, i-1, j); 
        }
        
        if (isOpen0(i+1, j)) {
            connect(i, j, i+1, j); 
        }
        
        if (j<n && isOpen(i, j+1)) {
            connect(i, j, i, j+1); 
        }
        
        if (j>=2 && isOpen(i, j-1)) {
            connect(i, j, i, j-1); 
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        checkBoundary(i, j);
        return isOpen0(i, j);
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        checkBoundary(i, j);
        int pos = calculatePos(i, j);
        return tree.connected(0, pos);
    }
    
    private boolean isOpen0(int i, int j) {
        if (i == 0 || i == (n+1))
            return true;
        
        int pos = calculatePos(i, j);
        return open[pos];
    }
    
    private void connect(int i0, int j0, int i, int j) {
        int pos0 = calculatePos(i0, j0);
        int pos = calculatePos(i, j);
        tree.union(pos0, pos);
    }
    
    // does the system percolate?
    public boolean percolates() {
        return tree.connected(0, n*n+1);
    }
    
    private void checkBoundary(int i, int j) {
        if (i<=0 || i>n) {
            throw new IndexOutOfBoundsException();
        }
        
        if (j<=0 || j>n) {
            throw new IndexOutOfBoundsException();
        }
    }

    // Visible for tests
    int calculatePos(int i, int j) {
        if (i == 0)
            return 0;
        
        if (i == (n+1))
            return n*n+1;
        
        return (i-1)*n + j;
    }
}
