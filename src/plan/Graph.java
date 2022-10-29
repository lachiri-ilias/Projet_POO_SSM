package plan;

import java.util.*;

/*
La structure d'un graph :
Un Map de liste de Connection -> a chaque Case est associee une liste de Connection :
  - Chaque Case atteignable par un type de robot donné est ajoutée
  - Les voisins de cette Case sont ajoutés dans la liste de voisins de la case avec le poids si atteignable par le robot 

*/
public class Graph {
  private Map<Case,List<Connection>> adjVertices;

  public Graph(){
    adjVertices = new Map<Case,List<Connection>>();
  }

  public void addVertex(Case case){
    adjVertices.putIfAbsent(case, new ArrayList<>());
  }

  public void removeVertex(Case case){
    // On supprime les voisins de la  -> les edges
    adjVertices.values().stream().forEach(e -> e.remove(case));
    // on supprime la case de la adjVertices
    adjVertices.remove(case);
  }

  public void addEdge(Case caseDepart, Case caseDestination, long tempsCaseDepart, long tempsCaseDestination){
    adjVertices.get(caseDepart).add(new Connection(caseDestination,tempsCaseDepart));
    adjVertices.get(caseDestination).add(new Connection(caseDepart, tempsCaseDestination);
  }

  public void removeEdge(Case caseDepart, Case caseDestination){
    List<Case> voisinsDepart = adjVertices.get(caseDepart);
    List<Case> voisinsDestination = adjVertices.get(caseDestination);
    if(voisinsDepart != NULL) voisinsDepart.remove(caseDestination);
    if(voisinsDestination != NULL) voisinsDestination.remove(caseDepart);
  }

  public List<Case> getAdjVertices(Case case){
    return adjVertices.get(case);
  }
}
