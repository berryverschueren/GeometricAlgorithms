/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

/**
 *
 * @author Berry-PC
 */
public class Vertex {
    private String label;
    private int x, y;
    
    public Vertex() {}
    public Vertex(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
}
