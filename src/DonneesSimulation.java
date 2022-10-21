import robot.*;
import plan.*;

import java.util.*;
import java.util.LinkedList;


public class DonneesSimulation {
  private Carte carte;
  private LinkedList<Robot> listeRobot;
  private LinkedList<Incendie> listeIncendie;

  public DonneesSimulation(){
    this.carte = new Carte(50, 100);
    this.listeRobot = new LinkedList<Robot>();
    this.listeIncendie = new LinkedList<Incendie>();
  }

  public DonneesSimulation(int nbLignes, int nbColonnes){
    this.carte = new Carte(nbLignes, nbColonnes);
    this.listeRobot = new LinkedList<Robot>();
    this.listeIncendie = new LinkedList<Incendie>();
  }
  public void ajouteRobot(Robot r){
    listeRobot.add(r);
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

  public LinkedList<Incendie> getListeIncendie(){
    return this.listeIncendie;
  }
}
