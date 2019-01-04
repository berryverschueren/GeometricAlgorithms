/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import geo.dataStructures.Polygon;
import geo.dataStructures.Vertex;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
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
    private TextField number;
    
    private GraphicsContext g; 
    private Polygon polygon;
    private List<Polygon> innerPolygons;
    
    @FXML
    private void handleButtonGenerate(ActionEvent event){
        int numberOfVerticesToGenerate = 0;
        try{
            numberOfVerticesToGenerate = Integer.parseInt(number.getText().trim());
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(new JFrame(), "Only numbers!");
        }
        
        //todo generator
    }

    @FXML
    private void handleButtonSave(ActionEvent event) {
        //todo:convertor
    }
    
    @FXML
    private void handleButtonClear(ActionEvent event){
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        polygon = new Polygon();
        innerPolygons = new ArrayList<>();
        choice.getSelectionModel().selectFirst();
    }
   
    @FXML
    private void handleClickAction(MouseEvent e) {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.BLACK);
        for(Vertex vertex:polygon.getVertices()){
            g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
        }
        g.setFill(Color.WHITE);
        for(Polygon innerPolygon:innerPolygons){
            for(Vertex vertex:innerPolygon.getVertices()){
                g.fillOval(vertex.getX()-5, vertex.getY()-5, 10, 10);
            }
        }
        
        Poly selectedPolygon = (Poly)choice.getSelectionModel().getSelectedItem();
        if(Poly.POLY == selectedPolygon){
            g.setFill(Color.BLACK);
            polygon.joinAndAddNewVertex((int)e.getX(), (int)e.getY());
            
            g.fillOval((int)e.getX()-5, (int)e.getY()-5, 10, 10);
        }else{
            g.setFill(Color.WHITE);
            Polygon innerPolygon = innerPolygons.stream().filter(s->s.getLabel()==selectedPolygon.toString()).findAny().orElse(null);
            if(innerPolygon==null){
                innerPolygon = new Polygon(selectedPolygon.toString());
                innerPolygons.add(innerPolygon);
            }
            innerPolygon.joinAndAddNewVertex((int)e.getX(), (int)e.getY());
            g.fillOval((int)e.getX()-5, (int)e.getY()-5, 10, 10);
        }   
        
        g.setFill(Color.BLACK);
        g.fillPolygon(polygon.getXs(), polygon.getYs(), polygon.getNumberVertices());
        g.setFill(Color.WHITE);
        for(Polygon innerPolygon:innerPolygons){
            g.fillPolygon(innerPolygon.getXs(), innerPolygon.getYs(), innerPolygon.getNumberVertices());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        polygon = new Polygon();
        innerPolygons = new ArrayList<>();
        g = canvas.getGraphicsContext2D();
        
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        choice.setItems(FXCollections.observableArrayList(Poly.values()));
        choice.getSelectionModel().selectFirst();
    }   
    
    public enum Poly{
        POLY,POLY1,POLY2,POLY3,POLY4,POLY5,POLY6,POLY7,POLY8
    }
}
