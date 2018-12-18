/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

import geo.dataStructures.TrapezoidalSearchStructure;

/**
 *
 * @author Berry-PC
 */
public class GEO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TestTrapezoidalSearchStructure();
    }
    
    public static void TestTrapezoidalSearchStructure() {
        // Init structure and root
        TrapezoidalSearchStructure tss = new TrapezoidalSearchStructure();
        String rootLabel = "R";
        tss.Initialize(rootLabel);
        
        // Define labels
        String l1 = "p1", l2 = "q1", l3 = "s1", l4 = "t1", l5 = "t2", l6 = "t3"
                , l7 = "t4", l8 = "p2", l9 = "q2", l10 = "t5", l11 = "s2", l12 = "t6"
                , l13 = "t7", l14 = "t8", l15 = "t9";
        
        // Add nodes
        tss.Initialize(l1);
        tss.AddNode(l1, l4, 0);
        tss.AddNode(l1, l2, 1);
        tss.AddNode(l2, l3, 0);
        tss.AddNode(l2, l7, 1);
        tss.AddNode(l3, l5, 0);
        tss.AddNode(l3, l6, 1);
        
        System.out.println("Done" + tss);
    }
    
}
