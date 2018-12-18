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
public class Trapezoid {
    private String label;
    private Vertex v1, v2, v3, v4;
    private Edge e1, e2, e3, e4;

    public Trapezoid() {
    }

    public Trapezoid(String label, Vertex v1, Vertex v2, Vertex v3, Vertex v4, Edge e1, Edge e2, Edge e3, Edge e4) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
    }

    public String getLabel() {
        return label;
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

    public Vertex getV4() {
        return v4;
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

    public Edge getE4() {
        return e4;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setV4(Vertex v4) {
        this.v4 = v4;
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

    public void setE4(Edge e4) {
        this.e4 = e4;
    }
    
}
