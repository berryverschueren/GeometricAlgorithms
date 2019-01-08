/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import geo.dataStructures.Edge;
import geo.dataStructures.Polygon;
import geo.dataStructures.TrapezoidalMap;
import geo.dataStructures.Vertex;
import geo.dataStructures.VisibilityGraph;
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
    private RadioButton art;
    @FXML
    private RadioButton exit;
    
    private GraphicsContext g; 
    private Polygon polygon;
    private List<Polygon> innerPolygon;
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
        int a=0;
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
        finalEdge();
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
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        polygon = new Polygon();
        innerPolygon = new ArrayList<>();
        //artList = new Arra
        choice.getSelectionModel().selectFirst();
    }
   
    @FXML
    private void handleClickAction(MouseEvent e) {
        if(checkUniqueX((int)e.getX())){
        setUpDraw(true);
        
        boolean addArt = art.isSelected();
        boolean addExit = exit.isSelected();
        if(addArt){
            addArt((int)e.getX(), (int)e.getY());
            //setUpDraw();
        }else if(addExit){
            addExit((int)e.getX(), (int)e.getY());
            
        }else{
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
        }
        finalizeDraw();
        setUpDraw(false);
        }else{
            JOptionPane.showMessageDialog(new JFrame(), "Same X!!!!!");
        }
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
    }
    
    private double distance(Vertex vertex1, Vertex vertex2){
        return Math.abs(Math.sqrt(Math.pow(vertex2.getX()-vertex1.getX(),2)+Math.pow(vertex2.getY()-vertex1.getY(),2)));
    }
    
    public enum Poly{
        POLY,POLY1,POLY2,POLY3,POLY4,POLY5,POLY6,POLY7,POLY8
    }
}
