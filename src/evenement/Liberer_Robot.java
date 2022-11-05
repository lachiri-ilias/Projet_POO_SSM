package evenement;

import io.Direction;
import robot.*;
import plan.*;


public class Liberer_Robot extends Evenement{
    private Robot robot;


    public Liberer_Robot(Robot robot,long date){
        super(date);
        this.robot =robot;

    }

    public void execute(long dateSimulation){
        if(dateSimulation>=this.robot.getTempsFin()){
           this.robot.setTempsFin(dateSimulation);
           robot.setIsLibre(true);
           super.setIsExe(true);
        }
        else{
            setDate(super.getDate()+this.robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);

        }
      }

}
