package evenement;
import io.Direction;
import robot.*;
import plan.*;
import incendie.*;
import java.util.List;
import java.util.LinkedList;

public class Feux_Eteint extends Evenement{
    private Robot robot;
    private Carte carte;
    private LinkedList<Incendie> listeIncendie;
    private Incendie incendie;
    private long dateEteintFeux = 1;

    public Feux_Eteint(Robot robot,LinkedList<Incendie> listeIncendie,Incendie incendie,long date,Carte carte){ 
        super(date);
        this.robot =robot;
        this.listeIncendie = listeIncendie;
        this.incendie = incendie;
        this.carte =carte;
    }
        
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
        //System.out.println("***feux eteint  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.setTempsFin(dateSimulation);
            this.listeIncendie.remove(this.incendie);
            if(dateSimulation==robot.getTempsFin() ){
                robot.setTempsFin(dateSimulation+dateEteintFeux);
            }

        }
        else{
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
        }
        //System.out.println("***feux eteint sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
    }
}
