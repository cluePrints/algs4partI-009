import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private DNode<Item> head;
    private DNode<Item> tail;
    private int size;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // return the number of items on the deque
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        DNode<Item> node = new DNode<Item>();
        node.item = item;
        node.previous = head;
        if (head != null) {
            head.next = node;
        }
        if (tail == null) {
            tail = node;
        }
        head = node;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        DNode<Item> node = new DNode<Item>();
        node.item = item;
        node.next = tail;
        tail = node;
        if (head == null) {
            head = node;
        }
        if (tail.next != null) {
            tail.next.previous = tail;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Item removed = head.item;
        head = head.previous;
        if (head != null) {
            head.next = null;
        } else {
            tail = null;
        }
        size--;
        return removed;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        size--;
        Item removed = tail.item;
        tail = tail.next;
        if (tail != null) {
            tail.previous = null;
        } else {
            head = null;
        }
        return removed;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DIterator(head) ;
    }

    public static void main(String[] args) {
        // unit testing
    }
    
    private class DIterator implements Iterator<Item> {
        private DNode<Item> currentNode;      
        
        public DIterator(Deque<Item>.DNode<Item> currentNode) {
            super();
            this.currentNode = currentNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (currentNode == null) {
                throw new NoSuchElementException();
            }
            
            Item result = currentNode.item;
            currentNode = currentNode.previous;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    private class DNode<Item> {
        DNode<Item> next;
        DNode<Item> previous;
        Item item;
    }
}