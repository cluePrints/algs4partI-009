import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
        // System.out.printf(str, params);
    }

    private void println(String str) {
        // System.out.println(str);
    }

    private Collection<LineSegment> doStuff(Point[] pointsParam) {
        List<SegmentCandidate> segmentCandidates = new ArrayList<SegmentCandidate>();

        ArrayList<Point> points = new ArrayList<Point>(Arrays.asList(pointsParam));
        while (!points.isEmpty()) {
            Point origin = points.remove(points.size() - 1);
            println("Current: " + origin);

            Collections.sort(points, origin.slopeOrder());
            checkNoDups(points, origin);
            findCollinears(origin, segmentCandidates, points);
        }

        List<LineSegment> results = removeDupSegments(segmentCandidates);

        return results;
    }

    private void checkNoDups(ArrayList<Point> points, Point origin) {
        // negative infinity sorts first so if there are any dups, we'll have
        // them first
        if (!points.isEmpty() && origin.slopeTo(points.get(0)) == Double.NEGATIVE_INFINITY) {
            throw new IllegalArgumentException();
        }
    }

    private List<LineSegment> removeDupSegments(List<SegmentCandidate> segmentCandidates) {
        List<LineSegment> results = new ArrayList<>();
        
        List<List<SegmentCandidate>> squashedCandidates = new ArrayList<List<SegmentCandidate>>();
        for (int i=0; i<segmentCandidates.size(); i++) {
            SegmentCandidate candidate = segmentCandidates.get(i);
        
            int idx = binarySearch(squashedCandidates, candidate.slope);
            List<SegmentCandidate> list;
            if (idx < 0) {
                list = new ArrayList<>();
                squashedCandidates.add(list);
            } else {
                list = squashedCandidates.get(idx);
            }
            
            boolean dup = false;
            for (int j=0; j<list.size(); j++) {
                double slopeBetween = list.get(j).min.slopeTo(candidate.max);
                if (Double.compare(slopeBetween, candidate.slope) == 0) {
                    dup = true;
                    break;
                }
            }
            
            if (!dup) {
                list.add(candidate);
                results.add(new LineSegment(candidate.min, candidate.max));
            }
        }       

        return results;
    }

    private int binarySearch(List<List<SegmentCandidate>> squashedCandidates, double slope) {
        int low = 0;
        int high = squashedCandidates.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            List<SegmentCandidate> midVal = squashedCandidates.get(mid);
            
            double iSlope = midVal.get(0).slope;
            int cmp = Double.compare(iSlope, slope);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    private void findCollinears(Point origin, List<SegmentCandidate> segmentCandidates, ArrayList<Point> points) {
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
            int length = startPos - slopeStartIdx + 1;
            if (length < 3) {
                println("...not enough points");
                slopeStartIdx = i;
                continue;
            }

            printf(" Identifying what to put on a segment [%s]..[%s] %s\n", slopeStartIdx, i, points);

            Point min = origin;
            Point max = origin;
            for (int j = startPos; j >= slopeStartIdx; j--) {
                Point p = points.get(j);

                if (min.compareTo(p) > 0) {
                    min = p;
                }
                if (max.compareTo(p) < 0) {
                    max = p;
                }
            }

            SegmentCandidate c = new SegmentCandidate(min, max, length);
            printf(" Adding candidate %s\n", c);
            segmentCandidates.add(c);

            slopeStartIdx = i;
        }
    }

    private static class SegmentCandidate implements Comparable<SegmentCandidate> {
        final Point min;
        final Point max;
        final int length;
        final double slope;

        public SegmentCandidate(Point min, Point max, int lenth) {
            super();
            this.min = min;
            this.max = max;
            this.length = lenth;
            this.slope = min.slopeTo(max);
        }

        @Override
        public int compareTo(SegmentCandidate o) {
            double slope1 = slope();
            double slope2 = o.slope();

            int slopeComparison = Double.compare(slope1, slope2);
            if (slopeComparison != 0) {
                return slopeComparison;
            }            
            
            return o.length - length;
        }

        double slope() {
            return slope;
        }

        @Override
        public String toString() {
            return "SC[min=" + min + ", max=" + max + ", length=" + length + ", slope=" + slope + "]";
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