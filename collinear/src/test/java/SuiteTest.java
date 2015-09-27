
import com.google.common.base.Function;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import org.junit.Assert;
import org.junit.Test;


public class SuiteTest {
    @Test
    public void test() {
        testOn("src/test/resources/input8.txt", new Brute(), "(10000, 0) -> (0, 10000)\n(3000, 4000) -> (20000, 21000)\n");
    }
    
    @Test
    public void test1() {
        testOn("src/test/resources/input8.txt", new Fast(), "(3000, 4000) -> (20000, 21000)\n(10000, 0) -> (0, 10000)\n");
    }
    
    @Test
    public void test2() {
        testOn("src/test/resources/input6.txt", new Fast(), "(14000, 10000) -> (32000, 10000)\n");
    }
    
    @Test
    public void test3() {
        testOn("src/test/resources/rnd_hseg.txt", new Fast(), "(1354, 18935) -> (20865, 18935)\n"
                + "(3684, 6527) -> (10380, 6527)\n"
                + "(9885, 15110) -> (12927, 15110)\n"
                + "(2075, 9798) -> (20677, 9798)\n"
                + "(1286, 14234) -> (8373, 14234)\n");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowErrorOnSamePoints5() {
        testOn("src/test/resources/test17_5.txt", new Fast(), "exception");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowErrorOnSamePoints4() {
        testOn("src/test/resources/test17_4.txt", new Fast(), "exception");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowErrorOnSamePoints3() {
        testOn("src/test/resources/test17_3.txt", new Fast(), "exception");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowErrorOnSamePoints2() {
        testOn("src/test/resources/test17_2.txt", new Fast(), "exception");
    }
    
    @Test
    public void test11_1() {
        testOn("src/test/resources/test11_1.txt", new Fast(), "");
    }
    
    @Test
    public void test11_2() {
        testOn("src/test/resources/test11_2.txt", new Fast(), "");
    }
    
    private void testOn(String input, Function<Point[], LineSegment[]> f, String expectedOutput) {
        In in = new In(input);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        
        String output = "";
        for (LineSegment segment : f.apply(points)) {
            StdOut.println(segment);
            segment.draw();
            output += segment + "\n";
        }
        
        Assert.assertEquals(expectedOutput, output);
    }
    
    private static class Brute implements Function<Point[], LineSegment[]> {
        @Override
        public LineSegment[] apply(Point[] points) {
            BruteCollinearPoints collinear = new BruteCollinearPoints(points);
            return collinear.segments();
        }
    }
    
    private static class Fast implements Function<Point[], LineSegment[]> {
        @Override
        public LineSegment[] apply(Point[] points) {
            FastCollinearPoints collinear = new FastCollinearPoints(points);
            return collinear.segments();
        }
    }
}
