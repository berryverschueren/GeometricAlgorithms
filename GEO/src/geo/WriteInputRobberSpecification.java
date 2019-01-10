package geo;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import geo.dataStructures.Robber;
import geo.dataStructures.PathRobber;
/**
 *
 * @author carina
 */
public class WriteInputRobberSpecification {
    
    public static void WriteInputRobberSpecification(Robber robber) {
        
        try {
            String fileName = "temp.txt";
            FileWriter fileWriter = new FileWriter(fileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            List<PathRobber> path = new ArrayList<PathRobber>();
            path = robber.getPath();
            int lengthPath = path.size();
            bufferedWriter.write(lengthPath + ", ");
            bufferedWriter.newLine();
               
            for (PathRobber pathRobber : path) {
                double x = pathRobber.getX();
                double y = pathRobber.getY();
                double timestamp = pathRobber.getTimestamp();
                bufferedWriter.write(x + ", " + y + ", " + timestamp + ", ");
                bufferedWriter.newLine();
                }
            
            
            bufferedWriter.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    } 
}

