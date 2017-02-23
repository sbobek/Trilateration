package model;

/**
 * Created by sbk on 23.02.17.
 */
public class Robot {
    private Location realLocation;
    private Location calculatedLocation;

    public Robot(Location realLocation) {
        this.realLocation = realLocation;
        calculatedLocation = new Location(0,0);
    }

    public Location getRealLocation(){
        return realLocation;
    }

    public Location getCalculatedLocation() {
        return calculatedLocation;
    }

    public Robot setCalculatedLocation(Location calculatedLocation) {
        this.calculatedLocation = calculatedLocation;
        return this;
    }

    public Location determineLocation(RSSI tower1Message, RSSI tower2Message, RSSI tower3Message){
        ////////////////////////////// YOUR CODE HERE ////////////////////////////////////////

        return new Location(0,0);

        /////////////////////////////////////////////////////////////////////////////////////
    }

}
