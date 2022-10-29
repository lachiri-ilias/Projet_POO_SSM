package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;



public class R_Chenille extends Robot {

    public R_Chenille(Case c){
        super(c, 60, 60, 2000, 0, 5*60, 8, 100);
    }

    public double getVitesseTerrain(NatureTerrain nat){
      if(nat==NatureTerrain.FORET) return this.vitesse/2;
      return this.vitesse;
    }

    // UTILISER getVitesseTerrain avant setVitesse
    public void setVitesse(double v){
        if(v<=getVitesseMax()){
            // if(this.position.getNature()==NatureTerrain.FORET) this.vitesse = v/2;
            // else this.vitesse = v;
            this.vitesse = v;
        }
        else  throw new IllegalArgumentException("Vitesse R_Chenille < 80 km/h !");
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        // Deverse t on quand même ce qui est disponible ?
        else throw new IllegalArgumentException("R_Chenille ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public boolean remplirReservoir(Carte carte){
      if(carte.existeTypeVoisin(this.getPosition(), NatureTerrain.EAU)){
        int t=0;
        while(t<this.tps_remplissage) t++; // Traduire dans le temps reel
        this.cap_actuelle = this.cap_max;
        return true;
      }
      return false;
    }

    public boolean verifCase(Case voisin){
        return !(voisin.getNature() == NatureTerrain.EAU
        || voisin.getNature() == NatureTerrain.ROCHE);
    }
    public String getType(){
        return "R_Chenille";
    }
}
