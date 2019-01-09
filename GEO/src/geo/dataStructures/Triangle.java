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
public class Triangle extends TrapezoidShape {
    // Coordinates
    private Vertex v1, v2, v3;
    private Edge e1, e2, e3;
    
    public Triangle() {
    }

    public Triangle(String label, Vertex v1, Vertex v2, Vertex v3, Edge e1, Edge e2, Edge e3) {
        super(label);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public Vertex getV3() {
        return v3;
    }
    
    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }

    public Edge getE3() {
        return e3;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public void setV3(Vertex v3) {
        this.v3 = v3;
    }

    public void setE1(Edge e1) {
        this.e1 = e1;
    }

    public void setE2(Edge e2) {
        this.e2 = e2;
    }

    public void setE3(Edge e3) {
        this.e3 = e3;
    }
    
    @Override
    public void print() {
        System.out.println("Triangle " + this.getLabel() + ": ((" + this.v1.getX() + ", " + this.v1.getY() + "), (" + this.v2.getX() + ", " + this.v2.getY() + "), (" + 
                this.v3.getX() + ", " + this.v3.getY() + "))");
    }
    
}
