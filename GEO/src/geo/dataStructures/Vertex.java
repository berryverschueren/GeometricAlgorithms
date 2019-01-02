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
    private int artFlag, exitFlag;
    
    public Vertex() {}
    public Vertex(Double x, Double y, int exitFlag, int artFlag, String label) {
        super(label);
        this.x = x;
        this.y = y;
        this.exitFlag = exitFlag;
        this.artFlag = artFlag; 
    }
    
    public Double getX() {
        return this.x;
    }
    
    public Double getY() {
        return this.y;
    }
    
    public int getExitFlag() {
        return this.exitFlag;
    }
    
    public int getArtFlag() {
        return this.artFlag;
    }
    
    public void setX(Double x) {
        this.x = x;
    }
    
    public void setY(Double y) {
        this.y = y;
    }
   
    public void setExitFlag() {
        this.exitFlag = exitFlag;
    }
    
    public void setArtFlag() {
        this.artFlag = artFlag;
    }
    
}
