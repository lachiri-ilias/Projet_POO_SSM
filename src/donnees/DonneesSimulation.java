package donnees;

import robot.*;
import plan.*;
import incendie.*;
import java.util.LinkedList;

public class DonneesSimulation {
  private Carte carte;
  private LinkedList<Robot> listeRobot;
  private LinkedList<Incendie> listeIncendie;

  private LinkedList<Graph> listeGraph;

  public DonneesSimulation(){
    this.carte = new Carte(50, 100);
    this.listeRobot = new LinkedList<Robot>();
    this.listeIncendie = new LinkedList<Incendie>();
    this.listeGraph = new LinkedList<Graph>();
  }

  public DonneesSimulation(int nbLignes, int nbColonnes){
    this.carte = new Carte(nbLignes, nbColonnes);
    this.listeRobot = new LinkedList<Robot>();
    this.listeIncendie = new LinkedList<Incendie>();
    this.listeGraph = new LinkedList<Graph>();
  }

  public void ajouteRobot(Robot r){
    listeRobot.add(r);
  }
  public void ajouteGraph(Graph graph){
    listeGraph.add(graph);
  }
  public void ajouteIncendie(Incendie i){
    listeIncendie.add(i);
  }
  public int getNbRobot(){
    return listeRobot.size();
  }
  public int getNbIncendie(){
    return listeIncendie.size();
  }
  public Carte getCarte(){
    return this.carte;
  }
  
  public LinkedList<Robot> getListeRobot(){
    return this.listeRobot;
  }
  public LinkedList<Graph> getListeGraph(){
    return this.listeGraph;
  }
  public LinkedList<Incendie> getListeIncendie(){
    return this.listeIncendie;
  }
}
