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
        /* TODO : Trouver comment gerer
        System.out.println("***Remplir eau  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.remplirReservoir(carte);
            robot.setTempsFin(dateSimulation + robot.getTempsFin() + robot.getTempsRemplissage());
            /* TODO (think about it )
                ajout evenement eteindre ajouter l'evenement en tete de liste event to be a priority
                c-a-d avoir acces a la liste event d'ici ??  ???
        }
        else{
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
        }
        System.out.println("***Remplir eau  sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
        */
        // POUR LA SUITE
        if(robot.remplirReservoir(carte)) {
          System.out.println("["+dateSimulation+"] Le robot "+robot+" a rempli son réservoir\n");
        }
      }

}
