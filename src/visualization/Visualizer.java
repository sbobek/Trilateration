package visualization;

import exceptions.WorldBuilderException;
import model.Location;
import model.Tower;
import model.Robot;
import model.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by sbk on 23.02.17.
 */
public class Visualizer extends JFrame {

    Image robotIcon = null;
    Image robotGhostIcon = null;
    Image towerIcon = null;
    World w;


    public Visualizer(World w) throws HeadlessException, IOException {
        robotIcon = ImageIO.read(new File("robotIcon.png"));
        robotGhostIcon = ImageIO.read(new File("robotGhostIcon.png"));
        towerIcon = ImageIO.read(new File("towerIcon.png"));
        this.w = w;
    }

    public void paintRobot(Graphics graphics) {
        Location robotLocation = w.getRobot().getCalculatedLocation();
        graphics.drawImage(robotGhostIcon, (int) w.getRobot().getRealLocation().getX(), (int) w.getRobot().getRealLocation().getY(), this);
        graphics.drawImage(robotIcon, (int)robotLocation.getX(), (int)robotLocation.getY(), this);
    }

    public void paintTowers(Graphics graphics){
        graphics.drawImage(towerIcon, (int) w.getT1().getLocation().getX(),
                (int) w.getT1().getLocation().getY(), this);

        graphics.drawImage(towerIcon, (int) w.getT2().getLocation().getX(),
                (int) w.getT2().getLocation().getY(), this);

        graphics.drawImage(towerIcon, (int) w.getT3().getLocation().getX(),
                (int) w.getT3().getLocation().getY(), this);
    }

    @Override
    public void paint(Graphics graphics) {
        paintRobot(graphics);
        paintTowers(graphics);
    }

    public static void main(String [] argv){
        Visualizer dp = null;
        try {
            World world = new World.WorldBuilder()
                    .setHeight(600)
                    .setWidth(800)
                    .setRobot(new Robot(new Location(400,306)))
                    .setT1(new Tower(new Location(10,10),100))
                    .setT2(new Tower(new Location(750,10),100))
                    .setT3(new Tower(new Location(638,500),100))
                    .setX(0)
                    .setY(0).build();
            dp = new Visualizer(world);

            dp.setSize(world.getWidth(), world.getHeight());
            dp.setVisible(true);

            Simulator s = new Simulator(world);
            dp.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    System.exit(0);
                    s.setStop(true);
                }
            });

            s.run();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldBuilderException e) {
            e.printStackTrace();
        }

        ;

    }

}
