/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.Comparator;

/**
 *
 * @author Berry
 */
public class TimePointComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        TimePoint t1 = (TimePoint) o1;
        TimePoint t2 = (TimePoint) o2;
        
        double x1 = t1.getStart(); 
        double x2 = t2.getStart(); 
        
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
