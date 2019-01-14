package geo.dataStructures;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import math.geom2d.Point2D;

//import weiss.nonstandard.PairingHeap;

// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException {
	public GraphException(String name) {
		super(name);
	}
}

// Represents an edge in the graph.
class EdgeDijkstra {
	public VertexDijkstra dest; // Second vertex in Edge
	public double cost; // Edge cost

	public EdgeDijkstra(VertexDijkstra d, double c) {
		dest = d;
		cost = c;
	}
}

// Represents an entry in the priority queue for Dijkstra's algorithm.
class Path implements Comparable<Path> {
	public VertexDijkstra dest; // w
	public double cost; // d(w)

	public Path(VertexDijkstra d, double c) {
		dest = d;
		cost = c;
	}

	public int compareTo(Path rhs) {
		double otherCost = rhs.cost;

		return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
	}
}

// Represents a vertex in the graph.
class VertexDijkstra {
	public String name; // Vertex name
	public List<EdgeDijkstra> adj; // Adjacent vertices
	public double dist; // Cost
	public VertexDijkstra prev; // Previous vertex on shortest path
	public int scratch;// Extra variable used in algorithm

	public VertexDijkstra(String nm) {
		name = nm;
		adj = new LinkedList<EdgeDijkstra>();
		reset();
	}

	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
		scratch = 0;
	}
	// { dist = Graph.INFINITY; prev = null; pos = null; scratch = 0; }
	// public PairingHeap.Position<Path> pos; // Used for dijkstra2 (Chapter 23)
}

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
// --> Add additional edge
// void printPath( String w ) --> Print path after alg is run
// void unweighted( String s ) --> Single-source unweighted
// void dijkstra( String s ) --> Single-source weighted
// void negative( String s ) --> Single-source negative weighted
// void acyclic( String s ) --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm. Exceptions are thrown if errors are detected.

public class Graph {
	public static final double INFINITY = Double.MAX_VALUE;
	private Map<String, VertexDijkstra> vertexMap = new HashMap<String, VertexDijkstra>();
        private double costCurrentPath = 0;

    public double getCostCurrentPath() {
        return costCurrentPath;
    }
 
        
	/**
	 * Add a new edge to the graph.
	 */
	public void addEdgeDijkstra(String sourceName, String destName, double cost) {
		VertexDijkstra v = getVertexDijkstra(sourceName);
		VertexDijkstra w = getVertexDijkstra(destName);
		v.adj.add(new EdgeDijkstra(w, cost));
	}

	/**
	 * Driver routine to handle unreachables and print total cost. It calls
	 * recursive routine to print shortest path to destNode after a shortest
	 * path algorithm has run.
	 */
	public VertexDijkstra printPath(String destName) {
		VertexDijkstra w = vertexMap.get(destName);
		if (w == null)
			throw new NoSuchElementException("Destination vertex not found");
		else if (w.dist == INFINITY)
			System.out.println(destName + " is unreachable");
		else {
			//System.out.print("(Cost is: " + w.dist + ") ");
			printPath(w);
			//System.out.println();
		}
                return w;
	}

	/**
	 * If vertexName is not present, add it to vertexMap. In either case, return
	 * the Vertex.
	 */
	private VertexDijkstra getVertexDijkstra(String vertexName) {
		VertexDijkstra v = vertexMap.get(vertexName);
		if (v == null) {
			v = new VertexDijkstra(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}

	/**
	 * Recursive routine to print shortest path to dest after running shortest
	 * path algorithm. The path is known to exist.
	 */
	private void printPath(VertexDijkstra dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
			//System.out.print(" to ");
		}
		//System.out.print(dest.name);
	}

	/**
	 * Initializes the vertex output info prior to running any shortest path
	 * algorithm.
	 */
	private void clearAll() {
		for (VertexDijkstra v : vertexMap.values())
			v.reset();
	}

	/**
	 * Single-source unweighted shortest-path algorithm.
	 */
	public void unweighted(String startName) {
		clearAll();

		VertexDijkstra start = vertexMap.get(startName);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		Queue<VertexDijkstra> q = new LinkedList<VertexDijkstra>( );
		q.add(start);
		start.dist = 0;

		while (!q.isEmpty()) {
			VertexDijkstra v = q.remove();

			for (EdgeDijkstra e : v.adj) {
				VertexDijkstra w = e.dest;
				if (w.dist == INFINITY) {
					w.dist = v.dist + 1;
					w.prev = v;
					q.add(w);
				}
					}
		}
	}

	/**
	 * Single-source weighted shortest-path algorithm.
	 */
	public void dijkstra(String startName) {
		PriorityQueue<Path> pq = new PriorityQueue<Path>();

		VertexDijkstra start = vertexMap.get(startName);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		clearAll();
		pq.add(new Path(start, 0));
		start.dist = 0;

		int nodesSeen = 0;
		while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
			Path vrec = pq.remove();
			VertexDijkstra v = vrec.dest;
			if (v.scratch != 0) // already processed v
				continue;

			v.scratch = 1;
			nodesSeen++;

			for (EdgeDijkstra e : v.adj) {
				VertexDijkstra w = e.dest;
				double cvw = e.cost;

				if (cvw < 0)
					throw new GraphException("Graph has negative edges");

				if (w.dist > v.dist + cvw) {
					w.dist = v.dist + cvw;
					w.prev = v;
					pq.add(new Path(w, w.dist));
				}
			}
			// ZZZ
			PriorityQueue<Path> pq2 = new PriorityQueue<Path>();
			Path p;
			//System.out.print("Priority queue is ");
			while (pq.size() > 0) {
				p = pq.remove();
				//System.out.print(p.dest.name + p.cost + " ");
				pq2.add(p);
			}
			pq = pq2;
			//System.out.println();
			// ZZZ
		}
	}

	/**
	 * Process a request; return false if end of file.
	 */
	public VertexDijkstra processRequest(String startName, String destName, Graph g) {
//		String startName = null;
//		String destName = null;

                g.dijkstra(startName);
		return g.printPath(destName);
                //g.printPath(destName);

//		try {
//			System.out.print("Enter start node:");
//			if ((startName = in.readLine()) == null)
//				return false;
//			System.out.print("Enter destination node:");
//			if ((destName = in.readLine()) == null)
//				return false;
//			System.out.print(" Enter algorithm (u, d, n, a ): ");
//			if ((alg = in.readLine()) == null)
//				return false;

//			if (alg.equals("u"))
//				g.unweighted(startName);
//			else if (alg.equals("d")) {
				
				// g.dijkstra2( startName );
//			} 
//		} catch (IOException e) {
//			System.err.println(e);
//		} catch (NoSuchElementException e) {
//			System.err.println(e);
//		} catch (GraphException e) {
//			System.err.println(e);
//		}
//		return true;
	}

	/**
	 * A main routine that: 1. Reads a file containing edges (supplied as a
	 * command-line parameter); 2. Forms the graph; 3. Repeatedly prompts for
	 * two vertices and runs the shortest path algorithm. The data file is a
	 * sequence of lines of the format source destination.
	 */
	public List<Vertex> dijkstraStart(List<Edge> edges, Vertex start, Vertex end, List<Vertex> vertices ) {
		Graph g = new Graph();

                int counter = 0;
                for(Edge edge : edges){
                    double cost = Point2D.distance(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
                    if(edge.getV1().getLabel().equals("")){
                        edge.getV1().setLabel(counter+"");
                        counter++;
                    }
                    if(edge.getV2().getLabel().equals("")){
                        edge.getV2().setLabel(counter+"");
                        counter++;
                    }
                    g.addEdgeDijkstra(edge.getV1().getLabel(), edge.getV2().getLabel(), cost);
                }
                
                if(!vertices.contains(start)){
                    System.out.println("");
                }
                
                if(!vertices.contains(end)){
                    System.out.println("");
                }

		System.out.println("Start:"+ start.getLabel());
		System.out.println("end:"+ end.getLabel());

		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		VertexDijkstra w = processRequest(start.getLabel(),end.getLabel(), g);
                costCurrentPath = w.dist;
                //VertexDijkstra w = vertexMap.get(end.getLabel());
                List<Vertex> vertexPath = new ArrayList<>();
                while(true){
                    w = w.prev;
                    if(w == null){
                        break;
                    }
                    Vertex v=null;
                    for(Vertex vertex : vertices){
                        if(vertex.getLabel().equals(w.name)){
                            v = vertex;
                        }
                    }
                    
                    vertexPath.add(v); 
                }
                Collections.reverse(vertexPath);
                
//                List<Edge> edgePath = new ArrayList<>();
//                for (int i = 0; i <= vertexPath.size()-2; i++) {
//                    edgePath.add(new Edge("",vertexPath.get(i), vertexPath.get(i+1)));
//                }
                return vertexPath;
			
	}
}
