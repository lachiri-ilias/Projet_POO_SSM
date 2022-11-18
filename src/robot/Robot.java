package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public abstract class Robot {
    protected Case position;
    protected double vitesse;
    protected double vitesse_max;
    protected int cap_max; 
    protected int cap_actuelle;
    protected long tps_remplissage;
    protected int tps_deversage;
    protected int qte_deversage;
    protected long tps_fin_act=0;
    protected int tmp = 100;
    protected boolean islibre;
    protected boolean findeplacement;


    public Robot(Case position, double vitesse, double vitesse_max, int cap_max, int cap_actuelle, int tps_remplissage, int tps_deversage, int qte_deversage){
        this.position = position;
        this.vitesse = vitesse;
        this.vitesse_max = vitesse_max;
        this.cap_max = cap_max;
        this.cap_actuelle = cap_actuelle;
        this.tps_remplissage = tps_remplissage;
        this.tps_deversage = tps_deversage;
        this.qte_deversage = qte_deversage;
        this.islibre = true;
        this.findeplacement = false;
    }

    public Robot(Robot robot){
      this.position = robot.position;
      this.vitesse = robot.vitesse;
      this.cap_max = robot.cap_max;
      this.cap_actuelle = robot.cap_actuelle;
      this.tps_remplissage = robot.tps_remplissage;
      this.tps_deversage = robot.tps_deversage;
      this.qte_deversage = robot.qte_deversage;
      this.islibre = true;
      this.findeplacement = false;
    }


    public void setFinDeplacement(boolean status){
        this.findeplacement = status;
    }
     public boolean getFinDeplacement(){
        return this.findeplacement;
    }

    public void setIsLibre(boolean status){
         this.islibre = status;
    }
    public boolean getIsLibre(){
        return this.islibre;
    }
    public int getTempsDeversage(){
        return this.tps_deversage;
    }
    public int getQteDeverssage(){
          return this.qte_deversage;
    }

    public Case getPosition(){
        return this.position;
    }

    public double getVitesseMax(){
      return this.vitesse_max;
    }

    public int getCapActuelle(){
        return this.cap_actuelle;
    }
    public void setCapActuelle(int cap_actuelle){
       this.cap_actuelle=cap_actuelle;
    }

    public int getCapMax(){
        return this.cap_max;
    }

    public long getTempsFin(){
        return this.tps_fin_act;
    }
    public void setTempsFin(long tps_fin_act){
        this.tps_fin_act = tps_fin_act;
    }

    protected void setPosition(Case c){
        this.position = c;
    }

    public long getTempsRemplissage(){
        return this.tps_remplissage;
    }

    /**
    Verifies if the box exists and if the robot can actually move to the box
     */
    public boolean verif_depl(Direction d , Carte carte){
        if(carte.voisinExiste(this.position, d))
             if(this.verifCase(carte.getVoisin(this.position, d)))
                        return true;
        return false;
    }
    /**
    Moves the robot in the specified direction
     */
    public void deplacer(Direction d, Carte carte){
        if(carte.voisinExiste(this.position, d)){
            Case voisin = carte.getVoisin(this.position, d);
            if(this.verifCase(voisin)){
                switch(d){
                    case NORD:
                       carte.addListToDrawTwo(getPosition(),voisin);
                       getPosition().setLigne(getPosition().getLigne()-1);
                       break;

                    case SUD:
                        carte.addListToDrawTwo(getPosition(),voisin);
                        getPosition().setLigne(getPosition().getLigne()+1);
                        break;

                    case EST:
                        carte.addListToDrawTwo(getPosition(),voisin);
                        getPosition().setColonne(getPosition().getColonne()+1);
                        break;

                    case OUEST:
                        carte.addListToDrawTwo(getPosition(),voisin);
                        getPosition().setColonne(getPosition().getColonne()-1);
                        break;
                }
            }

        }
    }

    /**
    returns the speed of the robot depending on the type of field
    */
    public abstract double getVitesseTerrain(NatureTerrain nat);

    /**
    Substracts the giving volume as an argument from the capacity of robot
    */
    public abstract void deverserEau(int vol);

    /**
    fills the tank of the robot from a water field
     */
    public abstract boolean remplirReservoir(Carte carte);
    /**
    verifies if the box exists
     */
    public abstract boolean verifCase(Case voisin);

    public abstract String getRobotType();
    public abstract void setVitesse(double v);
}
