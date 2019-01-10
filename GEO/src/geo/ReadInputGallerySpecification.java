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

public class ReadInputGallerySpecification {
    
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
    public static List<Double> distinctX = new ArrayList<>();
    public static Double xUpper = 0.05;
    public static List<Double> distinctY = new ArrayList<>();
    public static Double yUpper = 0.05;
    
    public static GalleryProblem readInputArtGallerySpecification(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);
            
            input.useDelimiter(",\\s*");
            
            /** line 1 */
            numOfVertices = input.nextInt();
            //System.out.println(numOfVertices);
            numOfHoles = input.nextInt();
            //System.out.println(numOfHoles);
            numOfExits = input.nextInt();
            //System.out.println(numOfExits);
            numOfArts = input.nextInt();
            //System.out.println(numOfArts);
            
            /** line 2 */
            int[] verticesPerHole = new int[numOfHoles];
            
            for (int i = 0; i < numOfHoles; i++) {
                verticesPerHole[i] = input.nextInt();
            }
            
            /** line 3 */
            numOfGuards = input.nextInt();
            //System.out.println(numOfGuards);
            vmaxGuards = input.nextDouble();
            //System.out.println(vmaxGuards);
            globalTime = input.nextDouble();
            //System.out.println(globalTime);
            deltaT = input.nextDouble();
            //System.out.println(deltaT);
            
            /** line 4 to 4+n */
            
            //first vertex, to ensure first edge can be made
            x = input.nextDouble();
            y = input.nextDouble();
            artFlag = input.nextInt();
            exitFlag = input.nextInt();

            distinctX.add(x);
            distinctY.add(y);
            
            Vertex firstVertex = new Vertex(x, y, artFlag, exitFlag, "label");
            //System.out.println("first vertex: x=" + x + ", y = " + y);
            vertices.add(firstVertex);
            Vertex oldVertex = firstVertex;
            
            //rest of vertices
            for (int j = 0; j < numOfVertices-1; j++) {
                x = input.nextDouble();
                y = input.nextDouble();
                artFlag = input.nextInt();
                exitFlag = input.nextInt();
                
                // CREATE DISTINCT X'S
                boolean contained = false;
                for (int i = 0; i < distinctX.size(); i++) {
                    if (distinctX.get(i) == x) {
                        contained = true;
                        break;
                    }
                }
                if (!contained) {
                    distinctX.add(x);
                } else {
                    x = x +xUpper;
                    xUpper = xUpper + 0.05;
                    distinctX.add(x);
                }
                
                // CREATE DISTINCT Y'S
                contained = false;
                for (int i = 0; i < distinctY.size(); i++) {
                    if (distinctY.get(i) == y) {
                        contained = true;
                        break;
                    }
                }
                if (!contained) {
                    distinctY.add(y);
                } else {
                    y = y +yUpper;
                    yUpper = yUpper + 0.05;
                    distinctY.add(y);
                }
                
                //System.out.println("current vertex: x=" + x + ", y = " + y);
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
                y = input.nextDouble();
                artFlag = input.nextInt();

                firstVertex = new Vertex(x, y, artFlag, "label");
                //System.out.println("first vertex hole: x=" + x + ", y = " + y);
                verticesInnerPolygon.add(firstVertex);
                oldVertex = firstVertex;

                for (int j = 0; j < verticesPerHole[i]-1; j++) {
                    x = input.nextDouble();
                    y = input.nextDouble();
                    artFlag = input.nextInt();
                    //System.out.println("current vertex hole: x=" + x + ", y = " + y);
                    //System.out.println("previous vertex hole: x = " + oldVertex.getX() + " y = " + oldVertex.getY());
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
            System.out.println("DISTINCT X'S = " + distinctX.size());
            System.out.println("DISTINCT Y'S = " + distinctY.size());
            System.out.println("VERTICES COUNT = " + numOfVertices);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //result: 
        gallery = new Gallery(numOfExits, numOfArts, outerPolygon, innerPolygons);
        galleryProblem = new GalleryProblem(gallery, numOfGuards, vmaxGuards, globalTime, deltaT); 
        return galleryProblem;
    }

//    public static void main(String[] args) {
//        GalleryProblem gallery = new GalleryProblem();
//        gallery = readInputArtGallerySpecification("ArtGallery1.txt");
//        WriteInputGallerySpecification.WriteInputGallerySpecification(gallery);
/**     System.out.println("num of vert = " + numOfVertices);
        System.out.println("num of holes = " + numOfHoles);
        System.out.println("num of exits = " + numOfExits);
        System.out.println("num of arts = " + numOfArts);
        System.out.println("num of guards = " + numOfGuards);
        System.out.println("vmax guards = " + vmaxGuards);
        System.out.println("global time limit T = " + globalTime);
        System.out.println("observation time delta t = " + deltaT);
    }*/
//    }
}
