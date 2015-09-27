import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkNotNull(points);

        List<LineSegment> segments = bruteSearch(points);

        lineSegments = segments.toArray(new LineSegment[0]);
    }

    private List<LineSegment> bruteSearch(Point[] points) {
        List<LineSegment> segments = new ArrayList<LineSegment>();

        for (int i0 = 0; i0 < points.length; i0++) {
            Point origin = points[i0];
            for (int i1 = i0 + 1; i1 < points.length; i1++) {
                Point p1 = points[i1];
                double slope1 = origin.slopeTo(p1);
                checkNoDupPoints(slope1);
                for (int i2 = i1 + 1; i2 < points.length; i2++) {
                    Point p2 = points[i2];
                    double slope2 = origin.slopeTo(p2);
                    checkNoDupPoints(slope2);
                    if (slope1 != slope2)
                        continue;
                    
                    for (int i3 = i2 + 1; i3 < points.length; i3++) {
                        Point p3 = points[i3];
                        double slope3 = origin.slopeTo(p3);
                        checkNoDupPoints(slope3);
                        if (slope2 != slope3)
                            continue;
                                               
                        Point[] matchingPoints = new Point[] { origin, p1, p2, p3 };
                        Arrays.sort(matchingPoints);
                        segments.add(new LineSegment(matchingPoints[0], matchingPoints[3]));
                    }
                }
            }
        }
        return segments;
    }

    private void checkNoDupPoints(double slope1) {
        if (slope1 == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotNull(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException();
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }
}