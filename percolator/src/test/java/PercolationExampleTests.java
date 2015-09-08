

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PercolationExampleTests {
    private final String fileName;
    private final boolean expectedResult;
    private static final File fixtureDir = new File("src/test/resources/");
    
    public PercolationExampleTests(String fileName) {
        super();
        this.fileName = fileName;
        this.expectedResult = !this.fileName.contains("no");
    }
    
    @Parameters(name="{index} {0}")
    public static List<Object[]> params() {
        List<Object[]> params = new ArrayList<Object[]>();
        File[] files = fixtureDir.listFiles(new FilenameFilter() {            
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        for (File file : files) {
            params.add(new Object[] { file.getName() } );
        }
        return params;
    }
    
    @Test
    public void newlyConnectedSiteShouldBeConnected() throws Exception {
        File file = new File(fixtureDir, fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String sizeStr = reader.readLine();
        int size = Integer.parseInt(sizeStr);
        
        Percolation percolation = new Percolation(size);
        String readStr = reader.readLine();
        int line=2;
        while (readStr != null) {
            readStr = readStr.trim();
            if (readStr.length() != 0) {
                try {
                    String[] coords = readStr.split("\\s+");
                    int i = Integer.parseInt(coords[0]);
                    int j = Integer.parseInt(coords[1]);
                    percolation.open(i, j);
                } catch (Exception ex) {
                    throw new RuntimeException("Problem with line " + line, ex);
                }
            }
            readStr = reader.readLine();
            line++;
        }
        
        Assert.assertEquals(expectedResult, percolation.percolates());
    }
}
