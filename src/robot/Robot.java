package robot;

import io.Direction;
import io.NatureTerrain;
import plan.*;

public abstract class Robot {
    protected Case position;
    protected double vitesse;
    protected double vitesse_max;
    protected int cap_max; // CHANGER
    protected int cap_actuelle;
    protected long tps_remplissage;
    protected int tps_deversage;
    protected int qte_deversage;
    protected long tps_fin_act=0;

    public Robot(Case c, double v, double vm, int cm, int ca, int tr, int td, int qd){
        this.position = c;
        this.vitesse = v;
        this.vitesse_max = vm;
        this.cap_max = cm;
        this.cap_actuelle = ca;
        this.tps_remplissage = tr;
        this.tps_deversage = td;
        this.qte_deversage = qd;
    }

    public Robot(Robot robot){
      this.position = robot.position;
      this.vitesse = robot.vitesse;
      this.cap_max = robot.cap_max;
      this.cap_actuelle = robot.cap_actuelle;
      this.tps_remplissage = robot.tps_remplissage;
      this.tps_deversage = robot.tps_deversage;
      this.qte_deversage = robot.qte_deversage;
    }

    public Case getPosition(){
        return this.position;
    }

    public double getVitesseMax(){
      return this.vitesse_max;
    }

    public int getCapActuelle(){
        return this.cap_actuelle;
    }

    public int getCapMax(){
        return this.cap_max;
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
        setVitesse(getVitesseMax());
         //System.out.println("[exe2] colonne : "+getPosition().getColonne()+"\tligne : "+getPosition().getLigne());
    }

    public abstract double getVitesseTerrain(NatureTerrain nat);
    public abstract void setVitesse(double v);
    public abstract void deverserEau(int vol);
    public abstract boolean remplirReservoir(Carte carte);
    public abstract boolean verif_depl(Direction d, Case voisin);
    public abstract String getType();
}
