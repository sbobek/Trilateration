package visualization;

import model.Location;
import model.Robot;
import model.Tower;
import model.World;

/**
 * Created by sbk on 23.02.17.
 */
public class Simulator {
    private static final double NOISE_RATIO = 0.0;
    private static final long SIMULATION_RATE= 500;

    private World w;
    private boolean stop = false;

    public Simulator(World w) {
        this.w = w;
    }

    public void simulationStep(){
        Robot r = w.getRobot();
        Tower t1 = w.getT1();
        Tower t2 = w.getT2();
        Tower t3 = w.getT3();
        Location robotLocation = r.determineLocation(
                t1.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO),
                t2.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO),
                t3.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO));

        r.setCalculatedLocation(robotLocation);
    }

    public synchronized Simulator setStop(boolean stop) {
        this.stop = stop;
        return this;
    }

    public synchronized boolean isStop() {
        return stop;
    }

    public void run(){
        while(!isStop()){
            simulationStep();
            try {
                Thread.sleep(SIMULATION_RATE);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
