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
  }

  public void dijkstra2(Graph graph, Case c_source,Case c_arrive){
    int arrive = c_arrive.getColonne() + c_arrive.getLigne() * graph.getCarte().getNbLignes() ;
    int source = c_source.getColonne() + c_source.getLigne() * graph.getCarte().getNbLignes() ;
		int nVertices = graph.getGraphSize();
    int[] shortestDistances = new int[nVertices];
		boolean[] added = new boolean[nVertices];
		for (int vertexIndex = 0; vertexIndex < nVertices;
											vertexIndex++)
		{
			shortestDistances[vertexIndex] = Integer.MAX_VALUE;
			added[vertexIndex] = false;
    }
		shortestDistances[source] = 0;
		int[] chemin = new int[nVertices];
		chemin[source] = -1;
		for (int i = 1; i < nVertices; i++)
		{
      if(graph.getRobot().verifCase(graph.getCase(i))){

            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
           // System.out.print("Heeere : "+graph.getCase(i)+"\n");
            for (int vertexIndex = 0;vertexIndex < nVertices;vertexIndex++)
            {
              if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance)
              {
                //System.out.print("SSuuuiiiii \n");
                nearestVertex = vertexIndex;
                shortestDistance = shortestDistances[vertexIndex];
              }
            }

            added[nearestVertex] = true;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
            {
              int edgeDistance = graph.getGraphVal(nearestVertex,vertexIndex);
              if (edgeDistance > 0
                && ((shortestDistance + edgeDistance) <
                  shortestDistances[vertexIndex]))
              {
                chemin[vertexIndex] = nearestVertex;
                shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
              }
            }
      }
		}
		courtChemin2(source,arrive, shortestDistances, chemin,graph);
    this.courtDistance = shortestDistances[arrive];
    //System.out.print("la liste chemin est : "+getListeChemin()+"\n");
    convertirListChemin(graph);
    // System.out.print("la liste direction  est : "+getListeDiretion()+"\n");

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
   private void convertirListChemin(Graph graph)
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



  private void courtChemin2(int source,int arrive,int[] distances,int[] chemin,Graph graph)
  { 
			if (arrive != source)
			{ if(distances[arrive] < Integer.MAX_VALUE){
          Path2(arrive, chemin,graph);
        }
        else{
           System.out.print("le robot ne peux pas acceede a la case : "+arrive+"\n");
        }
			}
	}
	public void Path2(int currentVertex,int[] chemin,Graph graph)
	{
		if (currentVertex == -1)
		{
			return ;
		}
    // System.out.print("\n ********************* TESST : "+currentVertex+" case :     "+graph.getCase(currentVertex)+" \n");
    // System.out.print("befoure : "+currentVertex+" case :     "+graph.getCase(currentVertex)+" \n");
    this.ajouteChemin(currentVertex);
		Path2(chemin[currentVertex], chemin,graph);
	}
  


}
