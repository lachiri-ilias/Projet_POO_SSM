package evenement;
import io.*;
import robot.*;
import plan.*;
import manager.*;
//import java.lang.Math.*;


public class Deplacer_Robot extends Evenement{
    private Direction direction;
    private Robot robot;
    private Carte carte;
    private double dateDeplacerRobot;

    public Deplacer_Robot(Direction direction,Robot robot,long date,Carte carte){
        super(date);
        this.direction = direction;
        this.robot =robot;
        this.carte =carte;
    }

    public void execute(long dateSimulation){     
       // System.out.println("**Deplacer le robot entree*****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            Case case_voisin;
            if(carte.voisinExiste(robot.getPosition(),this.direction)){
                case_voisin = this.carte.getVoisin( robot.getPosition(),this.direction);
            }
            else case_voisin = robot.getPosition();

            this.dateDeplacerRobot = (this.carte.getTailleCases()/2)* ( 1/robot.getVitesseTerrain(robot.getPosition().getNature()) + 1/this.robot.getVitesseTerrain(case_voisin.getNature()));
            //System.out.println("*** "+robot.getFindDeplacement());
            if(robot.getFinDeplacement()==false){
                System.out.println("*LA vitesse est : "+ (int)(this.dateDeplacerRobot*10+1)  +"\n");
                //robot.setTempsFin(dateSimulation+(int)(this.dateDeplacerRobot*10)+1);
                robot.setTempsFin(dateSimulation+( (int)(this.dateDeplacerRobot*10+1) ));
                setDate(robot.getTempsFin());
                super.setIsExe(false);
                robot.setFinDeplacement(true);
            }
            else{
                robot.setTempsFin(dateSimulation);
                robot.deplacer(direction,carte);
                super.setIsExe(true);
                robot.setFinDeplacement(false);
            }
            
        }
        else{
            setDate(robot.getTempsFin());
            super.setIsExe(false);
        }
       // System.out.println("**Deplacer le robot sortie *****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
    }
}
