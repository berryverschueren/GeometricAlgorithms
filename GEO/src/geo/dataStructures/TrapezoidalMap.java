/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.List;
import math.geom2d.Vector2D;

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
        
        this.trapezoids = new ArrayList<>();
        this.tss = new TrapezoidalSearchStructure();
        this.trapezoids.add(boundingBox);
        this.tss.Initialize(boundingBox.getLabel());
        
        // TODO compute random permutation of segments
        
        for (int i = 0; i < n; i++) {
            List<Trapezoid> intersectedTrapezoids = DoesSegmentIntersectTrapezoid(segments.get(i));
            
            if (intersectedTrapezoids != null && intersectedTrapezoids.size() > 0) {
                
                for (int j = 0; j < intersectedTrapezoids.size(); j++) {
                    if (DoesTrapezoidContainSegment(intersectedTrapezoids.get(j), segments.get(i))) {
                        System.out.println("Segment: " + segments.get(i).getLabel() + ", is completely contained in trapezoid: " + intersectedTrapezoids.get(j).getLabel());
                        Vertex tempv1 = new Vertex(segments.get(i).getSpecificVertex(0).getX(), segments.get(i).getSpecificVertex(0).getY(), "tempv1");
                        Vertex tempv2 = new Vertex(segments.get(i).getSpecificVertex(0).getX(), 10000, "tempv2");
                        Vertex tempv3 = new Vertex(segments.get(i).getSpecificVertex(0).getX(), -10000, "tempv3");
                        Edge tempe1 = new Edge("tempe1", tempv1, tempv2);
                        Edge tempe2 = new Edge("tempe2", tempv1, tempv3);
                        
                        // point on outer trapezoid top edge intersecting edge upwards from left vertex of inserted segment.
                        Vertex v1 = GetIntersectionPointOfSegments(intersectedTrapezoids.get(j).getSpecificEdge(1), tempe1);
                        
                        // point on outer trapezoid bottom edge intersecting edge downwards from left vertex of inserted segment.
                        Vertex v2 = GetIntersectionPointOfSegments(intersectedTrapezoids.get(j).getSpecificEdge(3), tempe2);
                        
                        tempv1 = new Vertex(segments.get(i).getSpecificVertex(1).getX(), segments.get(i).getSpecificVertex(1).getY(), "tempv1");
                        tempv2 = new Vertex(segments.get(i).getSpecificVertex(1).getX(), 10000, "tempv2");
                        tempv3 = new Vertex(segments.get(i).getSpecificVertex(1).getX(), -10000, "tempv3");
                        tempe1 = new Edge("tempe1", tempv1, tempv2);
                        tempe2 = new Edge("tempe2", tempv1, tempv3);
                        
                        // point on outer trapezoid top edge intersecting edge downwards from right vertex of inserted segment.
                        Vertex v3 = GetIntersectionPointOfSegments(intersectedTrapezoids.get(j).getSpecificEdge(1), tempe1);
                        
                        // point on outer trapezoid bottom edge intersecting edge downwards from right vertex of inserted segment.
                        Vertex v4 = GetIntersectionPointOfSegments(intersectedTrapezoids.get(j).getSpecificEdge(3), tempe2);
                        
                        Trapezoid t1 = new Trapezoid();
                        t1.setV1(intersectedTrapezoids.get(j).getSpecificVertex(0));
                        t1.setV2(intersectedTrapezoids.get(j).getSpecificVertex(1));
                        t1.setV3(v1);
                        t1.setV4(v2);
                        t1.setE1(new Edge("e1", t1.getV1(), t1.getV2()));
                        t1.setE2(new Edge("e2", t1.getV2(), t1.getV3()));
                        t1.setE3(new Edge("e3", t1.getV3(), t1.getV4()));
                        t1.setE4(new Edge("e4", t1.getV4(), t1.getV1()));
                        
                        // TODO find highest label and add 1 to it, to maintain unique trapezoid labels
                        
                        t1.setLabel("t1");
                        
                        Trapezoid t2 = new Trapezoid();
                        t2.setV1(v2);
                        t2.setV2(segments.get(i).getSpecificVertex(0));
                        t2.setV3(segments.get(i).getSpecificVertex(1));
                        t2.setV4(v4);
                        t2.setE1(new Edge("e1", t2.getV1(), t2.getV2()));
                        t2.setE2(new Edge("e2", t2.getV2(), t2.getV3()));
                        t2.setE3(new Edge("e3", t2.getV3(), t2.getV4()));
                        t2.setE4(new Edge("e4", t2.getV4(), t2.getV1()));
                        t2.setLabel("t2");
                        
                        Trapezoid t3 = new Trapezoid();
                        t3.setV1(segments.get(i).getSpecificVertex(0));
                        t3.setV2(v1);
                        t3.setV3(v3);
                        t3.setV4(segments.get(i).getSpecificVertex(1));
                        t3.setE1(new Edge("e1", t3.getV1(), t3.getV2()));
                        t3.setE2(new Edge("e2", t3.getV2(), t3.getV3()));
                        t3.setE3(new Edge("e3", t3.getV3(), t3.getV4()));
                        t3.setE4(new Edge("e4", t3.getV4(), t3.getV1()));
                        t3.setLabel("t3");
                        
                        Trapezoid t4 = new Trapezoid();
                        t4.setV1(v4);
                        t4.setV2(v3);
                        t4.setV3(intersectedTrapezoids.get(j).getSpecificVertex(2));
                        t4.setV4(intersectedTrapezoids.get(j).getSpecificVertex(3));
                        t4.setE1(new Edge("e1", t4.getV1(), t4.getV2()));
                        t4.setE2(new Edge("e2", t4.getV2(), t4.getV3()));
                        t4.setE3(new Edge("e3", t4.getV3(), t4.getV4()));
                        t4.setE4(new Edge("e4", t4.getV4(), t4.getV1()));
                        t4.setLabel("t4");
                    } 
                    else {
                        System.out.println("Segment: " + segments.get(i).getLabel() + ", intersects trapezoid: " + intersectedTrapezoids.get(j).getLabel());
                        
                    }
                }
                // TODO remove trapezoids from list and replace by new trapezoids that appear because of the insertion of segment i
                // TODO remove leaves for the trapezoids from the search structure and create new leaves for the new trapezoids
                // TODO link the new leaves to the existing inner nodes by adding some new inner nodes
            }
        }
    }
    
    public Trapezoid DetermineBoundingBox(List<Edge> segments) {
        Trapezoid boundingBox = new Trapezoid();
        
        int xl = Integer.MAX_VALUE, xr = Integer.MIN_VALUE, yb = Integer.MAX_VALUE, yt = Integer.MIN_VALUE;
        
        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getV1() != null && segments.get(i).getV2() != null) {
                xl = Math.min(xl, Math.min(segments.get(i).getV1().getX(), segments.get(i).getV2().getX()));
                xr = Math.max(xr, Math.max(segments.get(i).getV1().getX(), segments.get(i).getV2().getX()));
                yb = Math.min(yb, Math.min(segments.get(i).getV1().getY(), segments.get(i).getV2().getY()));
                yt = Math.max(yt, Math.max(segments.get(i).getV1().getY(), segments.get(i).getV2().getY()));
            }
        }
        
        boundingBox.setV1(new Vertex(xl - 1, yb - 1, "v1"));
        boundingBox.setV2(new Vertex(xl - 1, yt + 1, "v2"));
        boundingBox.setV3(new Vertex(xr + 1, yt + 1, "v3"));
        boundingBox.setV4(new Vertex(xr + 1, yb - 1, "v4"));
        boundingBox.setE1(new Edge("e1", boundingBox.getV1(), boundingBox.getV2()));
        boundingBox.setE2(new Edge("e2", boundingBox.getV2(), boundingBox.getV3()));
        boundingBox.setE3(new Edge("e3", boundingBox.getV3(), boundingBox.getV4()));
        boundingBox.setE4(new Edge("e4", boundingBox.getV4(), boundingBox.getV1()));
        boundingBox.setLabel("R");
        
        return boundingBox;
    }

    public List<Trapezoid> DoesSegmentIntersectTrapezoid(Edge segment) {
        List<Trapezoid> intersectedTrapezoids = new ArrayList<>();
        
        if (this.trapezoids == null || this.trapezoids.isEmpty()) {
            return intersectedTrapezoids;
        }
        
        for (int i = 0; i < this.trapezoids.size(); i++) {
            if (TrapezoidalMap.this.DoSegmentsIntersect(this.trapezoids.get(i).getE1(), segment)) {
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (TrapezoidalMap.this.DoSegmentsIntersect(this.trapezoids.get(i).getE2(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (TrapezoidalMap.this.DoSegmentsIntersect(this.trapezoids.get(i).getE3(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (TrapezoidalMap.this.DoSegmentsIntersect(this.trapezoids.get(i).getE4(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DoesTrapezoidContainSegment(this.trapezoids.get(i), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            }
        }
        
        return intersectedTrapezoids;
    }
    
    public boolean DoSegmentsIntersect(Edge e1, Edge e2) {
        return DoSegmentsIntersect(e1.getV1(), e1.getV2(), e2.getV1(), e2.getV2());
    }
    
    public boolean DoSegmentsIntersect(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
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
        int val = ((v2.getY() - v1.getY()) * (v3.getX() - v2.getX())) - ((v2.getX() - v1.getX()) * (v3.getY() - v2.getY()));
        return val == 0 ? 0 : (val > 0) ? 1 : 2;
    }
    
  
    // Given three colinear points v1, v2, v3, the function checks if 
    // point v2 lies on line segment 'v1v3' 
    public boolean OnSegment(Vertex v1, Vertex v2, Vertex v3) {
        return (v2.getX() <= Math.max(v1.getX(), v3.getX()) && v2.getX() >= Math.min(v1.getX(), v3.getX()) &&
                v2.getY() <= Math.max(v1.getY(), v3.getY()) && v2.getY() >= Math.min(v1.getY(), v3.getY()));
    }
    
    public boolean DoesTrapezoidContainSegment(Trapezoid trapezoid, Edge segment) {
        return DoesTrapezoidContainVertex(trapezoid, segment.getV1()) && DoesTrapezoidContainVertex(trapezoid, segment.getV2());
    }
    
    public boolean DoesTrapezoidContainVertex(Trapezoid trapezoid, Vertex vertex) {
        // HINT if int max causes overflow problems, use int = 10000 or something like that
        //Vertex extreme = new Vertex(Integer.MAX_VALUE, vertex.getY(), "Extreme Point");
        Vertex extreme = new Vertex(10000, vertex.getY(), "Extreme Point");
        Vertex[] polygon = new Vertex[] { trapezoid.getV1(), trapezoid.getV2(), trapezoid.getV3(), trapezoid.getV4() };
        int count = 0, i = 0, n = polygon.length;
        do {
            int next = (i+1)%n;
            // check if segment vertex-extreme intersects with the segment i-next
            if (DoSegmentsIntersect(polygon[i], polygon[next], vertex, extreme)) {
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

    public Vertex GetIntersectionPointOfSegments(Edge e1, Edge e2) {
        Vertex intersectionPoint = new Vertex();
        
        Vector2D p = new Vector2D(e1.getV1().getX(), e1.getV1().getY());
        Vector2D r = new Vector2D((e1.getV2().getX() - e1.getV1().getX()), (e1.getV2().getY() - e1.getV1().getY()));
        Vector2D q = new Vector2D(e2.getV1().getX(), e2.getV1().getY());
        Vector2D s = new Vector2D((e2.getV2().getX() - e2.getV1().getX()), (e2.getV2().getY() - e2.getV1().getY()));
        
        //https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
        // any point on e1 --> p + t * r (for scalar parameter t)
        // any point on e2 --> q + u * s (for scalar parameter u)
        // intersection e1 with e2 --> p + t * r = q + u * s
        // cross both sides with s
        // gets (p + t * r) X s = (q + u * s) X s
        // since s X s = 0, this means
        // t (r X s) = (q - p) X s
        // and therefore, solving for t (and in the same way for u)
        // t = (q - p) X s / (r X s)
        // u = (q - p) X r / (r X s)
        // now there are four cases, but 1 is important: r X s != 0 and 0<=t<=1 and 0<=u<=1
        // then the two lines meet at the point p + t * r = q + u * s
        
        double t = (((q.minus(p)).cross(s))/(r.cross(s)));
        double u = (((q.minus(p)).cross(r))/(r.cross(s)));
        
        if (r.cross(s) != 0 && 0 <= t && t <= 1 && 0 <= u && u <= 1) {
            Vector2D intersectionVector = p.plus((r.times(t)));
            intersectionPoint = new Vertex((int)intersectionVector.x(), (int)intersectionVector.y(), "");
            return intersectionPoint;
        }
        
        return intersectionPoint;
    }
}
