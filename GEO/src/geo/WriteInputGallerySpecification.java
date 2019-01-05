package geo;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import geo.dataStructures.Edge;
import geo.dataStructures.Gallery;
import geo.dataStructures.GalleryProblem;
import geo.dataStructures.Polygon;
import geo.dataStructures.Vertex;
/**
 *
 * @author carina
 */
public class WriteInputGallerySpecification {
    String fileName = "temp.txt";
    public static int numOfVertices;
    public static int numOfHoles;
    public static int numOfExits;
    public static int numOfArts;
    public static int numOfGuards;
    public static double vmaxGuards;
    public static double globalTime; //Global time limit
    public static double deltaT; //Observation time guards 
    //public String filename; //name file GallerySpecification
    
    public static double x; // x coordinate
    public static double y; // y coordinate
    public static int artFlag; // 1 art; 0 no art
    public static int exitFlag; // 1 exit; 0 no exit
    
    public WriteInputGallerySpecification(GalleryProblem galleryProblem) {
        
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            Gallery gallery = galleryProblem.getGallery();
            Polygon outerPolygon = gallery.getOuterPolygon();
            List<Vertex> verticesOuterPolygon = outerPolygon.getVertices();
            List<Polygon> innerPolygons = gallery.getInnerPolygons();
            bufferedWriter.write(numOfGuards);
            bufferedWriter.newLine();
            
            // line 1
            numOfVertices = verticesOuterPolygon.size();
            numOfHoles = innerPolygons.size();
            numOfExits = gallery.getExits();
            numOfArts = gallery.getArtPieces();
            
            bufferedWriter.write(numOfVertices + ", " + numOfHoles + ", " + numOfExits + ", " + numOfArts + ", ");
            bufferedWriter.newLine();
            
            for (Polygon innerPolygon : innerPolygons ) {
                List<Vertex> verticesHole = innerPolygon.getVertices();
                System.out.println(verticesHole.size() + ", ");
            }
            
            bufferedWriter.newLine();
            
            //g, vmax, T, deltaT
            numOfGuards = galleryProblem.getGuards();
            vmaxGuards = galleryProblem.getSpeed();
            globalTime = galleryProblem.getGlobalTime();
            deltaT = galleryProblem.getObservationTime();
            
            bufferedWriter.write(numOfGuards + ", " + vmaxGuards + ", " + globalTime + ", " + deltaT + ", ");
            bufferedWriter.newLine();
            
            for (Vertex vertexOuterPolygon : verticesOuterPolygon) {
                x = vertexOuterPolygon.getX();
                y = vertexOuterPolygon.getY();
                artFlag = vertexOuterPolygon.getArtFlag();
                exitFlag = vertexOuterPolygon.getExitFlag();
                bufferedWriter.write(x + ", " + y + ", " + artFlag + ", " + exitFlag + ", ");
                bufferedWriter.newLine();
            }
            
            for (Polygon innerPolygon : innerPolygons) {
                List<Vertex> verticesHole = innerPolygon.getVertices();
                for (Vertex verticeHole : verticesHole) {
                    verticeHole.getX();
                    verticeHole.getY();
                    verticeHole.getArtFlag();
                    bufferedWriter.write(x + ", " + y + ", " + artFlag + ", ");
                    bufferedWriter.newLine();
                }
            }
            
            bufferedWriter.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    } 
}

