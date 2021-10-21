package visualization;

import model.*;

/**
 * Created by sbk on 23.02.17.
 */
public class Simulator {
    private static final double NOISE_RATIO = 0.1;
    private static final long SIMULATION_RATE= 1000;

    private World w;
    private Visualizer v;
    private boolean stop = false;

    public Simulator(World w, Visualizer v) {
        this.w = w;
        this.v = v;
    }

    public void simulationStep(){
        Robot r = w.getRobot();
        Tower t1 = w.getT1();
        Tower t2 = w.getT2();
        Tower t3 = w.getT3();

        Telegram t1Beam  = t1.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        Telegram t2Beam = t2.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        Telegram t3Beam = t3.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);

        Location robotLocation = r.determineLocation(t1Beam,t2Beam,t3Beam);
        r.setCalculatedLocation(robotLocation);

        v.repaint();
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
