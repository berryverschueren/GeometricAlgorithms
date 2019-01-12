package geo.dataStructures;

/**
 *
 * @author carina
 */
public class PathRobber {
    private String label;
    private double x; 
    private double y; 
    private double timestamp;
    
    public PathRobber(){}
    
    public PathRobber(double x, double y, double timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

}
