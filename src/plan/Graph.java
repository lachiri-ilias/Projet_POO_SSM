package plan;

import java.util.*;
import robot.*;
import io.*;
/*
La structure d'un graph :
Un Map de liste de Connection -> a chaque Case est associee une liste de Connection :
  - Chaque Case atteignable par un type de robot donné est ajoutée
  - Les voisins de cette Case sont ajoutés dans la liste de voisins de la case avec le poids si atteignable par le robot 

*/
// public class Graph {
//   private Map<Case,List<Connection>> adjVertices;

//   public Graph(){
//     adjVertices = new Map<Case,List<Connection>>(); // Map ??
//   }

//   public void addVertex(Case c){
//     adjVertices.putIfAbsent(c, new ArrayList<>());
//   }

//   public void removeVertex(Case c){
//     // On supprime les voisins de la  -> les edges
//     adjVertices.values().stream().forEach(e -> e.remove(c));
//     // on supprime la case de la adjVertices
//     adjVertices.remove(c);
//   }

//   public void addEdge(Case caseDepart, Case caseDestination, long tempsCaseDepart, long tempsCaseDestination){
//     adjVertices.get(caseDepart).add(new Connection(caseDestination,tempsCaseDepart));
//     adjVertices.get(caseDestination).add(new Connection(caseDepart, tempsCaseDestination));
//   }

//   public void removeEdge(Case caseDepart, Case caseDestination){
//     List<Case> voisinsDepart = adjVertices.get(caseDepart);
//     List<Case> voisinsDestination = adjVertices.get(caseDestination);
//     if(voisinsDepart.size() != 0) voisinsDepart.remove(caseDestination);
//     if(voisinsDestination.size() != 0) voisinsDestination.remove(caseDepart);
//   }

//   public List<Case> getAdjVertices(Case c){
//     return adjVertices.get(c);
//   }
// }

public class Graph{
  private int [][] graph;
  private int len;
  private Case[] tab;
  private Robot robot;
  private Carte carte;
  private LinkedList<Integer> listeChemin;
  private LinkedList<Direction> listeDirection;
  private int courtDistance;

  /*
    la fct permet de creer le graphe a partir de la carte et du robot X avec le chemin possible de val 1 
   */
  public Graph(Carte carte,Robot robot) {
    int i,j,k=0;
    this.len = carte.getNbCases();
    this.graph = new int[carte.getNbCases()][carte.getNbCases()];
    this.tab = new Case[carte.getNbCases()]; 
    this.robot = robot;
    this.carte = carte;
    this.listeChemin = new LinkedList<Integer>();
    this.listeDirection = new LinkedList<Direction>();

    for(i=0;i<carte.getNbLignes();i++){
      for(j=0;j<carte.getNbColonnes();j++){
        tab[k] = carte.getCase(i,j);
        k++; 
      }
    }
    // System.out.println("\n\nBEFFOOORE");
    // for(i=0;i<carte.getNbCases();i++){
    //   for(j=0;j<carte.getNbCases();j++){
    //       System.out.println("(i: "+i+", j: "+j+") = "+getGraphVal(i,j));
    //   }
    //  }
    for(i=0;i<carte.getNbCases();i++){
      for(j=0;j<carte.getNbCases();j++){
          if(check(carte,robot,tab[i],tab[j])){
                // System.out.println("i : "+i+" j : "+j+" check = "+ check(carte,robot,tab[i],tab[j]));
                addEdge(i,j);
          }            
      }
    }
    /* le print */ 
    // System.out.println("\n\nAFTEEEEER");
    //  for(i=0;i<carte.getNbCases();i++){
    //   for(j=0;j<carte.getNbCases();j++){
    //       System.out.println("(i: "+i+", j: "+j+") = "+getGraphVal(i,j));
    //   }
    //  }
     
  }
 
  public boolean check(Carte carte,Robot robot,Case i,Case j){
       if(robot.verifCase(i)){
        if(carte.isVoisin(i, j)){
         System.out.println("verif : case "+j.getNature()+ " ROBOT : "+robot.getType()+" verif == "+ robot.verifCase(j));
          if(robot.verifCase(j))
                    return true;
        }
       }
        return false;       
    }
  public Case getCase(int i){
    return this.tab[i];
  }
  public int getGraphSize(){
    return this.len;
  }
  public Carte getCarte(){
    return this.carte;
  }
  public Robot getRobot(){
    return this.robot;
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
  public int getGraphVal(int ligne, int colonne){
      return this.graph[ligne][colonne];
  }
  public int getGraphVal(int indice){
      return this.graph[getCase(indice).getLigne()][getCase(indice).getColonne()];
  }
  public Case getcase(int i){
    return this.tab[i];
  }
  public void addEdge(int ligne, int colonne) {
    this.graph[ligne][colonne] = 1;
    this.graph[colonne][ligne] = 1;
  }
  public void removeEdge(int ligne, int colonne) {
    this.graph[ligne][colonne] = 0;
    this.graph[colonne][ligne] = 0;
  }
  public int getCourtDistance(){
      return this.courtDistance;
  }
  public void dijkstra2(Graph graph, Case c_source,Case c_arrive)
	{
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
            for (int vertexIndex = 0;
                vertexIndex < nVertices;
                vertexIndex++)
            {
              if (!added[vertexIndex] &&
                shortestDistances[vertexIndex] < shortestDistance)
              {
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
    System.out.print("la liste chemin est : "+getListeChemin()+"\n");
    convertirListChemin(graph);
    System.out.print("la liste direction  est : "+getListeDiretion()+"\n");

	}

   private void convertirListChemin(Graph graph)
   {
      Case c0;
      Case c1;
      for(int i=0;i<graph.getListeChemin().size()-1;i++){
          c0 = graph.getcase(graph.getListeChemin().get(i));
          c1 = graph.getcase(graph.getListeChemin().get(i+1));
          if(c0.getLigne() == c1.getLigne()){
                        if(c0.getColonne() == c1.getColonne() +1) graph.ajouteDirection(Direction.OUEST);
                        else if(c0.getColonne() == c1.getColonne() -1) graph.ajouteDirection(Direction.EST);
                    }
          else if(c0.getColonne() == c1.getColonne()){
                        if(c0.getLigne() == c1.getLigne() +1) graph.ajouteDirection(Direction.NORD);
                        else if(c0.getLigne() == c1.getLigne() -1) graph.ajouteDirection(Direction.SUD);
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
    graph.ajouteChemin(currentVertex);
		Path2(chemin[currentVertex], chemin,graph);
	}
/* ***************************************************************  */
  // public void dijkstra(Graph graph, int source) {
  //   int taille = graph.getGraphSize();
  //   System.out.println("taille : = " +taille);
  //   int i,v,u;
  //   boolean[] sommetVisite = new boolean[taille];
  //   // List<Integer> route = new ArrayList<>();
  //   // pour stocker le chemin le plus court
  //   int[] distance = new int[taille];
  //   for (i = 0; i < taille; i++) {
  //     sommetVisite[i] = false;
  //     distance[i] = 999; //Integer.MAX_VALUE; A modifier apres ;;;!!!!!
  //   }

  //   int[] chemin = new int[taille];
  //   chemin[source] = -1;

  //   // Distance of self loop is zero
  //   distance[source] = 0;
  //   for (i = 0; i < taille; i++) {
  //     System.out.println("i :+++++ : "+i);
  //     // Update the distance between neighbouring vertex and source vertex
  //     u = findMinDistance(distance, sommetVisite);
  //     sommetVisite[u] = true;
  //     // Update all the neighbouring vertex distances
  //     for (v = 0; v < taille; v++) {
  //       if (!sommetVisite[v] &&  getGraphVal(u,v) != 0 && (distance[u] + getGraphVal(u,v) < distance[v])) {
  //         System.out.println("\n  chemin "+chemin[v]+ " v " +v+" u "+u);
  //         chemin[v]=u ;
  //         distance[v] = distance[u] +  getGraphVal(u,v);
  //       }
  //     }
  //   }

  //   for (i = 0; i < distance.length; i++) {
  //    if(i!=source){
  //       System.out.println(String.format("\n ******************  \n Distance from %s to %s is %s", source, i, distance[i]));
  //       printChemin(i,chemin);
  //       System.out.println(" ili9 "+chemin[source]+ " ");
  //    }
  //   }
  // }

  // private  void printChemin(int arrive,int[] chemin)
  //   {
  //       System.out.println("size liste : "+chemin[arrive].size());
  //       if (chemin[arrive].isEmpty())
  //       {
  //           return;
  //       }
  //       System.out.println(" ili9 "+chemin[arrive].get(0) + " ");
  //       //System.out.print("T arrive "+arrive+" TT : "+chemin[arrive].get(0) +"**********");
  //       chemin[arrive].remove(0);
  //       //printChemin(arrive, chemin);
       
  //   }

  // // Finding the minimum distance
  // private  int findMinDistance(int[] distance, boolean[] sommetVisite) {
  //   int minDistance = 999; //Integer.MAX_VALUE;
  //   int minDistanceVertex = -1;
  //   //System.out.println("size diste : = " + distance.length);
  //   for (int i = 0; i < distance.length; i++) {
  //     if (!sommetVisite[i] && distance[i] < minDistance) { // !!!! probleme herree  !!
  //        //System.out.println("**** : "+i+" dist = "+distance[i]+"\n");
  //       minDistance = distance[i];
  //       minDistanceVertex = i;
  //     }
  //   }
  //   //System.out.println("return : "+minDistanceVertex);
  //   return minDistanceVertex;
  // }



  public void dijkstra(Graph graph, int source)
	{
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
            //System.out.print("i : "+i);
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                vertexIndex < nVertices;
                vertexIndex++)
            {
              if (!added[vertexIndex] &&
                shortestDistances[vertexIndex] < shortestDistance)
              {
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
      // else{
      //     System.out.print("le robot ne peux pas acceede a la case : "+i);
      // } 
		}
    // System.out.print("\n HEEERRE");
		printSolution(source, shortestDistances, chemin);
	}

  private static void printSolution(int source,int[] distances,int[] chemin)
	{
		int nVertices = distances.length;
		System.out.print("Vertex\t Distance\tPath\n");
		for (int vertexIndex = 0;
				vertexIndex < nVertices;
				vertexIndex++)
		{
			if (vertexIndex != source)
			{ if(distances[vertexIndex] < Integer.MAX_VALUE){
          System.out.print(source + " -> ");
          System.out.print(vertexIndex + " \t\t ");
          System.out.print(distances[vertexIndex] + "\t\t");
          printPath(vertexIndex, chemin);
          System.out.print("\n");
        }
        else{
           System.out.print("le robot ne peux pas acceede a la case : "+vertexIndex+"\n");
        }
			}
		}
	}
	private static void printPath(int currentVertex,int[] chemin)
	{
		if (currentVertex == -1)
		{
			return;
		}
		printPath(chemin[currentVertex], chemin);
		System.out.print(currentVertex + " ");
	}
 
}



