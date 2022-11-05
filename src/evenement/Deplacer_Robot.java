package evenement;
import io.Direction;
import robot.*;
import plan.*;
import manager.*;


public class Deplacer_Robot extends Evenement{
    private Direction direction;
    private Robot robot;
    private Carte carte;
    private long dateDeplacerRobot = 5;

    public Deplacer_Robot(Direction direction,Robot robot,long date,Carte carte){
        super(date);
        this.direction = direction;
        this.robot =robot;
        this.carte =carte;
    }

    public void execute(long dateSimulation){     
       // System.out.println("**Deplacer le robot entree*****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            // robot.setIsLibre(false);
            robot.setTempsFin(dateSimulation+dateDeplacerRobot);
            robot.deplacer(direction,carte);
            super.setIsExe(true);
        }
        else{
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);
        }
       // System.out.println("**Deplacer le robot sortie *****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
    }
}
