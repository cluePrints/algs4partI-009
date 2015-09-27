import org.junit.Assert;
import org.junit.Test;


public class PointTest {
    @Test
    public void shouldCompareSlopes() {
        Point p = new Point(193, 349);
        Point q = new Point(193, 188);
        Point r = new Point(193, 326);
        
        Assert.assertEquals(0, p.slopeOrder().compare(q, r));
    }
    
    @Test
    public void shouldBreakTiesWithY() {
        Point p1 = new Point(1, 5);
        Point p2 = new Point(1, 6);
        
        Assert.assertEquals(-1, p1.compareTo(p2));
        Assert.assertEquals(1, p2.compareTo(p1));
        
        Point p0 = new Point(0, 0);
        Assert.assertEquals(-1, p0.slopeOrder().compare(p1, p2));
        Assert.assertEquals(1, p0.slopeOrder().compare(p2, p1));
    }
    
    @Test
    public void shouldDetectEqualPoints() {
        Point p1 = new Point(3, 6);
        Point p2 = new Point(3, 6);
        
        Assert.assertEquals(0, p1.compareTo(p2));
        Assert.assertEquals(0, p2.compareTo(p1));
        
        Point p0 = new Point(1, 1);
        Assert.assertEquals(0, p0.slopeOrder().compare(p1, p2));
    }
    
    @Test
    public void shouldReturnNegInfAsSlopeToItself() {
        Point p1 = new Point(3, 6);
        
        Assert.assertTrue(Double.NEGATIVE_INFINITY == p1.slopeTo(p1));
    }
    
    @Test
    public void shouldReturnPosInfAsSlopeToVertLine() {
        Point p1 = new Point(3, 6);
        Point p2 = new Point(3, 8);
        
        Assert.assertTrue(Double.POSITIVE_INFINITY == p1.slopeTo(p2));
        Assert.assertTrue(Double.POSITIVE_INFINITY == p2.slopeTo(p1));
    }
    
    @Test
    public void shouldReturnZeroAsSlopeToHLine() {
        Point p1 = new Point(4, 6);
        Point p2 = new Point(3, 6);
        
        Assert.assertEquals(0, p1.slopeTo(p2), 0.00001);
        Assert.assertEquals(0, p2.slopeTo(p1), 0.00001);
    }
}
