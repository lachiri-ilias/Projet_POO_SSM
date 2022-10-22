package evenement;
import io.Direction;
import robot.*;
import plan.*;


public class Remplire_Eau extends Evenement{
    private Robot robot;
    private Carte carte;

    public Remplire_Eau(Robot robot,long date,Carte carte){ 
        super(date);
        this.robot =robot;
        this.carte =carte;
    }
        
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
        System.out.println("***Remplire eau  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.remplirReservoir(carte);
            robot.setTempsFin(dateSimulation + robot.getTempsFin() + robot.getTempsRemplissage());
            /* TODO (think about it ) 
                ajout evenement eteindre ajouter l'evenement en tete de liste event to be a priority 
                c-a-d avoir acces a la liste event d'ici ??  ??? */ 
        }
        else{
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
        }
        System.out.println("***Remplire eau  sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
    }
}
