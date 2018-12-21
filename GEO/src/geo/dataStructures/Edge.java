/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Berry-PC
 */
public class Edge {
    private String label;
    private Vertex v1, v2;

    public Edge() {
    }

    public Edge(String label, Vertex v1, Vertex v2) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }
    
    // 0 = left 
    // 1 = right
    public Vertex getSpecificVertex(int point) {        
        int firstx = Integer.MAX_VALUE, nextx = Integer.MAX_VALUE, firsty = Integer.MAX_VALUE, nexty = Integer.MAX_VALUE;
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(this.v1);
        vertices.add(this.v2);
        switch (point) {
            case 0:
                return this.v1.getX() < this.v2.getX() ? this.v1 : this.v2;
            case 1:
                return this.v1.getX() > this.v2.getX() ? this.v1 : this.v2;
            default:
                return null;
        }
    }
}
