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
public class Polygon {
    private String label;
    private List<Edge> edges;
    private List<Vertex> vertices;

    public Polygon() {
    }

    public Polygon(String label, List<Edge> edges, List<Vertex> vertices) {
        this.label = label;
        this.edges = edges;
        this.vertices = vertices;
    }

    public String getLabel() {
        return label;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }
    
}
