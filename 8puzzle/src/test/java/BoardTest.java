
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;


public class BoardTest {
    @Test
    public void testDetectsGoalBoard() {
        Board board = new Board(new int[][]{ new int[] { 1, 2}, new int[] {3, 0}});
        
        Assert.assertTrue(board.isGoal());
        Assert.assertEquals(0, board.hamming());
        Assert.assertEquals(0, board.manhattan());
    }
    
    @Test
    public void testManhattanIsCalculated0() {
        Board board = new Board(new int[][]{{0,3}, {1,2}});
        
        Assert.assertEquals(4, board.manhattan());
    }
    
    
    @Test
    public void testManhattanIsCalculated1() {
        Board board = new Board(new int[][]{{0,1}, {3,2}});
        
        Assert.assertEquals(2, board.manhattan());
    }
    
    @Test
    public void testManhattanIsCalculated() {
        Board board = new Board(new int[][]{{ 0,1,3},{4,2,5},{7,8,6}});
        
        Assert.assertEquals(4, board.manhattan());
    }
    
    @Test
    public void testManhattanIsCalculated2() {
        Board board = new Board(new int[][]{{ 1,2,3},{0,7,6},{5,4,8}});
        
        Assert.assertEquals(7, board.manhattan());
    }
    
    @Test
    public void testManhattanIsCalculated3() {
        Board board = new Board(new int[][]{{ 5,1,8},{2,7,3},{4,0,6}});
        
        Assert.assertEquals(13, board.manhattan());
    }
    
    @Test
    public void testDetectsNotGoalBoard() {
        Board board = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        
        Assert.assertFalse(board.isGoal());
    }
    
    @Test
    public void testCalculatesHammingDist() {
        Board board = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        
        Assert.assertEquals(1, board.hamming());
    }

    @Test
    public void testCalculatesManhattanDist() {
        Board board = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        
        Assert.assertEquals(1, board.manhattan());
    }
    
    @Test
    public void testCalculatesNeighbours() {
        Board board = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        
        Assert.assertEquals("[2\n0 1\n3 2, 2\n1 2\n3 0]", board.neighbors().toString());       
    }
    
    @Test
    public void testNeighboursAreNotGettingMoreZeroesBug() {
        Board board = new Board(new int[][]{{1,0,3},{4,2,5},{7,8,6}});
        for (int i=0; i<5; i++) {
            board = board.neighbors().iterator().next();
            ArrayList<Board> n = Lists.newArrayList(board.neighbors());
            Assert.assertEquals(n.size(), n.toString().split("0").length - 1 /** sloppy counting instances of a substring */);
        }
    }
    
    @Test
    public void testEquals() {
        Board board1 = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        Board board2 = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        Board board3 = board2.twin();
        
        Assert.assertEquals(board1, board2);
        Assert.assertNotEquals(board1, board3);
        Assert.assertNotEquals(board2, board3);
    }

    @Test
    public void testCreatesTwin() {
        Board board = new Board(new int[][]{ new int[] { 1, 0}, new int[] {3, 2}});
        
        Board twin = board.twin();
        Assert.assertFalse(twin.isGoal());
    }
}
