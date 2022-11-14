package plan;

import java.util.*;
import robot.*;
import io.*;


public class Graph{
  private int [][] graph;
  private int len;
  private Case[] tab;
  private Robot robot;
  private Carte carte;
  
  /*
    la fct permet de creer le graphe a partir de la carte et du robot X a
   */
  public Graph(Carte carte,Robot robot) {
    int i,j,k=0;
    this.len = carte.getNbCases();
    this.graph = new int[carte.getNbCases()][carte.getNbCases()];
    this.tab = new Case[carte.getNbCases()]; 
    this.robot = robot;
    this.carte = carte;
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
  }

  public boolean check(Carte carte,Robot robot,Case i,Case j){
       if(robot.verifCase(i)){
        if(carte.isVoisin(i, j)){
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
}



