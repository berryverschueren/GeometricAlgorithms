package geo;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import geo.dataStructures.Guard;
import geo.dataStructures.PathGuard;
/**
 *
 * @author carina
 */
public class WriteInputGuardSpecification {
    String fileName = "temp.txt";
    
    public WriteInputGuardSpecification(int numOfGuards, List<Guard> guards) {
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(numOfGuards);
            bufferedWriter.newLine();
            
            for (Guard guard : guards) {
                List<PathGuard> path = new ArrayList<PathGuard>();
                path = guard.getPath();
                int lengthPath = path.size();
                bufferedWriter.write(lengthPath);
                bufferedWriter.newLine();
                
                for (PathGuard pathGuard : path) {
                    double x = pathGuard.getX();
                    double y = pathGuard.getY();
                    double timestamp = pathGuard.getTimestamp();
                    int observing = pathGuard.getObserving();
                    bufferedWriter.write(x + ", " + y + ", " + timestamp + ", " + observing + ", ");
                    bufferedWriter.newLine();
                }
            }
            
            bufferedWriter.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    } 
}

