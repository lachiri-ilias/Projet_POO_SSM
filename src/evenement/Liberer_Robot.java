package evenement;

import robot.*;


public class Liberer_Robot extends Evenement{
    private Robot robot;


    public Liberer_Robot(Robot robot,long date){
        super(date);
        this.robot =robot;

    }

    public void execute(long dateSimulation){
        // if the robot is free to do the order in the date given 
        if(dateSimulation>=this.robot.getTempsFin()){
           this.robot.setTempsFin(dateSimulation);
           robot.setIsLibre(true);
           super.setisExecuted(true);
        }
        // if the robot is not free for now it pushes the event to the date where the robot finishes its actual order
        else{
            setDate(this.robot.getTempsFin());
            super.setisExecuted(false);

        }
      }

}
