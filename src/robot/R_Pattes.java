package robot;

import io.NatureTerrain;
import plan.*;


public  class R_Pattes extends Robot {

    public R_Pattes(Case c){
        super(c, 30,  Integer.MAX_VALUE,  Integer.MAX_VALUE, 0, 0, 1, 10);
    }

    public double getVitesseTerrain(NatureTerrain nat){
      if(nat==NatureTerrain.ROCHE) return 10;
        return this.vitesse;
    }

    public void setVitesse(double v){
        this.vitesse = v;
    }
    /**
    We force the use of the argument because we already defined the function in the class Robot
     */
    public void deverserEau(int vol){
        this.cap_actuelle = this.cap_max -vol + vol;
    }

    public boolean verifCase(Case voisin){
        return !(voisin.getNature()==NatureTerrain.EAU);
    }

    public boolean remplirReservoir(Carte carte) {
        this.cap_actuelle = this.cap_max;
        return true;
    }

    public String getRobotType(){
        return "R_Pattes";
    }
}
