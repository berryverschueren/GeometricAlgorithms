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
        List<Vertex> sortedList = circleSweepSort(vertex, allVertices(innerpolygon));
//        2. Let ρ be the half-line parallel to the positive x-axis starting at p. Find
//        the obstacle edges that are properly intersected by ρ, and store them in a
//        balanced search tree T in the order in which they are intersected by ρ.

        TreeMap<Double, Edge> tree = new TreeMap<>();
        //todo: goes wrong when polygon has values larger than a miljoen 
        //expensive but no other easy solution
        for(Edge edge : allEdges(innerpolygon)){
            Vertex intersection = mapFunction.GetIntersectionPointOfSegments(new Edge("",vertex, new Vertex(vertex.getX()+1000000,vertex.getY(),"")), edge);
//            Point intersection = lineLineIntersection(  new Point(vertex.getX(), vertex.getY()), 
//                                                        new Point(vertex.getX().floatValue()+1000000, vertex.getY().floatValue()),  
//                                                        new Point(edge.getV1().getX(), edge.getV1().getY()), 
//                                                        new Point(edge.getV2().getX(), edge.getV2().getY())
//            );
            if(intersection!=null){
                double distance = Point2D.distance(vertex.getX(), vertex.getY(), intersection.getX(), intersection.getY());
                tree.put(distance, edge);
            }
        }
        //balanced search tree
        boolean minIVisibility = false;
        for (int i = 0; i < sortedList.size()-1 ; i++) {
            boolean visible = visible(i, sortedList, vertex, innerpolygon, tree, minIVisibility);
            if(visible){
                minIVisibility = true;
                visibleVertex.add(sortedList.get(i));
            }else{
                minIVisibility = false;
            }
            //find edges of vertex
            Vertex vertexI = sortedList.get(i);
            Polygon polygon = null;
            for(Polygon poly : innerpolygon){
                if(poly.getVertices().contains(vertexI)){
                    polygon = poly;
                    break;
                }
            }
            List<Edge> edges = new ArrayList<>();
            for(Edge edge : polygon.getEdges()){
                if(edge.containsVertex(vertexI)){
                    edges.add(edge);
                }   
            }
            
            List<Edge> toAdd = new ArrayList<>();
            List<Edge> toDelete = new ArrayList<>();
            for(Edge edge : edges){
                if(tree.containsValue(edge)){
                    toDelete.add(edge);
                }else{
                    toAdd.add(edge);
                }
            }
            //remove edges in tree
            double prevKey = 0.0;
            if(toDelete.isEmpty()){
                prevKey = removeEdges(toDelete, tree); 
            }
            //add edges in tree
            if(toAdd.isEmpty()){
                tree = addEdges(toAdd, tree, prevKey, vertex);
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

    private List<Vertex> circleSweepSort(Vertex vertex, List<Vertex> allVertices) {
        List<Vertex> sortedList = allVertices;
        double deg = Math.atan2(-vertex.getY(), -vertex.getX());
        Collections.sort(sortedList, (a,b) -> 
                (Math.atan2(a.getY()-vertex.getY(), a.getX()-vertex.getX())) < (Math.atan2(b.getY()-vertex.getY(), b.getX()-vertex.getX()))? -1:
                (Math.atan2(a.getY()-vertex.getY(), a.getX()-vertex.getX())) == (Math.atan2(b.getY()-vertex.getY(), b.getX()-vertex.getX()))&&
                Point2D.distance(vertex.getX(), vertex.getY(), a.getX(), a.getY()) < Point2D.distance(vertex.getX(), vertex.getY(), b.getX(), b.getY())?-1:1
        );
        return sortedList;        
    }

    private List<Edge> allEdges(List<Polygon> innerpolygon) {
        List<Edge> edges = new ArrayList<>();
        for(Polygon polygon : innerpolygon){
            edges.addAll(polygon.getEdges());
        }
        return edges;
    }
    
    private TreeMap<Double, Edge> addEdges(List<Edge> edges, TreeMap<Double, Edge> tree, Double preKey, Vertex vertex){
        if(preKey != 0.0){
            tree.put(preKey, edges.get(0));
            return tree;
        }
        List<Edge> treeEdges = new ArrayList<>(tree.values());
        tree = new TreeMap<>();
        //two new added update all distances
        for(Edge edge : treeEdges){
            Vertex intersection = mapFunction.GetIntersectionPointOfSegments(new Edge("",vertex, new Vertex(vertex.getX()+1000000,vertex.getY(),"")), edge);
//            Point intersection = lineLineIntersection(  new Point(vertex.getX(), vertex.getY()), 
//                                                        new Point(vertex.getX().floatValue()+1000000, vertex.getY().floatValue()),  
//                                                        new Point(edge.getV1().getX(), edge.getV1().getY()), 
//                                                        new Point(edge.getV2().getX(), edge.getV2().getY())
//            );
            if(intersection!=null){
                double distance = Point2D.distance(vertex.getX(), vertex.getY(), intersection.getX(), intersection.getY());
                tree.put(distance, edge);
            }
        }
        return tree;
    }
    
    private Double removeEdges(List<Edge> edges, TreeMap<Double, Edge> tree) {
        Double edgeKey=0.0;
        for(Edge edge : edges){
            for(Entry<Double, Edge> entry : tree.entrySet()){
                if(entry.getValue()==edge){
                    edgeKey = entry.getKey();
                }
            }
            tree.remove(edgeKey);
        }
        return edgeKey;
    }
    //GetIntersectionPointOfSegments posible
//    private Point lineLineIntersection(Point A, Point B, Point C, Point D) 
//    { 
//        // Line AB represented as a1x + b1y = c1 
//        double a1 = B.y - A.y; 
//        double b1 = A.x - B.x; 
//        double c1 = a1*(A.x) + b1*(A.y); 
//       
//        // Line CD represented as a2x + b2y = c2 
//        double a2 = D.y - C.y; 
//        double b2 = C.x - D.x; 
//        double c2 = a2*(C.x)+ b2*(C.y); 
//       
//        double determinant = a1*b2 - a2*b1; 
//       
//        if (determinant == 0) 
//        { 
//            // The lines are parallel. This is simplified 
//            // by returning a pair of FLT_MAX 
//            return null;
//        } 
//        else
//        { 
//            double x = (b2*c1 - b1*c2)/determinant; 
//            double y = (a1*c2 - a2*c1)/determinant; 
//            
//            
//            if(between(x, A.x, B.x)&&between(x, C.x, D.x)&&between(y, A.y, B.y)&&between(y, C.y, D.y)){
//                return new Point(x, y); 
//            }else{
//                return null;
//            }
//        } 
//    } 

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
    
    
    
    
    
    

    private boolean visible(int i, List<Vertex> sortedList, Vertex p, List<Polygon> innerpolygon, TreeMap<Double, Edge> tree, boolean minIVisibility) {
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
                        Vertex intersection = mapFunction.GetIntersectionPointOfSegments(P, edge);
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
        if(i==0 || !mapFunction.OnSegment(p, sortedList.get(1-i), vertex)){
            //4. then Search in T for the edge e in the leftmost leaf.
            Edge edge = null;
            Entry<Double, Edge> entry = tree.firstEntry();//.pollFirstEntry();
            if(entry != null) {
                edge = entry.getValue();
                //pwi intersects e
                if(mapFunction.GetIntersectionPointOfSegments(edge, P)!=null){
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
                Edge edge = new Edge(vertex, sortedList.get(1-i));
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

    private boolean searchIntersectingEdge(TreeMap<Double, Edge> tree, Edge edge) {
        boolean foundIntersection = false;
        for(Entry<Double, Edge> entry : tree.entrySet()) {
           Edge value = entry.getValue();
           if(mapFunction.GetIntersectionPointOfSegments(edge, edge)!=null){
               return true;
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
