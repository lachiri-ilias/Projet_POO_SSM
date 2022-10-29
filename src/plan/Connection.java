package plan;

import java.util.*;

// graphSpecial est a ajouter dans les voisins d'un vertex = une case
// Il indique donc vers quelle case cette liason se dirige et le temps qu'il faut pour y aller

/*
Connection est un élément lié à une case dite caseDepart
On stocke :
  - la case d'arrivée si elle est accessible par le type de robot en question
  - le poids = le temps mis par ce type de robot pour parcourir la case sur laquelle il se trouve !

PS : tout les connections d'une case auront le meme poids = temps
     car il correspond a la duree poujr parcourir la case sur laquelle
     se trouve le robot -> ne depend pas de la case destination

*/

public class Connection {
  private Case case; // case de destination
  private long duree; // duree pour parcourir la case actuelle (pas la case destination)

  public Connection(Case case, long duree){
    this.case = case;
    this.duree = duree;
  }
}
