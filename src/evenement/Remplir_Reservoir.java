package evenement;
import io.Direction;
import robot.*;
import plan.*;


public class Remplir_Reservoir extends Evenement{
    private Robot robot;
    private Carte carte;

    public Remplir_Reservoir(Robot robot,long date,Carte carte){
        super(date);
        this.robot =robot;
        this.carte =carte;
    }

    public void execute(long dateSimulation){
        // if the robot is free to do the order in the date given 
        if(dateSimulation>=this.robot.getTempsFin()){
           // we are showen the evolution of the capacity of every robot each date
           this.robot.setTempsFin(dateSimulation+1);
           if(this.robot.remplirReservoir(carte)){
                 super.setisExecuted(true);
           }
           else{
                setDate(this.robot.getTempsFin());
                super.setisExecuted(false);
           }
        }
        // if the robot is not free for now it pushes the event to the date where the robot finishes its actual order
        else{
            setDate(this.robot.getTempsFin());
            super.setisExecuted(false);

        }
      }

}
