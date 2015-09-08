

import org.junit.Assert;
import org.junit.Test;

public class PercolationTests {
    @Test
    public void newlyConnectedSiteShouldBeConnected() {
        Percolation percolation = new Percolation(51);
        percolation.open(19, 28);
        
        Assert.assertFalse(percolation.isFull(19, 28));
    }
    
    @Test
    public void newlyConnectedBorderSiteShouldBeConnected() {
        Percolation percolation = new Percolation(50);
        percolation.open(50, 50);
        
        Assert.assertFalse(percolation.isFull(50, 50));
    }
    
    @Test
    public void newlyConnectedBorderSite2ShouldBeConnected() {
        Percolation percolation = new Percolation(50);
        percolation.open(1, 1);
        
        Assert.assertTrue(percolation.isOpen(1, 1));
        Assert.assertTrue(percolation.isFull(1, 1));
    }
    
    @Test
    public void shouldBeNotOpenNotFullByDefault() {
        Percolation percolation = new Percolation(50);
        
        Assert.assertFalse(percolation.isFull(1, 1));
        Assert.assertFalse(percolation.isOpen(1, 1));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void shouldReserveZeroZeroSite() {
        Percolation percolation = new Percolation(50);
        percolation.open(0, 1);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void shouldReserveMaxSite() {
        Percolation percolation = new Percolation(50);
        percolation.open(51, 50);
    }
    
    @Test
    public void shouldNotPercolateInSimpleSetting() {
        Percolation percolation = new Percolation(1);
        Assert.assertFalse(percolation.percolates());
    }
    
    @Test
    public void shouldPercolateInSimpleSetting() {
        Percolation percolation = new Percolation(1);
        percolation.open(1, 1);
        
        Assert.assertTrue(percolation.percolates());
    }
    
    @Test
    public void shouldCalculatePositionsCorrectly() {
        Percolation percolation = new Percolation(8);
        Assert.assertEquals(1, percolation.calculatePos(1, 1));
        Assert.assertEquals(2, percolation.calculatePos(1, 2));
        Assert.assertEquals(9, percolation.calculatePos(2, 1));
        Assert.assertEquals(64, percolation.calculatePos(8, 8));
    }
    
}
