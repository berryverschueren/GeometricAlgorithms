/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo.dataStructures;

/**
 *
 * @author Kaj75
 */
public class GalleryProblem {
    private Gallery gellery;
    private int guards;
    private int speed;
    private int globalTime;
    private int observationTime;

    public GalleryProblem() {
    }

    public GalleryProblem(Gallery gellery, int guards, int speed, int globalTime, int observationTime) {
        this.gellery = gellery;
        this.guards = guards;
        this.speed = speed;
        this.globalTime = globalTime;
        this.observationTime = observationTime;
    }
    
}
