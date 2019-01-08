/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import math.geom2d.Vector2D;

/**
 *
 * @author Berry
 */
public class TrapezoidalMap {
    private List<Triangle> triangles;
    private List<Trapezoid> trapezoids;
    private TrapezoidShape tree;
    private int trapezoidCounter;

    //input: set S of n non crossing line segments
    //output: trapezoidal map T(S) and search structure D for T(S) in a bounding box
    //1. determine a bounding box that contains all segments of S and initialize the trapezoidal map structure T and search structure D for it
    //2. compute a random permutation of the elements of S
    //3. for i = 1 to n
    //4. do find the set of trapezoids in T properly intersected by Si
    //5. remove trapezoids from T and replace them by the new trapezoids that appear because of the insertion of Si
    //6. remove the leaves for the trapezoids from D and create leaves for the new trapezoids. Link the new leaves to the existing inner nodes by adding some new inner nodes.

    public TrapezoidalMap() {
        this.trapezoidCounter = 0;
    }
    
    public void construct(List<Edge> segments) {
        // compute bounding box based on most extreme points of all segments
        Trapezoid boundingBox = DetermineBoundingBox(segments);
        // exit if there is no bounding box found
        if (boundingBox == null) {
            return;
        }
        
        // set up trapezoid list and add bounding box
        this.trapezoids = new ArrayList<>();
        this.trapezoids.add(boundingBox);
        // set up search structure as well
        this.tree = boundingBox;
        // compute random permutation of the segments 
        //Collections.shuffle(segments);
        
        // loop over the segments
        for (int i = 0; i < segments.size(); i++) {
            // compute intersected trapezoids
            List<Trapezoid> it = DoesSegmentIntersectTrapezoid(segments.get(i));
            
            // verify that there are any
            if (it == null || it.isEmpty()) {
                continue;
            }
            
            // sort trapezoids based on their bottom left vertex's x coordinate
            it.sort(new TrapezoidComparator());
            
            // trapezoid to be continued in the next intersected trapezoid
            Trapezoid ct = null;
            
            // loop over all intersected trapezoids
            for (int j = 0; j < it.size(); j++) {
                // short named variables for readability
                Trapezoid t = it.get(j);
                Edge s = segments.get(i); 
                
                // print for feedback
                System.out.println("--------------------------------------------------");
                t.print();
                s.print();
                
                // vertices of s lie on top of edges of t
                // s is contained in t
                if (DoesTrapezoidContainSegment(t, s)
                        && Objects.equals(t.getLeft().getX(), s.getSpecificVertex(0).getX())
                        && Objects.equals(t.getRight().getX(), s.getSpecificVertex(1).getX()) ) {
                    
                    // <editor-fold defaultstate="collapsed" desc="contained LLRR">
                    
                    // split intersected trapezoid into 2 trapezoids
                    // upper and lower part above and below segment
                    System.out.println(s.getLabel() + " contained in " + t.getLabel());
                    System.out.println("Both endpoints lie on the trapezoid");
                    
                    // trapezoid above segment
                    Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), t.getSpecificVertex(1), 
                            t.getSpecificVertex(2), s.getSpecificVertex(1));
                    t1.setLeft(s.getSpecificVertex(0));
                    t1.setRight(s.getSpecificVertex(1));
                    t1.setBottom(s);
                    t1.setTop(t.getTop());
                    
                    // trapezoid below segment
                    Trapezoid t2 = CreateTrapezoidByVertices(t.getSpecificVertex(0), s.getSpecificVertex(0), 
                            s.getSpecificVertex(1), t.getSpecificVertex(3));
                    t2.setLeft(s.getSpecificVertex(0));
                    t2.setRight(s.getSpecificVertex(1));
                    t2.setBottom(t.getBottom());
                    t2.setTop(s);
                    
                    // remove intersected trapezoid from the list
                    this.trapezoids.remove(t);
                    
                    // add the new trapezoids to the list
                    this.trapezoids.add(t1);
                    this.trapezoids.add(t2);
                    
                    // print for feedback
                    t1.print();
                    t2.print();
                    
                    // TODO tree structure
                    
                    // </editor-fold>
                }
                // left vertex of s lies on top of left edge of t
                // s is contained in t
                else if (DoesTrapezoidContainSegment(t, s)
                        && Objects.equals(t.getLeft().getX(), s.getSpecificVertex(0).getX())) {
                    
                    // <editor-fold defaultstate="collapsed" desc="contained LL">

                    // split intersected trapezoid into 3 trapezoids
                    // upper and lower part above and below segment
                    // and a trapezoid to the right of the segment
                    System.out.println(s.getLabel() + " contained in " + t.getLabel());
                    System.out.println("Left endpoint lies on the trapezoid");

                    // compute the required vertices for the new trapezoids
                    Edge e1 = new Edge("e1", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), 10000.0, "tv1"));
                    Edge e2 = new Edge("e2", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), -10000.0, "tv2"));
                    Vertex v1 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e1);
                    Vertex v2 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e2);
                    
                    // trapezoid above segment
                    Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), t.getSpecificVertex(1), 
                            v1, s.getSpecificVertex(1));
                    t1.setLeft(s.getSpecificVertex(0));
                    t1.setRight(s.getSpecificVertex(1));
                    t1.setBottom(s);
                    t1.setTop(t1.getSpecificEdge(1));
                    
                    // trapezoid below segment
                    Trapezoid t2 = CreateTrapezoidByVertices(t.getSpecificVertex(0), s.getSpecificVertex(0),
                            s.getSpecificVertex(1), v2);
                    t2.setLeft(s.getSpecificVertex(0));
                    t2.setRight(s.getSpecificVertex(1));
                    t2.setBottom(t2.getSpecificEdge(3));
                    t2.setTop(s);                    
                    
                    // trapezoid right of segment
                    Trapezoid t3 = CreateTrapezoidByVertices(v2, v1, t.getSpecificVertex(2), t.getSpecificVertex(3));
                    t3.setLeft(s.getSpecificVertex(1));
                    t3.setRight(t.getRight());
                    t3.setBottom(t3.getSpecificEdge(3));
                    t3.setTop(t3.getSpecificEdge(1));
                    
                    // remove intersected trapezoid from the list
                    this.trapezoids.remove(t);
                    
                    // add the new trapezoids to the list
                    this.trapezoids.add(t1);
                    this.trapezoids.add(t2);
                    this.trapezoids.add(t3);
                    
                    // print for feedback
                    t1.print();
                    t2.print();
                    t3.print();
                    
                    // TODO tree structure
                    
                    // </editor-fold>
                }
                // right vertex of s lies on top of right edge of t
                // s is contained in t
                else if (DoesTrapezoidContainSegment(t, s)
                        && Objects.equals(t.getRight().getX(), s.getSpecificVertex(1).getX())) { 
                    
                    // <editor-fold defaultstate="collapsed" desc="contained RR">
                    
                    // split intersected trapezoid into 3 trapezoids
                    // upper and lower part above and below segment
                    // and a trapezoid to the left of the segment               
                    System.out.println(s.getLabel() + " contained in " + t.getLabel());
                    System.out.println("Right endpoint lies on the trapezoid");
                    
                    // compute the required vertices for the new trapezoids
                    Edge e1 = new Edge("e1", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), 10000.0, "tv1"));
                    Edge e2 = new Edge("e2", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), -10000.0, "tv2"));
                    Vertex v1 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e1);
                    Vertex v2 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e2);
                    
                    // trapezoid above segment
                    Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), v1, 
                            t.getSpecificVertex(2), s.getSpecificVertex(1));
                    t1.setLeft(s.getSpecificVertex(0));
                    t1.setRight(s.getSpecificVertex(1));
                    t1.setBottom(s);
                    t1.setTop(t1.getSpecificEdge(1));
                    
                    // trapezoid below segment
                    Trapezoid t2 = CreateTrapezoidByVertices(v2, s.getSpecificVertex(0),
                            s.getSpecificVertex(1), t.getSpecificVertex(3));
                    t2.setLeft(s.getSpecificVertex(0));
                    t2.setRight(s.getSpecificVertex(1));
                    t2.setBottom(t2.getSpecificEdge(3));
                    t2.setTop(s);                    
                    
                    // trapezoid left of segment
                    Trapezoid t3 = CreateTrapezoidByVertices(t.getSpecificVertex(0), t.getSpecificVertex(1), v1, v2);
                    t3.setLeft(t.getLeft());
                    t3.setRight(s.getSpecificVertex(0));
                    t3.setBottom(t3.getSpecificEdge(3));
                    t3.setTop(t3.getSpecificEdge(1));
                    
                    // remove intersected trapezoid from the list
                    this.trapezoids.remove(t);
                    
                    // add the new trapezoids to the list
                    this.trapezoids.add(t1);
                    this.trapezoids.add(t2);
                    this.trapezoids.add(t3);
                    
                    // print for feedback
                    t1.print();
                    t2.print();
                    t3.print();
                    
                    // TODO tree structure
                    
                    // </editor-fold>
                }
                // s is contained in t
                else if (DoesTrapezoidContainSegment(t, s)) {
                    
                    // <editor-fold defaultstate="collapsed" desc="contained">
                    
                    // split intersected trapezoid into 4 trapezoids
                    System.out.println(s.getLabel() + " contained in " + t.getLabel());
                    
                    // compute the required vertices for the new trapezoids
                    Edge e1 = new Edge("e1", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), 10000.0, "tv1"));
                    Edge e2 = new Edge("e2", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), -10000.0, "tv2"));
                    Edge e3 = new Edge("e3", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), 10000.0, "tv3"));
                    Edge e4 = new Edge("e4", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), -10000.0, "tv4"));
                    Vertex v1 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e1);
                    Vertex v2 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e2);
                    Vertex v3 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e3);
                    Vertex v4 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e4);
                    
                    // trapezoid above segment
                    Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), v1, 
                            v3, s.getSpecificVertex(1));
                    t1.setLeft(s.getSpecificVertex(0));
                    t1.setRight(s.getSpecificVertex(1));
                    t1.setBottom(s);
                    t1.setTop(t1.getSpecificEdge(1));
                    
                    // trapezoid below segment
                    Trapezoid t2 = CreateTrapezoidByVertices(v2, s.getSpecificVertex(0),
                            s.getSpecificVertex(1), v4);
                    t2.setLeft(s.getSpecificVertex(0));
                    t2.setRight(s.getSpecificVertex(1));
                    t2.setBottom(t2.getSpecificEdge(3));
                    t2.setTop(s);                    
                    
                    // trapezoid left of segment
                    Trapezoid t3 = CreateTrapezoidByVertices(t.getSpecificVertex(0), t.getSpecificVertex(1), v1, v2);
                    t3.setLeft(t.getLeft());
                    t3.setRight(s.getSpecificVertex(0));
                    t3.setBottom(t3.getSpecificEdge(3));
                    t3.setTop(t3.getSpecificEdge(1));
                    
                    // trapezoid right of segment
                    Trapezoid t4 = CreateTrapezoidByVertices(v4, v3, t.getSpecificVertex(2), t.getSpecificVertex(3));
                    t4.setLeft(s.getSpecificVertex(1));
                    t4.setRight(t.getRight());
                    t4.setBottom(t4.getSpecificEdge(3));
                    t4.setTop(t4.getSpecificEdge(1));
                    
                    // remove intersected trapezoid from the list
                    this.trapezoids.remove(t);
                    
                    // add the new trapezoids to the list
                    this.trapezoids.add(t1);
                    this.trapezoids.add(t2);
                    this.trapezoids.add(t3);
                    this.trapezoids.add(t4);
                    
                    // print for feedback
                    t1.print();
                    t2.print();
                    t3.print();
                    t4.print();
                    
                    // TODO tree structure
                    
                    // </editor-fold>
                }
                // left vertex of s lies on top of left edge of t
                // s starts in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(0))
                        && Objects.equals(t.getLeft().getX(), s.getSpecificVertex(0).getX())) {
                
                    // <editor-fold defaultstate="collapsed" desc="starts LL">
                    
                    // split intersected trapezoid into 2 trapezoids
                    // upper and lower part above and below segment
                    System.out.println(s.getLabel() + " starts in " + t.getLabel());
                    System.out.println("Left endpoint lies on left edge of the trapezoid");
                    
                    // compute side of exit intersection
                    Vertex v1 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(2));
                    
                    // if segments don't intersect, go to the next intersected trapezoid
                    if (v1 == null || v1.getX() == null || v1.getY() == null) {
                        continue;
                    }
                    
                    boolean exitAbove = v1.getY() > t.getRight().getY();
                    
                    // if it exits above the defined right vertex of the intersected trapezoid
                    if (exitAbove) {
                        System.out.println("Exits above defined right vertex");
                        // trapezoid below segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(t.getSpecificVertex(0), s.getSpecificVertex(0), 
                                v1, t.getSpecificVertex(3));
                        t1.setRight(t.getRight());
                        t1.setLeft(s.getSpecificVertex(0));
                        t1.setBottom(t.getBottom());
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // trapezoid above segment continues
                        ct = new Trapezoid();
                        ct.setV1(s.getSpecificVertex(0));
                        ct.setV2(t.getSpecificVertex(1));
                        ct.setLeft(s.getSpecificVertex(0));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();

                        // TODO tree structure
                    }
                    // otherwise it exits below
                    else {
                        System.out.println("Exits below defined right vertex");
                        
                        // trapezoid above segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), t.getSpecificVertex(1), 
                                t.getSpecificVertex(2), v1);
                        t1.setRight(t.getRight());
                        t1.setLeft(s.getSpecificVertex(0));
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t.getTop());
                        
                        // trapezoid below segment continues
                        ct = new Trapezoid();
                        ct.setV1(t.getSpecificVertex(0));
                        ct.setV2(s.getSpecificVertex(0));
                        ct.setLeft(s.getSpecificVertex(0));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();

                        // TODO tree structure
                    }
                    
                    // </editor-fold>
                }
                // left vertex of s lies on top of right edge of t
                // s starts in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(0))
                        && Objects.equals(t.getRight().getX(), s.getSpecificVertex(0).getX())) {
                    
                    // <editor-fold defaultstate="collapsed" desc="starts LR">
                    
                    System.out.println(s.getLabel() + " starts in " + t.getLabel());
                    System.out.println("Left endpoint lies on right edge of the trapezoid");
                    
                    // </editor-fold>
                }
                // s starts in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(0))) {
                    
                    // <editor-fold defaultstate="collapsed" desc="starts">
                    
                    // split intersected trapezoid into 3 trapezoids
                    // upper and lower part above and below segment
                    // and a trapezoid to the left of the segment
                    System.out.println(s.getLabel() + " starts in " + t.getLabel());
                    
                    // compute side of exit intersection
                    Vertex v1 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(2));
                    boolean exitAbove = v1.getY() > t.getRight().getY();
                        
                    // compute the required vertices for the new trapezoids
                    Edge e1 = new Edge("e1", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), 10000.0, "tv1"));
                    Edge e2 = new Edge("e2", s.getSpecificVertex(0), new Vertex(s.getSpecificVertex(0).getX(), -10000.0, "tv2"));
                    Vertex v2 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e1);
                    Vertex v3 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e2);
                                        
                    // if it exits above the defined right vertex of the intersected trapezoid
                    if (exitAbove) {
                        System.out.println("Exits above defined right vertex");
                        
                        // trapezoid below segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(v3, s.getSpecificVertex(0), 
                                v1, t.getSpecificVertex(3));
                        t1.setRight(t.getRight());
                        t1.setLeft(s.getSpecificVertex(0));
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // trapezoid left of segment
                        Trapezoid t2 = CreateTrapezoidByVertices(t.getSpecificVertex(0), t.getSpecificVertex(1), 
                                v2, v3);
                        t2.setRight(s.getSpecificVertex(0));
                        t2.setLeft(t.getLeft());
                        t2.setBottom(t2.getSpecificEdge(3));
                        t2.setTop(t2.getSpecificEdge(1));
                                               
                        // trapezoid above segment continues
                        ct = new Trapezoid();
                        ct.setV1(s.getSpecificVertex(0));
                        ct.setV2(v2);
                        ct.setLeft(s.getSpecificVertex(0));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                    
                        // print for feedback
                        t1.print();
                        t2.print();

                        // TODO tree structure
                    }
                    // otherwise it exits below
                    else {
                        System.out.println("Exits below defined right vertex");
                    
                        // trapezoid above segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(s.getSpecificVertex(0), v2, 
                                t.getSpecificVertex(2), v1);
                        t1.setRight(t.getRight());
                        t1.setLeft(s.getSpecificVertex(0));
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // trapezoid left of segment
                        Trapezoid t2 = CreateTrapezoidByVertices(t.getSpecificVertex(0), t.getSpecificVertex(1), 
                                v2, v3);
                        t2.setRight(s.getSpecificVertex(0));
                        t2.setLeft(t.getLeft());
                        t2.setBottom(t2.getSpecificEdge(3));
                        t2.setTop(t2.getSpecificEdge(1));
                        
                        // trapezoid below segment continues
                        ct = new Trapezoid();
                        ct.setV1(v3);
                        ct.setV2(s.getSpecificVertex(0));
                        ct.setLeft(s.getSpecificVertex(0));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                    
                        // print for feedback
                        t1.print();
                        t2.print();

                        // TODO tree structure
                    }
                    
                    // </editor-fold>
                }
                // right vertex of s lies on top of left edge of t
                // s ends in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(1))
                        && Objects.equals(t.getLeft().getX(), s.getSpecificVertex(1).getX())) {
                    
                    // <editor-fold defaultstate="collapsed" desc="ends RL">
                    
                    System.out.println(s.getLabel() + " ends in " + t.getLabel());
                    System.out.println("Right endpoint lies on left edge of the trapezoid");
                    
                    // </editor-fold>
                }
                // right vertex of s lies on top of right edge of t
                // s ends in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(1))
                        && Objects.equals(t.getRight().getX(), s.getSpecificVertex(1).getX())) {
                    
                    // <editor-fold defaultstate="collapsed" desc="ends RR">
                    
                    // split intersected trapezoid into 2 trapezoids
                    // upper and lower part above and below segment
                    System.out.println(s.getLabel() + " ends in " + t.getLabel());
                    System.out.println("Right endpoint lies on right edge of the trapezoid");
                    
                    // compute side of entrance intersection
                    Vertex v1 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(0));
                    
                    // if segments don't intersect, go to the next intersected trapezoid
                    if (v1 == null || v1.getX() == null || v1.getY() == null) {
                        continue;
                    } 
                    else if (Objects.equals(v1.getX(), s.getSpecificVertex(1).getX()) 
                            && Objects.equals(v1.getY(), s.getSpecificVertex(1).getY())) {
                        continue;
                    }
                    
                    boolean entersAbove = v1.getY() > t.getLeft().getY();
                                        
                    // if it enters above the defined left vertex of the intersected trapezoid
                    if (entersAbove) {
                        System.out.println("Enters above defined left vertex");
                        
                        // trapezoid below segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(t.getSpecificVertex(0), v1, 
                                s.getSpecificVertex(1), t.getSpecificVertex(3));
                        t1.setRight(s.getSpecificVertex(1));
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t.getBottom());
                        t1.setTop(t1.getSpecificEdge(1));
                                               
                        // trapezoid above segment ends
                        Trapezoid t2 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), t.getSpecificVertex(2), s.getSpecificVertex(1));
                        t2.setLeft(ct.getLeft());
                        t2.setRight(s.getSpecificVertex(1));
                        t2.setBottom(s);
                        t2.setTop(t2.getSpecificEdge(1));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                    
                        // print for feedback
                        t1.print();
                        t2.print();

                        // TODO tree structure
                    }
                    // otherwise it enters below
                    else {
                        System.out.println("Enters below defined left vertex");
                    
                        // trapezoid above segment
                        Trapezoid t1 = CreateTrapezoidByVertices(v1, t.getSpecificVertex(1), 
                                t.getSpecificVertex(2), s.getSpecificVertex(1));
                        t1.setRight(s.getSpecificVertex(1));
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t.getTop());                      
                        
                        // trapezoid below segment ends
                        Trapezoid t2 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), s.getSpecificVertex(1), t.getSpecificVertex(3));
                        t2.setLeft(ct.getLeft());
                        t2.setRight(s.getSpecificVertex(1));
                        t2.setBottom(t2.getSpecificEdge(3));
                        t2.setTop(s);
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                    
                        // print for feedback
                        t1.print();
                        t2.print();
                        
                        // TODO tree structure
                    }
                    
                    // </editor-fold>
                }
                // s ends in t
                else if (DoesTrapezoidContainVertex(t, s.getSpecificVertex(1))) {
                    
                    // <editor-fold defaultstate="collapsed" desc="ends">
                    
                    // split intersected trapezoid into 3 trapezoids
                    // upper and lower part above and below segment
                    // and a trapezoid to the right of the segment
                    System.out.println(s.getLabel() + " ends in " + t.getLabel());
                    
                    // compute side of entrance intersection
                    Vertex v1 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(0));
                    boolean entersAbove = v1.getY() > t.getLeft().getY();
                        
                    // compute the required vertices for the new trapezoids
                    Edge e1 = new Edge("e1", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), 10000.0, "tv1"));
                    Edge e2 = new Edge("e2", s.getSpecificVertex(1), new Vertex(s.getSpecificVertex(1).getX(), -10000.0, "tv2"));
                    Vertex v2 = GetIntersectionPointOfSegments(t.getSpecificEdge(1), e1);
                    Vertex v3 = GetIntersectionPointOfSegments(t.getSpecificEdge(3), e2);
                                        
                    // if it enters above the defined left vertex of the intersected trapezoid
                    if (entersAbove) {
                        System.out.println("Enters above defined left vertex");
                        
                        // trapezoid below segment 
                        Trapezoid t1 = CreateTrapezoidByVertices(t.getSpecificVertex(0), v1, 
                                s.getSpecificVertex(1), v3);
                        t1.setRight(s.getSpecificVertex(1));
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // trapezoid right of segment
                        Trapezoid t2 = CreateTrapezoidByVertices(v2, t.getSpecificVertex(2), 
                                t.getSpecificVertex(3), v3);
                        t2.setRight(t.getRight());
                        t2.setLeft(s.getSpecificVertex(1));
                        t2.setBottom(t2.getSpecificEdge(3));
                        t2.setTop(t2.getSpecificEdge(1));
                                               
                        // trapezoid above segment ends
                        Trapezoid t3 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), v2, s.getSpecificVertex(1));
                        t3.setLeft(ct.getLeft());
                        t3.setRight(s.getSpecificVertex(1));
                        t3.setBottom(s);
                        t3.setTop(t3.getSpecificEdge(1));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                        this.trapezoids.add(t3);
                    
                        // print for feedback
                        t1.print();
                        t2.print();
                        t3.print();

                        // TODO tree structure
                    }
                    // otherwise it enters below
                    else {
                        System.out.println("Enters below defined left vertex");
                    
                        // trapezoid above segment
                        Trapezoid t1 = CreateTrapezoidByVertices(v1, t.getSpecificVertex(1),
                                v2, s.getSpecificVertex(1));
                        t1.setRight(s.getSpecificVertex(1));
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // trapezoid right of segment
                        Trapezoid t2 = CreateTrapezoidByVertices(v2, t.getSpecificVertex(2), 
                                t.getSpecificVertex(3), v3);
                        t2.setRight(t.getRight());
                        t2.setLeft(s.getSpecificVertex(1));
                        t2.setBottom(t2.getSpecificEdge(3));
                        t2.setTop(t2.getSpecificEdge(1));
                                               
                        // trapezoid below segment ends
                        Trapezoid t3 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), s.getSpecificVertex(1), v3);
                        t3.setLeft(ct.getLeft());
                        t3.setRight(s.getSpecificVertex(1));
                        t3.setBottom(t3.getSpecificEdge(3));
                        t3.setTop(s);
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                        this.trapezoids.add(t2);
                        this.trapezoids.add(t3);
                    
                        // print for feedback
                        t1.print();
                        t2.print();
                        t3.print();
                        
                        // TODO tree structure
                    }
                    
                    // </editor-fold>
                }
                // s completely intersects t
                else if (ct != null) {
                    
                    // <editor-fold defaultstate="collapsed" desc="intersects">
                    
                    // split intersected trapezoid into 2 trapezoids
                    // upper and lower part above and below segment
                    System.out.println(s.getLabel() + " completely intersects " + t.getLabel());
                    
                    // compute side of entrance intersection
                    Vertex v1 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(0));
                    
                    if (v1 == null || v1.getX() == null || v1.getY() == null) {
                        continue;
                    }
                    
                    boolean entersAbove = v1.getY() > t.getLeft().getY();
                    
                    // compute side of exit intersection
                    Vertex v2 = GetIntersectionPointOfSegments(s, t.getSpecificEdge(2));
                    
                    if (v2 == null || v2.getX() == null || v2.getY() == null) {
                        continue;
                    }
                    
                    boolean exitsAbove = v2.getY() > t.getRight().getY();
                    
                    // if it enters above the defined left vertex of the intersected trapezoid
                    // and it exits above the defined right vertex of the intersected trapezoid
                    if (entersAbove && exitsAbove) {
                        System.out.println("Enters and exits above defined vertices");
                        
                        // trapezoid below segment
                        Trapezoid t1 = CreateTrapezoidByVertices(t.getSpecificVertex(0), v1, 
                                v2, t.getSpecificVertex(3));
                        t1.setRight(t.getRight());
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t.getBottom());
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();
                    
                        // TODO tree structure
                    }
                    // if it enters above the defined left vertex of the intersected trapezoid
                    // and it exits below the defined right vertex of the intersected trapezoid
                    else if (entersAbove && !exitsAbove) {
                        System.out.println("Enters above defined left vertex and exits below defined right vertex");
                        
                        // trapezoid above segment ends
                        Trapezoid t1 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), t.getSpecificVertex(2), v2);
                        t1.setLeft(ct.getLeft());
                        t1.setRight(t.getRight());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);
                        
                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();
                        
                        // trapezoid below segment continues
                        ct = new Trapezoid();
                        ct.setV1(t.getSpecificVertex(0));
                        ct.setV2(v1);
                        ct.setLeft(t.getLeft());
                    
                        // TODO tree structure                        
                    }
                    // if it enters below the defined left vertex of the intersected trapezoid
                    // and it exits above the defined right vertex of the intersected trapezoid
                    else if (!entersAbove && exitsAbove) {
                        System.out.println("Enters below defined left vertex and exits above defined right vertex");
                        
                        // trapezoid below segment ends
                        Trapezoid t1 = CreateTrapezoidByVertices(ct.getV1(), ct.getV2(), v2, t.getSpecificVertex(3));
                        t1.setLeft(ct.getLeft());
                        t1.setRight(t.getRight());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t1.getSpecificEdge(1));
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);
                        
                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();
                        
                        // trapezoid above segment continues
                        ct = new Trapezoid();
                        ct.setV1(v1);
                        ct.setV2(t.getSpecificVertex(1));
                        ct.setLeft(t.getLeft());
                    
                        // TODO tree structure                        
                    }
                    // if it enters below the defined left vertex of the intersected trapezoid
                    // and it exits below the defined right vertex of the intersected trapezoid
                    else {
                        System.out.println("Enters and exits below defined vertices");
                        
                        // trapezoid above segment
                        Trapezoid t1 = CreateTrapezoidByVertices(v1, t.getSpecificVertex(1),
                                t.getSpecificVertex(2), v2);
                        t1.setRight(t.getRight());
                        t1.setLeft(t.getLeft());
                        t1.setBottom(t1.getSpecificEdge(3));
                        t1.setTop(t.getTop());
                        
                        // remove intersected trapezoid from the list
                        this.trapezoids.remove(t);

                        // add the new trapezoids to the list
                        this.trapezoids.add(t1);
                    
                        // print for feedback
                        t1.print();
                        
                        // TODO tree structure
                    }
                    
                    // </editor-fold>
                }
            }
        }
    }
    
    public void removeInnerTrapezoids(List<Polygon> polygons) {
        List<Trapezoid> outerTrapezoids = new ArrayList<>();
        
        if (polygons == null) {
            return;
        }
        
        System.out.println("Remove inner polygons, the following ones may stay:");
        
        // loop trapezoids
        for (int i = 0; i < this.trapezoids.size(); i++) {
            
            // take halfway points of vertical segments
//            Vertex hp1 = HalfwayPoint(this.trapezoids.get(i).getSpecificEdge(0));
//            Vertex hp2 = HalfwayPoint(this.trapezoids.get(i).getSpecificEdge(2));
            Vertex hp1 = HalfwayPoint(this.trapezoids.get(i).getSpecificVertex(0), this.trapezoids.get(i).getSpecificVertex(1));
            Vertex hp2 = HalfwayPoint(this.trapezoids.get(i).getSpecificVertex(2), this.trapezoids.get(i).getSpecificVertex(3));
            
            // variable to remember if tagged
            boolean isContained = false;
            
            // loop polygons
            for (int j = 0; j < polygons.size(); j++) {
                
                // tag if one of the halfway points is contained in a polygon
                if ((hp1 != null && DoesPolygonContainVertex(polygons.get(j), hp1)) 
                        || (hp2 != null && DoesPolygonContainVertex(polygons.get(j), hp2))) {
                    isContained = true;
                    
                    // no need to look further
                    break;
                }
            }
            
            // if not tagged
            if (!isContained) {
                
                // legit trapezoid for further processing
                outerTrapezoids.add(this.trapezoids.get(i));
                this.trapezoids.get(i).print();
                System.out.println("-------------------------------------------");
            }
        }
        
        // overwrite set of trapezoids with legit ones
        this.trapezoids = outerTrapezoids;
    }
    
    public void removeOuterTrapezoids(Polygon polygon) {
        List<Trapezoid> innerTrapezoids = new ArrayList<>();
        
        if (polygon == null) {
            return;
        }
        
        System.out.println("Remove outer polygons, the following ones may stay:");
        
        // loop trapezoids
        for (int i = 0; i < this.trapezoids.size(); i++) {
            
            // take halfway points of vertical segments
            Vertex hp1 = HalfwayPoint(this.trapezoids.get(i).getSpecificVertex(0), this.trapezoids.get(i).getSpecificVertex(1));// this.trapezoids.get(i).getSpecificEdge(0));
            Vertex hp2 = HalfwayPoint(this.trapezoids.get(i).getSpecificVertex(2), this.trapezoids.get(i).getSpecificVertex(3));//this.trapezoids.get(i).getSpecificEdge(2));
            
            
            // tag if one of the halfway points is contained in a polygon
            if ((hp1 != null && DoesPolygonContainVertex(polygon, hp1)) 
                    && (hp2 != null && DoesPolygonContainVertex(polygon, hp2))) {
                this.trapezoids.get(i).print();
                System.out.println("-------------------------------------------");
                innerTrapezoids.add(this.trapezoids.get(i));
            }
        }
        
        // overwrite set of trapezoids with legit ones
        this.trapezoids = innerTrapezoids;
    }
    
    public void triangulateTrapezoids() {
        List<Triangle> triangles = new ArrayList<>();
        
        System.out.println("Triangulate " + this.trapezoids.size() + " trapezoids");
        
        for (int i = 0; i < this.trapezoids.size(); i++) {
            
            this.trapezoids.get(i).print();            
            
            if (NotTheSameVertex(this.trapezoids.get(i).getV1(), this.trapezoids.get(i).getV2())
                    && NotTheSameVertex(this.trapezoids.get(i).getV1(), this.trapezoids.get(i).getV3())
                    && NotTheSameVertex(this.trapezoids.get(i).getV1(), this.trapezoids.get(i).getV4())
                    && NotTheSameVertex(this.trapezoids.get(i).getV2(), this.trapezoids.get(i).getV3())
                    && NotTheSameVertex(this.trapezoids.get(i).getV2(), this.trapezoids.get(i).getV4())
                    && NotTheSameVertex(this.trapezoids.get(i).getV3(), this.trapezoids.get(i).getV4())) {
                
                System.out.println("NOT TRIANGLE");
                
                // left bottom triangle
                Trapezoid t1 = CreateTrapezoidByVertices(this.trapezoids.get(i).getSpecificVertex(0), 
                        this.trapezoids.get(i).getSpecificVertex(0), this.trapezoids.get(i).getSpecificVertex(1), 
                        this.trapezoids.get(i).getSpecificVertex(3));
                t1.setRight(null);
                t1.setLeft(null);
                t1.setBottom(null);
                t1.setTop(null);
                
                // right upper triangle
                Trapezoid t2 = CreateTrapezoidByVertices(this.trapezoids.get(i).getSpecificVertex(1), 
                        this.trapezoids.get(i).getSpecificVertex(2), this.trapezoids.get(i).getSpecificVertex(2), 
                        this.trapezoids.get(i).getSpecificVertex(3));
                t2.setRight(null);
                t2.setLeft(null);
                t2.setBottom(null);
                t2.setTop(null);
                
                triangles.add(TrapezoidToTriangle(t1));
                triangles.add(TrapezoidToTriangle(t2));
            }
            else {
                
                // trapezoid was already a triangle
                triangles.add(TrapezoidToTriangle(this.trapezoids.get(i)));
            }
        }
        
        this.triangles = triangles;
    }
    
    public void colorTriangles() {
        System.out.println("Color " + this.triangles.size() + " triangles");
        
        if (this.triangles == null || this.triangles.isEmpty()) {
            return;
        }
        
        Vertex cv = this.triangles.get(0).getV1();
        Vertex nv = this.triangles.get(0).getV2();
        Vertex ov = this.triangles.get(0).getV3();
        
        List<Triangle> subTriangles = new ArrayList<>();
        
        // loop all triangles
        for (int i = 0; i < this.triangles.size(); i++) {
            
            // find all trapezoids that share the vertex cv
            if (TheSameVertex(cv, this.triangles.get(i).getV1())
                    || TheSameVertex(cv, this.triangles.get(i).getV2())
                    || TheSameVertex(cv, this.triangles.get(i).getV3())) {
                
                subTriangles.add(this.triangles.get(i));
            }
                        
            for (int j = 0; j < subTriangles.size(); j++) {
                
                Vertex[] vertices = new Vertex[] { 
                    subTriangles.get(j).getV1(), subTriangles.get(j).getV2(), subTriangles.get(j).getV3()
                };
                
                int cvIndex = 0;
                int nvIndex = 0;
                int ovIndex = 0;
                
                for (int k = 0; k < vertices.length; k++) {
                    if (TheSameVertex(cv, vertices[k])) {
                        cvIndex = k;
                        nvIndex = k + 1 % 3;
                        ovIndex = k + 2 % 3;
                    }
                }
                
                cv = vertices[cvIndex];
                nv = vertices[nvIndex];
                ov = vertices[ovIndex];
                
                cv.setColor(5);
                nv.setColor(3);
                ov.setColor(0);
                
                for (int k = 0; k < subTriangles.size(); k++) {
                    
                    if (k != j && TriangleHasEdge(subTriangles.get(k), cv, nv)) {
                        
                    }
                }
                
                for (int k = 0; k < subTriangles.size(); k++) {
                    
                    if (k != j && TriangleHasEdge(subTriangles.get(k), cv, ov)) {
                        
                    }
                }
            }
        }
    }
    
    public boolean NotTheSameVertex(Vertex v1, Vertex v2) {
        if (!Objects.equals(v1.getX(), v2.getX()) || !Objects.equals(v1.getY(), v2.getY())) {
            return true;
        }
        return false;
    }
    
    public boolean TheSameVertex(Vertex v1, Vertex v2) {
        if (Objects.equals(v1.getX(), v2.getX()) && Objects.equals(v1.getY(), v2.getY())) {
            return true;
        }
        return false;
    }
    
    public boolean TrianglesShareEdge(Triangle t1, Triangle t2) {
        int sameVerticesCount = 0;
        
        if (TheSameVertex(t1.getV1(), t2.getV1()) || TheSameVertex(t1.getV1(), t2.getV2())
                || TheSameVertex(t1.getV1(), t2.getV3())) {
            sameVerticesCount++;
        }
        
        if (TheSameVertex(t1.getV2(), t2.getV1()) || TheSameVertex(t1.getV2(), t2.getV2())
                || TheSameVertex(t1.getV2(), t2.getV3())) {
            sameVerticesCount++;
        }
        
        if (TheSameVertex(t1.getV3(), t2.getV1()) || TheSameVertex(t1.getV3(), t2.getV2())
                || TheSameVertex(t1.getV3(), t2.getV3())) {
            sameVerticesCount++;
        }
        
        return sameVerticesCount == 2;
    }
    
    public boolean TriangleHasEdge(Triangle t, Edge e) {
        return TriangleHasEdge(t, e.getV1(), e.getV2());
    }
    
    public boolean TriangleHasEdge(Triangle t, Vertex v1, Vertex v2) {
        if (TheSameVertex(v1, v2)) {
            return false;
        }
        
        if ((TheSameVertex(t.getV1(), v1) || TheSameVertex(t.getV2(), v1) 
                || TheSameVertex(t.getV3(), v1)) && (TheSameVertex(t.getV1(), v2) 
                || TheSameVertex(t.getV2(), v2) || TheSameVertex(t.getV3(), v2))) {
            return true;
        }
        
        return false;
    }
    
    public Vertex HalfwayPoint(Edge e) {
        if (e == null || e.getV1() == null || e.getV2() == null) {
            return null;
        }
        return HalfwayPoint(e.getV1(), e.getV2());
    }
    
    public Vertex HalfwayPoint(Vertex v1, Vertex v2) {
        if (v1 == null || v2 == null) {
            return null;
        }
        // the midpoint formula
        double x = (v1.getX() + v2.getX()) / 2;
        double y = (v1.getY() + v2.getY()) / 2;
        return new Vertex(x, y, "hp");
    }
    
    public Trapezoid CreateTrapezoidByVertices(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        Trapezoid t = new Trapezoid();
        t.setV1(v1);
        t.setV2(v2);
        t.setV3(v3);
        t.setV4(v4);
        t.setE1(new Edge("e1", t.getV1(), t.getV2()));
        t.setE2(new Edge("e2", t.getV2(), t.getV3()));
        t.setE3(new Edge("e3", t.getV3(), t.getV4()));
        t.setE4(new Edge("e4", t.getV4(), t.getV1()));
        t.setLabel(CreateTrapezoidLabel());
        return t;
    }
    
    public Triangle CreateTriangleByVertices(Vertex v1, Vertex v2, Vertex v3) {
        Triangle t = new Triangle();
        t.setV1(v1);
        t.setV2(v2);
        t.setV3(v3);
        t.setE1(new Edge("e1", t.getV1(), t.getV2()));
        t.setE2(new Edge("e2", t.getV2(), t.getV3()));
        t.setE3(new Edge("e3", t.getV3(), t.getV1()));
        t.setLabel(CreateTrapezoidLabel());
        return t;
    }
    
    public Triangle TrapezoidToTriangle(Trapezoid t) {
        Vertex[] triangleVertices = new Vertex[3];
        Vertex[] trapezoidVertices = new Vertex[] {
            t.getV1(), t.getV2(), t.getV3(), t.getV4()
        };
        
        for (int i = 0; i < trapezoidVertices.length; i++) {
            boolean found = false;
            for (int j = 0; j < trapezoidVertices.length; j++) {
                if (i != j && TheSameVertex(trapezoidVertices[i], trapezoidVertices[j])) {
                    triangleVertices[0] = trapezoidVertices[i];
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        
        int index = 1;
        
        for (int i = 0; i < trapezoidVertices.length; i++) {
            if (index == 3) {
                break;
            }
            if (NotTheSameVertex(trapezoidVertices[i], triangleVertices[0])) {
                triangleVertices[index] = trapezoidVertices[i];
                index++;
            }
        }
        
        return CreateTriangleByVertices(triangleVertices[0], triangleVertices[1], triangleVertices[2]);
    }
    
    public Trapezoid FinishTrapezoidWithVertices(Trapezoid t) {
        t.setE1(new Edge("e1", t.getV1(), t.getV2()));
        t.setE2(new Edge("e2", t.getV2(), t.getV3()));
        t.setE3(new Edge("e3", t.getV3(), t.getV4()));
        t.setE4(new Edge("e4", t.getV4(), t.getV1()));
        t.setLabel(CreateTrapezoidLabel());
        return t;
    }
    
    public String CreateTrapezoidLabel() {
        String label = "t";
        this.trapezoidCounter++;
        label += this.trapezoidCounter + "";
        return label;
    }
    
    public void ResetTrapezoidLabels() {
        this.trapezoidCounter = 0;
    }
    
    public Trapezoid DetermineBoundingBox(List<Edge> segments) {
        Trapezoid boundingBox = new Trapezoid();
        
        Double xl = Double.MAX_VALUE, xr = Double.MIN_VALUE, yb = Double.MAX_VALUE, yt = Double.MIN_VALUE;
        
        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getV1() != null && segments.get(i).getV2() != null) {
                xl = Math.min(xl, Math.min(segments.get(i).getV1().getX(), segments.get(i).getV2().getX()));
                xr = Math.max(xr, Math.max(segments.get(i).getV1().getX(), segments.get(i).getV2().getX()));
                yb = Math.min(yb, Math.min(segments.get(i).getV1().getY(), segments.get(i).getV2().getY()));
                yt = Math.max(yt, Math.max(segments.get(i).getV1().getY(), segments.get(i).getV2().getY()));
            }
        }
        
        boundingBox.setV1(new Vertex(xl - 1.0, yb - 1.0, "v1"));
        boundingBox.setV2(new Vertex(xl - 1.0, yt + 1.0, "v2"));
        boundingBox.setV3(new Vertex(xr + 1.0, yt + 1.0, "v3"));
        boundingBox.setV4(new Vertex(xr + 1.0, yb - 1.0, "v4"));
        boundingBox.setE1(new Edge("e1", boundingBox.getV1(), boundingBox.getV2()));
        boundingBox.setE2(new Edge("e2", boundingBox.getV2(), boundingBox.getV3()));
        boundingBox.setE3(new Edge("e3", boundingBox.getV3(), boundingBox.getV4()));
        boundingBox.setE4(new Edge("e4", boundingBox.getV4(), boundingBox.getV1()));
        boundingBox.setLeft(boundingBox.getV1());
        boundingBox.setRight(boundingBox.getV4());
        boundingBox.setTop(boundingBox.getE2());
        boundingBox.setBottom(boundingBox.getE4());
        boundingBox.setLabel("R");
        
        return boundingBox;
    }

    public List<Trapezoid> DoesSegmentIntersectTrapezoid(Edge segment) {
        List<Trapezoid> intersectedTrapezoids = new ArrayList<>();
        
        if (this.trapezoids == null || this.trapezoids.isEmpty()) {
            return intersectedTrapezoids;
        }
        
        for (int i = 0; i < this.trapezoids.size(); i++) {
            
            if (DoSegmentsIntersect(this.trapezoids.get(i).getE1(), segment)) {
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DoSegmentsIntersect(this.trapezoids.get(i).getE2(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DoSegmentsIntersect(this.trapezoids.get(i).getE3(), segment)) { 
                intersectedTrapezoids.add(this.trapezoids.get(i));
            } else if (DoSegmentsIntersect(this.trapezoids.get(i).getE4(), segment)) { 
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
        Double val = ((v2.getY() - v1.getY()) * (v3.getX() - v2.getX())) - ((v2.getX() - v1.getX()) * (v3.getY() - v2.getY()));
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
        Vertex extreme = new Vertex(10000.0, vertex.getY(), "Extreme Point");
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
    
    public boolean DoesPolygonContainVertex(Polygon p, Vertex v) {
        Vertex extreme = new Vertex(10000.0, v.getY(), "Extreme Point");
        Vertex[] polygon = p.getVertices().toArray(new Vertex[p.getVertices().size()]);
        int count = 0, i = 0, n = polygon.length;
        do {
            int next = (i + 1) % n;
            if (DoSegmentsIntersect(polygon[i], polygon[next], v, extreme)) {
                if (Orientation(polygon[i], v, polygon[next]) == 0) {
                    return OnSegment(polygon[i], v, polygon[next]);
                }
                count++;
            }
            i = next;
        } while (i != 0);
        return count % 2 == 1;
    }

    public Vertex GetIntersectionPointOfSegments(Edge e1, Edge e2) {
        if (e1 == null || e1.getV1() == null || e1.getV2() == null
                || e2 == null || e2.getV1() == null || e2.getV2() == null) {
            return null;
        }
        
        Vertex intersectionPoint = null;
        
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
            intersectionPoint = new Vertex(intersectionVector.x(), intersectionVector.y(), "");
            return intersectionPoint;
        }
        
        return intersectionPoint;
    }

    public List<Trapezoid> getTrapezoids() {
        return trapezoids;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }
}
