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
    private long dateEteintFeux = 1;

    public Feux_Eteint(Robot robot,LinkedList<Incendie> listeIncendie,long date,Carte carte){ 
        super(date);
        this.robot =robot;
        this.listeIncendie = listeIncendie;
        this.carte =carte;
    }
        
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
        //System.out.println("***feux eteint  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.setTempsFin(dateSimulation);
            /* TODO : trouver une methode pour enlever l'incedie de la liste (ici j'enleve juste le 1ere)*/
            this.listeIncendie.remove(0);
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
