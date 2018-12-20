/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Berry
 */
public class TrapezoidalMap {
    private List<Trapezoid> trapezoids;
    private TrapezoidalSearchStructure tss;

    //input: set S of n non crossing line segments
    //output: trapezoidal map T(S) and search structure D for T(S) in a bounding box
    //1. determine a bounding box that contains all segments of S and initialize the trapezoidal map structure T and search structure D for it
    //2. compute a random permutation of the elements of S
    //3. for i = 1 to n
    //4. do find the set of trapezoids in T properly intersected by Si
    //5. remove trapezoids from T and replace them by the new trapezoids that appear because of the insertion of Si
    //6. remove the leaves for the trapezoids from D and create leaves for the new trapezoids. Link the new leaves to the existing inner nodes by adding some new inner nodes.

    public TrapezoidalMap() {
    }

    public void Construct(List<Edge> segments) {
        int n = segments.size();
        
        Trapezoid boundingBox = DetermineBoundingBox(segments);
        
        if (boundingBox == null) { 
            return;
        }
        
        boundingBox.setLabel("R");
        
        this.trapezoids = new ArrayList<>();
        this.tss = new TrapezoidalSearchStructure();
        this.tss.Initialize(boundingBox.getLabel());
        
        // TODO compute random permutation of segments
        
        for (int i = 0; i < n; i++) {
            List<Trapezoid> intersectedTrapezoids = DetermineIntersectionWithTrapezoids(segments.get(i));
            
            if (intersectedTrapezoids != null && intersectedTrapezoids.size() > 0) {
                // TODO remove trapezoids from list and replace by new trapezoids that appear because of the insertion of segment i
                // TODO remove leaves for the trapezoids from the search structure and create new leaves for the new trapezoids
                // TODO link the new leaves to the existing inner nodes by adding some new inner nodes
            }
        }
    }
    
    public Trapezoid DetermineBoundingBox(List<Edge> segments) {
        Trapezoid boundingBox = new Trapezoid();
        
        int xl = 0, xr = 0, yb = 0, yt = 0;
        
        // Set first segment as default bounding box
        int sxl = segments.get(0).getV1().getX();
        int sxr = segments.get(0).getV2().getX();
        int syb = segments.get(0).getV1().getY();
        int syt = segments.get(0).getV2().getY();

        if (sxl > sxr) {
            int tempx = sxr;
            sxr = sxl;
            sxl = tempx;
        }

        if (syb > syt) {
            int tempy = syt;
            syt = syb;
            syb = tempy;
        }
        
        if (segments.size() > 1){
        // Find other segments that are more extreme
            for (int i = 1; i < segments.size(); i++) {
                if (segments.get(i).getV1() != null && segments.get(i).getV2() != null) {
                    sxl = segments.get(i).getV1().getX();
                    sxr = segments.get(i).getV2().getX();
                    syb = segments.get(i).getV1().getY();
                    syt = segments.get(i).getV2().getY();

                    if (sxl > sxr) {
                        int tempx = sxr;
                        sxr = sxl;
                        sxl = tempx;
                    }

                    if (syb > syt) {
                        int tempy = syt;
                        syt = syb;
                        syb = tempy;
                    }

                    if (sxl < xl) {
                        xl = sxl;
                    }
                    if (sxr > xr) {
                        xr = sxr;
                    }
                    if (syb < yb) {
                        yb = syb;
                    }
                    if (syt > yt) {
                        yt = syt;
                    }
                }
            }
        }
        return boundingBox;
    }

    public List<Trapezoid> DetermineIntersectionWithTrapezoids(Edge segment) {
        List<Trapezoid> intersectedTrapezoids = new ArrayList<>();
        
        if (this.trapezoids == null || this.trapezoids.isEmpty()) {
            return intersectedTrapezoids;
        }
        
        for (int i = 0; i < this.trapezoids.size(); i++) {
            if (DetermineIntersectionOfSegments(this.trapezoids.get(i).getE1(), segment)) {
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DetermineIntersectionOfSegments(this.trapezoids.get(i).getE2(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DetermineIntersectionOfSegments(this.trapezoids.get(i).getE3(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DetermineIntersectionOfSegments(this.trapezoids.get(i).getE4(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DetermineContainmentOfSegment(this.trapezoids.get(i), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            }
        }
        
        return intersectedTrapezoids;
    }
    
    public boolean DetermineIntersectionOfSegments(Edge e1, Edge e2) {
        int o1 = Orientation(e1.getV1(), e1.getV2(), e2.getV1());
        int o2 = Orientation(e1.getV1(), e1.getV2(), e2.getV2());
        int o3 = Orientation(e2.getV1(), e2.getV2(), e1.getV1());
        int o4 = Orientation(e2.getV1(), e2.getV2(), e1.getV2());
        
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        
        if (o1 == 0 && OnSegment(e1.getV1(), e2.getV1(), e1.getV2())) {
            return true;
        }
        
        if (o2 == 0 && OnSegment(e1.getV1(), e2.getV2(), e1.getV2())) {
            return true;
        }
        
        if (o3 == 0 && OnSegment(e2.getV1(), e1.getV1(), e2.getV2())) {
            return true;
        }
        
        if (o4 == 0 && OnSegment(e2.getV1(), e1.getV2(), e2.getV2())) {
            return true;
        }
        
        return false;
    }
    
    public boolean DetermineIntersectionOfSegments(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        int o1 = Orientation(v1, v2, v3);
        int o2 = Orientation(v1, v2, v4);
        int o3 = Orientation(v3, v4, v1);
        int o4 = Orientation(v3, v4, v2);
        
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        
        if (o1 == 0 && OnSegment(v1, v3, v2)) {
            return true;
        }
        
        if (o2 == 0 && OnSegment(v1, v4, v2)) {
            return true;
        }
        
        if (o3 == 0 && OnSegment(v3, v1, v4)) {
            return true;
        }
        
        if (o4 == 0 && OnSegment(v3, v2, v4)) {
            return true;
        }
        
        return false;
    }
    
    // To find orientation of ordered triplet (v1, v2, v3). 
    // The function returns following values 
    // 0 --> v1, v2 and v3 are colinear 
    // 1 --> Clockwise 
    // 2 --> Counterclockwise 
    public int Orientation(Vertex v1, Vertex v2, Vertex v3) {
        int val = (v2.getY() - v1.getY()) * (v3.getX() - v2.getX()) - (v2.getX() - v1.getX()) * (v3.getY() - v2.getY());
        
        if (val == 0) {
            return 0;
        }
        
        return (val > 0) ? 1 : 2;
    }
    
  
    // Given three colinear points v1, v2, v3, the function checks if 
    // point v2 lies on line segment 'v1v3' 
    public boolean OnSegment(Vertex v1, Vertex v2, Vertex v3) {
        return (v2.getX() <= Math.max(v1.getX(), v3.getX()) && v2.getX() >= Math.min(v1.getX(), v3.getX()) &&
                v2.getY() <= Math.max(v1.getY(), v3.getY()) && v2.getY() >= Math.min(v1.getY(), v3.getY()));
    }
    
    public boolean DetermineContainmentOfSegment(Trapezoid trapezoid, Edge segment) {
        return DetermineContainmentOfVertex(trapezoid, segment.getV1()) && DetermineContainmentOfVertex(trapezoid, segment.getV2());
    }
    
    public boolean DetermineContainmentOfVertex(Trapezoid trapezoid, Vertex vertex) {
        // HINT if int max causes overflow problems, use int = 10000 or something like that
        Vertex extreme = new Vertex(Integer.MAX_VALUE, vertex.getY(), "Extreme Point");
        Vertex[] polygon = new Vertex[] { trapezoid.getV1(), trapezoid.getV2(), trapezoid.getV3(), trapezoid.getV4() };
        int count = 0, i = 0, n = polygon.length;
        do {
            int next = (i+1)%n;
            // check if segment vertex-extreme intersects with the segment i-next
            if (DetermineIntersectionOfSegments(polygon[i], polygon[next], vertex, extreme)) {
                if (Orientation(polygon[i], vertex, polygon[next]) == 0) {
                    // if vertex is colinear with segment i-next
                    // then if it lies on the segment return true
                    return OnSegment(polygon[i], vertex, polygon[next]);
                }
                count++;
            }
            i = next;
        } while (i != 0);
        
        // return true if count is odd
        return count%2 == 1;
    }
}
