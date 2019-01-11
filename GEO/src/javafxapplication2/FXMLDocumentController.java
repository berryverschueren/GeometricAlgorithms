/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import geo.WriteInputGallerySpecification;
import geo.ReadInputGallerySpecification;
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
import geo.dataStructures.dummyVis;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
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
    private TextField guards;
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
    private int numOfGuards; 
    private double vMaxG;
    private double deltaTime;
    private double globalT;
    public int countArts = 0;
    public int countExits = 0;
    public final int count = 0;
    
    public Polygon visibilityGraph;
    //private List<Vertex> artList;
    //private List<Vertex> exitList;
    
//    @FXML
//    private void handleButtonGenerate(ActionEvent event){
//        int numberOfVerticesToGenerate = 0;
//        try{
//            numberOfVerticesToGenerate = Integer.parseInt(number.getText().trim());
//        } catch(NumberFormatException nfe){
//            JOptionPane.showMessageDialog(new JFrame(), "Only numbers!");
//        }
//        
//        //todo generator
//    }
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
        ps.addAll(innerPolygon);
        visibilityGraph = new dummyVis().visibiliyGraph(ps);
        g.setStroke(Color.AQUA);
        

//        List<Edge> path = new Graph().dijkstraStart(vis.getEdges(), vis.getVertices().get(0), vis.getVertices().get(vis.getVertices().size()-1), vis.getVertices());
//        
//        for(Edge edge : path){
//            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
//        }
    }
    
    public List<Edge> findPath(List<Vertex> vertices){
        List<Edge> path = new ArrayList<>();
        if(!vertices.isEmpty()){
            if(vertices.size()>2){
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

        TrapezoidalMap tm = new TrapezoidalMap();
        List<Edge> segments = new ArrayList<>();
        segments.addAll(this.polygon.getEdges());
        for (int i = 0; i < this.innerPolygon.size(); i++) {
            segments.addAll(this.innerPolygon.get(i).getEdges());
        }
        
        tm.construct(segments);
        tm.removeInnerTrapezoids(this.innerPolygon);
        tm.removeOuterTrapezoids(this.polygon);
        tm.computePossiblePaths();
        
        String workingDir = "file:\\\\\\" + System.getProperty("user.dir");        
        Image guardImage = new Image(workingDir + "\\guard.png", 40, 40, false, false);

        List<Guard> guards = new ArrayList<>();
        
        final long startNanoTime = System.nanoTime();
        
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 
                drawPath(gc, canvas, tm, guards, t, guardImage);
            }
        }.start();
        
        stage.show();
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
    
    private void drawPath(GraphicsContext gc, Canvas canvas, TrapezoidalMap tm, List<Guard> guards, double t, Image guardImage) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int i = 0; i < tm.getTrapezoids().size(); i++) {
            Trapezoid tz = tm.getTrapezoids().get(i);
            gc.strokePolygon(new double[] { tz.getV1().getX(), tz.getV2().getX(), tz.getV3().getX(), tz.getV4().getX() }, 
                    new double[] { tz.getV1().getY(), tz.getV2().getY(), tz.getV3().getY(), tz.getV4().getY() }, 4);
        }
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        for (int i = 0; i < guards.size(); i++) {
            List<PathGuard> pg = guards.get(i).getPath();
            for (int j = 0; j < pg.size(); j++) {
                int next = (i + 1) % pg.size();
                gc.strokeLine(pg.get(i).getX(), pg.get(i).getY(), pg.get(next).getX(), pg.get(next).getY());
                double maxTime = pg.get(pg.size() - 1).getTimestamp();
                double loopedTime = t % maxTime;
                PathGuard[] duo = getPathGuardForTime(loopedTime, pg);
                double[] point = getInterpolatedPoint(duo[0], duo[1], loopedTime);
                gc.drawImage(guardImage, point[0], point[1]);
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
    
    private double[] getInterpolatedPoint(PathGuard v1, PathGuard v2, double t) {
        double[] point = new double[2];
        double x1 = v1.getX(), y1 = v1.getY();
        double x2 = v2.getX(), y2 = v2.getY();
        double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        double travelTime = v2.getTimestamp() - v1.getTimestamp();
        double stepSize = d / travelTime;
        double n = stepSize * t;
        point[0] = x1 + ((n / d) * (x2 - x1));
        point[1] = y1 + ((n / d) * (y2 - y1));
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
        numOfGuards = Integer.parseInt(guards.getText());
        vMaxG = Integer.parseInt(vMaxGuards.getText());
        deltaTime = Integer.parseInt(deltaT.getText());
        globalT = Integer.parseInt(globalTime.getText());
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
            guards.setText(String.valueOf(galleryProblem.getGuards()));
            vMaxGuards.setText(String.valueOf(galleryProblem.getSpeed()));
            deltaT.setText(String.valueOf(galleryProblem.getObservationTime()));
            globalTime.setText(String.valueOf(galleryProblem.getGlobalTime()));
            setUpDraw(true);
            finalizeDraw();   
            setUpDraw(false);
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
    
    private void finalizeDraw(){
        g.setFill(Color.BLACK);
        g.fillPolygon(polygon.getXs(), polygon.getYs(), polygon.getNumberVertices());
        g.setFill(Color.WHITE);
        for(Polygon innerPolygon:innerPolygon){
            g.fillPolygon(innerPolygon.getXs(), innerPolygon.getYs(), innerPolygon.getNumberVertices());
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
