package evenement;
import io.Direction;
import robot.*;
import plan.*;


public class Deplacer_Robot extends Evenement{
    private Direction direction;
    private Robot robot;
    private Carte carte;
    private long dateDeplacerRobot = 1;

    public Deplacer_Robot(Direction direction,Robot robot,long date,Carte carte){ 
        super(date);
        this.direction = direction;
        this.robot =robot;
        this.carte =carte;
    }
        
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
        //System.out.println("***1*****["+dateSimulation+" ] laaa "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.deplacer(direction,carte);
            // if(dateSimulation==robot.getTempsFin() ){
            //     robot.setTempsFin(dateSimulation+dateDeplacerRobot);
            // }
        }
        else{
            System.out.println("heeerree");
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
            // robot.setTempsFin(robot.getTempsFin()+dateDeplacerRobot);
        }
        //System.out.println("***2*****["+dateSimulation+" ] laaa "+ robot.getTempsFin()+"\n");
    }
}
