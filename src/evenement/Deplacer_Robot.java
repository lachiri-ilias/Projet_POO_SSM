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
        /* TODO : Trouver comment gerer  */      
       // System.out.println("**Deplacer le robot entree*****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.setTempsFin(dateSimulation+dateDeplacerRobot);
            robot.deplacer(direction,carte);
            super.setIsExe(true);
            // if(dateSimulation==robot.getTempsFin() ){
            //     robot.setTempsFin(dateSimulation+dateDeplacerRobot);
            // }
        }
        else{
            // System.out.println("heeerree SSUUUIIIIIII");
           // System.out.print("\n *********** LE temps a ajouter est : "+(super.getDate()+robot.getTempsFin()-dateSimulation)+"  **** \n"  );
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);

            // robot.setTempsFin(robot.getTempsFin()+dateDeplacerRobot);
        }
       // System.out.println("**Deplacer le robot sortie *****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");

        /* POUR LA SUITE
        System.out.println("["+dateSimulation+"] Le Robot "+robot+ " se déplace !\n");
        robot.deplacer(direction,carte);
        */

    }
}
