package model;

/**
 * Created by sbk on 12.03.17.
 */
public class WorldMap {
    private static final double PARTICLE_COVERAGE = 0.8;
    private int maxParticles;
    private int width;
    private  int height;
    private Particle [] particles;

    public WorldMap(World w){
        width = w.getWidth();
        height = w.getHeight();
        maxParticles = width*height;

        resetParticles();
    }

    public void resetParticles(){
        particles = new Particle[(int)(maxParticles*PARTICLE_COVERAGE)];
        for(Particle p: particles){
            p.setWeight(1/particles.length);
        }
    }

    public Particle[] getParticles() {
        return particles;
    }
}
