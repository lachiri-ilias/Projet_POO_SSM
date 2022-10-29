package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;


public  class R_Pattes extends Robot {

    public R_Pattes(Case c){
        super(c, 30, (int)Double.POSITIVE_INFINITY, (int)Double.POSITIVE_INFINITY, 0, 0, 1, 10);
    }

    public double getVitesseTerrain(NatureTerrain nat){
      if(nat==NatureTerrain.ROCHE) return 10;
        return this.vitesse;
    }

    public void setVitesse(double v){
        // if(this.position.getNature()==NatureTerrain.ROCHE) this.vitesse = 10;
        // else this.vitesse = v;
        this.vitesse = v;
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        // Deverse t on quand même ce qui est disponible ?
        else throw new IllegalArgumentException("R_Pattes ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public boolean verifCase(Case voisin){
        return !(voisin.getNature()==NatureTerrain.EAU);
    }

    public boolean remplirReservoir(Carte carte) {return true;};

    public String getType(){
        return "R_Pattes";
    }
}
