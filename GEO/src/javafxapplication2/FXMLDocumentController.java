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
    private double vMaxG;
    private double deltaTime;
    private double globalT;
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
        Vertex v1, v2, v3;
        Edge e1,e2,e3;

        
        Polygon poly2 = new Polygon();
        poly2.setLabel("polygon2");
        
        v1 = new Vertex(1.0, 1.0, "p1");
        v2 = new Vertex(3.0, 1.0, "p2");
        v3 = new Vertex(2.0, 2.0, "p3");
        e1 = new Edge("e1", v1, v2);
        e2 = new Edge("e2", v2, v3);
        e3 = new Edge("e3", v3, v1);
        poly2.addVertex(v1);
        poly2.addVertex(v2);
        poly2.addVertex(v3);
        poly2.addEdge(e1);
        poly2.addEdge(e2);
        poly2.addEdge(e3);
        
        testPolygons.add(poly2);
        
        
        
        Polygon poly3 = new Polygon();
        poly3.setLabel("polygon3");
        
        v1 = new Vertex(5.0, 1.0, "pp1");
        v2 = new Vertex(6.0, 4.0, "pp2");
        v3 = new Vertex(4.0, 2.0, "pp3");
        e1 = new Edge("ee1", v1, v2);
        e2 = new Edge("ee2", v2, v3);
        e3 = new Edge("ee3", v3, v1);
        poly3.addVertex(v1);
        poly3.addVertex(v2);
        poly3.addVertex(v3);
        poly3.addEdge(e1);
        poly3.addEdge(e2);
        poly3.addEdge(e3);
                
        testPolygons.add(poly3);
        

        //innerPolygon = testPolygons;
        for(Polygon poly : testPolygons){
            Polygon p = new Polygon();
            for(Vertex vertex : poly.getVertices()){
                Vertex v = new Vertex(vertex.getX()*100, vertex.getY()*100, "");
                p.addVertex(v);
            }
            innerPolygon.add(p);
        }
        polygon.addVertex(new Vertex(0.0,0.0,""));
        polygon.addVertex(new Vertex(700.0,0.0,""));
        polygon.addVertex(new Vertex(700.0,700.0,""));
        polygon.addVertex(new Vertex(0.0,700.0,""));
        
        setUpDraw(true);
                
        
        //finalEdge();
        VisibilityGraph graph = new VisibilityGraph();
        //List<Polygon> polys = new ArrayList<>();
        //polys.add(polygon);
        //polys.addAll(innerPolygon);
        Polygon poly = graph.visibilityGraphAlgorithm(testPolygons);
        
//        g.setFill(Color.WHITE);
//        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        g.setFill(Color.BLACK);
//
//        for(Edge edge : poly.getEdges()){
//            g.strokeLine(10*edge.getV1().getX(), 10*edge.getV1().getY(), 10*edge.getV2().getX(), 10*edge.getV2().getY());
//        }
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
        tm.colorTriangles();

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
        
//        for (int i = 0; i < tm.getTrapezoids().size(); i++) {
//            gc.strokePolygon(new double[] {
//                tm.getTrapezoids().get(i).getV1().getX() * multiplier,
//                tm.getTrapezoids().get(i).getV2().getX() * multiplier,
//                tm.getTrapezoids().get(i).getV3().getX() * multiplier,
//                tm.getTrapezoids().get(i).getV4().getX() * multiplier
//            }, new double[] {
//                tm.getTrapezoids().get(i).getV1().getY() * multiplier,
//                tm.getTrapezoids().get(i).getV2().getY() * multiplier,
//                tm.getTrapezoids().get(i).getV3().getY() * multiplier,
//                tm.getTrapezoids().get(i).getV4().getY() * multiplier
//            }, 4);
//        }
        
        for (int i = 0; i < tm.getTriangles().size(); i++) {
            tm.getTriangles().get(i).print();
            gc.strokePolygon(new double[] {
                tm.getTriangles().get(i).getV1().getX() * multiplier,
                tm.getTriangles().get(i).getV2().getX() * multiplier,
                tm.getTriangles().get(i).getV3().getX() * multiplier
            }, new double[] {
                tm.getTriangles().get(i).getV1().getY() * multiplier,
                tm.getTriangles().get(i).getV2().getY() * multiplier,
                tm.getTriangles().get(i).getV3().getY() * multiplier
            }, 3);
        }
        
        for (int i = 0; i < tm.getTriangles().size(); i++) {
            Vertex v = tm.getTriangles().get(i).getV1();
            gc.setFill(v.getColor() == 1 ? Color.RED : 
                    (v.getColor() == 2 ? Color.GREEN : 
                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
            
            v = tm.getTriangles().get(i).getV2();
            gc.setFill(v.getColor() == 1 ? Color.RED : 
                    (v.getColor() == 2 ? Color.GREEN : 
                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
            
            v = tm.getTriangles().get(i).getV3();
            gc.setFill(v.getColor() == 1 ? Color.RED : 
                    (v.getColor() == 2 ? Color.GREEN : 
                            (v.getColor() == 3 ? Color.BLUE : Color.BLACK)));
            gc.fillOval(v.getX() - 5, v.getY() - 5, 10, 10);
            gc.strokeText("(" + Math.floor(v.getX()) + ", " + Math.floor(v.getY()) + ")", v.getX(), v.getY() - 10);
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
        
        String filename = "ArtGalleryV3.txt";
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
