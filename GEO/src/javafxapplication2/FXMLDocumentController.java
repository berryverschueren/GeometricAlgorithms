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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Scale;
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
    
    private GraphicsContext g; 
    private Polygon polygon;
    private List<Polygon> innerPolygon;
    private int numOfGuards; 
    private int vMaxG;
    private int deltaTime;
    private int globalT;
    public int countArts = 0;
    public int countExits = 0;
    public final int count = 0;
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
        
        List<Polygon> testPolygons = new ArrayList<>();
//        Polygon poly1 = new Polygon();
//        poly1.setLabel("polygon1");
        Vertex v1, v2;
        Edge e1;
//        
//        v1 = new Vertex(10.0, 2.0, "p1");
//        v2 = new Vertex(24.0, 3.0, "q1");
//        e1 = new Edge("s1", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(24.0, 3.0, "p2");
//        v2 = new Vertex(27.0, 8.0, "q2");
//        e1 = new Edge("s2", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(27.0, 8.0, "p3");
//        v2 = new Vertex(20.0, 20.0, "q3");
//        e1 = new Edge("s3", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(20.0, 20.0, "p4");
//        v2 = new Vertex(23.0, 25.0, "q4");
//        e1 = new Edge("s4", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(23.0, 25.0, "p5");
//        v2 = new Vertex(12.0, 26.0, "q5");
//        e1 = new Edge("s5", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(12.0, 26.0, "p6");
//        v2 = new Vertex(6.0, 19.0, "q6");
//        e1 = new Edge("s6", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(6.0, 19.0, "p7");
//        v2 = new Vertex(11.0, 15.0, "q7");
//        e1 = new Edge("s7", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(11.0, 15.0, "p8");
//        v2 = new Vertex(7.0, 10.0, "q8");
//        e1 = new Edge("s8", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        v1 = new Vertex(7.0, 10.0, "p9");
//        v2 = new Vertex(10.0, 2.0, "q9");
//        e1 = new Edge("s9", v1, v2);
//        poly1.addVertex(v1);
//        poly1.addVertex(v2);
//        poly1.addEdge(e1);
//        
//        polygons.add(poly1);
        
        Polygon poly2 = new Polygon();
        poly2.setLabel("polygon2");
        
        v1 = new Vertex(16.0, 9.0, "p10");
        v2 = new Vertex(19.0, 6.0, "q10");
        e1 = new Edge("s10", v1, v2);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addEdge(e1);
        
        v1 = new Vertex(19.0, 6.0, "p11");
        v2 = new Vertex(22.0, 7.0, "q11");
        e1 = new Edge("s11", v1, v2);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addEdge(e1);
        
        v1 = new Vertex(22.0, 7.0, "p12");
        v2 = new Vertex(21.0, 11.0, "q12");
        e1 = new Edge("s12", v1, v2);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addEdge(e1);
        
        v1 = new Vertex(21.0, 11.0, "p13");
        v2 = new Vertex(17.0, 12.0, "q13");
        e1 = new Edge("s13", v1, v2);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addEdge(e1);
        
        v1 = new Vertex(17.0, 12.0, "p14");
        v2 = new Vertex(16.0, 9.0, "q14");
        e1 = new Edge("s14", v1, v2);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addEdge(e1);
        
        testPolygons.add(poly2);
        
        Polygon poly3 = new Polygon();
        poly3.setLabel("polygon3");
        
        v1 = new Vertex(15.0, 17.0, "p15");
        v2 = new Vertex(18.0, 24.0, "q15");
        e1 = new Edge("s15", v1, v2);
        poly3.addVertex(v1);
        poly3.addVertex(v2);
        poly3.addEdge(e1);
        
        v1 = new Vertex(18.0, 24.0, "p16");
        v2 = new Vertex(14.0, 23.0, "q16");
        e1 = new Edge("s16", v1, v2);
        poly3.addVertex(v1);
        poly3.addVertex(v2);
        poly3.addEdge(e1);
        
        v1 = new Vertex(14.0, 23.0, "p17");
        v2 = new Vertex(13.0, 21.0, "q17");
        e1 = new Edge("s17", v1, v2);
        poly3.addVertex(v1);
        poly3.addVertex(v2);
        poly3.addEdge(e1);
        
        v1 = new Vertex(13.0, 21.0, "p18");
        v2 = new Vertex(15.0, 17.0, "q18");
        e1 = new Edge("s18", v1, v2);
        poly3.addVertex(v1);
        poly3.addVertex(v2);
        poly3.addEdge(e1);
        
        testPolygons.add(poly3);
        

    
        
        //finalEdge();
        VisibilityGraph graph = new VisibilityGraph();
        //List<Polygon> polys = new ArrayList<>();
        //polys.add(polygon);
        //polys.addAll(innerPolygon);
        Polygon poly = graph.visibilityGraphAlgorithm(testPolygons);
        
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.BLACK);

        for(Edge edge : poly.getEdges()){
            g.strokeLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
        }
    }
    
    @FXML
    private void handleButtonBerry(ActionEvent event) {
        finalEdge();
        Group root = new Group();
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");


        TrapezoidalMap tm = new TrapezoidalMap();
        
        List<Edge> segments = new ArrayList<>();
        segments.addAll(this.polygon.getEdges());
        for (int i = 0; i < this.innerPolygon.size(); i++) {
            segments.addAll(this.innerPolygon.get(i).getEdges());
        }
        
        Collections.shuffle(segments);      
        tm.construct(segments);

        tm.removeInnerTrapezoids(this.innerPolygon);
        tm.removeOuterTrapezoids(this.polygon);
        tm.triangulateTrapezoids();

        Canvas canvas = new Canvas(900, 700);
        
        drawShapes(canvas, tm);
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root, canvas.getWidth(), canvas.getHeight()));
        stage.show();
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
        }
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
        String filename = "ArtGalleryV1.txt";
        GalleryProblem galleryProblem = new GalleryProblem();
        galleryProblem = ReadInputGallerySpecification.readInputArtGallerySpecification(filename);
        //DRAW galleryProblem
        //finalEdge();
    }
    
    
    private void finalEdge(){
        List<Vertex> vertices = polygon.getVertices();
        Edge edge = new Edge(vertices.get(0),vertices.get(vertices.size()-1));
        polygon.addEdge(edge);
        for(Polygon poly : innerPolygon){
            List<Vertex> verticesI = poly.getVertices();
            Edge edgeI = new Edge(verticesI.get(0),verticesI.get(verticesI.size()-1));
            poly.addEdge(edgeI);
        }
    }

    @FXML
    private void handleButtonSave(ActionEvent event) {
        VisibilityGraph graph = new VisibilityGraph();
        List<Polygon> poly = new ArrayList<>();
        poly.add(polygon);
        poly.addAll(innerPolygon);

        graph.visibilityGraphAlgorithm(poly);
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
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        polygon = new Polygon();
        innerPolygon = new ArrayList<>();
        //artList = new Arra
        choice.getSelectionModel().selectFirst();
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
                JOptionPane.showMessageDialog(new JFrame(), "Same X!!!!!");
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
