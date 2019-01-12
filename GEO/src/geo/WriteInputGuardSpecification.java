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
    public static boolean print = false;
    public static void WriteInputGuardSpecification(List<Guard> guards) {
        
        try {
            String fileName = "tempGuard.txt";
            FileWriter fileWriter = new FileWriter(fileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            int numOfGuards = guards.size();
            if (print) {
                bufferedWriter.write(numOfGuards);
            } else {
                bufferedWriter.write(numOfGuards + ", ");
            }
            bufferedWriter.newLine();
            
            for (Guard guard : guards) {
                List<PathGuard> path = new ArrayList<PathGuard>();
                path = guard.getPath();
                int lengthPath = path.size();
                if (print) {
                    bufferedWriter.write(lengthPath);
                } else {
                    bufferedWriter.write(lengthPath + ", ");
                }
                bufferedWriter.newLine();
                
                for (PathGuard pathGuard : path) {
                    double x = pathGuard.getX();
                    double y = pathGuard.getY();
                    double timestamp = pathGuard.getTimestamp();
                    int observing = pathGuard.getObserving();
                    if(print){
                        bufferedWriter.write(x + ", " + y + ", " + timestamp + ", " + observing);
                    } else {
                        bufferedWriter.write(x + ", " + y + ", " + timestamp + ", " + observing + ", ");
                    }
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

