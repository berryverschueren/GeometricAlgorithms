/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

import geo.dataStructures.Edge;
import geo.dataStructures.TrapezoidalMap;
import geo.dataStructures.Vertex;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Berry-PC
 */
public class GEO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestTrapezoidalMapConstruction();
    }
    
    public static void TestTrapezoidalMapConstruction() {
        TrapezoidalMap tm = new TrapezoidalMap();
        
        List<Edge> segments = new ArrayList<>();
        segments.add(new Edge("s1", new Vertex(2.0,3.0,"p1"), new Vertex(6.0,4.0,"q1")));
        
        segments.add(new Edge("s3", new Vertex(3.0,3.0,"p3"), new Vertex(5.0,3.0,"q3")));
        
        segments.add(new Edge("s2", new Vertex(4.0,1.0,"p2"), new Vertex(8.0,2.0,"q2")));
        
        tm.construct(segments);
    }
}
