/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

/**
 *
 * @author Berry
 */
public class TimePoint {
    private double start, end, diff;

    public TimePoint(double start, double end) {
        this.start = start;
        this.end = end;
        this.diff = end - start;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double getDiff() {
        return diff;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }
    
    
}
