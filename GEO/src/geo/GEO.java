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
        
        // Define labels as in slideset 04 page 53(of 99)
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
        
        // Remove leafs
        tss.RemoveNode(l3, l5);
        tss.RemoveNode(l2, l7);
        
        // Add nodes, some specific in case labels used multiple times
        // parent of toLabel is deciding in which node to pick 
        tss.AddNode(l3, l8, 0);
        tss.AddNode(l8, l10, 0);
        tss.AddNode(l8, l11, 1);
        tss.AddNode(l11, l13, 0);
        tss.AddNode(l11, l12, 1);
        tss.AddNode(l2, l9, 1);        
        tss.AddNode(l9, l11, 0);
        tss.AddNode(l9, l15, 1);
        tss.AddNode(l9, l11, l14, 1);
        
        // Link existing nodes --> find p2-s2-t7 and link as child of q2-s2-(t7)
        tss.LinkNodes(l9, l11, l9, l11, 0);
        tss.AddNode(l9, l11, l13, 0);
        
        tss.Print();
    }
    
}
