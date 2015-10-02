import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private int blankRow = 0;
    private int blankCol = 0;
    private int manhattan = 0;
    private int hamming = 0;
    
    public Board(int[][] blocks) {
        int[][] blocksCopy = new int[blocks.length][];
        for (int i = 0; i < blocks.length; i++) {
            blocksCopy[i] = Arrays.copyOf(blocks[i], blocks.length);
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }               
            }
        }        

        this.blocks = blocksCopy;
        calculateDistances(blocksCopy);
    }

    private void calculateDistances(int[][] blocks) {
        int h = 0;
        int m = 0;
        for (int i=0; i<blocks.length; i++) {
            for (int j=0; j<blocks.length; j++) {
                // calculate manhattan distance too
                int blockValue = blocks[i][j];
                if (blockValue == 0)
                    continue;

                int expectedValue = i * blocks.length + j + 1;
                if (expectedValue != blockValue) 
                    h++;
                    
                if (expectedValue == blockValue)
                    continue;

                int row = (blockValue - 1) / blocks.length;
                int col = (blockValue - 1) % blocks.length;

                m += Math.abs(row - i) + Math.abs(col - j);
            }
        }
        
        this.hamming = h;
        this.manhattan = m;
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return blocks.length; // board dimension N
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    private int blockValue(int i) {
        int row = i / dimension();
        int col = i % dimension();
        int blockValue = blocks[row][col];
        return blockValue;
    }

    private void setBlockValue(int i, int val) {
        int row = i / dimension();
        int col = i % dimension();
        blocks[row][col] = val;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return manhattan == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int idxFirst = 0;
        while (blockValue(idxFirst) == 0) {
            idxFirst++;
        }
        int idxSecond = idxFirst + 1;
        while (blockValue(idxSecond) == 0) {
            idxSecond++;
        }
        int valFirst = blockValue(idxFirst);
        int valSecond = blockValue(idxSecond);

        Board twin = new Board(blocks);
        twin.setBlockValue(idxFirst, valSecond);
        twin.setBlockValue(idxSecond, valFirst);
        twin.calculateDistances(twin.blocks);
        return twin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (!Arrays.deepEquals(blocks, other.blocks))
            return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<>(4);

        if (blankCol > 0) {
            neighbours.add(neighbour(0, -1));
        }
        if (blankCol < dimension() - 1) {
            neighbours.add(neighbour(0, +1));
        }
        if (blankRow < dimension() - 1) {
            neighbours.add(neighbour(+1, 0));
        }
        if (blankRow > 0) {
            neighbours.add(neighbour(-1, 0));
        }

        return neighbours;
    }

    private Board neighbour(int dRow, int dCol) {
        Board twin = new Board(blocks);
        twin.blocks[blankRow][blankCol] = twin.blocks[blankRow + dRow][blankCol + dCol];
        twin.blocks[blankRow + dRow][blankCol + dCol] = 0;
        twin.blankCol = blankCol + dCol;
        twin.blankRow = blankRow + dRow;
        twin.calculateDistances(twin.blocks);
        return twin;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension()).append('\n');
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                str.append(blocks[i][j]);
                if (j < dimension() - 1) {
                    str.append(' ');
                }
            }
            if (i < dimension() - 1) {
                str.append('\n');
            }
        }

        return str.toString();
    }
}
