


import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;

public class DequeTest {
    @Test(expected=NullPointerException.class)
    public void shouldNpeOnNullAddFirst() {
        Deque<String> deque = new Deque<String>();
        deque.addFirst(null);
    }
    
    @Test(expected=NullPointerException.class)
    public void shouldNpeOnNullAddLast() {
        Deque<String> deque = new Deque<String>();
        deque.addLast(null);
    }
    
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnNullRemoveLastEmpty() {
        Deque<String> deque = new Deque<String>();
        deque.removeLast();
    }
    
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnNullRemoveFirstEmpty() {
        Deque<String> deque = new Deque<String>();
        deque.removeFirst();
    }
    
    
    @Test
    public void shouldDetectEmptyITerator() {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("i1");
        deque.removeLast();
        Assert.assertFalse(deque.iterator().hasNext());
    }
    
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnEmptyITerator() {
        Deque<String> deque = new Deque<String>();
        deque.addLast("i1");
        deque.removeFirst();
        deque.iterator().next();
    }
    
    @Test
    public void shouldEnqueAndDeque() {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("i1");
        deque.addFirst("i2");
        
        String result = deque.removeFirst();
        Assert.assertEquals("i2", result);
        
        result = deque.removeFirst();
        Assert.assertEquals("i1", result);
    }
    
    @Test
    public void shouldEnqueAndDequeL() {
        Deque<String> deque = new Deque<String>();
        Assert.assertEquals(0, deque.size());
        deque.addFirst("i1");
        Assert.assertEquals(1, deque.size());
        deque.addFirst("i2");
        Assert.assertEquals(2, deque.size());
        
        
        String result = deque.removeLast();
        Assert.assertEquals("i1", result);
        Assert.assertEquals(1, deque.size());
        
        result = deque.removeLast();
        Assert.assertEquals("i2", result);
        Assert.assertEquals(0, deque.size());
    }
    
    @Test
    public void should() {
        Deque<Integer> deque = new Deque<>();
        
        Assert.assertTrue(deque.isEmpty());
        Assert.assertTrue(deque.isEmpty());
        deque.addLast(2);
        Assert.assertEquals(2, (int) deque.removeFirst());
        deque.addLast(4);
        deque.addLast(5);
        
        Assert.assertEquals(4, (int) deque.removeFirst());
        Assert.assertEquals(5, (int) deque.removeFirst());
    }
    
    @Test
    public void shouldCreateIterator() {
        Deque<Integer> deque = new Deque<>();
        for (int i=0; i<100; i++) {
            Assert.assertEquals(i, deque.size());
            deque.addFirst(i);
        }
        
        int i = 99;
        for (Integer num : deque) {
            Assert.assertEquals(i--, (int) num);
        }
    }
    
    @Test
    public void shouldHaveWorkingIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addFirst(2);
        Iterator<Integer> it = deque.iterator();
        Assert.assertEquals(2, (int) it.next());
        Assert.assertEquals(1, (int) it.next());
    }
}
