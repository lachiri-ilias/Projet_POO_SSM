package incendie;

import plan.*;

public class Incendie {
    private int nbLitresEau;
    private int nbLitresEauInit;
    private Case position;
    protected boolean isTaken;


    public Incendie(int nbLitresEau, Case position){
        this.nbLitresEau = nbLitresEau;
        this.nbLitresEauInit = nbLitresEau;
        this.position = position;
        this.isTaken = false;
    }

    public void setIsTaken(boolean status){
         this.isTaken = status;
    }
    public boolean getIsTaken(){
        return this.isTaken;
    }
    public int getLitresEau(){
        return this.nbLitresEau;
    }
    public int getLitresEauInit(){
        return this.nbLitresEauInit;
    }

    public Case getCase(){
        return this.position;
    }

    public void setLitresEau(int nbl){
        this.nbLitresEau=nbl;
    }

    public void setCase(Case c){
        this.position=c;
    }
}
