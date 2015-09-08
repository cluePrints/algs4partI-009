import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;


public class PercolationStats {
   private int T;
   private double[] percolatedAt;
   public PercolationStats(int N, int T) {
       if (N <= 0)
           throw new IllegalArgumentException();
       if (T <= 0)
           throw new IllegalArgumentException();
       
       this.T = T;
       this.percolatedAt = new double[T];
       for (int expNumber = 0; expNumber < T; expNumber++) {
           Percolation percolation = new Percolation(N);
           
           int openSites = 0;
           while (!percolation.percolates()) {
               int i = StdRandom.uniform(1, N+1);
               int j = StdRandom.uniform(1, N+1);
               if (percolation.isOpen(i, j))
                   continue; 
               
               percolation.open(i, j);
               openSites++;
           }
           
           percolatedAt[expNumber] = ((double) openSites) / (double) (N*N);
       }
   }
   
   public double mean() {
       return StdStats.mean(percolatedAt);
   }
   
   public double stddev() {
       return StdStats.stddev(percolatedAt);
   }
   
   public double confidenceLo() {
       return mean() - 1.96*stddev()/Math.sqrt(T);
   }
   
   public double confidenceHi() {
       return mean() + 1.96*stddev()/Math.sqrt(T);
   }

   public static void main(String[] args) {
       int n = Integer.parseInt(args[0]);
       int t = Integer.parseInt(args[1]);
       Stopwatch stopWatch = new Stopwatch();
       PercolationStats stats = new PercolationStats(n, t);
       System.out.println("Elapsed: " +        stopWatch.elapsedTime());
       System.out.println("Mean: " + stats.mean());
       System.out.println("Std: " + stats.stddev());
       System.out.printf("Confint: %s, %s\n", stats.confidenceLo(), stats.confidenceHi());
   }
}