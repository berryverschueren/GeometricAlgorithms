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
    private Gallery gallery;
    private int guards;
    private double speed;
    private double globalTime;
    private double observationTime;

    public GalleryProblem() {
    }

    public GalleryProblem(Gallery gallery, int guards, double speed, double globalTime, double observationTime) {
        this.gallery = gallery;
        this.guards = guards;
        this.speed = speed;
        this.globalTime = globalTime;
        this.observationTime = observationTime;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public int getGuards() {
        return guards;
    }

    public double getSpeed() {
        return speed;
    }

    public double getGlobalTime() {
        return globalTime;
    }

    public double getObservationTime() {
        return observationTime;
    }
    
}
