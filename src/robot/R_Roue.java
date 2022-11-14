package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public class R_Roue extends Robot {

    public R_Roue(Case c){
        super(c, 80, Integer.MAX_VALUE, 5000, 0, 10*60, 5, 100);
    }


    public double getVitesseTerrain(NatureTerrain nat){
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

    public boolean remplirReservoir(Carte carte){
     // if(carte.existeTypeVoisin(this.getPosition(), NatureTerrain.EAU)){
        if(this.cap_actuelle < this.cap_max){
            this.cap_actuelle += 500;
        } 
        else{
            this.cap_actuelle = this.cap_max;
            return true;
        }  
        return false;
    }
    

    public boolean verifCase(Case voisin){
        return (voisin.getNature()==NatureTerrain.TERRAIN_LIBRE || voisin.getNature()==NatureTerrain.HABITAT);
    }
    public String getType(){
        return "R_Roue";
    }
}
