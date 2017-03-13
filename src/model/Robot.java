package model;

import java.util.Random;

/**
 * Created by sbk on 23.02.17.
 */
public class Robot {

    private Location realLocation;
    private Location calculatedLocation;
    private WorldMap map;
    private static final double MIN_RESAMPLING_NOISE =-10;
    private static final double MAX_RESAMPLING_NOISE = 10;

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

    public synchronized Location getCalculatedLocation() {
        return calculatedLocation;
    }

    public synchronized Robot setCalculatedLocation(Location calculatedLocation) {
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

        //Nasty fix for the numerical problems
        cumsum[idx-1] = 1.0;
        Random r = new Random();
        Particle [] newParticles = new Particle[map.getParticles().length];

        for(int i = 0; i < map.getParticles().length;i++){
            double val  = r.nextDouble();
            for(int j =0; j < cumsum.length;j++){
                if(val <= cumsum[j]){
                    newParticles[i]  = new Particle(map.getParticles()[j]);
                    Location newLocation = new Location(newParticles[i].location);
                    double resamplingNoiseX = (MIN_RESAMPLING_NOISE + (MAX_RESAMPLING_NOISE-MIN_RESAMPLING_NOISE)*r.nextDouble());
                    double resamplingNoiseY = (MIN_RESAMPLING_NOISE + (MAX_RESAMPLING_NOISE-MIN_RESAMPLING_NOISE)*r.nextDouble());
                    newLocation.setX(newLocation.getX()+resamplingNoiseX);
                    newLocation.setY(newLocation.getY()+resamplingNoiseY);
                    newParticles[i].setLocation(newLocation);
                    break;
                }
            }
        }
        getMap().setParticles(newParticles);

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

            double weight =gaussian(d1,r1,10)*gaussian(d2,r2,10)*gaussian(d3,r3,10);
            if(Double.isNaN(weight)){
                    weight = Double.MIN_VALUE;
            }
            p.setWeight(weight);
        }

        normalizeWeights();


    }

    public void normalizeWeights(){
        double sum = 0;
        for(Particle p: map.getParticles()){
            sum += p.getWeight();
        }

        for(Particle p: map.getParticles()){
            double normWeight = p.getWeight()/sum;
            if(Double.isNaN(normWeight)){
                normWeight = Double.MIN_NORMAL;
            }
            p.setWeight(normWeight);
        }
    }

    public void resetParticles(){
        map.resetParticles();
    }

    private double gaussian(double x, double mu, double sdv){
        return 1/(sdv*Math.sqrt(2*Math.PI))*Math.exp(-(x-mu)*(x-mu)/(2*sdv*sdv));
    }



    public synchronized Location determineLocation(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
        ////////////////////////////// YOUR CODE HERE ////////////////////////////////////////

        double r1 = Math.pow(10, (tower1Message.getTxPower()-tower1Message.getRssi())/(10*tower1Message.getN()));
        double r2 = Math.pow(10, (tower2Message.getTxPower()-tower2Message.getRssi())/(10*tower2Message.getN()));
        double r3 = Math.pow(10, (tower3Message.getTxPower()-tower3Message.getRssi())/(10*tower3Message.getN()));

        double p = tower2Message.getTowerLocation().getX();
        double x = (r1*r1-r2*r2+p*p)/(2*p);
        double y = Math.sqrt((r1*r1)-(x*x));

        double q = tower3Message.getTowerLocation().getX();
        double t = tower3Message.getTowerLocation().getY();


        return new Location(0,0);


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
