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
public class Vertex extends TrapezoidShape {
    private Double x, y;
    
    public Vertex() {}
    public Vertex(Double x, Double y, String label) {
        super(label);
        this.x = x;
        this.y = y;
    }
    
    public Double getX() {
        return this.x;
    }
    
    public Double getY() {
        return this.y;
    }
    
    public void setX(Double x) {
        this.x = x;
    }
    
    public void setY(Double y) {
        this.y = y;
    }
}
