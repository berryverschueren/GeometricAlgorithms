/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import geo.WriteInputGallerySpecification;
import geo.ReadInputGallerySpecification;
import geo.ReadInputGuardSpecification;
import geo.WriteInputGuardSpecification;
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
import geo.dataStructures.Trapezoid;
import geo.dataStructures.VertexInfo;
import geo.dataStructures.dummyVis;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    
    
    private Map<Double, List<Vertex>> getExitToArtPath(){
        Map<Double, List<Vertex>> robberPaths = new TreeMap();

        for(Vertex exit : vis.getExitList()){
            for(Vertex art : vis.getArtList()){
                Graph graph = new Graph();
                List<Vertex> path = findSinglePathWithGraph(exit,art, graph);
                path.add(art);
                robberPaths.put(graph.getCostCurrentPath(), path);
            } 
        }   
        return robberPaths;
    }
    
    private Map<Double, List<List<Vertex>>> getAllExitToArtPath(){
        Map<Double, List<List<Vertex>>> robberPaths = new TreeMap();

        for(Vertex exit : vis.getExitList()){
            for(Vertex art : vis.getArtList()){
                Graph graph = new Graph();
                List<Vertex> path = findSinglePathWithGraph(exit,art, graph);
                path.add(art);
                addToTree(graph.getCostCurrentPath(), path, robberPaths);
            } 
        }   
        return robberPaths;
    }
    
    private void addToTree(double cost, List<Vertex> path, Map<Double, List<List<Vertex>>> tree){
        List<List<Vertex>> allPaths = new ArrayList<>();
        if(tree.containsKey(cost)){
            allPaths = tree.get(cost);
            allPaths.add(path);
        }else{
            allPaths.add(path);      
        }
        tree.put(cost, allPaths);
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
//>>>>>>> b1efeab0ffdbba3673f5d51f616bc8b44ae54ff1

        //shortestPath = findPath(interestingVertices);
        return shortestPath;
    }
    
    @FXML
    private void handleButtonKaj(ActionEvent event) {

        finalEdge();
//        int edgeCounter = 0;
//        int vertexCounter = 0;
//        
//        
//        Polygon p = polygon;
//        p.setLabel("Polygon");
//        for(Edge edge : p.getEdges()){
//            edge.setLabel(p.getLabel()+" : Edge."+edgeCounter+"  ");
//            edgeCounter++;
//        }
//        for(Vertex vertex : p.getVertices()){
//            vertex.setLabel(p.getLabel()+" : Vertex."+vertexCounter+"  ");
//            vertexCounter++;
//        }
//        
        List<Polygon> ps = innerPolygon;
//        int pcount = 0;
//        for(Polygon poly : ps){
//            poly.setLabel("inner Polygon"+pcount);
//            edgeCounter = 0;
//            vertexCounter = 0;
//            for(Edge edge : poly.getEdges()){
//                edge.setLabel(poly.getLabel()+" : Edge."+edgeCounter+"  ");
//                edgeCounter++;
//            }
//            for(Vertex vertex : poly.getVertices()){
//                vertex.setLabel(poly.getLabel()+" : Vertex."+vertexCounter+"  ");
//                vertexCounter++;
//            }
//            pcount++;
//        }
//        
        System.out.println("");
        System.out.println("");
        //System.out.println(ps.get(0).getVertices().get(0).getLabel());
        
        ps.add(polygon);
        //ps.addAll(innerPolygon);
        vis =new dummyVis();
        visibilityGraph = vis.visibiliyGraph(ps);
        

//        List<Edge> path = new Graph().dijkstraStart(vis.getEdges(), vis.getVertices().get(0), vis.getVertices().get(vis.getVertices().size()-1), vis.getVertices());
        
//        for(Edge edge : visibilityGraph.getEdges()){
//            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
//        }
        
        List<Vertex> ve = vis.getBestExitGuards();
        List<Vertex> v = findPath(ve);
        setUpDraw(false);
        g.setStroke(Color.RED);
        for (int i = 0; i < v.size()-1; i++) {
            g.strokeLine(v.get(i).getX(), v.get(i).getY(), v.get(i+1).getX(), v.get(i+1).getY());
        }
        
    }
    
    public void calculateVisibilityGraph(){
        finalEdge();
        List<Polygon> polys = new ArrayList<>();
        polys.add(polygon);
        polys.addAll(innerPolygon);
        vis = new dummyVis();
        
                g.setStroke(Color.AQUA);

        visibilityGraph = vis.visibiliyGraph(polys);
        for(Edge edge : visibilityGraph.getEdges()){
            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
        }
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
    
    @FXML
    private void handleButtonBerry(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle( "Timeline Example" );
        Group root = new Group();
        Scene theScene = new Scene(root);
        stage.setScene(theScene);

        Canvas canvas = new Canvas(1000, 1000);
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

        calculateVisibilityGraph();
        List<Vertex> interestingVertices = new ArrayList<>(); // vis.getBestExitGuards();

        List<Vertex> verts = this.polygon.getVertices();
        int exitCounter = 0;
        for (int i = 0; i < verts.size(); i++) {
            if (verts.get(i).getExitFlag() == 1) {
                interestingVertices.add(verts.get(i));
                exitCounter++;
            }
        }
        
        List<Vertex> verticesForGuardPath = getSmartSmartPath(numOfGuards, exitCounter); //findPath(interestingVertices);
        List<Guard> guards = makeGuardList(verticesForGuardPath);
        
        final long startNanoTime = System.nanoTime();
                
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
                drawPath(gc, canvas, guards, t, guardImage);
                if (t >= globalT) {
                    this.stop();
                }
            }
        }.start();
        
        stage.show();
        writeGuardFile(guards);
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
    
    private void drawPath(GraphicsContext gc, Canvas canvas, List<Guard> guards, double t, Image guardImage) {
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
        
        for (int i = 0; i < guards.size(); i++) {
            List<PathGuard> pg = guards.get(i).getPath();
            double maxTime = 0.0;
            for (int j = 0; j < pg.size(); j++) {
                if (pg.get(j).getTimestamp() > maxTime) {
                    maxTime = pg.get(j).getTimestamp();
                }
            }
            double loopedTime = t % maxTime;
            PathGuard[] duo = getPathGuardForTime(loopedTime, pg);
            double[] point = getInterpolatedPoint(duo[0], duo[1], loopedTime);
            gc.drawImage(guardImage, point[0] - (guardImage.getWidth() / 2), point[1] - (guardImage.getHeight() / 2));
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
    
    private double[] getInterpolatedPoint(PathGuard v1, PathGuard v2, double t) {
        double[] point = new double[2];
        if (v1 == null || v1.getX() == 0.0|| v1.getY() == 0.0
                || v2 == null || v2.getX() == 0.0 || v2.getY() == 0.0) {
            System.out.println("HELP");
        }
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
        Gallery gallery = new Gallery(countExits, countArts, polygon, innerPolygon);
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
    
    private int observingGuard(Vertex vertex) {
        if (vertex.getArtFlag()== 1 || vertex.getExitFlag() == 1) {
                observing = 1;
            } else {
                observing = 0; 
            }
        return observing;
    }
    
    private void writeGuardFile(List<Guard> guards) {
        WriteInputGuardSpecification.WriteInputGuardSpecification(guards);
    }
    
    private void readGuardFile() {
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
            guards = ReadInputGuardSpecification.ReadInputGuardSpecification(filename);
        }
        
        
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

    @FXML
    private void handleButtonSave(ActionEvent event) {
        calculateVisibilityGraph();
        getAllExitToArtPath();
        int a=2;
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
    
    public enum Poly{
        POLY,POLY1,POLY2,POLY3,POLY4,POLY5,POLY6,POLY7,POLY8
    }
}
