package geo.dataStructures;

/**
 *
 * @author carina
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ReadInputGallerySpecification extends Vertex {

    public static int numOfVertices;
    public static int numOfHoles;
    public static int numOfExits;
    public static int numOfArts;
    public static int numOfGuards;
    public static double vmaxGuards;
    public static double globalTime; //Global time limit
    public static double deltaT; //Observation time guards 
    //public String filename; //name file GallerySpecification
    
    static List<Polygon> innerPolygons = new ArrayList<Polygon>();
    //Polygon polygon = new Polygon();
    static List<Edge> edges = new ArrayList();
    static List<Vertex> vertices = new ArrayList();
    //Gallery gallery = new Gallery();
    
    public static void readInput(String filename) {
        try {
            FileReader reader = new FileReader(filename);

            Scanner input = new Scanner(reader);

            input.useDelimiter("[, \n]");

            numOfVertices = input.nextInt();
            input.next();
            numOfHoles = input.nextInt();
            input.next();
            numOfExits = input.nextInt();
            input.next();
            numOfArts = input.nextInt();

            for (int i = 0; i < numOfHoles; i++) {
                //deal with input number of vertices for holes
                System.out.println(input.nextInt());
                input.next();
            }

            numOfGuards = input.nextInt();
            input.next();
            vmaxGuards = input.nextDouble();
            input.next();
            globalTime = input.nextDouble();
            input.next();
            deltaT = input.nextDouble();

            //first vertex, to ensure first edge can be made
            double x = input.nextDouble();
            input.next();
            double y = input.nextDouble();
            input.next();
            int flagArt = input.nextInt();
            input.next();
            int flagExit = input.nextInt();

            //Vertex vertex = new Vertex(x, y, "label");
            Vertex vertex = new Vertex(x, y, flagArt, flagExit, "label");
            //System.out.println("first vertex: x=" + x + ", y = " + y);
            
            Vertex oldVertex = vertex;

            for (int j = 0; j < numOfVertices-1; j++) {
                x = input.nextDouble();
                input.next();
                y = input.nextDouble();
                input.next();
                flagArt = input.nextInt();
                input.next();
                flagExit = input.nextInt();
                //System.out.println("next vertex: x=" + x + ", y = " + y);
                //System.out.println("previous vertex: x = " + oldVertex.getX() + " y = " + oldVertex.getY());
                //Vertex newVertex = new Vertex(x, y, "label");
                Vertex newVertex = new Vertex(x, y, flagArt, flagExit, "label");
                Edge edge = new Edge("label", oldVertex, newVertex);
                //adjust Vertex with flags for exits and art pieces

                vertices.add(newVertex);
                edges.add(edge);
                oldVertex = newVertex;
            }
            //last edge
            Edge closingEdge = new Edge("label", oldVertex, vertex);
            edges.add(closingEdge);

            //deal with vertices of the holes
            //while (input.hasNext()) {
            //    break;
            //}

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //result: 
        Polygon outerPolygon = new Polygon("label", edges, vertices);
        Gallery gallery = new Gallery(numOfExits, numOfArts, outerPolygon, innerPolygons);
        GalleryProblem galleryProblem = new GalleryProblem(gallery, numOfGuards, vmaxGuards, globalTime, deltaT); 
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
