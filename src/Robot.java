
// import io.Direction;
// import io.NatureTerrain;
enum NatureTerrain{
    TERRAIN_LIBRE, EAU, HABITAT, FORET, ROCHE;
}
enum Direction{
    NORD, SUD, EST, OUEST;
}
public abstract class Robot {
    protected Case position;
    protected double vitesse;
    private int cap_max; // CHANGER
    protected int cap_reservoir;
    private int tps_remplissage;
    private int tps_deversage;
    private int qte_deversage;

    public Robot(Case c, double v, int cm, int cr, int tr, int td, int qd){
        this.position = c;
        this.vitesse = v;
        this.cap_max = cm;
        this.cap_reservoir = cr;
        this.tps_remplissage = tr;
        this.tps_deversage = td;
        this.qte_deversage = qd;
    }

    public Case getPosition(){
        return this.position;
    }

    protected void setPosition(Case c){
        this.position = c;
    }

    public void deplacer(Direction d, Carte carte){
        if(carte.voisinExiste(this.position, d)){
            Case voisin = carte.getVoisin(this.position, d);
            if(this.verif_depl(d, voisin)){
                switch(d){
                    case NORD:
                        this.position.setLigne(this.position.getLigne()+1);
                    
                    case SUD:
                        this.position.setLigne(this.position.getLigne()-1);

                    case EST:
                        this.position.setLigne(this.position.getColonne()+1);

                    case OUEST:
                        this.position.setLigne(this.position.getColonne()-1);
                }
            }
        }
    }

    public abstract double getVitesse(NatureTerrain nat);
    public abstract void setVitesse(double v);
  //  public abstract void deverserEau(int vol);
   // public abstract void remplirReservoir();
    public abstract boolean verif_depl(Direction d, Case voisin);
}

        
class Drone extends Robot {

    public Drone(Case c){
        super(c, 100, 10000, 0, 30*60, 30, 10000);
    }

    public void setPosition(Case c){
        this.position = c;
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=150) this.vitesse = v;
        else throw new IllegalArgumentExeption("Vitesse Drone < 150 km/h !");
    }

    public boolean verif_depl(Direction d, Case voisin){
        return true;
    }
    
}


class R_Roue extends Robot {

    public R_Roue(Case c){
        super(c, 80, 5000, 0, 10*60, 5, 100);
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
         this.vitesse = v;
    }

    public boolean verif_depl(Direction d, Case voisin){
        return (voisin.getNature()==TERRAIN_LIBRE || voisin.getNature()==HABITAT);
    }
}



class R_Chenille extends Robot {

    public R_Chenille(Case c){
        super(c, 60, 2000, 0, 5*60, 8, 100);
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=80){
            if(this.position.getNature()==FORET) this.vitesse = v/2;
            else this.vitesse = v;
        }
        else  throw new IllegalArgumentExeption("Vitesse R_Chenille < 80 km/h !"); 
    }

    public boolean verif_depl(Direction d, Case voisin){
        return !(voisin.getNature()==EAU || voisin.getNature()==ROCHE);
    }
}



class R_Pates extends Robot {

    public R_Pates(Case c){
        super(c, 30, (int)Double.POSITIVE_INFINITY, 0, 0, 1, 10);
    }

    public double getVitesse(NatureTerrain nat){
        return this.vitesse;
    }

    public void setVitesse(double v){
        if(v<=80){
            if(this.position.getNature()==ROCHE) this.vitesse = 10;
            else this.vitesse = v;
        }
        else  throw new IllegalArgumentExeption("Vitesse R_Chenille < 80 km/h !");
    }

    public boolean verif_depl(Direction d, Case voisin){
        return !(voisin.getNature()==EAU);
    }
}