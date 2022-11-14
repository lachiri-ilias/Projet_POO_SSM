package plan;

import io.Direction;
import io.NatureTerrain;
import java.util.LinkedList;


public class Carte {
    private int tailleCases;
    private int nbLignes;
    private int nbColonnes;
    private Case [][] carte;
    private LinkedList<Case> listToDraw;

    public Carte(int nbLignes, int nbColonnes){
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.listToDraw = new LinkedList<Case>();
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
    public void setTailleCases(int tailleCases){
        this.tailleCases = tailleCases;
    }

    public int getTailleCases(){
        return tailleCases;
    }
    public int getNbCases(){
        return this.nbColonnes * this.nbLignes;
    }
    public Case getCase(int l, int c){
        return carte[l][c];
    }
    public LinkedList<Case> getListToDraw(){
        return this.listToDraw;
    }
    public void addListToDrawTwo(Case origin, Case dest){
      getListToDraw().add(origin);
      getListToDraw().add(dest);
    }
    public boolean voisinExiste(Case src, Direction dir){
        int c, l;
        c = src.getColonne();
        l = src.getLigne();
        switch(dir){
            case NORD:
                return l-1>0;

            case SUD:
                return l+1 < this.getNbLignes();
            case EST:
                return c+1 < this.getNbColonnes();

            case OUEST:
                return c-1>0;
        }
        return false;
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
    public boolean isVoisin(Case src, Case arriv){
        int c1, l1, c2,l2;
        c1 = src.getColonne();
        l1 = src.getLigne();
        c2 = arriv.getColonne();
        l2 = arriv.getLigne();
        if(c1 == c2){
            if( (l1 == l2+1) && (l2+1<this.getNbLignes()))
                return true;
            if( (l1 == l2-1) && (l2-1>=0))
                 return true;
        }
        if(l1 == l2){
            if( (c1 == c2+1) && (c2+1<this.getNbColonnes()))
                return true;
            if( (c1 == c2-1) && (c2-1>=0))
                return true;
        }
        return false;
    }

    public boolean existeTypeVoisin(Case src, NatureTerrain nature){
      for(Direction dir : Direction.values()){
        if(this.getVoisin(src, dir).getNature() == nature) return true;
      }
      return false;
    }
}
