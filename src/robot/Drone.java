package robot;

import io.NatureTerrain;
import plan.*;

public class Drone extends Robot {

    public Drone(Case c){
         super(c, 100, 150, 10000, 0, 30*60, 30, 10000); 
    }

    public void setPosition(Case c){
        this.position = c;
    }

    public double getVitesseTerrain(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=getVitesseMax()) this.vitesse = v;
        else throw new IllegalArgumentException("Vitesse Drone < 150 km/h !");
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        else throw new IllegalArgumentException("Drone ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public boolean remplirReservoir(Carte carte){
        if(this.cap_actuelle < this.cap_max){
            this.cap_actuelle += (int)(super.cap_max / super.tps_remplissage);
        } 
        else{
            this.cap_actuelle = this.cap_max;
            return true;
        }  
        return false;
    }
    

    public boolean verifCase(Case voisin){
        return true;
    }
    public String getRobotType(){
        return "Drone";
    }

}
