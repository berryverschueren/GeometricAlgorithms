package geo;

/**
 *
 * @author carina
 */
import geo.dataStructures.Edge;
import geo.dataStructures.Gallery;
import geo.dataStructures.GalleryProblem;
import geo.dataStructures.Polygon;
import geo.dataStructures.Vertex;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadInputGallerySpecification extends Vertex {
    
    /** Variables */
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
    
    /** Data structures */
    public static List<Polygon> innerPolygons = new ArrayList<Polygon>();
    public static Polygon outerPolygon = new Polygon();
    public static List<Edge> edges = new ArrayList();
    public static List<Vertex> vertices = new ArrayList();
    public static Gallery gallery = new Gallery();
    public static GalleryProblem galleryProblem = new GalleryProblem();
    
    public static void readInputArtGallerySpecification(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);

            input.useDelimiter("[, \n]");
            
            /** line 1 */
            numOfVertices = input.nextInt();
            input.next();
            numOfHoles = input.nextInt();
            input.next();
            numOfExits = input.nextInt();
            input.next();
            numOfArts = input.nextInt();
            
            /** line 2 */
            int[] verticesPerHole = new int[numOfHoles];
            
            for (int i = 0; i < numOfHoles; i++) {
                verticesPerHole[i] = input.nextInt();
                input.next();
            }
            
            /** line 3 */
            numOfGuards = input.nextInt();
            input.next();
            vmaxGuards = input.nextDouble();
            input.next();
            globalTime = input.nextDouble();
            input.next();
            deltaT = input.nextDouble();
            
            /** line 4 to 4+n */
            
            //first vertex, to ensure first edge can be made
            x = input.nextDouble();
            input.next();
            y = input.nextDouble();
            input.next();
            artFlag = input.nextInt();
            input.next();
            exitFlag = input.nextInt();

            Vertex firstVertex = new Vertex(x, y, artFlag, exitFlag, "label");
            //System.out.println("first vertex: x=" + x + ", y = " + y);
            
            Vertex oldVertex = firstVertex;
            
            //rest of vertices
            for (int j = 0; j < numOfVertices-1; j++) {
                x = input.nextDouble();
                input.next();
                y = input.nextDouble();
                input.next();
                artFlag = input.nextInt();
                input.next();
                exitFlag = input.nextInt();
                //System.out.println("next vertex: x=" + x + ", y = " + y);
                //System.out.println("previous vertex: x = " + oldVertex.getX() + " y = " + oldVertex.getY());
                Vertex vertex = new Vertex(x, y, artFlag, exitFlag, "label");
                Edge edge = new Edge("label", oldVertex, vertex);
                
                vertices.add(vertex);
                edges.add(edge);
                oldVertex = vertex;
            }
            //last edge
            Edge closingEdge = new Edge("label", oldVertex, firstVertex);
            edges.add(closingEdge);
            outerPolygon = new Polygon("label", edges, vertices);
            
            /** line 4+n to end */
            for (int i = 0; i < verticesPerHole.length; i++) {
                //Initialize variable current hole
                Polygon innerPolygon = new Polygon();
                List<Edge> edgesInnerPolygon = new ArrayList();
                List<Vertex> verticesInnerPolygon = new ArrayList();
                
                //first vertex, to ensure first edge can be made
                x = input.nextDouble();
                input.next();
                y = input.nextDouble();
                input.next();
                artFlag = input.nextInt();

                firstVertex = new Vertex(x, y, artFlag, "label");
                //System.out.println("first vertex: x=" + x + ", y = " + y);

                oldVertex = firstVertex;

                for (int j = 0; j < verticesPerHole[i]-1; j++) {
                    x = input.nextDouble();
                    input.next();
                    y = input.nextDouble();
                    input.next();
                    artFlag = input.nextInt();
                    //System.out.println("next vertex: x=" + x + ", y = " + y);
                    //System.out.println("previous vertex: x = " + oldVertex.getX() + " y = " + oldVertex.getY());
                    Vertex vertex = new Vertex(x, y, artFlag, "label");
                    Edge edge = new Edge("label", oldVertex, vertex);

                    verticesInnerPolygon.add(vertex);
                    edgesInnerPolygon.add(edge);
                    oldVertex = vertex;
                }
                //last edge
                closingEdge = new Edge("label", oldVertex, firstVertex);
                edgesInnerPolygon.add(closingEdge);
                innerPolygon = new Polygon("label", edgesInnerPolygon, verticesInnerPolygon);
                innerPolygons.add(innerPolygon);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //result: 
        gallery = new Gallery(numOfExits, numOfArts, outerPolygon, innerPolygons);
        galleryProblem = new GalleryProblem(gallery, numOfGuards, vmaxGuards, globalTime, deltaT); 
    }

//    public static void main(String[] args) {
//        readInput("ArtGallerySpecification.txt");
//        System.out.println("num of vert = " + numOfVertices);
//        System.out.println("num of holes = " + numOfHoles);
//        System.out.println("num of exits = " + numOfExits);
//        System.out.println("num of arts = " + numOfArts);
//        System.out.println("num of guards = " + numOfGuards);
//        System.out.println("vmax guards = " + vmaxGuards);
//        System.out.println("global time limit T = " + globalTime);
//        System.out.println("observation time delta t = " + deltaT);
//    }

}
