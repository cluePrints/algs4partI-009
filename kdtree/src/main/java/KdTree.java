import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KdTree {
    private static final RectHV UNIVERSE = new RectHV(-1, -1, Double.MAX_VALUE, Double.MAX_VALUE);
    private int size;
    private KdNode root;
    private boolean debugEnabled = false;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        printf("Inserting\n");
        if (p == null) {
            throw new NullPointerException();
        }

        KdNode insertedNode = new KdNode(p);
        if (root == null) {
            size++;
            root = insertedNode;
            return;
        }
        
        searchForParentAndPossiblyInsert(p, insertedNode);
        printf("---\n");
    }
    
    private KdNode searchForParentAndPossiblyInsert(Point2D p, KdNode insertedNode) {
        KdNode currentPoint = root;
        int level = 0;
        while (true) {
            level++;
            double diff = (level % 2 == 1) 
                    ? currentPoint.point.x() - p.x() 
                    : currentPoint.point.y() - p.y();
            printf("Examining %s(current) vs %s(inserted). Mode is %s\n", currentPoint, insertedNode, (level % 2 == 1 ? "|" : "-"));
            boolean weGoLeft = diff > 0;
            if (currentPoint.point.equals(p)) {
                printf("(!) Looks like we've found a match %s to %s\n", currentPoint, p);
                return currentPoint;
            }
            
            if (weGoLeft) {
                if (currentPoint.left != null) {
                    printf("-> we went left\n");
                    currentPoint = currentPoint.left;
                } else {
                    currentPoint.left = insertedNode;
                    printf("-> nothing to the left, we are done\n");
                    size++;
                    return currentPoint;
                }
            } else {
                if (currentPoint.right != null) {
                    printf("-> we went right\n");
                    currentPoint = currentPoint.right;
                } else {
                    currentPoint.right = insertedNode;
                    printf("-> nothing to the right, we are done\n");
                    size++;
                    return currentPoint;
                }
            }
        }
    }

    public boolean contains(Point2D p) {
        printf("Containsing...\n");
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (root == null)
            return false;

        KdNode parent = searchForParentAndPossiblyInsert(p, null);
        boolean result = parent != null && parent.point.distanceTo(p) == 0;
        printf("---\n");
        return result;
    }

    public void draw() {
        throw new IllegalStateException();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        
        if (root == null) {
            return new ArrayList<Point2D>(0);
        }
        
        PrunningRule pruningRule = new IntersectsRect(rect);
        Collection<Point2D> results = new ArrayList<Point2D>();
        
        Queue<KdNodeWithBox> pointsToFollow = new LinkedList<KdNodeWithBox>();
        pointsToFollow.add(new KdNodeWithBox(root, UNIVERSE, 1));
        while (!pointsToFollow.isEmpty()) {
            KdNodeWithBox currentItem = pointsToFollow.remove();
            if (!pruningRule.allow(currentItem.rect)) {
                printf("%s pruned.\n", currentItem.node.point);
                continue;
            }
            
            if (rect.contains(currentItem.node.point)) {
                results.add(currentItem.node.point);
                printf("%s belongs to %s\n", currentItem.node.point, rect);
            }
            
            if (currentItem.level % 2 == 1) {
                List<KdNodeWithBox> newPts = currentItem.splitVertically(pruningRule);
                printf("%s --> %s\n", currentItem, newPts);
                pointsToFollow.addAll(newPts);
            } else {
                List<KdNodeWithBox> newPts = currentItem.splitHorizontally(pruningRule);
                printf("%s --> %s\n", currentItem, newPts);
                pointsToFollow.addAll(newPts);
            }
        }
        
        return results;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }        
        
        printf("Looking for nearest to %s.\n", p);
        if (root == null) {
            return null;
        }
        
        Point2D result = root.point;
        double bestDistance = result.distanceTo(p);
        PrunningRule pruningRule = new Closer(bestDistance, p);
        
        Queue<KdNodeWithBox> pointsToFollow = new LinkedList<KdNodeWithBox>();
        pointsToFollow.add(new KdNodeWithBox(root, UNIVERSE, 1));
        while (!pointsToFollow.isEmpty()) {
            KdNodeWithBox currentItem = pointsToFollow.remove();
            if (!pruningRule.allow(currentItem.rect)) {
                printf("%s pruned.\n", currentItem.node.point);
                continue;
            }
            
            bestDistance = result.distanceTo(p);
            double newDist = currentItem.node.point.distanceTo(p);
            pruningRule = new Closer(bestDistance, p);
            if (bestDistance > newDist) {
                result = currentItem.node.point;
                bestDistance = newDist;
                printf("now nearest is %s (%s)\n", result, bestDistance);
            }
            
            if (currentItem.level % 2 == 1) {
                List<KdNodeWithBox> newPts = currentItem.splitVertically(pruningRule);
                printf("%s --> %s\n", currentItem, newPts);
                pointsToFollow.addAll(newPts);
            } else {
                List<KdNodeWithBox> newPts = currentItem.splitHorizontally(pruningRule);
                printf("%s --> %s\n", currentItem, newPts);
                pointsToFollow.addAll(newPts);
            }
        }
        
        return result;
    }
    
    private void printf(String format, Object...args) {
        if (!debugEnabled)
            return;
        
        StdOut.printf(format, args);
    }
    
    
    private class KdNodeWithBox {
        private KdNode node;
        private RectHV rect;
        private int level;
        
        public KdNodeWithBox(KdNode node, RectHV rect, int level) {
            super();
            this.node = node;
            this.rect = rect;
            this.level = level;
            checkX();
            checkY();
        }
        
        public List<KdNodeWithBox> splitVertically(PrunningRule rule) {
            printf("%s is being split vertically by %s\n", rect, node.point.x());
            ArrayList<KdNodeWithBox> result = new ArrayList<KdTree.KdNodeWithBox>(2);
            
            if (node.left != null) {
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(),           rect.ymax());
                if (rule.allow(newRect)) {
                    result.add(new KdNodeWithBox(node.left, newRect, level + 1));
                }
            }
            
            if (node.right != null) {
                RectHV newRect = new RectHV(node.point.x(),           rect.ymin(), rect.xmax(), rect.ymax());
                if (rule.allow(newRect)) {
                    result.add(new KdNodeWithBox(node.right, newRect, level + 1));
                }
            }
            
            return result;
        }

        private double checkX() {
            double x = node.point.x();
            
            if (x < rect.xmin()) {
                throw new IllegalStateException();
            }
            if (x > rect.xmax()) {
                throw new IllegalStateException();
            }
            return x;
        }
        
        public List<KdNodeWithBox> splitHorizontally(PrunningRule rule) {
            printf("%s is being split horizontally by %s\n", rect, node.point.y());
            ArrayList<KdNodeWithBox> result = new ArrayList<KdTree.KdNodeWithBox>(2);
            
            double y = node.point.y();
            
            if (node.left != null) {
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), y);
                if (rule.allow(newRect)) {
                    result.add(new KdNodeWithBox(node.left, newRect, level + 1));
                }
            }
            
            if (node.right != null) {
                RectHV newRect = new RectHV(rect.xmin(), y, rect.xmax(), rect.ymax());
                if (rule.allow(newRect)) {
                    result.add(new KdNodeWithBox(node.right, newRect, level + 1));
                }
            }
            
            return result;
        }

        private double checkY() {
            double y = node.point.y();
            
            if (y > rect.ymax()) {
                throw new IllegalStateException();
            }
            if (y < rect.ymin()) {
                throw new IllegalStateException();
            }
            return y;
        }

        @Override
        public String toString() {
            return ("Box [rect=" + rect 
                    + ", point=" + node.point 
                    + ", l=" + (node.left == null ? "null" : ".") 
                    + ", r=" + (node.right == null ? "null" : ".")  + "]"
                    )
                    .replaceAll(String.valueOf(Double.MIN_VALUE), "min")
                    .replaceAll(String.valueOf(Double.MAX_VALUE), "MAX");
        }
    }
    
    private static interface PrunningRule {
        boolean allow(RectHV newRect);
    }
    
    private static class IntersectsRect implements PrunningRule {
        private final RectHV target;

        private IntersectsRect(RectHV target) {
            super();
            this.target = target;
        }
        
        public final boolean allow(RectHV newRect) {
            return target.intersects(newRect);
        }
    }
    
    private class Closer implements PrunningRule {
        private final double bestDistance;
        private final Point2D targetPoint;
        
        private Closer(double bestDistance, Point2D targetPoint) {
            super();
            this.bestDistance = bestDistance;
            this.targetPoint = targetPoint;
        }

        public final boolean allow(RectHV newRect) {
            double newDist = newRect.distanceTo(targetPoint);
            boolean result = newDist <= bestDistance;
            if (!result) {
                printf("%s was pruned. (%s > %s).\n", newRect, newDist, bestDistance);
            }
            return result;
        }
    }
    
    private static class KdNode {
        private Point2D point;
        private KdNode left;
        private KdNode right;
        public KdNode(Point2D point) {
            super();
            this.point = point;
        }
        
        @Override
        public String toString() {
            return "KdNode [point=" + point + "]";
        }
    }
}