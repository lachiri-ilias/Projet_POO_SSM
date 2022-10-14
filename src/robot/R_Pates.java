package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;


public  class R_Pates extends Robot {

    public R_Pates(Case c){
        super(c, 30, (int)Double.POSITIVE_INFINITY, 0, 0, 1, 10);
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=80){
            if(this.position.getNature()==NatureTerrain.ROCHE) this.vitesse = 10;
            else this.vitesse = v;
        }
        else  throw new IllegalArgumentException("Vitesse R_Chenille < 80 km/h !");
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        // Deverse t on quand même ce qui est disponible ?
        else throw new IllegalArgumentException("R_Pates ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public boolean verif_depl(Direction d, Case voisin){
        return !(voisin.getNature()==NatureTerrain.EAU);
    }

    public void remplirReservoir(Carte carte){};
}