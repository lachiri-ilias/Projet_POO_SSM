import io.NatureTerrain;

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
        this.nature = NatureTerrain.TERRAIN_LIBRE;
    }

    public Case(Case other){
        this(other.ligne,other.colonne,other.nature);
    }

    public Case(){
        this(100, 100, NatureTerrain.TERRAIN_LIBRE);
    }

    public int getColonne(){
        return colonne;
    }

    public int getLigne(){
        return ligne;
    }

    public void setColonne(int col){
        this.colonne = col;
    }

    public void setLigne(int lig){
        this.ligne = lig;
    }

    public NatureTerrain getNature(){
        return nature;
    }

    public void setNature(NatureTerrain n){
        this.nature = n;
    }

    @Override
    public String toString(){
        return "ligne : "+getLigne()+" colonne : "+getColonne()+" nature : "+getNature();
    }
}
