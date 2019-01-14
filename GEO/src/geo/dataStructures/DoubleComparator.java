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
public class DoubleComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Double t1 = (Double) o1;
        Double t2 = (Double) o2;
        
        if (t1 == t2) {
            return 0;
        } 
        else if (t1 > t2) {
            return 1;
        }
        else {
            return -1;
        }
    }
    
}
