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
 * @author Kaj75
 */
public class VertexInfo {
    private Vertex vertex;
    private int numExit;
    private int numArt;
    private List<Vertex> exits;
    private List<Vertex> arts;
    private boolean isExit = false;
    private List<Vertex> seeMe;
    
    public VertexInfo(){
        exits = new ArrayList<>();
        arts = new ArrayList<>();
        seeMe = new ArrayList<>();
        numArt=0;
        numExit=0;
    }

    public VertexInfo(Vertex vertex, int numExit, int numArt, List<Vertex> exits, List<Vertex> arts) {
        this.vertex = vertex;
        this.numExit = numExit;
        this.numArt = numArt;
        this.exits = exits;
        this.arts = arts;
    }

    public boolean isIsExit() {
        return isExit;
    }

    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }

    public List<Vertex> getSeeMe() {
        return seeMe;
    }

    public void setSeeMe(List<Vertex> seeMe) {
        this.seeMe = seeMe;
    }
    
    

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public int getNumExit() {
        return numExit;
    }

    public void setNumExit(int numExit) {
        this.numExit = numExit;
    }

    public int getNumArt() {
        return numArt;
    }

    public void setNumArt(int numArt) {
        this.numArt = numArt;
    }

    public List<Vertex> getExits() {
        return exits;
    }

    public void setExits(List<Vertex> exits) {
        this.exits = exits;
    }

    public List<Vertex> getArts() {
        return arts;
    }

    public void setArts(List<Vertex> arts) {
        this.arts = arts;
    }

    void addArt(Vertex vertex) {
        arts.add(vertex);
        numArt++;
    }

    void addExit(Vertex vertex) {
        exits.add(vertex);
        numExit++;
    }

    void addSeesMe(Vertex vertex) {
        seeMe.add(vertex);
    }
    
    
    
}
