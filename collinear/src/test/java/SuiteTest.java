
import java.util.Collections;

import java.util.Arrays;
import com.google.common.base.Function;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;


public class SuiteTest {
    @Test
    public void test() {
        testOn("src/test/resources/input8.txt", new Brute(), "(10000, 0) -> (0, 10000)\n(3000, 4000) -> (20000, 21000)\n");
    }
    
    @Test
    public void test1() {
        testOn("src/test/resources/input8.txt", new Fast(), "(10000, 0) -> (0, 10000)\n(3000, 4000) -> (20000, 21000)\n");
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
                + "(1286, 14234) -> (8373, 14234)\n"
                + "(2075, 9798) -> (20677, 9798)\n");
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
    
    @Test
    public void shouldHandlePointsBelongingToMultipleCollinears() {
        testOn("src/test/resources/test11_3.txt", new Fast(), "(7, 1) -> (3, 9)\n(0, 9) -> (9, 9)\n");
    }
    
    @Test
    public void test11_4() {
        testOn("src/test/resources/test11_4.txt", new Fast(), "(9, 0) -> (5, 4)\n(5, 4) -> (5, 8)\n");
    }
    
    @Test
    public void test11_5() {
        testOn("src/test/resources/test11_5.txt", new Fast(), "(7, 1) -> (7, 8)\n");
    }
    
    @Test
    public void test11_6() {
        testOn("src/test/resources/test11_6.txt", new Fast(), "(1, 1) -> (1, 8)\n(6, 0) -> (6, 9)\n");
    }
    
    @Test(timeout=5000)
    public void testPerfEiths() {
        int n=512;
        TreeSet<Point> points = new TreeSet<Point>();
        while (points.size() < n*8) {
            points.add(new Point(StdRandom.uniform(n/8), StdRandom.uniform(n)));
        }
        
        long time = System.currentTimeMillis();
        new FastCollinearPoints(points.toArray(new Point[0]));
        time = System.currentTimeMillis() - time;
        System.out.println(time);
        Assert.assertTrue(time < 2000);
    }
    
    @Test
    public void test200points() {
        List<Point> points = new ArrayList<Point>();

        addSegment(points, "(31, 5) -> (21, 25) -> (18, 31) -> (15, 37) -> (13, 41)");
        addSegment(points, "(6, 0) -> (5, 2) -> (4, 4) -> (3, 6) -> (2, 8) -> (1, 10)");
        
        StdRandom.setSeed(10123);
        Point[] ptsParam = points.toArray(new Point[0]);
        StdRandom.shuffle(ptsParam);
        
        LineSegment[] segments = new FastCollinearPoints(ptsParam).segments();
        Assert.assertEquals("[(6, 0) -> (1, 10), (31, 5) -> (13, 41)]", Arrays.toString(segments));
    }

    private void addSegment(List<Point> points, String chain) {
        for (String item : chain.split(" -> ")) {
            String[] coordsStr = item.replaceAll("[\\(\\)]", "").split(", ");
            points.add(new Point(Integer.parseInt(coordsStr[0]), Integer.parseInt(coordsStr[1])));
        }
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
        
        List<String> output = new ArrayList<String>();
        for (LineSegment segment : f.apply(points)) {
            StdOut.println(segment);
            segment.draw();
            output.add(segment.toString());
        }
        
        String[] expectedOutputArr = expectedOutput.split("\n");
        Arrays.sort(expectedOutputArr);
        Collections.sort(output);
        expectedOutput = Arrays.toString(expectedOutputArr).replaceAll("\\), ", ")\n");
        String outputStr = output.toString().replaceAll("\\), ", ")\n");
        
        Assert.assertEquals(expectedOutput, outputStr);
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
