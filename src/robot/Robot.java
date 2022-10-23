package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public abstract class Robot {
    protected Case position;
    protected double vitesse;
    protected int cap_max; // CHANGER
    protected int cap_actuelle;
    protected long tps_remplissage;
    protected int tps_deversage;
    protected int qte_deversage;
    protected long tps_fin_act=0;

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

    public long getTempsFin(){
        return this.tps_fin_act;
    }
    public void setTempsFin(long tps_fin_act){
        this.tps_fin_act = tps_fin_act;
    }

    protected void setPosition(Case c){
        this.position = c;
    }

    public long getTempsRemplissage(){
        return this.tps_remplissage;
    }

    public void deplacer(Direction d, Carte carte){
        // System.out.println("[exe1] colonne : "+getPosition().getColonne()+"\tligne : "+getPosition().getLigne());
        // System.out.println("VOISIN EXISTE : "+carte.voisinExiste(this.position, d)+"\n");
        if(carte.voisinExiste(this.position, d)){
            Case voisin = carte.getVoisin(this.position, d);
            //System.out.println("VERIF DEPL : "+this.verif_depl(d, voisin)+"\n");
            if(this.verif_depl(d, voisin)){
                //System.out.println("on se deplace :"+d+"\n");
                // TODO : renvoyer une erreur lorsque la position est innateignable !
                switch(d){
                    case NORD:
                        System.out.println("NORD");
                       getPosition().setLigne(getPosition().getLigne()-1);
                       break;

                    case SUD:
                        System.out.println("SUD");
                        getPosition().setLigne(getPosition().getLigne()+1);
                        break;

                    case EST:
                        System.out.println("EST");
                        getPosition().setColonne(getPosition().getColonne()+1);
                        break;

                    case OUEST:
                        System.out.println("OUEST");
                        getPosition().setColonne(getPosition().getColonne()-1);
                        break;
                }
            }
        }
         //System.out.println("[exe2] colonne : "+getPosition().getColonne()+"\tligne : "+getPosition().getLigne());
    }

    public abstract double getVitesse(NatureTerrain nat);
    public abstract void setVitesse(double v);
    public abstract void deverserEau(int vol);
    public abstract int remplirReservoir(Carte carte);
    public abstract boolean verif_depl(Direction d, Case voisin);
    public abstract String getType();
}
