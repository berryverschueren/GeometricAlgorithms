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
    
    private TrapezoidalMap math;
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
        //prio 3: #rest  doesPolygonContainVertex
        priorityVertexExit = new TreeMap<>();
        priorityVertexArt = new TreeMap<>();
        math = new TrapezoidalMap();
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
    
    private Polygon getPolygon(Vertex vertex, List<Polygon> pList){
        Polygon poly = new Polygon();
        outerloop:
        for(Polygon p : pList){
            poly =p;
            for(Vertex v : p.getVertices()){
                if(v==vertex){
                    break outerloop;
                }
            }     
        }
        return poly;
    }
    
    public Polygon visibiliyGraph(List<Polygon> polygons){
        //boolean mayAdd = false;
        boolean seesArt = false;
        boolean seesExit = false;
        Polygon outerPolygon = polygons.get(0);
        
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
                        boolean isNoSelf = true;
                        
                        if(noSelfEdge(newEdge, polygons)){
                            allPoly.addEdge(newEdge);
                            //vertexInfo.addSeesMe(vertex2);
                            if(vertex1.getExitFlag()==1){
                                vertexInfo.setIsExit(true);
                                //vertexInfo.addSeesMe(vertex2);
                            }else{
                                
                            }
                        }else{
                            //mayAdd+is to Same polygon
                            isNoSelf = false;
                            boolean outer = false;
                            Polygon p = outerPolygon;
                            for(Vertex vertex : outerPolygon.getVertices()){
                                if(vertex==vertex2){
                                    outer = true;
                                }
                            }
                            Vertex halfway = math.halfwayPoint(newEdge);
                            Vertex halfHalfway1 = math.halfwayPoint(new Edge("", vertex1, halfway));
                            Vertex halfHalfway2 = math.halfwayPoint(new Edge("", halfway, vertex2));
                            if(outer){
                                if(math.doesPolygonContainVertex(p, halfway)
                                        || math.doesPolygonContainVertex(p, halfHalfway1)
                                        || math.doesPolygonContainVertex(p, halfHalfway2)){
                                    allPoly.addEdge(newEdge);
                                    vertexInfo.addSeesMe(vertex2);
                                }                                
                            }else{
                                p = getPolygon(vertex2, polygons);
                                if(!math.doesPolygonContainVertexNotEdge(p, halfway) && 
                                        !math.doesPolygonContainVertexNotEdge(p, halfHalfway1) && 
                                        !math.doesPolygonContainVertexNotEdge(p, halfHalfway2)){
                                    allPoly.addEdge(newEdge);
                                    vertexInfo.addSeesMe(vertex2);
                                }
                            }
                        }
                            
                        if(isNoSelf){
                            vertexInfo.addSeesMe(vertex2);
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
                        
                        //vertexInfo.addSeesMe(vertex2);
                    }
            }
            info.add(vertexInfo);
            if(seesArt){
                savePriority(numberOf, vertex1, priorityVertexArt);
//                if(priorityVertexArt.containsKey(numberOf)){
//                    List<Vertex> vertices = priorityVertexArt.get(numberOf);
//                    vertices.add(vertex1);
//                    priorityVertexArt.put(numberOf, vertices);
//                }else{
//                    List<Vertex> vertices = new ArrayList<>();
//                    vertices.add(vertex1);
//                    priorityVertexArt.put(numberOf, vertices);
//                }
            }
            if(seesExit){
                savePriority(numberOf, vertex1, priorityVertexExit);
//                if(priorityVertexExit.containsKey(numberOf)){
//                    List<Vertex> vertices = priorityVertexExit.get(numberOf);
//                    vertices.add(vertex1);
//                    priorityVertexExit.put(numberOf, vertices);
//                }else{
//                    List<Vertex> vertices = new ArrayList<>();
//                    vertices.add(vertex1);
//                    priorityVertexExit.put(numberOf, vertices);
//                }
            }
        }
        
        for(Polygon polygon : polygons){
            allPoly.addVertices(polygon.getVertices());
            allPoly.addEdges(polygon.getEdges());
        }

        System.out.println("number of edge from visibility graph = "+allPoly.getEdges().size());
        return allPoly;
    }
    
    private void savePriority(int numberOf, Vertex vertex, Map<Integer, List<Vertex>> queue){
        if(queue.containsKey(numberOf)){
            List<Vertex> vertices = queue.get(numberOf);
            vertices.add(vertex);
            queue.put(numberOf, vertices);
        }else{
            List<Vertex> vertices = new ArrayList<>();
            vertices.add(vertex);
            queue.put(numberOf, vertices);
        }
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
