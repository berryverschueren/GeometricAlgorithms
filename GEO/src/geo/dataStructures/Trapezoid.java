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
public class Trapezoid {
    private String label;
    private Vertex v1, v2, v3, v4;
    private Edge e1, e2, e3, e4;

    public Trapezoid() {
    }

    public Trapezoid(String label, Vertex v1, Vertex v2, Vertex v3, Vertex v4, Edge e1, Edge e2, Edge e3, Edge e4) {
        this.label = label;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
    }

    public String getLabel() {
        return label;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public Vertex getV3() {
        return v3;
    }

    public Vertex getV4() {
        return v4;
    }

    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }

    public Edge getE3() {
        return e3;
    }

    public Edge getE4() {
        return e4;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public void setV3(Vertex v3) {
        this.v3 = v3;
    }

    public void setV4(Vertex v4) {
        this.v4 = v4;
    }

    public void setE1(Edge e1) {
        this.e1 = e1;
    }

    public void setE2(Edge e2) {
        this.e2 = e2;
    }

    public void setE3(Edge e3) {
        this.e3 = e3;
    }

    public void setE4(Edge e4) {
        this.e4 = e4;
    }
    
    // 0 = left
    // 1 = top
    // 2 = right
    // 3 = bottom
    public Edge getSpecificEdge(int side) {
        List<Edge> edges = new ArrayList<>();
        edges.add(this.e1);
        edges.add(this.e2);
        edges.add(this.e3);
        edges.add(this.e4);
        switch (side) {
            case 0:
                for (int i = 0; i < edges.size(); i++) {
                    if ((edges.get(i).getV1() == getSpecificVertex(0) && edges.get(i).getV2() == getSpecificVertex(1))
                            || (edges.get(i).getV1() == getSpecificVertex(1) && edges.get(i).getV2() == getSpecificVertex(0))) {
                        return edges.get(i);
                    }
                }
            case 1:
                for (int i = 0; i < edges.size(); i++) {
                    Vertex v1 = getSpecificVertex(1);
                    Vertex v2 = getSpecificVertex(2);
                    Vertex v3 = edges.get(i).getV1();
                    Vertex v4 = edges.get(i).getV2();
                    if (((v3.getX() == v1.getX() && v3.getY() == v1.getY()) && (v4.getX() == v2.getX() && v4.getY() == v2.getY()))
                            || ((v4.getX() == v1.getX() && v4.getY() == v1.getY()) && (v3.getX() == v2.getX() && v3.getY() == v2.getY()))){
                        return edges.get(i);
                    }
                }
            case 2:
                for (int i = 0; i < edges.size(); i++) {
                    if ((edges.get(i).getV1() == getSpecificVertex(2) && edges.get(i).getV2() == getSpecificVertex(3))
                            || (edges.get(i).getV1() == getSpecificVertex(3) && edges.get(i).getV2() == getSpecificVertex(2))) {
                        return edges.get(i);
                    }
                }
            case 3:
                for (int i = 0; i < edges.size(); i++) {
                    if ((edges.get(i).getV1() == getSpecificVertex(0) && edges.get(i).getV2() == getSpecificVertex(3))
                            || (edges.get(i).getV1() == getSpecificVertex(3) && edges.get(i).getV2() == getSpecificVertex(0))) {
                        return edges.get(i);
                    }
                }
            default:
                return null;
        }
    }
    
    // 0 = left bottom
    // 1 = left top
    // 2 = right top
    // 3 = right bottom
    public Vertex getSpecificVertex(int point) {  
        int firstx, nextx, firsty, nexty;
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(this.v1);
        vertices.add(this.v2);
        vertices.add(this.v3);
        vertices.add(this.v4);
        switch (point) {
            case 0:
                firstx = Integer.MAX_VALUE; 
                nextx = Integer.MAX_VALUE; 
                firsty = Integer.MAX_VALUE;
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getX() <= firstx) {
                        nextx = firstx;
                        firstx = vertices.get(i).getX();
                    } else if (vertices.get(i).getX() > firstx && vertices.get(i).getX() < nextx) {
                        nextx = vertices.get(i).getX();
                    }
                }                
                for (int i = 3; i > -1; i--) {
                    if (vertices.get(i).getX() != firstx && vertices.get(i).getX() != nextx) {
                        vertices.remove(i);
                    }
                }
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getY() < firsty) {
                        firsty = vertices.get(i).getY();
                    }
                }
                for (int i = 1; i > -1; i--) {
                    if (vertices.get(i).getY() != firsty) { 
                        vertices.remove(i);
                    }
                }
                return vertices.get(0);
            case 1:
                firstx = Integer.MAX_VALUE; 
                nextx = Integer.MAX_VALUE; 
                firsty = Integer.MIN_VALUE;
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getX() <= firstx) {
                        nextx = firstx;
                        firstx = vertices.get(i).getX();
                    } else if (vertices.get(i).getX() > firstx && vertices.get(i).getX() < nextx) {
                        nextx = vertices.get(i).getX();
                    }
                }                
                for (int i = 3; i > -1; i--) {
                    if (vertices.get(i).getX() != firstx && vertices.get(i).getX() != nextx) {
                        vertices.remove(i);
                    }
                }
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getY() > firsty) {
                        firsty = vertices.get(i).getY();
                    }
                }
                for (int i = 1; i > -1; i--) {
                    if (vertices.get(i).getY() != firsty) {
                        vertices.remove(i);
                    }
                }
                return vertices.get(0);
            case 2:
                firstx = Integer.MIN_VALUE; 
                nextx = Integer.MIN_VALUE; 
                firsty = Integer.MIN_VALUE;
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getX() >= firstx) {
                        nextx = firstx;
                        firstx = vertices.get(i).getX();
                    } else if (vertices.get(i).getX() < firstx && vertices.get(i).getX() > nextx) {
                        nextx = vertices.get(i).getX();
                    }
                }                
                for (int i = 3; i > -1; i--) {
                    if (vertices.get(i).getX() != firstx && vertices.get(i).getX() != nextx) {
                        vertices.remove(i);
                    }
                }
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getY() > firsty) {
                        firsty = vertices.get(i).getY();
                    }
                }
                for (int i = 1; i > -1; i--) {
                    if (vertices.get(i).getY() != firsty) {
                        vertices.remove(i);
                    }
                }
                return vertices.get(0);
            case 3:
                firstx = Integer.MIN_VALUE; 
                nextx = Integer.MIN_VALUE; 
                firsty = Integer.MAX_VALUE;
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getX() >= firstx) {
                        nextx = firstx;
                        firstx = vertices.get(i).getX();
                    } else if (vertices.get(i).getX() < firstx && vertices.get(i).getX() > nextx) {
                        nextx = vertices.get(i).getX();
                    }
                }                
                for (int i = 3; i > -1; i--) {
                    if (vertices.get(i).getX() != firstx && vertices.get(i).getX() != nextx) {
                        vertices.remove(i);
                    }
                }
                for (int i = 0; i < vertices.size(); i++) {
                    if (vertices.get(i).getY() < firsty) {
                        firsty = vertices.get(i).getY();
                    }
                }
                for (int i = 1; i > -1; i--) {
                    if (vertices.get(i).getY() != firsty) {
                        vertices.remove(i);
                    }
                }
                return vertices.get(0);
            default:
                return null;
        }
    }
}
