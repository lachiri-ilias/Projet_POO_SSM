package evenement;

import io.Direction;
import robot.*;
import plan.*;
import incendie.*;
import java.util.List;
import java.util.LinkedList;

public class Eteindre_Incendie extends Evenement{
    private Robot robot;
    private Carte carte;
    private LinkedList<Incendie> listeIncendie;
    private Incendie incendie;
    private long dateEteintFeux;

    public Eteindre_Incendie(Robot robot,LinkedList<Incendie> listeIncendie,Incendie incendie,long date,Carte carte){
        super(date);
        this.robot =robot;
        this.listeIncendie = listeIncendie;
        this.incendie = incendie;
        this.carte =carte;
    }

    public void execute(long dateSimulation){
        // if the robot is free to do the order in the date given 
        if(dateSimulation>=robot.getTempsFin()){
            dateEteintFeux =  this.robot.getTempsDeversage();
            this.robot.setTempsFin(dateSimulation+dateEteintFeux);
            if(this.robot.getCapActuelle() >= this.incendie.getLitresEau()){
                    if(this.robot.getRobotType()!="R_Pattes")
                        this.robot.setCapActuelle( this.robot.getCapActuelle()-this.robot.getQteDeverssage());
                    this.incendie.setLitresEau(this.incendie.getLitresEau()-this.robot.getQteDeverssage());
                    if(this.incendie.getLitresEau() <= 0){
                        this.listeIncendie.remove(this.incendie);
                        // we have to draw to show that there is no longer fire
                        this.carte.getListToDraw().add(this.incendie.getCase()); 
                        super.setisExecuted(true);
                    }
                    else{
                        setDate(robot.getTempsFin());
                        super.setisExecuted(false);
                    }
            }
            else{
                this.incendie.setLitresEau(this.incendie.getLitresEau()-this.robot.getQteDeverssage());
                if(this.robot.getRobotType()!="R_Pattes")
                    this.robot.setCapActuelle( this.robot.getCapActuelle()-this.robot.getQteDeverssage());
                setDate(robot.getTempsFin());
                super.setisExecuted(true);
            }
        }
        // if the robot is not free for now it pushes the event to the date where the robot finishes its actual order
        else{
            setDate(robot.getTempsFin());
            super.setisExecuted(false);

        }
    }
}
