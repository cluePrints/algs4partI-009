import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;

public class Solver {
    private boolean solvable;
    private LinkedList<Board> solution;

//    private static long mh;
//    private static long equals;
//    private static long skipped;
//    private static long insert;
//    private static long isgoal;
//    private static long delmin;
//    private static long nb;
    public static long mh;
    public static long equals;
    public static long skipped;
    public static long insert;
    public static long isgoal;
    public static long delmin;
    public static long nb;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }

        MinPQ<SearchNode> initialHeap = new MinPQ<>();
        MinPQ<SearchNode> twinHeap = new MinPQ<>();
        Board modified = initial.twin();

        initialHeap.insert(new SearchNode(0, initial, null));
        twinHeap.insert(new SearchNode(0, modified, null));

        trySolve(initialHeap, twinHeap);
    }

    private class SearchNode implements Comparable<SearchNode> {
        private int movesMadeAlready;
        private Board board;
        private SearchNode origin;

        public SearchNode(int movesMadeAlready, Board board, SearchNode origin) {
            super();
            this.movesMadeAlready = movesMadeAlready;
            this.board = board;
            this.origin = origin;
        }

        @Override
        public int compareTo(SearchNode o) {
            Solver.mh++;
            return (movesMadeAlready + board.manhattan()) - (o.movesMadeAlready + o.board.manhattan());
        }

        @Override
        public String toString() {
            return movesMadeAlready + "+" + board.manhattan();
        }
    }

    private void trySolve(MinPQ<SearchNode> initialHeap, MinPQ<SearchNode> twinHeap) {
        SearchNode previousRegular = initialHeap.min();
        SearchNode previousTwin = twinHeap.min();
        while (true) {
            SearchNode regularNode = initialHeap.delMin();
            Board regular = regularNode.board;

            if (regular.isGoal()) {
                solvable = true;
                solution = new LinkedList<>();
                SearchNode iterate = regularNode;
                while (iterate != null) {
                    solution.addFirst(iterate.board);
                    iterate = iterate.origin;
                }
                break;
            }

            delmin++;
            isgoal++;

            previousRegular = doSearch(initialHeap, previousRegular, regularNode, regular);

            SearchNode twinNode = twinHeap.delMin();
            Board twin = twinNode.board;

            delmin++;
            isgoal++;
            if (twin.isGoal()) {
                solution = null;
                solvable = false;
                break;
            }
            previousTwin = doSearch(twinHeap, previousTwin, twinNode, twin);
        }
    }

    private SearchNode doSearch(MinPQ<SearchNode> heap, SearchNode prevNode, SearchNode currentNode, Board currentBoard) {
        {
            nb++;
            for (Board neighbour : currentBoard.neighbors()) {
                equals++;
                boolean dup = false;
                SearchNode toCheck = prevNode;
                while (toCheck != null) {
                    skipped++;
                    if (neighbour.equals(toCheck.board)) {
                        dup = true;
                        break;
                    }
                    toCheck = toCheck.origin;
                }
                if (dup) {
                    continue;
                }

                SearchNode candidate = new SearchNode(currentNode.movesMadeAlready + 1, neighbour, currentNode);
                insert++;
                heap.insert(candidate);
            }
            prevNode = currentNode;
        }
        return prevNode;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return solvable ? solution.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return solution;
    }
}
