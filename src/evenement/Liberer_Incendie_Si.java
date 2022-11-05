package evenement;

import io.Direction;
import robot.*;
import plan.*;
import incendie.*;
import java.util.List;
import java.util.LinkedList;

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
        if(dateSimulation>=this.robot.getTempsFin()){
           this.robot.setTempsFin(dateSimulation);
           //if(listeIncendie.contains(incendie))
           incendie.setIsTaken(false);
           super.setIsExe(true);
        }
        else{
            setDate(super.getDate()+this.robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);
        }
      }
}
