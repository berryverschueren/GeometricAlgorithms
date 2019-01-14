/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import geo.WriteInputGallerySpecification;
import geo.ReadInputGallerySpecification;
import geo.ReadInputGuardSpecification;
import geo.ReadInputRobberSpecification;
import geo.WriteInputGuardSpecification;
import geo.WriteInputRobberSpecification;
import geo.dataStructures.Edge;
import geo.dataStructures.Polygon;
import geo.dataStructures.TrapezoidalMap;
import geo.dataStructures.Vertex;
import geo.dataStructures.VisibilityGraph;
import geo.dataStructures.Gallery;
import geo.dataStructures.GalleryProblem;
import geo.dataStructures.Graph;
import geo.dataStructures.Guard;
import geo.dataStructures.PathGuard;
import geo.dataStructures.PathRobber;
import geo.dataStructures.Robber;
import geo.dataStructures.TimePoint;
import geo.dataStructures.TimePointComparator;
import geo.dataStructures.Trapezoid;
import geo.dataStructures.VertexInfo;
import geo.dataStructures.dummyVis;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import math.geom2d.Point2D;

/**
 *
 * @author Kaj75
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Canvas canvas;
    @FXML
    private ChoiceBox choice;
    @FXML
    private TextField Guards;
    @FXML
    private TextField vMaxGuards;
    @FXML
    private TextField globalTime;
    @FXML
    private TextField deltaT;
    @FXML
    private RadioButton art;
    @FXML
    private RadioButton exit;
    @FXML
    private Button readInput;
    @FXML
    private Button readGuard;
    
    
    private GraphicsContext g; 
    private Polygon polygon;
    private List<Polygon> innerPolygon;
    private List<Guard> guards; 
    private int numOfGuards; 
    private double vMaxG;
    private double deltaTime;
    private double globalT;
    public int countArts = 0;
    public int countExits = 0;
    public final int count = 0;
    public int observing; 
    public Polygon visibilityGraph;
    
    private List<Vertex> stopVertices = new ArrayList<>();
    private List<VertexInfo> information = new ArrayList<>();
    
    private dummyVis vis;
    
    private Map<Double, List<Vertex>> getShortestArtPath(){
        Map<Double, List<Vertex>> robberPaths = new TreeMap();
        
        for(Vertex art : vis.getArtList()){
            double cost = 1000000000000.0;
            List<Vertex> shortestPath = null;
            
            for(Vertex exit : vis.getExitList()){
                Graph graph = new Graph();
                List<Vertex> path = findSinglePathWithGraph(exit,art, graph);
                double currentCost = graph.getCostCurrentPath();
                path.add(art);
                if(currentCost < cost){
                    cost=currentCost;
                    shortestPath = path;
                }
            } 
            if(shortestPath != null){
                robberPaths.put(cost, shortestPath);
            }
        }   
        
        return robberPaths;
    }
    
    private List<Edge> crossVisiblePath(List<Edge> visibleArea){
        List<Edge> visibleEdges = new ArrayList<>();
        TrapezoidalMap tm = new TrapezoidalMap();
        for(Edge edge : visibilityGraph.getEdges()){
            boolean doesIntersection = false;
            for(Edge visibleEdge : visibleArea){
                Vertex intersection = tm.getIntersectionPointOfSegments(edge, visibleEdge);
                if(intersection != null){
                    doesIntersection = true;
                }
            }
            if(!doesIntersection){
                visibleEdges.add(edge);
            }
        }
        visibleEdges.addAll(visibleArea);
        return visibleEdges;
    }

    private List<Vertex> getSmartSmartPath(int numGuards, int numExits){
        List<VertexInfo> infoList = vis.getVertexInfo();
        
        for(VertexInfo v : infoList){
            if(v.isIsExit()){          
                stopVertices.addAll(v.getSeeMe());
                Vertex vv = v.getVertex();
                stopVertices.add(vv);
                for(Edge edge : polygon.getEdges()){
                    if(edge.hasSameCoordinates(vv)){
                        if(edge.getV1() != vv){
                            stopVertices.add(edge.getV1());
                        }
                        if(edge.getV2() != vv){
                            stopVertices.add(edge.getV2());
                        }
                    }    
                }
            }
        }
        
        List<Vertex> shortestPath = new ArrayList<>();
        
        for(VertexInfo info : infoList){
            if(info.getNumExit() == numExits){
                shortestPath.add(info.getVertex());
                return shortestPath;
            }
        }
        
        List<Vertex> interestingVertices = new ArrayList<>(); 
        
        List<Vertex> verts = this.polygon.getVertices();
        
        for (int i = 0; i < verts.size(); i++) {
            if (verts.get(i).getExitFlag() == 1) {
                interestingVertices.add(verts.get(i));
            }
        }
         
        List<Vertex> path = new ArrayList<>();
        interestingVertices.add(interestingVertices.get(0));
        for (int i = 0; i < interestingVertices.size()-1; i++) {
            List<Vertex> currentPath = new ArrayList<>();
            currentPath = findSinglePath(interestingVertices.get(i), interestingVertices.get(i+1));
            currentPath.remove(0);
            if(!path.isEmpty() && !currentPath.isEmpty()){
                //currentPath = findSinglePath(interestingVertices.get(i), interestingVertices.get(i+1));
                //currentPath.remove(0);
                List<Vertex> inter = findSinglePath(path.get(path.size()-1), currentPath.get(0));
                inter.remove(0);
                shortestPath.addAll(inter);
            }
            path = currentPath;
            //path.remove(0);
            shortestPath.addAll(path);
            
        }
        return shortestPath;
    }
    
    @FXML
    private void handleButtonKaj(ActionEvent event) {

        Edge e1,e2,e3;
        Vertex v1,v2,v3,v4;
        v1 = new Vertex(1.0,1.0,"");
        v2 = new Vertex(0.0,1.0,"");
        v3 = new Vertex(2.0,1.0,"");
        v4 = new Vertex(1.0,2.0,"");
        e1 = new Edge(v2,v1);
        e2 = new Edge(v1,v3);
        e3 = new Edge(v4,v1);
        List<Edge> test = new ArrayList<>();
        test.add(e3);

        visibilityGraph= new Polygon();
        visibilityGraph.addEdge(e1);
        visibilityGraph.addEdge(e2);
        test = crossVisiblePath(test);
        finalEdge();
        
        
 
//        List<Polygon> ps = innerPolygon;
//        
//        ps.add(polygon);
//        vis =new dummyVis();
//        visibilityGraph = vis.visibiliyGraph(ps);
//          
//        List<Vertex> ve = vis.getBestExitGuards();
//        List<Vertex> v = findPath(ve);
//        setUpDraw(false);
//        g.setStroke(Color.RED);
//        for (int i = 0; i < v.size()-1; i++) {
//            g.strokeLine(v.get(i).getX(), v.get(i).getY(), v.get(i+1).getX(), v.get(i+1).getY());
//        }   
    }
    
    public void calculateVisibilityGraph(){
        finalEdge();
        List<Polygon> polys = new ArrayList<>();
        polys.add(polygon);
        polys.addAll(innerPolygon);
        vis = new dummyVis();
        
                g.setStroke(Color.AQUA);

        visibilityGraph = vis.visibiliyGraph(polys);
//        for(Edge edge : visibilityGraph.getEdges()){
//            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
//        }
    }
    
    public List<Vertex> findSinglePath(Vertex vertex1, Vertex vertex2){
        List<Vertex> path = new ArrayList<>();
        path.addAll(new Graph().dijkstraStart(visibilityGraph.getEdges(), vertex1, vertex2, visibilityGraph.getVertices()));
        return path;
    }
    
    public List<Vertex> findSinglePathWithGraph(Vertex vertex1, Vertex vertex2, Graph graph){
        List<Vertex> path = new ArrayList<>();
        path.addAll(graph.dijkstraStart(visibilityGraph.getEdges(), vertex1, vertex2, visibilityGraph.getVertices()));
        return path;
    }
    
    public List<Vertex> findPath(List<Vertex> vertices){
        List<Vertex> path = new ArrayList<>();
        if(!vertices.isEmpty()){
            if(vertices.size()>=2){
                for (int i = 0; i < vertices.size()-1; i++) {
                     path.addAll(new Graph().dijkstraStart(visibilityGraph.getEdges(), vertices.get(i), vertices.get(i+1), visibilityGraph.getVertices()));
                }         
            }
        }
        path.addAll(new Graph().dijkstraStart(visibilityGraph.getEdges(), vertices.get(vertices.size()-1), vertices.get(0), visibilityGraph.getVertices()));
        return path;
    }
    
    public boolean theSameVertex(Vertex v1, double x2, double y2) {
        if (Objects.equals(v1.getX(), x2) && Objects.equals(v1.getY(), y2)) {
            return true;
        }
        return false;
    }
    
    @FXML
    private void handleButtonBerry(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle( "Timeline Example" );
        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        Canvas canvas = new Canvas(1900, 1000);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

//        TrapezoidalMap tm = new TrapezoidalMap();
//        List<Edge> segments = new ArrayList<>();
//        segments.addAll(this.polygon.getEdges());
//        for (int i = 0; i < this.innerPolygon.size(); i++) {
//            segments.addAll(this.innerPolygon.get(i).getEdges());
//        }
//        
//        tm.construct(segments);
//        tm.removeInnerTrapezoids(this.innerPolygon);
//        tm.removeOuterTrapezoids(this.polygon);
//        tm.computePossiblePaths();
        
        String workingDir = "file:\\\\\\" + System.getProperty("user.dir");        
        Image guardImage = new Image(workingDir + "\\guard.png", 30, 70, false, false);
        Image robberImage = new Image(workingDir + "\\robber.png", 30, 70, false, false);

        calculateVisibilityGraph();

        List<Vertex> verts = this.polygon.getVertices();
        int exitCounter = 0;
        for (int i = 0; i < verts.size(); i++) {
            if (verts.get(i).getExitFlag() == 1) {
                exitCounter++;
            }
        }
        
        List<Vertex> verticesForGuardPath = getSmartSmartPath(numOfGuards, exitCounter);
        
        List<Guard> guards = makeGuardList(verticesForGuardPath);
        
        List<TimePoint> timePoints = ComputeTimePoints(guards);
        
        List<PathGuard> stopGuards = GuardsObserving(guards);
        
        Map<Double, List<Vertex>> shortestArtPath = getShortestArtPath();
        
        Map<Double, List<PathRobber>> possiblePathsRobber = possiblePathsRobber(shortestArtPath);
        
        //SortedMap<TimePoint, List<PathRobber>> robberPaths = ComputePossiblePathRobbers(possiblePathsRobber, timePoints);
        SortedMap<TimePoint, List<PathRobber>> robberPaths = ComputePossiblePathRobbersNew(possiblePathsRobber, stopGuards);
        
        Robber robber = ComputeRobber(robberPaths);
        
        final long startNanoTime = System.nanoTime();
                
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
                drawPath(gc, canvas, guards, robber, t, guardImage, robberImage);
                if (t >= globalT) {
                    this.stop();
                }
            }
        }.start();
        
        stage.show();
        writeGuardFile(guards);
        writeRobberFile(robber);
    }
    
    private void drawShapes(GraphicsContext gc, Canvas canvas, TrapezoidalMap tm, double t) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int i = 0; i < ((int)t % tm.getTrapezoids().size() + 1); i++) {
            gc.strokePolygon(new double[] {
                tm.getTrapezoids().get(i).getV1().getX(),
                tm.getTrapezoids().get(i).getV2().getX(),
                tm.getTrapezoids().get(i).getV3().getX(),
                tm.getTrapezoids().get(i).getV4().getX()
            }, new double[] {
                tm.getTrapezoids().get(i).getV1().getY(),
                tm.getTrapezoids().get(i).getV2().getY(),
                tm.getTrapezoids().get(i).getV3().getY(),
                tm.getTrapezoids().get(i).getV4().getY()
            }, 4);
        }
    }
    
    private void drawPath(GraphicsContext gc, Canvas canvas, List<Guard> guards, Robber robber, 
            double t, Image guardImage, Image robberImage) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        finalizeDraw(gc);
        setUpDraw(gc, false);
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        
        for (int i = 0; i < guards.size(); i++) {
            List<PathGuard> pg = guards.get(i).getPath();
            for (int j = 0; j < pg.size(); j++) {
                int next = (j + 1) % pg.size();
                gc.strokeLine(pg.get(j).getX(), pg.get(j).getY(), pg.get(next).getX(), pg.get(next).getY());
            }
        }
                
        List<PathRobber> pr = robber.getPath();        
        double maxTime = 0.0;
        for (int i = 0; i < pr.size(); i++) {
            if (pr.get(i).getTimestamp() > maxTime) {
                maxTime = pr.get(i).getTimestamp();
            }
        }
        double loopedTime = t % maxTime;
        double[] point;
        PathRobber[] prduo = getPathRobberForTime(t, pr);
        if (prduo[0] != null && prduo[1] != null) {
            point = getInterpolatedPoint(prduo[0], prduo[1], loopedTime);
            gc.drawImage(robberImage, point[0] - (robberImage.getWidth() / 2), 
                    point[1] - (robberImage.getHeight() / 2));
        }
        
        for (int i = 0; i < guards.size(); i++) {
            List<PathGuard> pg = guards.get(i).getPath();
            if (pg.size() == 2) {
                gc.drawImage(guardImage, pg.get(0).getX() - (guardImage.getWidth() / 2), 
                        pg.get(0).getY() - (guardImage.getHeight() / 2));
            } else {
                maxTime = 0.0;
                for (int j = 0; j < pg.size(); j++) {
                    if (pg.get(j).getTimestamp() > maxTime) {
                        maxTime = pg.get(j).getTimestamp();
                    }
                }
                loopedTime = t % maxTime;
                PathGuard[] duo = getPathGuardForTime(loopedTime, pg);
                if (duo[0] != null && duo[1] != null) {
                    point = getInterpolatedPoint(duo[0], duo[1], loopedTime);
                    gc.drawImage(guardImage, point[0] - (guardImage.getWidth() / 2), 
                            point[1] - (guardImage.getHeight() / 2));
                }
            }
        }
    }
    
    private PathGuard[] getPathGuardForTime(double loopedTime, List<PathGuard> pathGuards) {
        PathGuard[] pathGuardDuo = new PathGuard[2];
        for (int i = 0; i < pathGuards.size(); i++) {
            int next = (i + 1) % pathGuards.size();
            if (loopedTime >= pathGuards.get(i).getTimestamp()
                    && loopedTime <= pathGuards.get(next).getTimestamp()) {
                pathGuardDuo[0] = pathGuards.get(i);
                pathGuardDuo[1] = pathGuards.get(next);
                break;
            }
        }        
        return pathGuardDuo;
    }
    
    private PathRobber[] getPathRobberForTime(double loopedTime, List<PathRobber> pathRobbers) {
        PathRobber[] pathRobberDuo = new PathRobber[2];
        for (int i = 0; i < pathRobbers.size(); i++) {
            int next = (i + 1) % pathRobbers.size();
            if (loopedTime >= pathRobbers.get(i).getTimestamp()
                    && loopedTime <= pathRobbers.get(next).getTimestamp()
                    && pathsAreAttached(pathRobbers.get(i), pathRobbers.get(next))) {
                pathRobberDuo[0] = pathRobbers.get(i);
                pathRobberDuo[1] = pathRobbers.get(next);
                System.out.println(loopedTime + " -- prduo: " + i + ", " + next);
                break;
            }
        }        
        return pathRobberDuo;
    }
    
    private double[] getInterpolatedPoint(PathGuard v1, PathGuard v2, double t) {
        double[] point = new double[2];
        double x1 = v1.getX(), y1 = v1.getY();
        double x2 = v2.getX(), y2 = v2.getY();
        double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));  
        double travelTime = v2.getTimestamp() - v1.getTimestamp() - (v1.getObserving() == 1 ? this.deltaTime : 0);
        double stepSize = d / travelTime;
        double n = stepSize * (v1.getObserving() == 1 ? (t - v1.getTimestamp() - this.deltaTime) : t - v1.getTimestamp());
        if (x1 < x2 && y1 < y2) {
            point[0] = x1 + ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 + ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else if (x1 < x2 && y1 > y2) {
            point[0] = x1 + ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 - ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else if (x1 > x2 && y1 < y2) {
            point[0] = x1 - ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 + ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else {
            point[0] = x1 - ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 - ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        }
        return point;
    }
    
    private double[] getInterpolatedPoint(PathRobber v1, PathRobber v2, double t) {
        double[] point = new double[2];
        double x1 = v1.getX(), y1 = v1.getY();
        double x2 = v2.getX(), y2 = v2.getY();
        double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));  
        double travelTime = v2.getTimestamp() - v1.getTimestamp();
        double stepSize = d / travelTime;
        double n = stepSize * (t - v1.getTimestamp());
        if (x1 < x2 && y1 < y2) {
            point[0] = x1 + ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 + ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else if (x1 < x2 && y1 > y2) {
            point[0] = x1 + ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 - ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else if (x1 > x2 && y1 < y2) {
            point[0] = x1 - ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 + ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        } else {
            point[0] = x1 - ((Math.max(0, n) / d) * (Math.abs(x2 - x1)));
            point[1] = y1 - ((Math.max(0, n) / d) * (Math.abs(y2 - y1)));
        }
        return point;
    }
    
    private List<PathGuard> GuardsObserving(List<Guard> guards) {
        List<PathGuard> stopGuards = new ArrayList<>();
        // loop guards
        for (int i = 0; i < guards.size(); i++) {
            // current guard
            Guard g = guards.get(i);
            // storage for previous path guard
            PathGuard ppg = null;
            // loop current guards path
            for (int j = 0; j < g.getPath().size(); j++) {
                // current guards path point
                PathGuard pg = g.getPath().get(j);
                if (pg.getObserving() == 1) {
                    stopGuards.add(pg);
                }
            }
        }
        return stopGuards;                
    }
    
    private List<TimePoint> ComputeTimePoints(List<Guard> guards) {
        // storage for start, end, diff for guard schedules
        List<TimePoint> timePoints = new ArrayList<>();
        // loop guards
        for (int i = 0; i < guards.size(); i++) {
            // current guard
            Guard g = guards.get(i);
            // storage for previous path guard
            PathGuard ppg = null;
            // loop current guards path
            for (int j = 0; j < g.getPath().size(); j++) {
                // current guards path point
                PathGuard pg = g.getPath().get(j);
                // if this is a stopping point
                // connect to the previous stopping point
                // store in timepoint the start and end timestamps
                if (ppg == null && pg.getObserving() == 1) {
                    ppg = pg;
                } else if (ppg != null && pg.getObserving() == 1) {
                    timePoints.add(new TimePoint(ppg.getTimestamp() + this.deltaTime, pg.getTimestamp()));
                    ppg = pg;
                }
            }
        }
        // tp now contains all stopping point time differences, now we have to combine overlapping ones
        // first we order all timepoints based on their starting timestamp
        timePoints.sort(new TimePointComparator());
        // storage for non overlappen timepoints
        List<TimePoint> nonOverlappingTimePoints = new ArrayList<>();
        // loop timepoints
        for (int i = 0; i < timePoints.size() - 1; i++) {
            // current timepoint
            TimePoint tp = timePoints.get(i);
            // next timepoint
            TimePoint ntp = timePoints.get(i + 1);
            
            if (tp.getEnd() <= ntp.getStart()) {
                nonOverlappingTimePoints.add(tp);
            } else {
                // diff between starting points
                double diffSps = ntp.getStart() - tp.getStart();
                // if not the same starting point
                if (diffSps > 0) {
                    // add to non overlapping time points
                    nonOverlappingTimePoints.add(new TimePoint(tp.getStart(), ntp.getStart()));
                }
            }
        }
        if (timePoints.isEmpty()) {
            return null;
        }
        // manually for the last timepoint
        TimePoint tp = timePoints.get(timePoints.size() - 1);
        // diff between starting points
        double diffSps = tp.getEnd() - tp.getStart();
        // if not the same starting point
        if (diffSps > 0) {
            // add to non overlapping time points
            nonOverlappingTimePoints.add(tp);
        }
        return nonOverlappingTimePoints;
    }

    private SortedMap<TimePoint, List<PathRobber>> ComputePossiblePathRobbersNew(Map<Double, List<PathRobber>> pathRobbers, List<PathGuard> stopGuards) {
        // storage for possible paths
        SortedMap<TimePoint, List<PathRobber>> prs = new TreeMap<>(new TimePointComparator());
        // if not enough info, abort mission
        if (pathRobbers == null || pathRobbers.size() < 2) {
            return prs;
        } 
        // otherwise, try to find a valid path
        else {
            // remember time taken to not overlap paths
            Double timeTaken = 0.0;
            
            // compute all forbidden edges for guard stops
            Map<PathGuard, List<Edge>> forbiddenEdges = new HashMap<>(); 
            for (PathGuard pg : stopGuards) {
                List<Edge> visEdges = findVertexRange(pg.getX(), pg.getY());
                forbiddenEdges.put(pg, crossVisiblePath(visEdges));
            }
            
            // loop to match path robbers to guard stops
            for (Map.Entry pair : pathRobbers.entrySet()) {
                Double requiredTime = (Double) pair.getKey();
                boolean pathAllowedTotal = true;
                
                // check for every guard if the path robber is allowed
                for (int i = 0; i < stopGuards.size(); i++) {
                    PathGuard currentGuard = stopGuards.get(i);
                    
                    // if guard stops within the time required for the path robber
                    if (currentGuard.getTimestamp() >= timeTaken && currentGuard.getTimestamp() <= timeTaken + requiredTime) {
                        boolean pathAllowed = true;
                        List<Edge> edges = forbiddenEdges.get(currentGuard);
                        
                        // find if any of the vertices in the path robber is matching with 
                        // any of the vertices of the forbidden path for this particular guard
                        for (PathRobber pr : pathRobbers.get(requiredTime)) {
                            for (Edge e : edges) {
                                if ((e.getV1().getX() == pr.getX() && e.getV1().getY() == pr.getY())
                                        || (e.getV2().getX() == pr.getX() && e.getV2().getY() == pr.getY())) {
                                    pathAllowed = false;
                                    break;
                                }
                            }
                            if (!pathAllowed) {
                                break;
                            }
                        }
                        pathAllowedTotal = pathAllowed;
                        if (!pathAllowedTotal) {
                            break;
                        }
                    }
                }
                
                // if the path is allowed
                if (pathAllowedTotal) {
                    // if not exceeds total time limit
                    if (timeTaken + requiredTime <= this.globalT) {
                        prs.put(new TimePoint(timeTaken, timeTaken + requiredTime), pathRobbers.get(requiredTime));
                        timeTaken = timeTaken + requiredTime;
                    }
                }
            }
        }
        return prs;
    }
    
    private SortedMap<TimePoint, List<PathRobber>> ComputePossiblePathRobbers(Map<Double, List<PathRobber>> pathRobbers, List<TimePoint> timePoints) {
        // storage for possible paths
        SortedMap<TimePoint, List<PathRobber>> prs = new TreeMap<>(new TimePointComparator());
        // storage for taken keys
        List<Double> keysTaken = new ArrayList<>();       
        // if not enough info, abort mission
        if (pathRobbers == null || pathRobbers.size() < 2 
                || timePoints == null || timePoints.isEmpty()) {
            return prs;
        } 
        // otherwise, try to find a valid path
        else {
            // loop timepoints
            for (int i = 0; i < timePoints.size(); i++) {
                // current timepoint
                TimePoint tp = timePoints.get(i);
                // path with least time difference from the diff of tp
                Double ldkey = null;
                // loop map as entry set
                // get every key and compare it to the timepoint
                for (Map.Entry pair : pathRobbers.entrySet()) {
                    Double key = (Double) pair.getKey();
                    // if the path requires less time than the available time
                    // and it is not already used
                    if (key <= tp.getDiff() && (ldkey == null || ldkey < key) && !keysTaken.contains(key)) {
                        ldkey = key;
                    }
                }
                // if there is a key
                if (ldkey != null) {                    
                    // add path to list of possible paths together with start, end, diff times
                    prs.put(tp, pathRobbers.get(ldkey));
                    // mark key as used
                    keysTaken.add(ldkey);
                }
            }
            return prs;
        }
    }
    
    private Robber ComputeRobber(SortedMap<TimePoint, List<PathRobber>> robberPaths) {
        // storage for robber
        Robber robber = new Robber();
        robber.setPath(new ArrayList<>());
        // storage for previous timepoint
//        TimePoint ptp = null;
//        boolean isFirst = true;
        // loop paths
        for (Map.Entry pair : robberPaths.entrySet()) {
            // take key and value from entry pair
            TimePoint key = (TimePoint) pair.getKey();
            List<PathRobber> path = (List<PathRobber>) pair.getValue();
//            // first vertex must start at 0
//            if (isFirst) {
//                if (key.getStart() == 0.0) {
//                    
//                } else {
//                    PathRobber pr = new PathRobber();
//                    pr.setX(0.0);
//                    pr.setY(0.0);
//                    pr.setTimestamp(0.0);
//                }
//            } else {
//                                
//            }
            
            // add additional time to the path vertices to shift it to the correct timepoint
            // this will chain paths to eachother and create a valid total path
            for (int i = 0; i < path.size(); i++) {
                PathRobber pr = path.get(i);
                pr.setTimestamp(pr.getTimestamp() + key.getStart());
            }
            robber.getPath().addAll(path);         
        }
        return robber;
    }
    
    private void drawShapes(Canvas canvas, TrapezoidalMap tm) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        int multiplier = 1;
                
        for (int i = 0; i < tm.getTrapezoids().size(); i++) {
            gc.strokePolygon(new double[] {
                tm.getTrapezoids().get(i).getV1().getX() * multiplier,
                tm.getTrapezoids().get(i).getV2().getX() * multiplier,
                tm.getTrapezoids().get(i).getV3().getX() * multiplier,
                tm.getTrapezoids().get(i).getV4().getX() * multiplier
            }, new double[] {
                tm.getTrapezoids().get(i).getV1().getY() * multiplier,
                tm.getTrapezoids().get(i).getV2().getY() * multiplier,
                tm.getTrapezoids().get(i).getV3().getY() * multiplier,
                tm.getTrapezoids().get(i).getV4().getY() * multiplier
            }, 4);
//            Vertex v = tm.getTrapezoids().get(i).getV1();
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//            
//            v = tm.getTrapezoids().get(i).getV2();
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//            
//            v = tm.getTrapezoids().get(i).getV3();
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//            
//            v = tm.getTrapezoids().get(i).getV4();
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
        }
        
        if (tm.getPossiblePathEdges() != null) {
            for (int i = 0; i < tm.getPossiblePathEdges().size(); i++) {
                Edge e = tm.getPossiblePathEdges().get(i);
                Vertex v1 = e.getV1();
                Vertex v2 = e.getV2();
                gc.setStroke(Color.RED);
                gc.strokeLine(v1.getX(), v1.getY(), v2.getX(), v2.getY());
            }
        }
        
//        for (int i = 0; i < tm.getTriangles().size(); i++) {
//            tm.getTriangles().get(i).print();
//            gc.strokePolygon(new double[] {
//                tm.getTriangles().get(i).getV1().getX() * multiplier,
//                tm.getTriangles().get(i).getV2().getX() * multiplier,
//                tm.getTriangles().get(i).getV3().getX() * multiplier
//            }, new double[] {
//                tm.getTriangles().get(i).getV1().getY() * multiplier,
//                tm.getTriangles().get(i).getV2().getY() * multiplier,
//                tm.getTriangles().get(i).getV3().getY() * multiplier
//            }, 3);
//        }
//        
//        for (int i = 0; i < tm.getTriangles().size(); i++) {
//            Vertex v = tm.getTriangles().get(i).getV1();
//            gc.setFill(v.getColor() == 1 ? Color.RED : 
//                    (v.getColor() == 2 ? Color.GREEN : 
//                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
//            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//            
//            v = tm.getTriangles().get(i).getV2();
//            gc.setFill(v.getColor() == 1 ? Color.RED : 
//                    (v.getColor() == 2 ? Color.GREEN : 
//                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
//            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//            
//            v = tm.getTriangles().get(i).getV3();
//            gc.setFill(v.getColor() == 1 ? Color.RED : 
//                    (v.getColor() == 2 ? Color.GREEN : 
//                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
//            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
//            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
//        }
    }
    
    private boolean checkUniqueX(int x){
        boolean exists = polygon.containsX(x);
        for(Polygon poly : innerPolygon){
            exists = poly.containsX(x);
        }
        return !exists;
    }
    
    @FXML
    private void handleButtonCarina(ActionEvent event) {
        numOfGuards = Integer.parseInt(Guards.getText());
        vMaxG = Double.parseDouble(vMaxGuards.getText());
        deltaTime = Double.parseDouble(deltaT.getText());
        globalT = Double.parseDouble(globalTime.getText());
        List<Vertex> vertices = this.polygon.getVertices();
        int exitCounter = 0;
        int artCounter = 0;
        for (Vertex vertex : vertices){
            if(vertex.getExitFlag()==1){
                exitCounter++;
            }
            if(vertex.getArtFlag()==1){
                artCounter++;
            }
        }
        for (Polygon innerP : innerPolygon) {
            for (Vertex vertex : innerP.getVertices()) {
                if(vertex.getArtFlag()==1){
                artCounter++;
                }
            }
        }
        Gallery gallery = new Gallery(exitCounter, artCounter, polygon, innerPolygon);
        GalleryProblem galleryProblem = new GalleryProblem(gallery, numOfGuards, vMaxG, globalT, deltaTime);
        WriteInputGallerySpecification.WriteInputGallerySpecification(galleryProblem);
        //finalEdge();
    }
    
    @FXML
    private void handleButtonInputRead(ActionEvent event) {
        String workingDir = System.getProperty("user.dir");
//            String dataDir = workingDir.substring(0, workingDir.length() - 13) + "set1_data\\set1_data\\";
        Stage stage = (Stage) this.readInput.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(workingDir));
        fileChooser.setTitle("Open Folder");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String filename = file.getName(); //"ArtGalleryV3.txt";
            GalleryProblem galleryProblem = new GalleryProblem();
            galleryProblem = ReadInputGallerySpecification.readInputArtGallerySpecification(filename);
            Gallery gallery = galleryProblem.getGallery();
            polygon = gallery.getOuterPolygon();
            innerPolygon = gallery.getInnerPolygons();
            Guards.setText(String.valueOf(galleryProblem.getGuards()));
            vMaxGuards.setText(String.valueOf(galleryProblem.getSpeed()));
            deltaT.setText(String.valueOf(galleryProblem.getObservationTime()));
            globalTime.setText(String.valueOf(galleryProblem.getGlobalTime()));
            
            numOfGuards = galleryProblem.getGuards();
            vMaxG = galleryProblem.getSpeed();
            deltaTime = galleryProblem.getObservationTime();
            globalT = galleryProblem.getGlobalTime();
            
            setUpDraw(true);
            finalizeDraw();   
            setUpDraw(false);
        }

    }
    
    private List<Guard> makeGuardList(List<Vertex> verticesPathGuards) {
        guards = new ArrayList();
        int plus = verticesPathGuards.size() / numOfGuards; 
        int currentIndex = 0;
        for (int i = 0; i < numOfGuards; i++ ) {
            Guard guard = new Guard();
            guard = makePathGuard(verticesPathGuards, currentIndex);
            guards.add(guard);
            currentIndex = currentIndex + plus;
            }
        return guards;
    }
    
    private Guard makePathGuard(List<Vertex> verticesPathGuard, int index) {
        double x; 
        double y; 
        double tCurrent;
        List<PathGuard> path = new ArrayList<>();
        
        int firstIndex = index % verticesPathGuard.size();
        Vertex firstVertex = verticesPathGuard.get(firstIndex);
        double initX = firstVertex.getX();
        double initY = firstVertex.getY();
        double initT = 0; 
        
        observing = stopVertices.contains(firstVertex) ? 1 : 0;//observingGuard(firstVertex);
        
        if (verticesPathGuard.size() == 1) {
            observing = 1;
        }
        
        PathGuard step = new PathGuard(initX, initY, initT, observing);
        path.add(step);
        
        double tPrevious = initT; 
        Vertex vertexPrevious = firstVertex;
                  
        for (int i = 1; i < verticesPathGuard.size(); i++) {
            Vertex vertexTemp = verticesPathGuard.get((i + index) % verticesPathGuard.size());
            x = vertexTemp.getX();
            y = vertexTemp.getY();
            observing = stopVertices.contains(vertexTemp) ? 1 : 0;//observingGuard(vertexTemp);
            if (step.getObserving() == 1){
                tCurrent = (distance(vertexPrevious, vertexTemp)/vMaxG ) + tPrevious + deltaTime;
            } else {
                tCurrent = distance(vertexPrevious, vertexTemp)/vMaxG + tPrevious ;
            }
            step = new PathGuard(x, y, tCurrent, observing);
            path.add(step);
            vertexPrevious = vertexTemp; 
            tPrevious = tCurrent;
        }
        observing = stopVertices.contains(firstVertex) ? 1 : 0;//observingGuard(firstVertex);
        if (path.get(0).getObserving() == 1){
                tCurrent = (distance(firstVertex, vertexPrevious)/vMaxG ) + tPrevious + deltaTime;
            } else {
                tCurrent = distance(firstVertex, vertexPrevious)/vMaxG + tPrevious ;
            }
        step = new PathGuard(initX, initY, tCurrent, observing);
        
        path.add(step);
        Guard guard = new Guard(initX, initY, path);
        
        return guard;
    }
    
    private void writeGuardFile(List<Guard> guards) {
        WriteInputGuardSpecification.WriteInputGuardSpecification(guards);
    }
    
    private void writeRobberFile(Robber robber) {
        WriteInputRobberSpecification.WriteInputRobberSpecification(robber);
    }
    
    @FXML
    private void handleButtonGuardRead(ActionEvent event) {
        String workingDir = System.getProperty("user.dir");
        Stage stage = (Stage) this.readGuard.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(workingDir));
        fileChooser.setTitle("Open Folder");
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            String filename = file.getName(); //"ArtGalleryV3.txt";
            String pathname = file.getAbsolutePath();
            //List<Guard> guards = new ArrayList();
            guards = ReadInputGuardSpecification.ReadInputGuardSpecification(pathname);
//            for (Guard guard: guards) {
//                List<PathGuard> path = new ArrayList<PathGuard>();
//                path = guard.getPath(); 
//                double initX = guard.getX();
//                double initY = guard.getY();
//            }
        }
        Stage stage2 = new Stage();
        stage2.setTitle( "Timeline Example" );
        Group root = new Group();
        Scene theScene = new Scene(root);
        stage2.setScene(theScene);

        Canvas canvas = new Canvas(1900, 1000);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        String workingDire = "file:\\\\\\" + System.getProperty("user.dir");        
        Image guardImage = new Image(workingDire + "\\guard.png", 30, 70, false, false);
        Image robberImage = new Image(workingDire + "\\robber.png", 30, 70, false, false);
        
        calculateVisibilityGraph();
        
        List<TimePoint> timePoints = ComputeTimePoints(guards);
        
        Map<Double, List<Vertex>> shortestArtPath = getShortestArtPath();
        
        Map<Double, List<PathRobber>> possiblePathsRobber = possiblePathsRobber(shortestArtPath);
        
        SortedMap<TimePoint, List<PathRobber>> robberPaths = ComputePossiblePathRobbers(possiblePathsRobber, timePoints);
        
        Robber robber = ComputeRobber(robberPaths);
        
        final long startNanoTime = System.nanoTime();
                
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
                drawPath(gc, canvas, guards, robber, t, guardImage, robberImage);
                if (t >= globalT) {
                    this.stop();
                }
            }
        }.start();
        
        stage2.show();
        writeGuardFile(guards);
        writeRobberFile(robber);
    
    }
    
    private void readRobberFile() {
        String workingDir = System.getProperty("user.dir");
//            String dataDir = workingDir.substring(0, workingDir.length() - 13) + "set1_data\\set1_data\\";
        Stage stage = (Stage) this.readInput.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(workingDir));
        fileChooser.setTitle("Open Folder");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String filename = file.getName(); //"ArtGalleryV3.txt";
            //List<Guard> guards = new ArrayList();
            Robber robber = new Robber();
            robber = ReadInputRobberSpecification.ReadInputRobberSpecification(filename);
        }
    }
    
    private Map<Double, List<PathRobber>> possiblePathsRobber(Map<Double, List<Vertex>> verticesPossPaths ) {
        Map<Double, List<PathRobber>> possPaths = new HashMap<>();
        
        for(Entry<Double, List<Vertex>> entry : verticesPossPaths.entrySet()) {
            double distanceLocal = entry.getKey();
            double timePath = 2 *(distanceLocal / vMaxG);
            List<Vertex> verticesLocal = entry.getValue();
            
            List<PathRobber> pathRobber = new ArrayList();
            PathRobber pathRobberStep = new PathRobber();
            
            // first vertex
            Vertex firstVertex = verticesLocal.get(0);
            double initX = firstVertex.getX();
            double initY = firstVertex.getY();
            double prevT = 0;
            pathRobberStep = new PathRobber(initX, initY, prevT);
            pathRobber.add(pathRobberStep);
            Vertex previousVertex = firstVertex;
            
            // rest vertices
            for (int i = 1; i < verticesLocal.size(); i++) {
                Vertex tempVertex = verticesLocal.get(i);
                double x = tempVertex.getX();
                double y = tempVertex.getY();
                double t = prevT + (distance(tempVertex, previousVertex) / vMaxG); 
                pathRobberStep = new PathRobber(x, y, t);
                pathRobber.add(pathRobberStep);
                previousVertex = tempVertex;
                prevT = t;
            }
            // and back : skip last vertex and include first vertex
            for (int i = verticesLocal.size()-1; i > 0; i--) {
                Vertex tempVertex = verticesLocal.get(i-1);
                double x = tempVertex.getX();
                double y = tempVertex.getY();
                double t = prevT + (distance(tempVertex, previousVertex) / vMaxG); 
                pathRobberStep = new PathRobber(x, y, t);
                pathRobber.add(pathRobberStep);
                previousVertex = tempVertex;
                prevT = t;
            }
            possPaths.put(timePath, pathRobber);
        }
        return possPaths;
    }
    
    @FXML
    private void finalEdgeButton(ActionEvent event){
        if(polygon.getEdges().size() != polygon.getVertices().size()){
            List<Vertex> vertices = polygon.getVertices();
            Edge edge = new Edge(vertices.get(0),vertices.get(vertices.size()-1));
            polygon.addEdge(edge);
            for(Polygon poly : innerPolygon){
                List<Vertex> verticesI = poly.getVertices();
                Edge edgeI = new Edge(verticesI.get(0),verticesI.get(verticesI.size()-1));
                poly.addEdge(edgeI);
            }
        }
    }
    
    private void finalEdge(){
        if(polygon.getEdges().size() != polygon.getVertices().size()){
            List<Vertex> vertices = polygon.getVertices();
            Edge edge = new Edge(vertices.get(0),vertices.get(vertices.size()-1));
            polygon.addEdge(edge);
            for(Polygon poly : innerPolygon){
                List<Vertex> verticesI = poly.getVertices();
                Edge edgeI = new Edge(verticesI.get(0),verticesI.get(verticesI.size()-1));
                poly.addEdge(edgeI);
            }
        }
    }

    private double angleValue(Vertex a, Vertex b){
        return Math.atan2(a.getY()-b.getY(), a.getX()-b.getX())*(180/Math.PI);
    }
    
    private List<Vertex> circleSweepSort(Vertex vertex, List<Vertex> allVertices) {
        List<Vertex> sortedList = allVertices;
        
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
        //printSort(sortedList, vertex);
        String s = ""; 
        for(Vertex v : sortedList){
            double angle = angleValue(v, vertex);
            if(angle<0){
                //nextVertices.add(v);
                s = s+", "+v.getLabel();
            }
        }
        System.out.println(s);
        return sortedList;        
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
    
    private List<Edge> findVertexRange(double x, double y){
        calculateVisibilityGraph();
        
        List<Polygon> allPolygons = new ArrayList<>();
        allPolygons.addAll(innerPolygon);
        allPolygons.add(polygon);
        
        List<Edge> edges = new ArrayList<>();
        for(Polygon p : allPolygons){
            edges.addAll(p.getEdges());
        }
        VertexInfo vertexInfo = new VertexInfo();
        for(VertexInfo info : vis.getVertexInfo()){
            if(info.getVertex().getX()==x &&info.getVertex().getY()==y){
                vertexInfo = info;
            }
        }
        List<Vertex> visibleVertices = vertexInfo.getSeeMe();
        
        //Vertex testVertex = vis.getVertexInfo().get(0).getVertex();
        List<Edge> startEdges = new ArrayList<>();
        for(Edge edge : edges){
            if(edge.containsVertex(vertexInfo.getVertex())){
                startEdges.add(edge);
            }
        }
          
        //order list
        visibleVertices = circleSweepSort(vertexInfo.getVertex(), visibleVertices);
        visibleVertices.remove(0);
        visibleVertices.add(visibleVertices.get(0));
        

        List<Edge> noGoEdge = new ArrayList<>();
        noGoEdge.addAll(startEdges);
        Vertex lastVertex = null;
        for(Vertex vertex : visibleVertices){
            //skip first
            if(lastVertex==null){
                lastVertex=vertex;
                continue;
            }
            if(startEdges.get(0).containsVertex(vertex) || startEdges.get(1).containsVertex(vertex)){
                
                if(startEdges.get(0).containsVertex(lastVertex) || startEdges.get(1).containsVertex(lastVertex)){
                    lastVertex=vertex;
                    continue;
                }
            }
            boolean noEdge = true;
            //check if edge exists
            Polygon p = getPolygon(vertex, allPolygons);
            for(Edge e : p.getEdges()){
                if(e.containsVertex(vertex) && e.containsVertex(lastVertex)){
                    noGoEdge.add(e);
                    noEdge = false;
                    break;
                }
            }
            if(noEdge)
                noGoEdge.add(new Edge("",vertex, lastVertex));
            
            lastVertex = vertex;
        }
        return noGoEdge;
    }
    
    @FXML
    private void handleButtonSave(ActionEvent event) {
        calculateVisibilityGraph();
        
        for (int i = 0; i < vis.getVertexInfo().size(); i++) {
            VertexInfo vi = vis.getVertexInfo().get(i);
            if(vi.getSeeMe().isEmpty()){
                int a= 4;
            }
        }
        
        List<Polygon> allPolygons = innerPolygon;
        allPolygons.add(polygon);
        
        List<Edge> edges = new ArrayList<>();
        for(Polygon p : allPolygons){
            edges.addAll(p.getEdges());
        }
        
        
        Vertex testVertex = vis.getVertexInfo().get(0).getVertex();
        List<Edge> startEdges = new ArrayList<>();
        for(Edge edge : edges){
            if(edge.containsVertex(testVertex)){
                startEdges.add(edge);
            }
        }
        
        
        List<Vertex> visibleVertices = vis.getVertexInfo().get(0).getSeeMe();
        
        //order list
        visibleVertices = circleSweepSort(testVertex, visibleVertices);
        visibleVertices.remove(0);
        visibleVertices.add(visibleVertices.get(0));
        
        List<Edge> remember = new ArrayList<>();
        List<Edge> noGoEdge = new ArrayList<>();
        noGoEdge.addAll(startEdges);
        Vertex lastVertex = null;
        for(Vertex vertex : visibleVertices){
            //skip first
            if(lastVertex==null){
                lastVertex=vertex;
                continue;
            }
            if(startEdges.get(0).containsVertex(vertex) || startEdges.get(1).containsVertex(vertex)){
                
                if(startEdges.get(0).containsVertex(lastVertex) || startEdges.get(1).containsVertex(lastVertex)){
                    lastVertex=vertex;
                    continue;
                }
            }
            boolean noEdge = true;
            //check if edge exists
            Polygon p = getPolygon(vertex, allPolygons);
            for(Edge e : p.getEdges()){
                if(e.containsVertex(vertex) && e.containsVertex(lastVertex)){
                    noGoEdge.add(e);
                    noEdge = false;
                    break;
                }
            }
            if(noEdge)
                remember.add(new Edge("",vertex, lastVertex));
            
            lastVertex = vertex;
        }
        
        g.setStroke(Color.RED);
        for(Edge edge : remember){
            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
        }
        
        g.setStroke(Color.BLUE);
        for(Edge edge : noGoEdge){
            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
        }
        

        g.setFill(Color.LIGHTPINK);
        for(Vertex v : vis.getVertexInfo().get(0).getSeeMe()){
                g.fillOval(v.getX()-10, v.getY()-10, 20, 20);
        }
        
        
        g.setFill(Color.ORANGE);
        g.fillOval(vis.getVertexInfo().get(0).getVertex().getX()-10, vis.getVertexInfo().get(0).getVertex().getY()-10, 20, 20);

                
        
    }
    
    @FXML
    private void handleRadioButtonArt(MouseEvent event) {
        if(art.isSelected())
            exit.setSelected(false);
    }
    @FXML
    private void handleRadioButtonExit(MouseEvent event) {
        if(exit.isSelected())
            art.setSelected(false);
    }
    
    @FXML
    private void handleButtonClear(ActionEvent event){
        countArts = count;
        countExits = count;
        g.setFill(Color.WHITE);
        g.setStroke(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        polygon = new Polygon();
        innerPolygon = new ArrayList<>();
        //artList = new Arra
        choice.getSelectionModel().selectFirst();
        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        }
   

    
    @FXML
    private void handleClickAction(MouseEvent e) {
        
        setUpDraw(true);
        
        boolean addArt = art.isSelected();
        boolean addExit = exit.isSelected();
        if(addArt){
            addArt((int)e.getX(), (int)e.getY());
            //setUpDraw();
        }else if(addExit){
            addExit((int)e.getX(), (int)e.getY());
            
        }else{
            if(checkUniqueX((int)e.getX())){
            Poly selectedPolygon = (Poly)choice.getSelectionModel().getSelectedItem();
                if(Poly.POLY == selectedPolygon){
                    g.setFill(Color.BLACK);
                    polygon.joinAndAddNewVertex((int)e.getX(), (int)e.getY());
                    g.fillOval((int)e.getX()-5, (int)e.getY()-5, 10, 10);
                }else{
                    g.setFill(Color.WHITE);
                    Polygon innerPoly = innerPolygon.stream().filter(s->s.getLabel()==selectedPolygon.toString()).findAny().orElse(null);
                    if(innerPoly==null){
                        innerPoly = new Polygon(selectedPolygon.toString());
                        innerPolygon.add(innerPoly);
                    }
                    innerPoly.joinAndAddNewVertex((int)e.getX(), (int)e.getY());
                    g.fillOval((int)e.getX()-5, (int)e.getY()-5, 10, 10);
                }   
            }else{
                JOptionPane.showMessageDialog(new JFrame(), "Same X");
            }
        }
        finalizeDraw();
        setUpDraw(false);
    }
    
    private void setUpDraw(boolean clear){
        if(clear){
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g.setFill(Color.WHITE);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        //polygon
        for(Vertex vertex:polygon.getVertices()){
            if(vertex.getArtFlag()==1){
                    g.setFill(Color.GREEN);
                } else if(vertex.getExitFlag()==1){
                    g.setFill(Color.RED);
                } else{
                    g.setFill(Color.BLACK);
                }
            g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
        }
        //inner
        for(Polygon innerPolygon:innerPolygon){
            for(Vertex vertex:innerPolygon.getVertices()){
                if(vertex.getArtFlag()==1){
                    g.setFill(Color.GREEN);
                }else{
                    g.setFill(Color.WHITE);
                }
                g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
            }
        }
    }
    private void setUpDraw(GraphicsContext g, boolean clear){
        if(clear){
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g.setFill(Color.WHITE);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        //polygon
        for(Vertex vertex:polygon.getVertices()){
            if(vertex.getArtFlag()==1){
                    g.setFill(Color.GREEN);
                } else if(vertex.getExitFlag()==1){
                    g.setFill(Color.RED);
                } else{
                    g.setFill(Color.BLACK);
                }
            g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
        }
        //inner
        for(Polygon innerPolygon:innerPolygon){
            for(Vertex vertex:innerPolygon.getVertices()){
                if(vertex.getArtFlag()==1){
                    g.setFill(Color.GREEN);
                }else{
                    g.setFill(Color.WHITE);
                }
                g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
            }
        }
    }
    
    private void finalizeDraw(){
        g.setFill(Color.BLACK);
        g.fillPolygon(polygon.getXs(), polygon.getYs(), polygon.getNumberVertices());
        g.setFill(Color.WHITE);
        for(Polygon innerPolygon:innerPolygon){
            g.fillPolygon(innerPolygon.getXs(), innerPolygon.getYs(), innerPolygon.getNumberVertices());
        }
    }
    
    private void finalizeDraw(GraphicsContext g){
        g.setFill(Color.BLACK);
        g.fillPolygon(polygon.getXs(), polygon.getYs(), polygon.getNumberVertices());
        g.setStroke(Color.RED);
        for (int i = 0; i < polygon.getVertices().size(); i++) {
            Vertex v = polygon.getVertices().get(i);
            g.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
        }
        g.setFill(Color.WHITE);
        for(Polygon innerPolygon:innerPolygon){
            g.fillPolygon(innerPolygon.getXs(), innerPolygon.getYs(), innerPolygon.getNumberVertices());
            for (int i = 0; i < innerPolygon.getVertices().size(); i++) {
                Vertex v = innerPolygon.getVertices().get(i);
                g.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        polygon = new Polygon();
        innerPolygon = new ArrayList<>();
        g = canvas.getGraphicsContext2D();
        
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        choice.setItems(FXCollections.observableArrayList(Poly.values()));
        choice.getSelectionModel().selectFirst();
    }   

    private void addArt(int xCoordinate, int yCoordinate) {
        Vertex clickPosition = new Vertex((double)xCoordinate, (double)yCoordinate,"");
        Vertex nearstVertex = clickPosition;
        double dist = 100000000.0;
        for(Vertex vertex : polygon.getVertices()){
            double newDistance = distance(vertex, clickPosition);
            if(newDistance<dist){
                nearstVertex = vertex;
                dist = newDistance;
            }
        }
        for(Polygon polygon : innerPolygon){
            for(Vertex vertex : polygon.getVertices()){
                double newDistance = distance(vertex, clickPosition);
                if(newDistance<dist){
                    nearstVertex = vertex;
                    dist = newDistance;
                }
            }
        }
        nearstVertex.setArtFlag(1);
        countArts++;
    }

    private void addExit(int xCoordinate, int yCoordinate) {
        Vertex clickPosition = new Vertex((double)xCoordinate, (double)yCoordinate,"");
        Vertex nearstVertex = clickPosition;
        double dist = 100000000.0;
        for(Vertex vertex : polygon.getVertices()){
            double newDistance = distance(vertex, clickPosition);
            if(newDistance<dist){
                nearstVertex = vertex;
                dist = newDistance;
            }
        }
        nearstVertex.setExitFlag(1);
        countExits++;
    }
    
    private double distance(Vertex vertex1, Vertex vertex2){
        return Math.abs(Math.sqrt(Math.pow(vertex2.getX()-vertex1.getX(),2)+Math.pow(vertex2.getY()-vertex1.getY(),2)));
    }

    private boolean pathsAreAttached(PathRobber pr1, PathRobber pr2) {
        Vertex v1 = new Vertex (pr1.getX(), pr1.getY(), "");
        Vertex v2 = new Vertex (pr2.getX(), pr2.getY(), "");
        
        VertexInfo vi1 = null;
        for (int i = 0; i < vis.getVertexInfo().size(); i++) {
            VertexInfo vi = vis.getVertexInfo().get(i);
            double vix = vi.getVertex().getX();
            double viy = vi.getVertex().getY();
            if (v1.getX() == vix && v1.getY() == viy) {
                vi1 = vi;
            }
        }
        if (vi1 != null) {
            List<Vertex> seeMe = vi1.getSeeMe();
            if (seeMe != null) {
                for (int i = 0; i < seeMe.size(); i++) {
                    if (Objects.equals(seeMe.get(i).getX(), v2.getX())
                            && Objects.equals(seeMe.get(i).getY(), v2.getY())) {
                        return true;
                    }
                }
            }
        }
        return false;
//        
//        double d = distance(v1, v2);
//        double requiredTime = d/vMaxG;
//        if ((Math.abs(pr2.getTimestamp() - pr1.getTimestamp() + 1) >= requiredTime) == false) {
//            System.out.println("REQ: " + Math.abs(pr2.getTimestamp() - pr1.getTimestamp()));
//            System.out.println("REQ: " + requiredTime);
//        }
//        return (Math.abs(pr2.getTimestamp() - pr1.getTimestamp() + 1) >= requiredTime);
    }
    
    public enum Poly{
        POLY,POLY1,POLY2,POLY3,POLY4,POLY5,POLY6,POLY7,POLY8
    }
}
