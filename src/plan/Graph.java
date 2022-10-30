package plan;

import java.util.*;
import robot.*;

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
  /*
    la fct permet de creer le graphe a partir de la carte et du robot X avec le chemin possible de val 1 
   */
  public Graph(Carte carte,Robot robot) {
    int i,j,k=0;
    this.len = carte.getNbCases();
    this.graph = new int[carte.getNbCases()][carte.getNbCases()];
    this.tab = new Case[carte.getNbCases()]; 

    for(i=0;i<carte.getNbLignes();i++){
      for(j=0;j<carte.getNbColonnes();j++){
        tab[k] = carte.getCase(i,j);
        k++; 
      }
    }

    for(i=0;i<carte.getNbCases();i++){
      for(j=0;j<carte.getNbCases();j++){
          if(check(carte,robot,tab[i],tab[j])){
                addEdge(i,j);
          }            
      }
    }
    /* le print */ 
     for(i=0;i<carte.getNbCases();i++){
      for(j=0;j<carte.getNbCases();j++){
          System.out.println("(i: "+i+", j: "+j+") = "+getGraphVal(i,j));
      }
     }
     
  }

  public boolean check(Carte carte,Robot robot,Case i,Case j){
        if(carte.isVoisin(i, j))
            // if(robot.verifCase(carte.getCase(i,j)))
                        return true;
        return false;       
    }

  public int getGraphSize(){
    return this.len;
  }
  public int getGraphVal(int ligne, int colonne){
      return this.graph[ligne][colonne];
  }
  public int getGraphVal(int indice){
      return this.graph[getCase(indice).getLigne()][getCase(indice).getColonne()];
  }
  public Case getCase(int indice){
    return tab[indice];
  }

  public void addEdge(int ligne, int colonne) {
    this.graph[ligne][colonne] = 1;
    this.graph[colonne][ligne] = 1;
  }

  public void removeEdge(int ligne, int colonne) {
    this.graph[ligne][colonne] = 0;
    this.graph[colonne][ligne] = 0;
  }


  public void dijkstra(Graph graph, int source) {
    int taille = graph.getGraphSize();
    System.out.println("taille : = " +taille);
    int i,v,u;
    boolean[] sommetVisite = new boolean[taille];
    int[] distance = new int[taille];
    for (i = 0; i < taille; i++) {
      sommetVisite[i] = false;
      distance[i] = 999; //Integer.MAX_VALUE; A modifier apres ;;;!!!!!
    }

    // Distance of self loop is zero
    distance[source] = 0;
    for (i = 0; i < taille; i++) {
      System.out.println("i :+++++ : "+i);
      // Update the distance between neighbouring vertex and source vertex
      u = findMinDistance(distance, sommetVisite);
      sommetVisite[u] = true;

      // Update all the neighbouring vertex distances
      for (v = 0; v < taille; v++) {
        if (!sommetVisite[v] &&  getGraphVal(v) != 0 && (distance[u] + getGraphVal(v) < distance[v])) {
          distance[v] = distance[u] +  getGraphVal(v);
          System.out.println("v : "+v+"= diste : = " +distance[v]);
        }
      }
    }
    for (i = 0; i < distance.length; i++) {
      System.out.println(String.format("Distance from %s to %s is %s", source, i, distance[i]));
    }
  }

  // Finding the minimum distance
  private  int findMinDistance(int[] distance, boolean[] sommetVisite) {
    int minDistance = 999; //Integer.MAX_VALUE;
    int minDistanceVertex = -1;
    System.out.println("size diste : = " + distance.length);
    for (int i = 0; i < distance.length; i++) {
      if (!sommetVisite[i] && distance[i] < minDistance) { // !!!! probleme herree  !!
         System.out.println("**** : "+i+" dist = "+distance[i]+"\n");
        minDistance = distance[i];
        minDistanceVertex = i;
      }
    }
    System.out.println("return : "+minDistanceVertex);
    return minDistanceVertex;
  }

}