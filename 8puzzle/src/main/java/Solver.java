import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;

public class Solver {
    private boolean solvable;
    private LinkedList<Board> solution;

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

    private static class SearchNode implements Comparable<SearchNode> {
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
            return (movesMadeAlready + board.manhattan()) - (o.movesMadeAlready + o.board.manhattan());
        }
        
        @Override
        public String toString() {
            return movesMadeAlready + "+" + board.manhattan();
        }
    }

    private void trySolve(MinPQ<SearchNode> initialHeap, MinPQ<SearchNode> twinHeap) {
        Board previousRegular = null;
        Board previousTwin = null;
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
                return;
            }

            SearchNode twinNode = twinHeap.delMin();
            Board twin = twinNode.board;
            if (twin.isGoal()) {
                solution = null;
                solvable = false;
                return;
            }

            {
                for (Board neighbour : regular.neighbors()) {
                    if (neighbour.equals(previousRegular))
                        continue;

                    SearchNode candidate = new SearchNode(regularNode.movesMadeAlready + 1, neighbour, regularNode);
                    
                    initialHeap.insert(candidate);
                }
                previousRegular = regular;
            }
            {
                for (Board neighbour : twin.neighbors()) {
                    if (neighbour.equals(previousTwin))
                        continue;
                    
                    twinHeap.insert(new SearchNode(twinNode.movesMadeAlready + 1, neighbour, twinNode));
                }
                previousTwin = twin;
            }
        }
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
