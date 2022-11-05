package manager;

import io.*;
import plan.*;
import evenement.*;
import robot.*;
import incendie.*;
import donnees.*;

import java.util.List;
import java.util.LinkedList;


public class ChefCurry {

  private Carte carte;
  private LinkedList<Robot> listeRobot;
  private LinkedList<Incendie> listeIncendie;
  private LinkedList<Evenement> listeEvenement;
  private LinkedList<Case> listeCaseEau;

  public ChefCurry(DonneesSimulation data){
    this.carte = data.getCarte();
    this.listeRobot = data.getListeRobot();
    this.listeIncendie = data.getListeIncendie();
    this.listeEvenement = new LinkedList<Evenement>();
    this.listeCaseEau = new LinkedList<Case>();
    for (int lig = 0; lig < data.getCarte().getNbLignes(); lig++) {
        for (int col = 0; col < data.getCarte().getNbColonnes(); col++) {
            if(data.getCarte().getCase(lig, col).getNature()==NatureTerrain.EAU){
              listeCaseEau.add(data.getCarte().getCase(lig, col));
            }
        }
    }
  }
  
  public void Simulation(long date){
    /* TODO optimiser : trouver comment dimunier les appele new graph (par exemple : reset val graph !) */
    Graph graph ;
    long k = date +1;
    Case plusprocheCaseEau;
    // System.out.print("\n\n\n********** Liste incendie : "+listeIncendie+" \n\n");
    // System.out.print("Liste Evenement : "+listeEvenement);
    for(Incendie incendies : listeIncendie){
      for(Robot robots : listeRobot){
        // System.out.print("\n heerrre "+robots.getIsLibre() +" // "+ incendies.getIsTaken());
        if(robots.getIsLibre() && !(incendies.getIsTaken()) ){
              // System.out.print("   in \n");
            incendies.setIsTaken(true);
            plusprocheCaseEau=plusProcheCaseEau(robots);
            graph = new Graph(this.carte,robots);
            graph.dijkstra2(graph,robots.getPosition(),plusprocheCaseEau); 
            for(Direction d : graph.getListeDiretion()){
                ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
                k++;
            }
            ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
            k++;
            graph = new Graph(this.carte,robots);
            graph.dijkstra2(graph,plusprocheCaseEau,incendies.getCase()); 
            for(Direction d : graph.getListeDiretion()){
                ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
                k++;
            }
            ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),this.listeIncendie,incendies,k,this.carte));
            k++;
            ajouteEvenement(new Liberer_Robot(graph.getRobot(),k));
            k++;
            ajouteEvenement(new Liberer_Incendie_Si(robots,incendies,listeIncendie,k));
            robots.setIsLibre(false);
            break;
        }
      } 
    }
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
private LinkedList<Case> getListeCaseEau(){
    return this.listeCaseEau;
}
private void ajouteEvenement(Evenement e){
      this.listeEvenement.addLast(e);
    }
public LinkedList<Evenement> getListeEvenements(){
      return this.listeEvenement;
    }
private Case plusProcheCaseEau(Robot robot){
    /* Faire la difference entre les robots drone au dessus des cases les autres a cote ! */
    Case casePlusProche=getListeCaseEau().get(0) ;
    int distancemin = Integer.MAX_VALUE;
    Graph graph ;
    for(Case c : getListeCaseEau()){
      graph = new Graph(this.carte,robot);
      graph.dijkstra2(graph,robot.getPosition(),c); 
      if( graph.getCourtDistance() < distancemin ){
        distancemin = graph.getCourtDistance();
        casePlusProche = c;
      }
    }
    return casePlusProche;
  }

}











