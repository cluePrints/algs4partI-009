


public class Tree {
    private final int[] pointers;
    private final int[] weights;
    public Tree(int nPointers) {
        super();
        this.pointers = new int[nPointers];
        this.weights = new int[nPointers];
        for (int i=0; i<this.pointers.length; i++) {
            this.pointers[i] = i;
        }
    }
    
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) {
            return;
        }
        
        if (weights[i] < weights[j]) {
            pointers[i] = j;
            weights[j] += weights[i];
        } else {
            pointers[j] = i;
            weights[i] += weights[j];
        }
    }
    
    public int root(int i) {
        while (i != pointers[i]) {
            pointers[i] = pointers[pointers[i]];
            i = pointers[i];
        }
        
        return i;
    }
    
    public boolean pathExists(int i, int j) {
        return root(i) == root(j);
    }
    
    public boolean isIsolated(int i) {
        return pointers[i] == i;
    }
    
}
