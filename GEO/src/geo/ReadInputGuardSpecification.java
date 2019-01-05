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

/**
 *
 * @author carina
 */
public class ReadInputGuardSpecification {
    public int numOfGuards;
    public double x; //x coordinate
    public double y; //y coordinate
    public double timestamp;
    public int observing; // 0 if guard is not observing and 1 if guard is
    public double initX;
    public double initY;
                
    public List<PathGuard> path = new ArrayList<PathGuard>();
    public List<Guard> guards = new ArrayList<Guard>();
    
    public void ReadInputGuardSpecification(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);
            
            input.useDelimiter(",\\s*");
            
            /** line 1 */
            numOfGuards = input.nextInt();
            
            /** line rest */
            for (int i = 0; i < numOfGuards; i++) {
                int verticesGuard = input.nextInt();
                initX = input.nextDouble();
                initY = input.nextDouble();
                timestamp = input.nextDouble();
                observing = input.nextInt();  
                PathGuard step = new PathGuard(initX, initY, timestamp, observing);
                path.add(step);
                
                for (int j = 0; j < verticesGuard - 1; j++) {
                    x = input.nextDouble();
                    y = input.nextDouble();
                    timestamp = input.nextDouble();
                    observing = input.nextInt();
                    step = new PathGuard(x, y, timestamp, observing);
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
        
    }
}
