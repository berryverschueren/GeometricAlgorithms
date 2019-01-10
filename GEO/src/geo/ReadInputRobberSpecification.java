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
    public static int numOfVertices;
    public static double x; //x coordinate
    public static double y; //y coordinate
    public static double timestamp;
    public static double initX;
    public static double initY;
                
    public static List<PathRobber> path = new ArrayList<PathRobber>();
    public static Robber robber = new Robber();
    
    public static Robber ReadInputRobberSpecification(String filename) {
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
            robber = new Robber(initX, initY, path);
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return robber;    
    }
    //    public static void main(String[] args) {
    //    Robber robber = new Robber();
    //    robber = ReadInputRobberSpecification("Robber1.txt");
    //    WriteInputRobberSpecification.WriteInputRobberSpecification(robber);
    //}

}
