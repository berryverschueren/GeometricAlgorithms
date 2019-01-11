/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import com.sun.javafx.geom.Line2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javafx.scene.shape.Line;
import math.geom2d.Point2D;

/**
 *
 * @author Kaj75
 */
public class VisibilityGraph {
    private Polygon visibilityGraph;
    private List<Polygon> innerpolygon;
    private TrapezoidalMap mapFunction;

    public VisibilityGraph() {
        innerpolygon = new ArrayList<>();
        mapFunction = new TrapezoidalMap();
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
    
    private List<Vertex> nextVertices = new ArrayList<>();
    
    private List<Vertex> visibleVertices(Vertex vertex, List<Polygon> innerpolygon) {
        System.out.println("Testing Vertex: " + vertex.getLabel());
        List<Vertex> visibleVertex = new ArrayList<>();
        


//        Sort the obstacle vertices according to the clockwise angle that the halfline from p to each vertex makes with the positive x-axis. In case of
//        ties, vertices closer to p should come before vertices farther from p. Let
//        w1,...,wn be the sorted list.

        //printSort(allVertices(innerpolygon), vertex);
        List<Vertex> sortedList = circleSweepSort(vertex, allVertices(innerpolygon));
        
//        2. Let ρ be the half-line parallel to the positive x-axis starting at p. Find
//        the obstacle edges that are properly intersected by ρ, and store them in a
//        balanced search tree T in the order in which they are intersected by ρ.

        TreeMap<Double, List<Edge>> tree = new TreeMap<>();
        //todo: goes wrong when polygon has values larger than a miljoen 
        //expensive but no other easy solution
        for(Edge edge : allEdges(innerpolygon)){
            System.out.println("testing: "+ edge.getLabel());
            Edge startEdge = new Edge("",vertex, new Vertex(vertex.getX()+1000000,vertex.getY(),""));
            Vertex intersection = mapFunction.getIntersectionPointOfSegments(startEdge, edge);
//            Point intersection = lineLineIntersection(  new Point(vertex.getX(), vertex.getY()), 
//                                                        new Point(vertex.getX().floatValue()+1000000, vertex.getY().floatValue()),  
//                                                        new Point(edge.getV1().getX(), edge.getV1().getY()), 
//                                                        new Point(edge.getV2().getX(), edge.getV2().getY())
//            );
            boolean isParrallel = false;
            if(intersection == null){
                int col = mapFunction.orientation(startEdge.getV1(), startEdge.getV2(), edge.getV1());
                col = col + mapFunction.orientation(startEdge.getV1(), startEdge.getV2(), edge.getV2());
                if(col == 0){
                    double distanceV1 = Point2D.distance(vertex.getX(), vertex.getY(),edge.getV1().getX(), edge.getV1().getY());
                    double distanceV2 = Point2D.distance(vertex.getX(), vertex.getY(),edge.getV2().getX(), edge.getV2().getY());
                    if(distanceV1<distanceV2){
                        intersection = edge.getV1();
                    }else{
                        intersection = edge.getV2();
                    }
                    isParrallel = true;
                }
            }
            
            if(intersection!=null){
                double distance = Point2D.distance(vertex.getX(), vertex.getY(), intersection.getX(), intersection.getY());
                if(distance == 0.0 && !isParrallel){
                }else{
                    if(tree.containsKey(distance)){
                        List<Edge> e = tree.get(distance);
                        e.add(edge);

                        tree.put(distance, e);
                    }else{
                        List<Edge> e = new ArrayList<>();
                        e.add(edge);

                        tree.put(distance, e);
                    }
                }
                
            }
        }
        for (Entry<Double, List<Edge>> entry : tree.entrySet()) {
            for(Edge edge : entry.getValue()){
                System.out.println("Key: " + entry.getKey() + ". Value: " + edge.getLabel());
            }
        }
        //balanced search tree
        boolean minIVisibility = false;
        System.out.println("For Vertex: "+vertex.getLabel());
        for (int i = 1; i <= sortedList.size()-1 ; i++) {
            Vertex currentVertex = sortedList.get(i);
            
            boolean visible = visible(i, sortedList, vertex, innerpolygon, tree, minIVisibility);
            if(visible){
                System.out.println("Vertex: "+currentVertex.getLabel()+" is Visible");
                minIVisibility = true;
                visibleVertex.add(sortedList.get(i));
            }else{
                System.out.println("Vertex: "+currentVertex.getLabel()+" is Not Visible");
                minIVisibility = false;
            }
            //find edges of vertex
            
            Polygon polygon = null;
            for(Polygon poly : innerpolygon){
                if(poly.getVertices().contains(currentVertex)){
                    polygon = poly;
                    break;
                }
            }
            List<Edge> edges = new ArrayList<>();
            for(Edge edge : polygon.getEdges()){
                if(edge.containsVertex(currentVertex)){
                    edges.add(edge);
                }   
            }
            ///////////////nextVertices
            List<Edge> toAdd = new ArrayList<>();
            List<Edge> toDelete = new ArrayList<>();
            for(Edge edge : edges){
                boolean notNext = true;
                for(Vertex v : nextVertices){
                    if(edge.containsVertex(v)){
                        notNext = false;
                    }
                }
                if(notNext){
                    boolean isInTree = false;
                    for(Entry<Double, List<Edge>> entry : tree.entrySet()){
                        if(entry.getValue().contains(edge)){
                            isInTree = true;
                        }
                    }
                    if(isInTree){
                        toDelete.add(edge);
                    }else{
                        toAdd.add(edge);
                    }
                }
            }
            //remove edges in tree
            double prevKey = -1000.0;
            if(!toDelete.isEmpty()){
                prevKey = removeEdges(toDelete, tree); 
            }
            //add edges in tree
            if(!toAdd.isEmpty()){
                tree = addEdges(toAdd, tree, prevKey, vertex, allEdges(innerpolygon), currentVertex);
            }

            if(!toDelete.isEmpty()){
                removeEdges(toDelete, tree); 
            }
            System.out.println("");
            for (Entry<Double, List<Edge>> entry : tree.entrySet()) {
                
                for(Edge edge : entry.getValue()){
                    System.out.println("Key: " + entry.getKey() + ". Value: " + edge.getLabel());
                }
            }
        }
//        4. for i ← 1 to n
//        5. do if VISIBLE(wi) then Add wi to W.
//        6. Insert into T the obstacle edges incident to wi that lie on the clockwise side of the half-line from p to wi.
//        7. Delete from T the obstacle edges incident to wi that lie on the
//        counterclockwise side of the half-line from p to wi.
//        8. return W
        
        return visibleVertex;
    }
    
        private boolean visible(int i, List<Vertex> sortedList, Vertex p, List<Polygon> innerpolygon, TreeMap<Double, List<Edge>> tree, boolean minIVisibility) {
//        java.awt.Polygon poly = new java.awt.Polygon();
//        java.awt.Polygon linePoly = new java.awt.Polygon();
        boolean intersectsPolygon = false;
        Vertex vertex = sortedList.get(i);
        Edge P = new Edge(p,vertex);
        //1. if pwi intersects the interior of the obstacle of which wi is a vertex, locally at wi
        outerloop:
        for(Polygon polygon : innerpolygon){
            if(polygon.contains(vertex)){
                for(Edge edge : polygon.getEdges()){
                    if(!edge.containsVertex(vertex)){
                        Vertex intersection = mapFunction.getIntersectionPointOfSegments(P, edge);
                        if(intersection!=null){
                            intersectsPolygon=true;
                            break outerloop;
                        }
                    }
                }
            }
        }
        //2. then return false
        if(intersectsPolygon){
            return false;
            //3. else if i = 1 or wi−1 is not on the segment pwi  
        }  
        if(i==1){
            
            //4. then Search in T for the edge e in the leftmost leaf.
            Edge edge = null;
            Entry<Double, List<Edge>> entry = tree.firstEntry();//.pollFirstEntry();
            if(entry != null) {
                edge = entry.getValue().get(0);
                //pwi intersects e
                boolean doesIntersect = false;
                Vertex inter = mapFunction.getIntersectionPointOfSegments(edge, P);
                // note: maybe parrallel
                if(inter != null){
                    if(edge.hasSameCoordinates(inter)){
                        doesIntersect = false;
                    }else{
                        doesIntersect = true;
                    }
                }
                if(doesIntersect){
                    return false;
                }else{
                    return true;
                }
            }else{
                return true;
            }
        }else if(!mapFunction.onSegment(p, sortedList.get(i-1), vertex)){
            //4. then Search in T for the edge e in the leftmost leaf.
            Edge edge = null;
            Entry<Double, List<Edge>> entry = tree.firstEntry();//.pollFirstEntry();
            if(entry != null) {
                edge = entry.getValue().get(0);
                
                boolean doesIntersect = false;
                Vertex inter = mapFunction.getIntersectionPointOfSegments(edge, P);
                // note: maybe parrallel
                if(inter != null){
                    if(edge.hasSameCoordinates(inter)){
                        doesIntersect = false;
                    }else{
                        doesIntersect = true;
                    }
                }
                if(doesIntersect){
                //pwi intersects e
                //if(mapFunction.GetIntersectionPointOfSegments(edge, P)!=null){
                    return false;
                }else{
                    return true;
                }
            }else{
                return true;
            }
        }else{
            //8. else if wi−1 is not visible
            if(!minIVisibility){
                return false;
            }else{
                Edge edge = new Edge(vertex, sortedList.get(i-1));
                boolean anyEdgeIntersects = searchIntersectingEdge(tree, edge);
               //Search in T for an edge e that intersects wi−1wi.
               //if e exists
                if(anyEdgeIntersects){
                    return false;       
                }else{
                    return true;
                }
            }
        }
        
        
        
        
//        linePoly.addPoint(vertex.getX().intValue(), vertex.getY().intValue());
//        
//        Area area = new Area(poly);
//        Area lineArea = new Area(poly);
//        area.contains(0, 0);
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
    
    
    
    
    
    private void printSort(List<Vertex> list, Vertex vertex){
        //System.out.println(Math.atan2(0, -2)*(180/Math.PI)+ "math");
        //System.out.println("Vertex="+vertex.getLabel());
        String sort = ""; 
        for(Vertex vertex1 : list){
            sort = sort + vertex1.getLabel()+" , ";
        }
        
        //for(Vertex a : list){
            //System.out.println((((Math.atan2(a.getY()-vertex.getY(), a.getX()-vertex.getX())))*(180/Math.PI))+" "+a.getLabel());
        //}
        System.out.println(sort);
    }

    
    
    private double angleValue(Vertex a, Vertex b){
        return Math.atan2(a.getY()-b.getY(), a.getX()-b.getX())*(180/Math.PI);
    }

    private List<Vertex> circleSweepSort(Vertex vertex, List<Vertex> allVertices) {
        List<Vertex> sortedList = allVertices;
        double deg = Math.atan2(-vertex.getY(), -vertex.getX());
        
        //angleValue (angle value, offset);
        Collections.sort(sortedList, (a,b) ->         
                angleValue(a,vertex) == angleValue(b,vertex)? 
                    Point2D.distance(vertex.getX(), vertex.getY(), a.getX(), a.getY()) < Point2D.distance(vertex.getX(), vertex.getY(), b.getX(), b.getY())?-1:1:
                (angleValue(a,vertex) == 0.0)?-1:
                (angleValue(b,vertex) == 0.0)?1:
                (angleValue(a,vertex) < 0 && angleValue(b,vertex) < 0)?(angleValue(a,vertex) > angleValue(b,vertex))?-1:1:
                (angleValue(a,vertex) > 0 && angleValue(b,vertex) > 0)?(angleValue(a,vertex) > angleValue(b,vertex))?-1:1:        
                (angleValue(a,vertex) < angleValue(b,vertex))?-1:1
        );
        printSort(sortedList, vertex);
        String s = ""; 
        for(Vertex v : sortedList){
            double angle = angleValue(v, vertex);
            if(angle<0){
                nextVertices.add(v);
                s = s+", "+v.getLabel();
            }
        }
        System.out.println(s);
        return sortedList;        
    }

    private List<Edge> allEdges(List<Polygon> innerpolygon) {
        List<Edge> edges = new ArrayList<>();
        for(Polygon polygon : innerpolygon){
            edges.addAll(polygon.getEdges());
        }
        return edges;
    }
    
    private TreeMap<Double, List<Edge>> addEdges(List<Edge> edges, TreeMap<Double, List<Edge>> tree, Double preKey, Vertex vertex, List<Edge> allEdges, Vertex currentVertex){
        if(edges.size()==1){
            if(tree.containsKey(preKey)){
                    List<Edge> e = tree.get(preKey);
                    e.add(edges.get(0));
                    
                    tree.put(preKey, e);
                }else{
                    List<Edge> e = new ArrayList<>();
                    e.add(edges.get(0));
                        
                    tree.put(preKey, e);
                }
            return tree;
        }
        List<Edge> treeEdges = new ArrayList<>(); 
        for(Entry<Double, List<Edge>> entry : tree.entrySet()){
            treeEdges.addAll(entry.getValue());
        }
        
        treeEdges.addAll(edges);
        tree = new TreeMap<>();
        //two new added update all distances
        Edge halfLine =  new Edge("",vertex, currentVertex);
        for(Edge edge : treeEdges){
            Vertex intersection = mapFunction.getIntersectionPointOfSegments(halfLine, edge);
//            Point intersection = lineLineIntersection(  new Point(vertex.getX(), vertex.getY()), 
//                                                        new Point(vertex.getX().floatValue()+1000000, vertex.getY().floatValue()),  
//                                                        new Point(edge.getV1().getX(), edge.getV1().getY()), 
//                                                        new Point(edge.getV2().getX(), edge.getV2().getY())
//            );
            if(intersection!=null){
                double distance = Point2D.distance(vertex.getX(), vertex.getY(), intersection.getX(), intersection.getY());
                if(distance != 0.0){
                    if(tree.containsKey(distance)){
                        List<Edge> e = tree.get(distance);
                        e.add(edge);

                        tree.put(distance, e);
                    }else{
                        List<Edge> e = new ArrayList<>();
                        e.add(edge);

                        tree.put(distance, e);
                    }
                }
                //tree.put(distance, edge);
            }
        }
        return tree;
    }
    
    private Double removeEdges(List<Edge> edges, TreeMap<Double, List<Edge>> tree) {
        Double edgeKey=-1000.0;
        
        for(Edge edge : edges){
            List<Edge> edgeValues = new ArrayList<>();
            for(Entry<Double, List<Edge>> entry : tree.entrySet()){
                for(Edge e : entry.getValue())
                    if(e==edge){
                        edgeKey = entry.getKey();
                        edgeValues = entry.getValue();
                    }
            } 
            if(!edgeValues.isEmpty()){
                edgeValues.remove(edge);
                if(edgeValues.isEmpty()){
                    tree.remove(edgeKey);
                }else{
                    tree.put(edgeKey, edgeValues);
                }
            }
        }     
        return edgeKey;
    }

    private boolean between(double x, double x0, double x1) {
        if(x0==x1){
            if(x==x0){
                return true;
            }else{
                return false;
            }
        }
        double smallest, largest; 
        if(x0<x1){
            smallest=x0;
            largest=x1;
        } else{
            smallest=x1;
            largest=x0;
        }
        
        if(x >= smallest && x <= largest){
            return true;
        }
        return false;
    }
    
    
    
    
    
    



    private boolean searchIntersectingEdge(TreeMap<Double, List<Edge>> tree, Edge edge) {
        boolean foundIntersection = false;
        for(Entry<Double, List<Edge>> entry : tree.entrySet()) {
            for(Edge e : entry.getValue()){
                if(mapFunction.getIntersectionPointOfSegments(edge, e)!=null){
                    return true;
                }
            }           
        }
        return foundIntersection;
    }


}
//for lineLineIntersection method
class Point 
{ 
    double x,y; 
      
    public Point(double x, double y)  
    { 
        this.x = x; 
        this.y = y; 
    } 
}
