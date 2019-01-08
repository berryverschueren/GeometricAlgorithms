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
    private int color;
    
    public Vertex() {}
    
    public Vertex(Double x, Double y, String label) {
        super(label);
        this.x = x;
        this.y = y;
        this.color = 0;
    }
    
    public Vertex(Double x, Double y, int artFlag, String label) {
        super(label);
        this.x = x;
        this.y = y;
        this.artFlag = artFlag;
        this.color = 0;
    }
    
    public Vertex(Double x, Double y, int artFlag, int exitFlag, String label) {
        super(label);
        this.x = x;
        this.y = y;
        this.exitFlag = exitFlag;
        this.artFlag = artFlag; 
        this.color = 0;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
    //1 = true
    public void setExitFlag(int exitFlag) {
        this.exitFlag = exitFlag;
    }
    //1 = true
    public void setArtFlag(int artFlag) {
        this.artFlag = artFlag;
    }
    
}
