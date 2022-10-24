package evenement;
import io.Direction;
import robot.*;
import plan.*;
import incendie.*;
import java.util.List;
import java.util.LinkedList;

public class Eteindre_Incendie extends Evenement{
    private Robot robot;
    private Carte carte;
    private LinkedList<Incendie> listeIncendie;
    private long dateEteintFeux = 1;
    private int ind_incendie;

    public Eteindre_Incendie(Robot robot,LinkedList<Incendie> listeIncendie,long date,Carte carte, int indice_incendie){
        super(date);
        this.robot =robot;
        this.listeIncendie = listeIncendie;
        this.carte =carte;
        this.ind_incendie = indice_incendie;
    }

    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
        //System.out.println("***feux eteint  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            robot.setTempsFin(dateSimulation);
            /* TODO : trouver une methode pour enlever l'incedie de la liste (ici j'enleve juste le 1ere)*/
            // this.listeIncendie.remove(0);
            if(this.robot.getCapActuelle()>=this.listeIncendie.get(this.ind_incendie).getLitresEau()){
              this.listeIncendie.remove(this.ind_incendie);
            }
            else{
              this.listeIncendie.get(this.ind_incendie).setLitresEau(this.listeIncendie.get(this.ind_incendie).getLitresEau - this.robot.getCapActuelle);
            }
            if(dateSimulation==robot.getTempsFin() ){
                robot.setTempsFin(dateSimulation+dateEteintFeux);
            }

        }
        else{
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
        }
        //System.out.println("***feux eteint sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");

        /* POUR LA SUITE
        if(this.robot.getCapActuelle()>=this.listeIncendie.get(this.ind_incendie).getLitresEau()){
          System.out.println("["+dateSimulation+"] Incendie "+this.ind_incendie+ " eteint !\n");
          this.listeIncendie.remove(this.ind_incendie);
        }
        else{
          System.out.println("["+dateSimulation+"] Incendie "+this.ind_incendie+ " en cours d'extinction !\n");
          this.listeIncendie.get(this.ind_incendie).setLitresEau(this.listeIncendie.get(this.ind_incendie).getLitresEau - this.robot.getCapActuelle);
        }
        */

    }


}
