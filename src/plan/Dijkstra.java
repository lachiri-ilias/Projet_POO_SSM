package plan;

import java.util.*;
import robot.*;
import io.*;
public class Dijkstra {

  private LinkedList<Integer> listeChemin;
  private LinkedList<Direction> listeDirection;
  private int courtDistance;

  public Dijkstra(){
    this.listeChemin = new LinkedList<Integer>();
    this.listeDirection = new LinkedList<Direction>();
    this.courtDistance = Integer.MAX_VALUE;
  }

  /**
  Fills the linked list of integers with the shortest path. (example 0->5->9->11->13->14)
  Then fills the linked list of the directions according to the previous list of integers
  */
  public void dijkstra2(Graph graph, Case c_source,Case c_arrive){
    if(graph.getRobot().verifCase(c_arrive)){
            int arrive = c_arrive.getColonne() + c_arrive.getLigne() * graph.getCarte().getNbLignes() ;
            int source = c_source.getColonne() + c_source.getLigne() * graph.getCarte().getNbLignes() ;
            int nVertices = graph.getGraphSize();
            int[] shortestDistances = new int[nVertices];
            boolean[] added = new boolean[nVertices];
            for (int indice = 0; indice < nVertices; indice++) {
              shortestDistances[indice] = Integer.MAX_VALUE;
              added[indice] = false;
            }
            shortestDistances[source] = 0;
            int[] chemin = new int[nVertices];
            chemin[source] = -1;
            for (int i = 1; i < nVertices; i++) {
              if(graph.getRobot().verifCase(graph.getCase(i))){

                int sommetProche = -1;
                int shortestDistance = Integer.MAX_VALUE;
                for (int indice = 0;indice < nVertices;indice++) {
                  if (!added[indice] && shortestDistances[indice] < shortestDistance) {
                    sommetProche = indice;
                    shortestDistance = shortestDistances[indice];
                  }
                }
                added[sommetProche] = true;
                for (int indice = 0; indice < nVertices; indice++)
                {
                  int edgeDistance = graph.getGraphVal(sommetProche,indice);
                  if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[indice])) {
                    chemin[indice] = sommetProche;
                    shortestDistances[indice] = shortestDistance + edgeDistance;
                  }
                }
              }
            }
            courtChemin(source,arrive, shortestDistances, chemin,graph);
            this.courtDistance = shortestDistances[arrive];
            convertirListCheminlistDirection(graph);
    }
	}


  public void ajouteChemin(int c){
    this.listeChemin.add(0,c);
  }
  public void ajouteDirection(Direction d){
    this.listeDirection.add(d);
  }
  public LinkedList<Integer> getListeChemin(){
    return this.listeChemin;
  }
  public LinkedList<Direction> getListeDiretion(){
    return this.listeDirection;
  }
  public int getCourtDistance(){
      return this.courtDistance;
  }

  /**
  From the list of path we fill the list of direction
  */
   private void convertirListCheminlistDirection(Graph graph)
   {
      Case c0;
      Case c1;
      for(int i=0;i<this.getListeChemin().size()-1;i++){
          c0 = graph.getcase(this.getListeChemin().get(i));
          c1 = graph.getcase(this.getListeChemin().get(i+1));
          if(c0.getLigne() == c1.getLigne()){
                        if(c0.getColonne() == c1.getColonne() +1) this.ajouteDirection(Direction.OUEST);
                        else if(c0.getColonne() == c1.getColonne() -1) this.ajouteDirection(Direction.EST);
                    }
          else if(c0.getColonne() == c1.getColonne()){
                        if(c0.getLigne() == c1.getLigne() +1) this.ajouteDirection(Direction.NORD);
                        else if(c0.getLigne() == c1.getLigne() -1) this.ajouteDirection(Direction.SUD);
                    }
      }
    }


  private void courtChemin(int source,int arrive,int[] distances,int[] chemin,Graph graph)
  { 
			if (arrive != source)
			{ if(distances[arrive] < Integer.MAX_VALUE){
          path(arrive, chemin,graph);
        }
        else{
        }
			}
	}
  
	public void path(int current,int[] chemin,Graph graph)
	{
		if (current == -1)
		{
			return ;
		}
    this.ajouteChemin(current);
		path(chemin[current], chemin,graph);
	}
  


}
