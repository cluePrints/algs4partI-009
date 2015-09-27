import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        if (x < 0 || x > 32767) {
            throw new IllegalArgumentException();
        }        
        if (y < 0 || y > 32767) {
            throw new IllegalArgumentException();
        }
        this.x = x;
        this.y = y;
    }

    public void draw() {
    } // draws this point

    public void drawTo(Point that) {
    } // draws the line segment from this point to that point

    public String toString() {
        return "(" + x + ", " + y + ")";
    } // string representation

    public int compareTo(Point that) {
        if (that == null) {
            throw new NullPointerException();
        }
        
        if (this.y < that.y) {
            return -1;
        } else if (this.y > that.y) {
            return 1;
        }
        
        if (this.x < that.x) {
            return -1;
        } else if (this.x > that.x) {
            return 1;
        }

        return 0;
    }

    public double slopeTo(Point that) {
        if (that == null) {
            throw new NullPointerException();
        }        
        
        double vDist = this.y - that.y;
        double hDist = this.x - that.x;        
        if (vDist == 0 && hDist == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (hDist == 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (vDist == 0) {
            return 0;
        }
        
        return vDist / hDist;
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeComparator(this);
    }

    private static class SlopeComparator implements Comparator<Point> {
        private final Point o0;
        
        public SlopeComparator(Point o0) {
            super();
            this.o0 = o0;
        }

        public int compare(Point o1, Point o2) {
            double slope1 = o1.slopeTo(o0);
            double slope2 = o2.slopeTo(o0);
            if (slope1 == Double.POSITIVE_INFINITY && slope2 == Double.POSITIVE_INFINITY) {
                return 0;
            }
            
            if (slope1 == Double.NEGATIVE_INFINITY && slope2 == Double.NEGATIVE_INFINITY) {
                return 0;
            }
            
            double slopeDiff = slope1 - slope2;
            if (slopeDiff == 0) {
                return 0;
            } else if (slopeDiff > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}