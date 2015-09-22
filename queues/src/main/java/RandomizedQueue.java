import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[10];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == items.length) {
            items = Arrays.copyOf(items, size * 2);
        }
    }
    
    private void freeUnusedCapacity() {
        if (size <= items.length / 4) {
            items = Arrays.copyOf(items, size);
        }
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        ensureCapacity();
        items[size++] = item;
    }

    public Item dequeue() {
        freeUnusedCapacity();
        
        if (size == 0) {
            throw new NoSuchElementException();
        }

        int itemIdx = StdRandom.uniform(size);
        Item returnedItem = items[itemIdx];
        size = size - 1;
        items[itemIdx] = items[size];
        items[size] = null;
        return returnedItem;
    }

    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException();
        
        int itemIdx = StdRandom.uniform(size);
        return items[itemIdx];
    }

    public Iterator<Item> iterator() {
        return new RIterator(Arrays.copyOf(items, size));
    }

    private class RIterator implements Iterator<Item> {
        private final Item[] items;
        private int position;

        public RIterator(Item[] items) {
            super();
            this.items = items;
            StdRandom.shuffle(items);
        }

        @Override
        public Item next() {
            if (position >= items.length) {
                throw new NoSuchElementException();
            }

            Item item = items[position];
            items[position] = null;
            position = position + 1;
            return item;
        }

        @Override
        public boolean hasNext() {
            return position < items.length;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}