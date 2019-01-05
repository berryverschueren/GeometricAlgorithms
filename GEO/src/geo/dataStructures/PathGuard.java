package geo.dataStructures;

/**
 *
 * @author carina
 */
public class PathGuard {
    private String label;
    private double x; 
    private double y; 
    private double timestamp;
    private int observing;
    
    public PathGuard(double x, double y, double timestamp, int observing) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
        this.observing = observing; 
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public int getObserving() {
        return observing;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public void setObserving(int observing) {
        this.observing = observing;
    }
    
}
