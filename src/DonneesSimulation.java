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
}
