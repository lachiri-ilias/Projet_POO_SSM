package evenement;
import io.*;
import robot.*;
import plan.*;
import manager.*;


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
        // if the robot is free to do the order in the date given 
        if(dateSimulation>=robot.getTempsFin()){ // getTempsFin is initialised to 0 and filled inside here
            Case case_voisin;
            if(carte.voisinExiste(robot.getPosition(),this.direction)){
                case_voisin = this.carte.getVoisin( robot.getPosition(),this.direction);
            }
            else case_voisin = robot.getPosition();
            //calculates the date of the event using the supposition giving in the requirements of the project
            this.dateDeplacerRobot = (this.carte.getTailleCases()/2)
                                    * ( 1/robot.getVitesseTerrain(robot.getPosition().getNature()) 
                                        + 1/this.robot.getVitesseTerrain(case_voisin.getNature()));

            // fills robot's attribut with the date this event ends
            if(robot.getFinDeplacement()==false){
                robot.setTempsFin(dateSimulation+( (int)(this.dateDeplacerRobot*10+1) ));
                setDate(robot.getTempsFin());
                super.setisExecuted(false);
                robot.setFinDeplacement(true);
            }
            else{
                robot.setTempsFin(dateSimulation);
                robot.deplacer(direction,carte);
                super.setisExecuted(true);
                robot.setFinDeplacement(false);
            }
            
        }
        // if the robot is not free for now it pushes the event to the date where the robot finishes its actual order
        else{
            setDate(robot.getTempsFin());
            super.setisExecuted(false);
        }
    }
}
