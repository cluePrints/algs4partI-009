import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Test;


public class RndQueueTest {
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnNextOnEmptyIterator() {
        new RandomizedQueue<>().iterator().next();
    }
    
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnNextOnEmptyGet() {
        new RandomizedQueue<>().dequeue();
    }
    
    @Test(expected=NoSuchElementException.class)
    public void shouldErrorOnNextOnEmptySample() {
        new RandomizedQueue<>().sample();
    }
    
    @Test(expected=NullPointerException.class)
    public void shouldErrorOnNullAdd() {
        new RandomizedQueue<>().enqueue(null);
    }
    
    @Test
    public void shouldBeAbleToContainMoreThanDefaultCapacityElements() {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (int i=0; i<20; i++) {
            q.enqueue(String.format("%05d", i));
        }
        
        List<String> deqed = new ArrayList<>();
        for (int i=0; i<20; i++) {
            deqed.add(q.dequeue());
        }
        
        Collections.sort(deqed);
        for (int i=0; i<20; i++) {
            Assert.assertEquals(String.format("%05d", i), deqed.get(i));
        }
    }
    
    @Test
    public void shouldBeAbleToIterate() {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (int i=0; i<20; i++) {
            q.enqueue(String.format("%05d", i));
        }
        
        List<String> deqed = new ArrayList<>();
        for (String str : q) {
            deqed.add(str);
        }
        
        Collections.sort(deqed);
        for (int i=0; i<20; i++) {
            Assert.assertEquals(String.format("%05d", i), deqed.get(i));
        }
    }
}
