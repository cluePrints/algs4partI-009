import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) throws Exception {
        String param = args[0];
        int k = Integer.parseInt(param);

        String[] strings = StdIn.readAllStrings();
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (String string : strings) {
            if (queue.size() == k && k > 0) {
                queue.dequeue();
            }

            queue.enqueue(string);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
