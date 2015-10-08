import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class KdTreeTest {
    @Test
    public void test100kx100k() {
        insert(100 * 1000, 100 * 1000);
    }

    @Test
    public void test10kx10k() {
        insert(10000, 100 * 1000);
    }

    @Test
    public void test100x100() {
        insert(1000, 100 * 1000);
    }

    @Test
    public void test1x1() {
        insert(1, 10);
    }
    
    @Test(timeout=1000)
    public void testContains() {
        StdRandom.setSeed(14123);
        containsTest(100*1000, 100*1000);
        containsTest(100*1000, 10*1000);
        containsTest(100*1000, 1000);
        containsTest(10*1000, 100);
        containsTest(100, 10);
        containsTest(1, 1);
    }
    
    @Test
    public void testRange() {
        StdRandom.setSeed(1);
        int size =100 * 1000;
        int numPoints = 4000;
        int numRectangles = 4000;
        KdTree tree = new KdTree();
        for (int i=0; i<numPoints; i++) {
            Point2D point = distinctPoint(size);
            tree.insert(point);
            try {
                Collection<Point2D> range = (Collection<Point2D>) tree.range(
                        new RectHV(
                                Math.max(0, point.x()-0.01),
                                Math.max(0, point.y()-0.01),
                                Math.min(1, point.x()+0.01), 
                                Math.min(1, point.y()+0.01)));
                
                Assert.assertTrue("Failed at step #" + i, range.contains(point));
            } catch (IllegalStateException ex) {
                throw new AssertionError("Failed at step #" + i, ex);
            }
        }
    }

    private void containsTest(int nPoints, int gridSize) {
        KdTree tree = new KdTree();
        Set<Point2D> inserted = new HashSet<Point2D>(nPoints);
        for (int i = 0; i < nPoints; i++) {
            double x = StdRandom.uniform(1, gridSize + 1);
            double y = StdRandom.uniform(1, gridSize + 1);
            
            Point2D point = new Point2D(x, y);
            inserted.add(point);
            boolean poi = false; //point.equals(new Point2D(72852.0, 68853.0));
            if (poi) {
                point.toString();
                tree.debugEnabled = true;
            }

            tree.insert(point);
            Assert.assertTrue("Problem with iteration #" + i + "point: " + point, tree.contains(point));
            tree.debugEnabled = false;
        }
        
        for (int i=0; i<nPoints; i++) {
            Point2D distinct = distinctPoint(gridSize);
            boolean expected = inserted.contains(distinct);
            boolean actual = tree.contains(distinct);
            if (expected != actual) {
                tree.debugEnabled = true;
                tree.contains(distinct);
            }
            Assert.assertEquals("Failed on iteration #" + i + "point: " + distinct, expected, actual);
        }
    }

    private Point2D distinctPoint(int gridSize) {
        double x = StdRandom.uniform(0, 1/(gridSize + 1.0));
        double y = StdRandom.uniform(0, 1/(gridSize + 1.0));
        while (y == x) {
            y = StdRandom.uniform(1, 1/(gridSize + 1.0));
        }
        Point2D distinct = new Point2D(x, y);
        return distinct;
    }

    private void insert(int gridSize, int nPoints) {
        Set<Point2D> points = new HashSet<Point2D>(nPoints);
        KdTree tree = new KdTree();
        for (int i = 0; i < nPoints; i++) {
            double x = StdRandom.uniform(1, gridSize + 1);
            double y = StdRandom.uniform(1, gridSize + 1);
            Point2D point = new Point2D(x, y);
            points.add(point);
            tree.insert(point);
            Assert.assertEquals(points.size(), tree.size());
        }
    }

}
