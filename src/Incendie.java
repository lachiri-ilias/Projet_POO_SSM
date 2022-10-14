

public class Incendie {
    private int nbLitresEau;
    private Case position;

    public Incendie(int nbl, int l, int c){
        Case case = new Case(l,c);
        this.nbLitresEau = nbl;
        this.position = case;
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