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
    
}
