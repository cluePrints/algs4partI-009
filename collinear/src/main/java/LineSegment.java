public class LineSegment {
   private Point p;
   private Point q;
   public LineSegment(Point p, Point q) {
       this.p = p;
       this.q = q;
   }
   
   public void draw() {
       
   }
   
   public String toString() {
       return p + " -> " + q;
   }
}