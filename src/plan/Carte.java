package plan;

import io.Direction;
import io.NatureTerrain;

public class Carte {
    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    private Case [][] carte;

    public Carte(int nbLignes, int nbColonnes){
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.carte = new Case[this.nbLignes][this.nbColonnes];
        int i,j;
        for(i=0;i<this.nbLignes;i++){
            for(j=0;j<this.nbColonnes;j++){
                carte[i][j] = new Case(i, j);
            }
        }

    }

    public int getNbLignes(){
        return nbLignes;
    }

    public void setNbLignes(int nbLignes){
        this.nbLignes = nbLignes;
    }

    public int getNbColonnes(){
        return nbColonnes;
    }

    public void setNbColonnes(int nbColonnes){
        this.nbColonnes = nbColonnes;
    }

    public int getTailleCases(){
        return tailleCases;
    }

    public Case getCase(int l, int c){
        return carte[l][c];
    }

    public boolean voisinExiste(Case src, Direction dir){
        int c, l;
        c = src.getColonne();
        l = src.getLigne();

        switch(dir){
            case NORD:
                return l>0; // l-1>=0

            case SUD:
                return l< this.getNbLignes();

            case EST:
                return c< this.getNbColonnes();

            case OUEST:
                return c>0;
        }
        return false;  // a discuter !!!!!
    }

    public Case getVoisin(Case src, Direction dir){
        int c, l;

        if(this.voisinExiste(src, dir)){
            c = src.getColonne();
            l = src.getLigne();

            switch(dir){
                case NORD:
                    return getCase(l-1,c);

                case SUD:
                    return getCase(l+1,c);

                case EST:
                    return getCase(l,c+1);

                case OUEST:
                    return getCase(l,c-1);
            }
        }
        else throw new IllegalArgumentException("No Voisin !");
        return getCase(0,0); // ......??????????????
    }

    public boolean existeTypeVoisin(Case src, NatureTerrain nature){
      for(Direction dir : Direction.values()){
        if(this.getVoisin(src, dir).getNature() == nature) return true;
      }
      return false;
    }
}
