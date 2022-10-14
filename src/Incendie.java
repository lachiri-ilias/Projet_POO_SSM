

public class Incendie {
    private int nbLitresEau;
    private Case position;

    public Incendie(int nbl, Case c){
        this.nbLitresEau = nbl;
        this.position = c;
    }

    public getLitresEau(){
        return this.nbLitresEau;
    }

    public getCase(){
        return this.position;
    }

    public setLitresEau(int nbl){
        this.nbLitresEau=nbl;
    }

    public setCase(Case c){
        this.position=c;
    }
}