package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public class R_Roue extends Robot {

    public R_Roue(Case c){
        super(c, 80, 5000, 0, 10*60, 5, 100);
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
         this.vitesse = v;
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        // Deverse t on quand même ce qui est disponible ?
        else throw new IllegalArgumentException("R_Roue ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public void remplirReservoir(Carte carte){
      if(carte.existeTypeVoisin(this.getPosition(), NatureTerrain.EAU)){
        int t=0;
        while(t<this.tps_remplissage) t++; // Traduire dans le temps reel
        this.cap_actuelle = this.cap_max;
      }
    }

    public boolean verif_depl(Direction d, Case voisin){
        return (voisin.getNature()==NatureTerrain.TERRAIN_LIBRE
        || voisin.getNature()==NatureTerrain.HABITAT);
    }
    public String getType(){
        return "R_Roue";
    }
}
