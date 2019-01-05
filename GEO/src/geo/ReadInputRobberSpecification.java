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
import geo.dataStructures.PathRobber;
import geo.dataStructures.Robber;

/**
 *
 * @author carina
 */
public class ReadInputRobberSpecification {
    public int numOfVertices;
    public double x; //x coordinate
    public double y; //y coordinate
    public double timestamp;
    public double initX;
    public double initY;
                
    public List<PathRobber> path = new ArrayList<PathRobber>();
    public Robber robber = new Robber();
    
    public void ReadInputRobberSpecification(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);
            
            input.useDelimiter(",\\s*");
            
            /** line 1 */
            numOfVertices = input.nextInt();
            
            /** line rest */
            initX = input.nextDouble();
            initY = input.nextDouble();
            timestamp = input.nextDouble();
            PathRobber step = new PathRobber(initX, initY, timestamp);
            path.add(step);
            
            for (int i = 0; i < numOfVertices - 1; i++) {
                x = input.nextDouble();
                y = input.nextDouble();
                timestamp = input.nextDouble();
                step = new PathRobber(x, y, timestamp);
                path.add(step);
            }
            Robber robber = new Robber(initX, initY, path);
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
