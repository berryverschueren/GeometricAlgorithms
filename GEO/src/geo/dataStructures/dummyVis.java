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
public class dummyVis {

    private TrapezoidalMap mapFunction;
    
    public dummyVis() {
        mapFunction = new TrapezoidalMap();
    }
    
    private boolean mayAdd(Edge newEdge, List<Edge> edges){
        //System.out.println("newEdge v1:"+newEdge.getV1() +"v2:" +newEdge.getV2());
        boolean noCrossing = true;
        for(Edge edge : edges){
                if(!edge.containsVertex(newEdge.getV1()) && !edge.containsVertex(newEdge.getV2()) ){
                    Vertex inter = mapFunction.GetIntersectionPointOfSegments(edge, newEdge);
                    if(inter != null){
                        noCrossing = false;
                    }
                }
                
                // note: maybe parrallel
//                if(inter != null){
//                    if(edge.hasSameCoordinates(inter) || newEdge.hasSameCoordinates(inter) ){
////
//                    }else{
//                        noCrossing = false;
//                    }
//                }
        }

        return noCrossing;
    }
    
    public Polygon visibiliyGraph(List<Polygon> polygons){
        //mapFunction.GetIntersectionPointOfSegments(edge, P);
        Polygon allPoly = new Polygon();
        for(Vertex vertex1 : allVertices(polygons)){
            for(Vertex vertex2 : allVertices(polygons)){
                Edge newEdge = new Edge("", vertex1,vertex2);
                if(noSelfEdge(newEdge, polygons)){
                    if(mayAdd(newEdge, allEdges(polygons))){
                        allPoly.addEdge(newEdge);
                    }
                }
                //allPolygon
            }
        }
        
        for(Polygon polygon : polygons){
            allPoly.addVertices(polygon.getVertices());
            allPoly.addEdges(polygon.getEdges());
        }

        
        return allPoly;
    }
    private List<Vertex> allVertices(List<Polygon> innerpolygon) {
        List<Vertex> vertices = new ArrayList<>();
        for(Polygon polygon : innerpolygon){
            vertices.addAll(polygon.getVertices());
        }
        return vertices;
    }
    private List<Edge> allEdges(List<Polygon> innerpolygon) {
        List<Edge> edges = new ArrayList<>();
        for(Polygon polygon : innerpolygon){
            edges.addAll(polygon.getEdges());
        }
        return edges;
    }

    private boolean noSelfEdge(Edge newEdge, List<Polygon> polygons) {
        Polygon p = new Polygon();
        for(Polygon polygon : polygons){
            if(polygon.contains(newEdge.getV1())){
                p=polygon;
                break;
            }
        }
        
        return !p.contains(newEdge.getV2());
    }
}
