/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.LinkedList;
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
        edges = new LinkedList<>();
        vertices = new ArrayList<>();
    }
    
    public Polygon(String label) {
        edges = new LinkedList<>();
        vertices = new ArrayList<>();
        this.label = label;
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
    
    
    public Vertex addVertex(Vertex vertex){
        vertices.add(vertex);
        return vertex;
    }
    
    public Vertex addVertex(String label, double x, double y){
        return addVertex(new Vertex(x,y,label));
    }

    public Edge addEdge(Edge edge){
        edges.add(edge);
        return edge;
    }
    
    public Edge addEdge(String label, Vertex vertex1, Vertex vertex2){
        return addEdge(new Edge(label,vertex1,vertex2));
    }
    
    //region polygonGenerator
    public void joinAndAddNewVertex(int x, int y){
        //get final vertex
        int lastVertexNumber = vertices.size()-1;
        if(lastVertexNumber == -1){
            addVertex("",x,y);
        }else{
            Vertex lastVertex = vertices.get(vertices.size()-1);
            Vertex newVertex = addVertex("",x,y);
            addEdge("",lastVertex,newVertex);
        }
    }
    
    public double[] getXs(){
        double[] x = new double[getNumberVertices()];
        for (int i = 0; i <= getNumberVertices()-1; i++) {
            x[i] = vertices.get(i).getX();
        }
        return x;
    }
    
    public double[] getYs(){
        double[] y = new double[getNumberVertices()];
        for (int i = 0; i <= getNumberVertices()-1; i++) {
            y[i] = vertices.get(i).getY();
        }
        return y;
    }
    
    public int getNumberVertices(){
        return vertices.size();
    }
    //endregion

    public boolean contains(Vertex vertex) {
        return vertices.contains(vertex);
    }
}
