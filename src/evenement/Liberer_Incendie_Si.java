package evenement;

import io.Direction;
import robot.*;
import plan.*;
import incendie.*;
import java.util.List;
import java.util.LinkedList;
/**
In case a robot is working putting off a fire we considered it impossible for another robot to join him so we're adding if a fire is taken or not
 */
public class Liberer_Incendie_Si extends Evenement{
    private Robot robot;
    private Incendie incendie;
    private LinkedList<Incendie> listeIncendie;

    public Liberer_Incendie_Si(Robot robot,Incendie incendie,LinkedList<Incendie> listeIncendie,long date){
        super(date);
        this.robot =robot;
        this.incendie = incendie;
        this.listeIncendie = new LinkedList<Incendie>();
    }

    public void execute(long dateSimulation){
        // if the robot is free to do the order in the date given 
        if(dateSimulation>=this.robot.getTempsFin()){
           this.robot.setTempsFin(dateSimulation);
           incendie.setIsTaken(false);
           super.setisExecuted(true);
        }
        // if the robot is not free for now it pushes the event to the date where the robot finishes its actual order
        else{
            setDate(super.getDate()+this.robot.getTempsFin()-dateSimulation);
            super.setisExecuted(false);
        }
      }
}
