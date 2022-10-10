// import io.Direction;
// import io.NatureTerrain;

// enum NatureTerrain{
//     TERRAIN_LIBRE, EAU, HABITAT, FORET, ROCHE;
// }
// enum Direction{
//     NORD, SUD, EST, OUEST;
// }

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

    public int getNbColonnes(){
        return nbColonnes;
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

        // switch(dir){
        //     case NORD:
        //         return l>0; // l-1>=0
            
        //     case SUD:
        //         return l<this.getNbLignes();

        //     case EST:
        //         return c<this.getNbColonne();

        //     case OUEST:
        //         return c>0;
        // }
    }

    public Case getVoisin(Case src, Direction dir){
        int c, l;

        if(this.voisinExiste(src, dir)){
            c = src.getColonne();
            l = src.getLigne();

            // switch(dir){
            //     case NORD:
            //         return getCase(l-1,c); 
                   
            //     case SUD:
            //         return getCase(l+1,c); 

            //     case EST:
            //         return getCase(l,c+1); 

            //     case OUEST:
            //         return getCase(l,c-1); 
            // }
        }

        else throw new IllegalArgumentExeption("No Voisin !");
    }

}

