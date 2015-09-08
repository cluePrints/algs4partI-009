

public class Tree {
    private final int[] pointers;
    public Tree(int nPointers) {
        super();
        this.pointers = new int[nPointers];
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
        
        pointers[i] = j;
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
