package incendie;

import plan.*;

public class Incendie {
    private int nbLitresEau;
    private Case position;

    public Incendie(int nbl, Case pos){
        this.nbLitresEau = nbl;
        this.position = pos;
    }

    public int getLitresEau(){
        return this.nbLitresEau;
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