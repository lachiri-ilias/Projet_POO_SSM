package evenement;
import io.Direction;
import robot.*;
import plan.*;


public class Remplir_Reservoir extends Evenement{
    private Robot robot;
    private Carte carte;
    //private long tempsRemplissage ; pas besoin car chaque rebot a sont temps 

    public Remplir_Reservoir(Robot robot,long date,Carte carte){
        super(date);
        this.robot =robot;
        this.carte =carte;
    }

/* TODO regler le probleme de remplire du robot verif Carte ... avoir */
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer */
      //  System.out.println("***Remplir eau  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=this.robot.getTempsFin()){
          // tempsRemplissage = this.robot.getTempsRemplissage()/;
           this.robot.setTempsFin(dateSimulation+1);
           if(this.robot.remplirReservoir(carte)){
                 super.setIsExe(true);
           }
           else{
                setDate(super.getDate()+this.robot.getTempsFin()-dateSimulation);
                super.setIsExe(false);
           }
        }
        else{
            setDate(super.getDate()+this.robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);

        }
        //System.out.println("***Remplir eau  sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
        
        // POUR LA SUITE
        // if(robot.remplirReservoir(carte)) {
        //   System.out.println("["+dateSimulation+"] Le robot "+robot+" a rempli son réservoir\n");
        // }
      }

}
