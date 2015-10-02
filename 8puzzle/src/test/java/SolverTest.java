import org.junit.Assert;
import org.junit.Test;


public class SolverTest {
    @Test
    public void testCanDetectAlreadySolvedBoard() {
        Board initial = new Board(new int[][] {{1, 2}, {3, 0}});
        Solver solver = new Solver(initial);
        
        Assert.assertTrue(solver.isSolvable());
    }
    
    @Test
    public void testCanSolveSimpleBoard() {
        Board initial = new Board(new int[][] {{1, 0}, {3, 2}});
        Solver solver = new Solver(initial);
        
        Assert.assertTrue(solver.isSolvable());
        Assert.assertEquals(1, solver.moves());
    }

    @Test
    public void testCanDetectTrivialUnsolvableBoard() {
        Board initial = new Board(new int[][] {{2, 1}, {3, 0}});
        Solver solver = new Solver(initial);
        
        Assert.assertFalse(solver.isSolvable());
    }
    
    @Test
    public void test04() {
        Solver solver = PuzzleChecker.solveFile("src/test/resources/puzzle04.txt");
        Assert.assertEquals("[3\n0 1 3\n4 2 5\n7 8 6\n--\n"
                + "3\n1 0 3\n4 2 5\n7 8 6\n--\n"
                + "3\n1 2 3\n4 0 5\n7 8 6\n--\n"
                + "3\n1 2 3\n4 5 0\n7 8 6\n--\n"
                + "3\n1 2 3\n4 5 6\n7 8 0]", solver.solution().toString().replaceAll(", ", "\n--\n"));
        Assert.assertEquals(4, solver.moves());
    }
}
