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
    private Incendie incendie;
    private long dateEteintFeux;

    public Eteindre_Incendie(Robot robot,LinkedList<Incendie> listeIncendie,Incendie incendie,long date,Carte carte){ 
        super(date);
        this.robot =robot;
        this.listeIncendie = listeIncendie;
        this.incendie = incendie;
        this.carte =carte;
    }
        
    public void execute(long dateSimulation){
        /* TODO : Trouver comment gerer  */
            System.out.print("La date est  "+(dateSimulation) );

        //System.out.println("***feux eteint  entre****["+dateSimulation+" ] temps fin : "+ robot.getTempsFin()+"\n");
        if(dateSimulation>=robot.getTempsFin()){
            dateEteintFeux =  this.robot.getTempsDeversage();
            this.robot.setTempsFin(dateSimulation+dateEteintFeux);
            if(this.robot.getCapActuelle() >= this.incendie.getLitresEau()){
                    this.robot.setCapActuelle( this.robot.getCapActuelle()-this.robot.getQteDeverssage());
                    this.incendie.setLitresEau(this.incendie.getLitresEau()-this.robot.getQteDeverssage());
                    if(this.incendie.getLitresEau() <= 0){
                        this.listeIncendie.remove(this.incendie);
                        super.setIsExe(true);
                    }
                    else{
                        setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
                        super.setIsExe(false);     
                    }
            }
            else{ 
                this.incendie.setLitresEau(this.incendie.getLitresEau()-this.robot.getCapActuelle());
                this.robot.setCapActuelle(0);
                setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
                super.setIsExe(false);
            }
        }
        else{
            System.out.print("LE temps a ajouter est : "+(super.getDate()+robot.getTempsFin()-dateSimulation) );
            setDate(super.getDate()+robot.getTempsFin()-dateSimulation);
            super.setIsExe(false);

        }
        //System.out.println("***feux eteint sortie****["+dateSimulation+" ] temps fin :  "+ robot.getTempsFin()+"\n");
    }
}
