
import org.junit.Assert;
import org.junit.Test;


public class BFTest {
    @Test
    public void shouldDetectPointsOnHLine() {
        Point[] p = new Point[5];
        p[1] = new Point(1,3);
        p[0] = new Point(0,0);
        p[2] = new Point(2,3);
        p[3] = new Point(3,3);
        p[4] = new Point(4,3);
        
        LineSegment[] segments = new BruteCollinearPoints(p).segments();
        Assert.assertEquals(segments.length, 1);        
    }    
    
    @Test
    public void shouldDetectPointsOnVLine() {
        Point[] p = new Point[5];
        p[1] = new Point(3,1);
        p[0] = new Point(0,0);
        p[2] = new Point(3,2);
        p[3] = new Point(3,3);
        p[4] = new Point(3,4);
        
        LineSegment[] segments = new BruteCollinearPoints(p).segments();
        Assert.assertEquals(segments.length, 1);        
    }    
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldDieOnSamePoints() {
        Point[] p = new Point[] {new Point(1,3), new Point(1,3), new Point(1,3), new Point(1,3) };
        
        new BruteCollinearPoints(p);
    }  
}
