/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import geo.dataStructures.PathGuard;
import geo.dataStructures.Guard;
import java.util.Locale;

/**
 *
 * @author carina
 */
public class ReadInputGuardSpecification {
    public static int numOfGuards;
    public static double x; //x coordinate
    public static double y; //y coordinate
    public static double timestamp;
    public static int observing; // 0 if guard is not observing and 1 if guard is
    public static double initX;
    public static double initY;
                
    //public static List<PathGuard> path = new ArrayList<PathGuard>();
    public static List<Guard> guards = new ArrayList<Guard>();
    
    public static List<Guard> ReadInputGuardSpecification(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);
            
            input.useDelimiter(",\\s*");
            input.useLocale(Locale.US);
            
            /** line 1 */
            numOfGuards = input.nextInt();
            
            /** line rest */
            for (int i = 0; i < numOfGuards; i++) {
                int verticesGuard = input.nextInt();
                List<PathGuard> path = new ArrayList<PathGuard>();
                //initX = input.nextDouble();
                //initY = input.nextDouble();
                //timestamp = input.nextDouble();
                //observing = input.nextInt();  
                //PathGuard step = new PathGuard(initX, initY, timestamp, observing);
                //path.add(step);
                
                for (int j = 0; j < verticesGuard+1; j++) {
                    x = input.nextDouble();
                    y = input.nextDouble();
                    timestamp = input.nextDouble();
                    observing = input.nextInt();
                    PathGuard step = new PathGuard(x, y, timestamp, observing);
                    path.add(step);
                }
                Guard guard = new Guard(initX, initY, path);
                guards.add(guard);
            }
            
            // end up with list of guards with their paths
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guards;
    }
    //    public static void main(String[] args) {
    //    List<Guard> guards = new ArrayList<Guard>();
    //    guards = ReadInputGuardSpecification("Guard1.txt");
    //    WriteInputGuardSpecification.WriteInputGuardSpecification(guards);
    //}

}
