
enum NatureTerrain{
    TERRAIN_LIBRE, EAU, HABITAT, FORET, ROCHE;
}

enum Direction{
    NORD, SUD, EST, OUEST;
}


public class Case {
    private int ligne, colonne;
    private NatureTerrain nature;

    
    public Case(int l, int c, NatureTerrain n){
        this.ligne = l;
        this.colonne = c;
        this.nature = n;
    }

    public Case(int l, int c){
        this.ligne = l;
        this.colonne = c;
        this.nature = TERRAIN_LIBRE;
    }

    public Case(Case other){
        this(other.ligne,other.colonne,other.nature);
    }

    public Case(){
        this(100, 100, TERRAIN_LIBRE);
    }

    public int getColonne(){
        return colonne;
    }

    public int getLigne(){
        return ligne;
    }

    public NatureTerrain getNature(){
        return nature;
    }

    public void setNature(NatureTerrain n){
        this.nature = n;
    }

    @Override
    public String toString(args){
        return "ligne : "+getLigne()+" colonne : "+getColonne()+" nature : "+getNature();
    }
}
