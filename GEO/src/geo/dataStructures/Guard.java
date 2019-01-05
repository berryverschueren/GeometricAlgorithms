/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.List;

/**
 *
 * @author Berry-PC
 */
public class Guard {
    private String label;
    private int x, y;
    private List<PathGuard> path;
    private double initX, initY;

    public Guard() {
    }

    public Guard(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }
    
    public Guard(double initX, double initY, List<PathGuard> path) {
        this.path = path;
        this.initX = initX; //starting x
        this.initY = initY; //starting y
    }

    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public List<PathGuard> getPath() {
        return path;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setPath() {
        this.path = path; 
    }
}
