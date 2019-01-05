/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;

import geo.dataStructures.Edge;
import geo.dataStructures.TrapezoidalMap;
import geo.dataStructures.Vertex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author Berry-PC
 */
public class GEOFX extends Application {
    
    int sliceCount = 0;
    
    @Override
    public void start(Stage primaryStage) {
        TrapezoidalMap tm = new TrapezoidalMap();
        
//        List<Edge> segments = testSet1();
        
//        List<Edge> segments = testSet2();
        
//        List<Edge> segments = testSet3();
        
        List<Edge> segments = testSet4();
        
        //Collections.shuffle(segments);      
        tm.construct(segments.subList(0, sliceCount));
  
        //tm.construct(segments);
        
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent t) -> {
            if (sliceCount >= segments.size()) {
                sliceCount = 0;
            }
            if (t.getClickCount() > 1) {
                sliceCount = 1;
            } else {
                sliceCount++;
            }
            System.out.println("--------------------------------------------------");
            System.out.println("--------------------------------------------------");
            tm.ResetTrapezoidLabels();
            tm.construct(segments.subList(0, sliceCount));
            drawShapes(canvas, tm);  
        });
        
        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(-1);

        scale.pivotYProperty().bind(Bindings.createDoubleBinding(() -> 
            canvas.getBoundsInLocal().getMinY() + canvas.getBoundsInLocal().getHeight() /2, 
            canvas.boundsInLocalProperty()));
        canvas.getTransforms().add(scale);
        drawShapes(canvas, tm);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root, canvas.getWidth() * 2, canvas.getHeight() * 2));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void drawShapes(Canvas canvas, TrapezoidalMap tm) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        int multiplier = 15;
        
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
    
    private List<Edge> testSet1() {
        List<Edge> segments = new ArrayList<>();
        segments.add(new Edge("s4", new Vertex(1.0,6.0,"p4"), new Vertex(4.0,5.0,"q4"))); 
        segments.add(new Edge("s5", new Vertex(9.0,3.0,"p5"), new Vertex(10.0,1.0,"q5"))); 
        segments.add(new Edge("s6", new Vertex(10.0,1.0,"p6"), new Vertex(12.0,3.0,"q6"))); 
        segments.add(new Edge("s1", new Vertex(2.0,3.0,"p1"), new Vertex(6.0,4.0,"q1")));
        segments.add(new Edge("s2", new Vertex(4.0,1.0,"p2"), new Vertex(8.0,2.0,"q2"))); 
        segments.add(new Edge("s3", new Vertex(3.0,3.0,"p3"), new Vertex(5.0,2.0,"q3")));          
        return segments;
    }
    
    private List<Edge> testSet2() {
        List<Edge> segments = new ArrayList<>();
        segments.add(new Edge("s2", new Vertex(6.0,2.0,"p2"), new Vertex(12.0,7.0,"q1")));
        segments.add(new Edge("s4", new Vertex(1.0,6.0,"p4"), new Vertex(5.0,13.0,"q4")));
        segments.add(new Edge("s3", new Vertex(5.0,13.0,"p3"), new Vertex(12.0,7.0,"q3")));          
        segments.add(new Edge("s1", new Vertex(1.0,6.0,"p1"), new Vertex(6.0,2.0,"q1")));          
        return segments;
    }
    
    private List<Edge> testSet3() {
        List<Edge> segments = new ArrayList<>();
        segments.add(new Edge("s1", new Vertex(14.0,2.0,"p1"), new Vertex(23.0,3.0,"q1")));
        segments.add(new Edge("s2", new Vertex(23.0,3.0,"p2"), new Vertex(27.0,8.0,"q2")));
        segments.add(new Edge("s3", new Vertex(27.0,8.0,"p3"), new Vertex(20.0,20.0,"q3")));          
        segments.add(new Edge("s4", new Vertex(20.0,20.0,"p4"), new Vertex(23.0,25.0,"q4")));          
        segments.add(new Edge("s5", new Vertex(23.0,25.0,"p5"), new Vertex(12.0,26.0,"q5")));
        segments.add(new Edge("s6", new Vertex(12.0,26.0,"p6"), new Vertex(6.0,19.0,"q6")));
        segments.add(new Edge("s7", new Vertex(6.0,19.0,"p7"), new Vertex(13.0,15.0,"q7")));          
        segments.add(new Edge("s8", new Vertex(13.0,15.0,"p8"), new Vertex(7.0,10.0,"q8")));          
        segments.add(new Edge("s9", new Vertex(7.0,10.0,"p9"), new Vertex(14.0,2.0,"q8")));
        segments.add(new Edge("s10", new Vertex(15.0,9.0,"p10"), new Vertex(18.0,6.0,"q10")));
        segments.add(new Edge("s11", new Vertex(18.0,6.0,"p11"), new Vertex(21.0,7.0,"q11")));          
        segments.add(new Edge("s12", new Vertex(21.0,7.0,"p12"), new Vertex(20.0,11.0,"q12")));          
        segments.add(new Edge("s13", new Vertex(20.0,11.0,"p13"), new Vertex(16.0,12.0,"q13")));
        segments.add(new Edge("s14", new Vertex(16.0,12.0,"p14"), new Vertex(15.0,9.0,"q14")));
        segments.add(new Edge("s15", new Vertex(15.0,17.0,"p15"), new Vertex(18.0,24.0,"q15")));          
        segments.add(new Edge("s16", new Vertex(18.0,24.0,"p16"), new Vertex(14.0,23.0,"q16")));          
        segments.add(new Edge("s17", new Vertex(14.0,23.0,"p17"), new Vertex(13.0,21.0,"q17")));          
        segments.add(new Edge("s18", new Vertex(13.0,21.0,"p18"), new Vertex(15.0,17.0,"q18")));          
        return segments;
    }
    
    private List<Edge> testSet4() {
        List<Edge> segments = new ArrayList<>();
        segments.add(new Edge("s10", new Vertex(15.0,9.0,"p10"), new Vertex(18.0,6.0,"q10")));
        segments.add(new Edge("s14", new Vertex(16.0,12.0,"p14"), new Vertex(15.0,9.0,"q14")));
        segments.add(new Edge("s16", new Vertex(18.0,24.0,"p16"), new Vertex(14.0,23.0,"q16")));          
        segments.add(new Edge("s5", new Vertex(23.0,25.0,"p5"), new Vertex(12.0,26.0,"q5")));
        segments.add(new Edge("s13", new Vertex(20.0,11.0,"p13"), new Vertex(16.0,12.0,"q13")));
        
        segments.add(new Edge("s1", new Vertex(14.0,2.0,"p1"), new Vertex(23.0,3.0,"q1")));
        segments.add(new Edge("s2", new Vertex(23.0,3.0,"p2"), new Vertex(27.0,8.0,"q2")));
        segments.add(new Edge("s3", new Vertex(27.0,8.0,"p3"), new Vertex(20.0,20.0,"q3")));          
        segments.add(new Edge("s4", new Vertex(20.0,20.0,"p4"), new Vertex(23.0,25.0,"q4")));          
        segments.add(new Edge("s6", new Vertex(12.0,26.0,"p6"), new Vertex(6.0,19.0,"q6")));
        segments.add(new Edge("s7", new Vertex(6.0,19.0,"p7"), new Vertex(13.0,15.0,"q7")));          
        segments.add(new Edge("s8", new Vertex(13.0,15.0,"p8"), new Vertex(7.0,10.0,"q8")));          
        segments.add(new Edge("s9", new Vertex(7.0,10.0,"p9"), new Vertex(14.0,2.0,"q8")));
        segments.add(new Edge("s11", new Vertex(18.0,6.0,"p11"), new Vertex(21.0,7.0,"q11")));          
        segments.add(new Edge("s12", new Vertex(21.0,7.0,"p12"), new Vertex(20.0,11.0,"q12")));          
        segments.add(new Edge("s15", new Vertex(15.0,17.0,"p15"), new Vertex(18.0,24.0,"q15")));          
        segments.add(new Edge("s17", new Vertex(14.0,23.0,"p17"), new Vertex(13.0,21.0,"q17")));          
        segments.add(new Edge("s18", new Vertex(13.0,21.0,"p18"), new Vertex(15.0,17.0,"q18")));          
        return segments;
    }
}
