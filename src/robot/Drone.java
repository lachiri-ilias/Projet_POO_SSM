package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public class Drone extends Robot {

    public Drone(Case c){
       // super(c, 100, 10000, 0, 30*60, 30, 10000);
         super(c, 100, 10000, 0, 3, 30, 10000);  // modifier le temps pour le test !!!
    }

    public void setPosition(Case c){
        this.position = c;
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=150) this.vitesse = v;
        else throw new IllegalArgumentException("Vitesse Drone < 150 km/h !");
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        // Deverse t on quand même ce qui est disponible ?
        else throw new IllegalArgumentException("Drone ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public void remplirReservoir(Carte carte){
      if(this.getPosition().getNature() == NatureTerrain.EAU){
        long t=0;
        while(t<this.tps_remplissage) t++; // Traduire dans le temps reel
        this.cap_actuelle = this.cap_max;
      }
    }

    public boolean verif_depl(Direction d, Case voisin){
        return true;
    }
    public String getType(){
        return "Drone";
    }

}