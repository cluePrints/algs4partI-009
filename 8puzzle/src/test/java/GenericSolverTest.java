import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class GenericSolverTest {
    private static final File fixturesDir = new File("src/test/resources");
    private String fileName;
    private boolean expectToBeSolvable = true;
    private int expectedNumberOfMoves;

    public GenericSolverTest(String fileName) {
        super();
        this.fileName = fileName;
        Assert.assertTrue(fileName.startsWith("puzzle"));
        String code = fileName.replaceAll(".txt", "").replace("puzzle", "");
        if (code.contains("unsolvable")) {
            expectToBeSolvable = false;
            expectedNumberOfMoves = -1;
        } else {
            expectedNumberOfMoves = Integer.parseInt(code.substring(code.length()-2));
        }
    }
    
    @Test//(timeout=10000)
    public void test() {
        String fullFileName = new File(fixturesDir, fileName).toString();
        Solver solver = PuzzleChecker.solveFile(fullFileName);
        System.out.println("Skipped:"  + Solver.skipped);
        System.out.println("Delmin: "  + Solver.delmin);
        System.out.println("equals: "  + Solver.equals);
        System.out.println("insert: "  + Solver.insert);
        System.out.println("Isgoa:  "  + Solver.isgoal);
        System.out.println("Manhat: "  + Solver.mh);
        System.out.println("Neigh:  "  + Solver.nb);
        System.out.println("Total:  "  + (Solver.nb + Solver.mh + Solver.isgoal + solver.insert + solver.equals + solver.delmin));
        Assert.assertTrue(Solver.insert < 212831);
        Assert.assertEquals(expectToBeSolvable, solver.isSolvable());
        Assert.assertEquals(expectedNumberOfMoves, solver.moves());
        if (expectToBeSolvable) {
            Board initial = PuzzleChecker.initialBoard(fullFileName);
            Assert.assertEquals(initial, solver.solution().iterator().next());
        }
    }
    
    @Parameters(name="{0}")
    public static List<Object[]> params() {
        List<Object[]> params = new ArrayList<Object[]>();
        String[] names = fixturesDir.list();
        for (String name : names) {
            params.add(new Object[] {name});
        }
        return params;
    }
}
