/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.Comparator;

/**
 *
 * @author Berry-PC
 */
public class TrapezoidComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Trapezoid t1 = (Trapezoid) o1;
        Trapezoid t2 = (Trapezoid) o2;
        
        double x1 = t1.getSpecificVertex(0).getX(); 
        double x2 = t2.getSpecificVertex(0).getX();
        
        if (x1 == x2) {
            return 0;
        } 
        else if (x1 > x2) {
            return 1;
        }
        else {
            return -1;
        }
    }
    
}
