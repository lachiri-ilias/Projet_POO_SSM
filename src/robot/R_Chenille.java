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

    public void setVitesse(double v){
        if(v<=getVitesseMax()){
            this.vitesse = v;
        }
        else  throw new IllegalArgumentException("Vitesse R_Chenille < 80 km/h !");
    }

    public void deverserEau(int vol){
        if(this.cap_actuelle >= vol) this.cap_actuelle -= vol;
        else throw new IllegalArgumentException("R_Chenille ne peut pas deverser plus d'eau qu'il en contient !");
    }

    public boolean remplirReservoir(Carte carte){
        if(this.cap_actuelle < this.cap_max){
            this.cap_actuelle += 400;
        } 
        else{
            this.cap_actuelle = this.cap_max;
            return true;
        }  
        return false;
    }

    public boolean verifCase(Case voisin){
        return !(voisin.getNature() == NatureTerrain.EAU || voisin.getNature() == NatureTerrain.ROCHE);
    }
    public String getRobotType(){
        return "R_Chenille";
    }
}
