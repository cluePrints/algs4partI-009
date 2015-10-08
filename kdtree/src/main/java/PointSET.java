import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Set;

public class PointSET {
    private TreeSet<Point2D> points = new TreeSet<Point2D>();

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }

        Set<Point2D> range = new TreeSet<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                range.add(point);
            }
        }

        return range;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (points.isEmpty())
            return null;

        Point2D nearest = points.first();
        double nearestDist = nearest.distanceTo(p);
        for (Point2D point : points) {
            double currDist = point.distanceTo(p);
            if (currDist < nearestDist) {
                nearest = point;
                nearestDist = currDist;
            }
        }

        return nearest;
    }
}