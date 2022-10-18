package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public abstract class Robot {
    protected Case position;
    protected double vitesse;
    protected int cap_max; // CHANGER
    protected int cap_actuelle;
    protected int tps_remplissage;
    protected int tps_deversage;
    protected int qte_deversage;

    public Robot(Case c, double v, int cm, int ca, int tr, int td, int qd){
        this.position = c;
        this.vitesse = v;
        this.cap_max = cm;
        this.cap_actuelle = ca;
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
    public abstract void deverserEau(int vol);
    public abstract void remplirReservoir(Carte carte);
    public abstract boolean verif_depl(Direction d, Case voisin);
    public abstract String getType();
}




