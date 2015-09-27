import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        checkNotNull(points);

        Collection<LineSegment> segments = doStuff(points);

        lineSegments = segments.toArray(new LineSegment[0]);
    }
    
    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    private void printf(String str, Object... params) {
//        System.out.printf(str, params);
    }

    private void println(String str) {
  //      System.out.println(str);
    }

    private Collection<LineSegment> doStuff(Point[] pointsParam) {
        List<LineSegment> segments = new ArrayList<LineSegment>();
        List<Point[]> segmentEnds = new ArrayList<Point[]>();
        
        ArrayList<Point> points = new ArrayList<Point>(Arrays.asList(pointsParam));
        while (!points.isEmpty()) {
            Point origin = points.remove(points.size() - 1);
            println("Current: " + origin);

            Collections.sort(points, origin.slopeOrder());

            double slope = 0;
            int slopeStartIdx = 0;
            for (int i = 0; i < points.size(); i++) {
                Point iterated = points.get(i);
                printf(" Looking at %s\n", iterated);
                double iSlope = origin.slopeTo(iterated);
                checkNotDup(iSlope);

                if (points.size() < 3) {
                    // we're only here for dup checks
                    break;
                }

                boolean weHaveJustStarted = i == 0;
                if (weHaveJustStarted) {
                    slope = iSlope;
                }

                int startPos = i - 1;
                if (slope == iSlope) {
                    if (i == points.size() - 1) {
                        startPos = i;
                        printf(" reached the end of the array, so let's see what do we got");
                    } else {
                        printf("  Slope #%s is the same (%s), next\n", i, slope);
                        continue;
                    }
                }

                printf(" Slope changed (%s), let's see what do we have", iSlope);
                slope = iSlope;

                // if we have 3+ in row --> add that
                if (startPos - slopeStartIdx + 1 < 3) {
                    println("...not enough points");
                    slopeStartIdx = i;
                    continue;
                }

                printf(" Identifying what to put on a segment [%s]..[%s] %s\n", slopeStartIdx, i, points);

                Point min = origin;
                Point max = origin;
                for (int j = startPos; j >= slopeStartIdx; j--) {
                    Point p = points.get(j);
                    checkNoDups(points, slopeStartIdx, j, p);
                    
                    if (min.compareTo(p) > 0) {
                        min = p;
                    }
                    if (max.compareTo(p) < 0) {
                        max = p;
                    }
                }

                boolean dupSegment = dupSegment(segmentEnds, min, max);
                if (!dupSegment) {
                    segments.add(new LineSegment(min, max));
                    segmentEnds.add(new Point[] { min, max });
                }

                // TODO: seems this piece is not needed
                slope = iSlope;
                slopeStartIdx = i;
            }
        }

        return segments;
    }

    private boolean dupSegment(List<Point[]> segmentEnds, Point min, Point max) {
        for (Point[] ends : segmentEnds) {
            double iteratedSlope = ends[0].slopeTo(ends[1]);
            double originSlope = min.slopeTo(max);
            double betweenFragmentsSlope = ends[0].slopeTo(max);
            if (iteratedSlope == originSlope && betweenFragmentsSlope == iteratedSlope) {
                printf("The segmenet we're looking at (%s -> %s) looks like dup of (%s -> %s) - not adding",
                        min, max, ends[0], ends[1]);
                return true;
            }
        }
        
        return false;
    }

    private void checkNoDups(ArrayList<Point> points, int slopeStartIdx, int j, Point p1) {
        for (int k = j - 1; k >= slopeStartIdx; k--) {
            Point pk = points.get(k);
            if (p1.slopeTo(pk) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkNotDup(double iSlope) {
        if (iSlope == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException(); // dup points
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
}