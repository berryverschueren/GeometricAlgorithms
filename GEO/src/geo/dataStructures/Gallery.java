/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

import java.util.List;

/**
 *
 * @author Kaj75
 */
public class Gallery {
    private int exits;
    private int artPieces;
    private Polygon outerPolygon;
    private List<Polygon> innerPolygons; //holes

    public Gallery() {
    }

    public Gallery(int exits, int artPieces, Polygon outerPolygon, List<Polygon> innerPolygons) {
        this.exits = exits;
        this.artPieces = artPieces;
        this.outerPolygon = outerPolygon;
        this.innerPolygons = innerPolygons;
    }

    public int getExits() {
        return exits;
    }

    public void setExits(int exits) {
        this.exits = exits;
    }

    public int getArtPieces() {
        return artPieces;
    }

    public void setArtPieces(int artPieces) {
        this.artPieces = artPieces;
    }

    public Polygon getOuterPolygon() {
        return outerPolygon;
    }

    public void setOuterPolygon(Polygon outerPolygon) {
        this.outerPolygon = outerPolygon;
    }

    public List<Polygon> getInnerPolygons() {
        return innerPolygons;
    }

    public void setInnerPolygons(List<Polygon> innerPolygons) {
        this.innerPolygons = innerPolygons;
    }
    
}
