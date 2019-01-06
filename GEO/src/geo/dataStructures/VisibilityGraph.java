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
 * @author Kaj75
 */
public class VisibilityGraph {
    private Polygon visibilityGraph;
    private List<Polygon> innerpolygon;

    public VisibilityGraph() {
        innerpolygon = new ArrayList<>();
    }
    
    public Polygon visibilityGraphAlgorithm(List<Polygon> innerpolygon){
        visibilityGraph = new Polygon();
        visibilityGraph.setVertices(allVertices(innerpolygon));
        this.innerpolygon = innerpolygon;
        
        for (Vertex vertex : visibilityGraph.getVertices()){
            List<Vertex> vertices = visibleVertices(vertex, innerpolygon);
            for (Vertex v : vertices){
                visibilityGraph.addEdge(new Edge("", vertex, v));
            }
        }
 
        return visibilityGraph;       
    }
    
    //all visibility graphs of guards in one graph
    public Polygon guardVisibilityGraph(List<Vertex> guards){
        Polygon guardsVisibilityGraph = visibilityGraph;
        
        for (Vertex vertex : guards){
            List<Vertex> vertices = visibleVertices(vertex, innerpolygon);
            for (Vertex v : vertices){
                guardsVisibilityGraph.addEdge(new Edge("", vertex, v));
            }
        }
        
        return guardsVisibilityGraph;
    }

    private List<Vertex> allVertices(List<Polygon> innerpolygon) {
        List<Vertex> vertices = new ArrayList<>();
        for(Polygon polygon : innerpolygon){
            vertices.addAll(polygon.getVertices());
        }
        return vertices;
    }
    
    

    private List<Vertex> visibleVertices(Vertex vertex, List<Polygon> innerpolygon) {
        List<Vertex> visibleVertex = new ArrayList<>();
        
//        Sort the obstacle vertices according to the clockwise angle that the halfline from p to each vertex makes with the positive x-axis. In case of
//        ties, vertices closer to p should come before vertices farther from p. Let
//        w1,...,wn be the sorted list.
//        2. Let ρ be the half-line parallel to the positive x-axis starting at p. Find
//        the obstacle edges that are properly intersected by ρ, and store them in a
//        balanced search tree T in the order in which they are intersected by ρ.
//        3. W ← /0
//        4. for i ← 1 to n
//        5. do if VISIBLE(wi) then Add wi to W.
//        6. Insert into T the obstacle edges incident to wi that lie on the clockwise side of the half-line from p to wi.
//        7. Delete from T the obstacle edges incident to wi that lie on the
//        counterclockwise side of the half-line from p to wi.
//        8. return W
        
        return visibleVertex;
    }
}
