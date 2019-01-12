/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.util.Pair;

/**
 *
 * @author Kaj75
 */
public class dummyVis {

    private TrapezoidalMap mapFunction;
    //prio 1: #exits sorted on prio
    //prio 2: #arts
    private List<VertexInfo> info;
    private Map<Integer, List<Vertex>> priorityVertexExit;
    private Map<Integer, List<Vertex>> priorityVertexArt;
    
    private List<Vertex> artList;
    private List<Vertex> exitList;
    
    
    public List<Vertex> getBestExitGuards(){
        List<Vertex> bestVertices = new ArrayList<>();

        int key = Collections.max(priorityVertexExit.keySet());
        bestVertices.addAll(priorityVertexExit.get(key));

        return bestVertices;
    }
    
    public List<VertexInfo> getVertexInfo(){
        return info;
    }

    public List<Vertex> getArtList() {
        return artList;
    }

    public List<Vertex> getExitList() {
        return exitList;
    }
    
    
    
    public dummyVis() {
        mapFunction = new TrapezoidalMap();
        info = new ArrayList<>();
        artList = new ArrayList<>();
        exitList = new ArrayList<>();
        //prio 1: #exits sorted on prio
        //prio 2: #arts
        //prio 3: #rest
        priorityVertexExit = new TreeMap<>();
        priorityVertexArt = new TreeMap<>();
;
    }
    
    private boolean mayAdd(Edge newEdge, List<Edge> edges){
        //System.out.println("newEdge v1:"+newEdge.getV1() +"v2:" +newEdge.getV2());
        boolean noCrossing = true;
        for(Edge edge : edges){
                if(!edge.containsVertex(newEdge.getV1()) && !edge.containsVertex(newEdge.getV2()) ){
                    Vertex inter = mapFunction.getIntersectionPointOfSegments(edge, newEdge);
                    if(inter != null){
                        noCrossing = false;
                    }
                }
        }

        return noCrossing;
    }
    
    
    public Polygon visibiliyGraph(List<Polygon> polygons){
        //boolean mayAdd = false;
        boolean seesArt = false;
        boolean seesExit = false;

        
        Polygon allPoly = new Polygon();
        for(Vertex vertex1 : allVertices(polygons)){
            storeStatus(vertex1);
            int numberOf = 0;
            VertexInfo vertexInfo = new VertexInfo();
            vertexInfo.setVertex(vertex1);
            for(Vertex vertex2 : allVertices(polygons)){
                Edge newEdge = new Edge("", vertex1,vertex2);
//                if(noSelfEdge(newEdge, polygons)){
                    if(mayAdd(newEdge, allEdges(polygons))){
                        if(noSelfEdge(newEdge, polygons)){
                            allPoly.addEdge(newEdge);
                            
                            if(vertex1.getExitFlag()==1){
                                vertexInfo.setIsExit(true);
                                vertexInfo.addSeesMe(vertex2);
                            }
                        }
                        //todo: add list of may add to check if in or out of polygon 
                        //mayAdd = true;
                        //can see (see trough walls!!!)
                        if(vertex2.getArtFlag()==1){
                            seesArt = true;
                            vertexInfo.addArt(vertex2);
                            numberOf++;
                        }
                       
                        if(vertex2.getExitFlag()==1){
                            if(!seesExit){
                                seesExit = true;
                                seesArt = false;
                                numberOf = 0;
                            }
                            vertexInfo.addExit(vertex2);
                            numberOf++;
                        }
                        
                        
                    }
            }
            info.add(vertexInfo);
            if(seesArt){
                if(priorityVertexArt.containsKey(numberOf)){
                    List<Vertex> vertices = priorityVertexArt.get(numberOf);
                    vertices.add(vertex1);
                    priorityVertexArt.put(numberOf, vertices);
                }else{
                    List<Vertex> vertices = new ArrayList<>();
                    vertices.add(vertex1);
                    priorityVertexArt.put(numberOf, vertices);
                }
            }
            if(seesExit){
                if(priorityVertexExit.containsKey(numberOf)){
                    List<Vertex> vertices = priorityVertexExit.get(numberOf);
                    vertices.add(vertex1);
                    priorityVertexExit.put(numberOf, vertices);
                }else{
                    List<Vertex> vertices = new ArrayList<>();
                    vertices.add(vertex1);
                    priorityVertexExit.put(numberOf, vertices);
                }
            }
        }
        
        for(Polygon polygon : polygons){
            allPoly.addVertices(polygon.getVertices());
            allPoly.addEdges(polygon.getEdges());
        }

        System.out.println("number of edge from visibility graph = "+allPoly.getEdges().size());
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

    private void storeStatus(Vertex vertex) {
        if(vertex.getArtFlag()==1){
            artList.add(vertex);
        }
        if(vertex.getExitFlag()==1){
            exitList.add(vertex);
        }
    }
}
