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
public class Robber {
    private String label;
    private int x, y;
    private double initX, initY;
    private List<PathRobber> path;
    

    public Robber() {
    }

    public Robber(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public Robber(double initX, double initY, List<PathRobber> path) {
        this.path = path;
        this.initX = initX; //starting x
        this.initY = initY; //starting y
    }

    public double getInitX() {
        return initX;
    }

    public double getInitY() {
        return initY;
    }

    public List<PathRobber> getPath() {
        return path;
    }

    public void setInitX(double initX) {
        this.initX = initX;
    }

    public void setInitY(double initY) {
        this.initY = initY;
    }

    public void setPath(List<PathRobber> path) {
        this.path = path;
    }

}
