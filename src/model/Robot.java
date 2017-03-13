package model;

import java.util.Random;

/**
 * Created by sbk on 23.02.17.
 */
public class Robot {
    private Location realLocation;
    private Location calculatedLocation;
    private WorldMap map;

    public Robot(WorldMap map, Location realLocation) {
        this.map = map;
        this.realLocation = realLocation;
        calculatedLocation = new Location(0,0);
    }

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

    public void resampleParticles(){
        //caclulate cumulative sum, and resample with weight 1/length
        double[] cumsum = new double[map.getParticles().length];
        int idx = 0;
        double previous = 0;
        for(Particle p : map.getParticles()){
            cumsum[idx] = previous + p.getWeight();
            previous = cumsum[idx];
            idx++;
        }

        Random r = new Random();
        Particle [] newParticles = new Particle[map.getParticles().length];
        for(int i = 0; i < map.getParticles().length;i++){
            double val  = r.nextDouble();
            for(int j =0; j < cumsum.length;j++){
                if(val <= cumsum[j]){
                    newParticles[i]  = new Particle(map.getParticles()[j]);
                    break;
                }
            }
        }

        normalizeWeights();

    }

    public void weightParticles(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
        double r1 = Math.pow(10, (tower1Message.getTxPower()-tower1Message.getRssi())/(10*tower1Message.getN()));
        double r2 = Math.pow(10, (tower2Message.getTxPower()-tower2Message.getRssi())/(10*tower2Message.getN()));
        double r3 = Math.pow(10, (tower3Message.getTxPower()-tower3Message.getRssi())/(10*tower3Message.getN()));

        for(Particle p : map.getParticles()){
            //calculate distance;
            double d1 = tower1Message.getTowerLocation().euclideanDistanceTo(p.getLocation());
            double d2 = tower2Message.getTowerLocation().euclideanDistanceTo(p.getLocation());
            double d3 = tower3Message.getTowerLocation().euclideanDistanceTo(p.getLocation());

            p.setWeight(gaussian(d1,r1,1)*gaussian(d2,r2,1)*gaussian(d3,r3,1));
        }

    //    normalizeWeights();
        for(Particle p: getMap().getParticles()){
            System.out.print(" "+p.getWeight());
        }

    }

    public void normalizeWeights(){
        double sum = 0;
        for(Particle p: map.getParticles()){
            sum += p.getWeight();
        }
        System.out.println("Sum: "+sum);
        for(Particle p: map.getParticles()){
            p.setWeight(p.getWeight()/sum);
        }
    }

    public void resetParticles(){
        map.resetParticles();
    }

    private double gaussian(double x, double mu, double sdv){
        return 1/(sdv*Math.sqrt(2*Math.PI))*Math.exp(-(x-mu)*(x-mu)/(2*sdv*sdv));
    }



    public Location determineLocation(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
        ////////////////////////////// YOUR CODE HERE ////////////////////////////////////////

        double r1 = Math.pow(10, (tower1Message.getTxPower()-tower1Message.getRssi())/(10*tower1Message.getN()));
        double r2 = Math.pow(10, (tower2Message.getTxPower()-tower2Message.getRssi())/(10*tower2Message.getN()));
        double r3 = Math.pow(10, (tower3Message.getTxPower()-tower3Message.getRssi())/(10*tower3Message.getN()));

        double p = tower2Message.getTowerLocation().getX();
        double x = (r1*r1-r2*r2+p*p)/(2*p);
        double y = Math.sqrt((r1*r1)-(x*x));

        double q = tower3Message.getTowerLocation().getX();
        double t = tower3Message.getTowerLocation().getY();


        double r3_calc =Math.sqrt((x-q)*(x-q) + (y-t)*(y-t));
        double err = Math.sqrt((r3-r3_calc)*(r3-r3_calc))/r3;


        weightParticles(tower1Message,tower2Message,tower3Message);
        resampleParticles();

        if(err < 0.1){
            System.out.println("X is : "+x+" and Y is "+y+" err "+err);
            return new Location(x,y);
        }else{
            System.out.println("X is : "+x+" and Y is "+-y+" err "+err);
            return new Location(x,-y);
        }



        /////////////////////////////////////////////////////////////////////////////////////
    }

    public WorldMap getMap() {
        return map;
    }

    public Robot setMap(WorldMap map) {
        this.map = map;
        return this;
    }
}
