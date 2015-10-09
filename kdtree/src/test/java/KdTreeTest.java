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
    public void test10x10() {
        insert(10, 100 * 1000);
    }

    @Test
    public void test1x1() {
        insert(1, 100*1000);
    }
    
    @Test
    public void testSize() {
        KdTree tree = new KdTree();
        Assert.assertEquals(0, tree.size());
        
        tree.insert(new Point2D(1, 1));
        Assert.assertEquals(1, tree.size());
        tree.insert(new Point2D(1.0, 1.0));
        tree.insert(new Point2D(0.5, 0));
        tree.insert(new Point2D(1, 0.5));
        Assert.assertEquals(3, tree.size());
    }
    
    @Test(timeout=5000)
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
                        new RectHV(point.x()-0.01,
                                point.y()-0.01,
                                point.x()+0.01, 
                                point.y()+0.01));
                
                Assert.assertTrue("Failed at step #" + i, range.contains(point));
            } catch (IllegalStateException ex) {
                throw new AssertionError("Failed at step #" + i, ex);
            }
        }
    }
    
    @Test
    public void testNearest() {
        StdRandom.setSeed(2);
        runNearestWithSizeAndPointsCount(1000*100, 4000);
        runNearestWithSizeAndPointsCount(1000*10, 4000);
        runNearestWithSizeAndPointsCount(1000, 4000);
        runNearestWithSizeAndPointsCount(10, 4000);
    }

    private void runNearestWithSizeAndPointsCount(int size, int numPoints) throws AssertionError {
        Point2D target = distinctPoint(size);
        Point2D expectedNearest = distinctPoint(size);
        KdTree tree = new KdTree();
        tree.insert(expectedNearest);
        for (int i=0; i<numPoints; i++) {
            Point2D newPoint = distinctPoint(size);
            tree.insert(newPoint);
            if (newPoint.distanceTo(target) < expectedNearest.distanceTo(target)) {
                expectedNearest = newPoint;
            }
            try {
                Point2D nearestResult = tree.nearest(target);
                
                Assert.assertEquals(
                        String.format("Failed at step #%s. Target point was: %s  %s(newDist) vs %s(expectedDist)", 
                                i,
                                target,
                                newPoint.distanceTo(target), 
                                expectedNearest.distanceTo(target)), 
                        expectedNearest, nearestResult);
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
                //tree.debugEnabled = true;
            }

            tree.insert(point);
            Assert.assertTrue("Problem with iteration #" + i + "point: " + point, tree.contains(point));
            //tree.debugEnabled = false;
        }
        
        for (int i=0; i<nPoints; i++) {
            Point2D distinct = distinctPoint(gridSize);
            boolean expected = inserted.contains(distinct);
            boolean actual = tree.contains(distinct);
            if (expected != actual) {
//                tree.debugEnabled = true;
                tree.contains(distinct);
            }
            Assert.assertEquals("Failed on iteration #" + i + "point: " + distinct, expected, actual);
        }
    }

    private Point2D distinctPoint(int gridSize) {
        double x = StdRandom.uniform(0, gridSize + 1);
        double y = StdRandom.uniform(0, gridSize + 1);
        while (y == x) {
            y = StdRandom.uniform(0, gridSize + 1);
        }
        Point2D distinct = new Point2D(x, y);
        return distinct;
    }

    private void insert(int gridSize, int nPoints) {
        Set<Point2D> points = new HashSet<Point2D>(nPoints);
        KdTree tree = new KdTree();
        for (int i = 0; i < nPoints; i++) {
            double x = 1/StdRandom.uniform(1, gridSize + 1);
            double y = 1/StdRandom.uniform(1, gridSize + 1);
            Point2D point = new Point2D(x, y);
            points.add(point);
            tree.insert(point);
            Assert.assertEquals(points.size(), tree.size());
        }
    }

}
