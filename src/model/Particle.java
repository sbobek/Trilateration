package model;

/**
 * Created by sbk on 12.03.17.
 */
public class Particle {
    Location location;
    double weight;

    public Particle(Location location, double weight) {
        this.location = location;
        this.weight = weight;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Location getLocation() {
        return location;
    }
}
