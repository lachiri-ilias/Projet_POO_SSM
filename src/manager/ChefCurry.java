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
  private LinkedList<Case> listeCaseEauVosin;

  public ChefCurry(DonneesSimulation data){
    this.carte = data.getCarte();
    this.listeRobot = data.getListeRobot();
    this.listeIncendie = data.getListeIncendie();
    this.listeEvenement = new LinkedList<Evenement>();
    this.listeCaseEau = new LinkedList<Case>();
    this.listeCaseEauVosin = new LinkedList<Case>();
    for (int lig = 0; lig < this.carte.getNbLignes(); lig++) {
        for (int col = 0; col < this.carte.getNbColonnes(); col++) {
            if(carte.getCase(lig, col).getNature()==NatureTerrain.EAU){
                listeCaseEau.add(carte.getCase(lig, col));

                if(carte.voisinExiste(carte.getCase(lig, col), Direction.NORD)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.NORD).getNature() !=NatureTerrain.EAU) && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.NORD))))
                    listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.NORD));
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.SUD)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.SUD).getNature() !=NatureTerrain.EAU) && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.SUD))))
                    listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.SUD));  
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.EST)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.EST).getNature() !=NatureTerrain.EAU) && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.EST))))
                    listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.EST));  
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.OUEST)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.OUEST).getNature() !=NatureTerrain.EAU) && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.OUEST))))
                    listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.OUEST));  
                }
            }
        }
    }
   // System.out.print("\n*******************\nListe  : "+listeCaseEauVosin);
  }
  /* EST CE QUE le ROBOT PEUT ALLER AU FEUX OU PAS ...???????  */ 
  public void Simulation(long date){
    /* TODO optimiser : trouver comment dimunier les appele new graph (par exemple : reset val graph !) */
    Graph graph ;
    long k = date +1;
    Case derniereposition; // enregistrer la derniere pos du robot :!!!
    // System.out.print("\n\n\n********** Liste incendie : "+listeIncendie+" \n\n");
    // System.out.print("Liste Evenement : "+listeEvenement);
    for(Incendie incendies : listeIncendie){
      for(Robot robots : listeRobot){
        if(robots.getIsLibre() && !(incendies.getIsTaken()) ){
              // System.out.print("   in \n");
            incendies.setIsTaken(true);
            if(robots.getCapActuelle() == 0){
              derniereposition = goRemplir(robots,k);
            }
            else{
              derniereposition = robots.getPosition();
            }
            k++;
            if(!goEteindre(robots,derniereposition,incendies,k)){  // verifier est ce que le robots peut accedes ou pas si oui il part etaindre  sinon rien.
              robots.setIsLibre(true);
              incendies.setIsTaken(false);
             
            }
            else{
                break;
            }
            
        }
      } 
    }
 }
 /* 
    C'est la 1ere version ou le chef pompier ordone au robot de remplir l'eau + etaindre le feux or le remplissage de l'eau depend des robots
    d'ou on vas faire une methode remplire eau et qui va traité  le cas. Inspiré de la fct de Robin :) 
 */
 public void Simulation_V01(long date){
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
private LinkedList<Case> getListeCaseEauVosin(){
    return this.listeCaseEauVosin;
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
private Case plusProcheCaseEauVoisin(Robot robot){
    /* Faire la difference entre les robots drone au dessus des cases les autres a cote ! */
    Case casePlusProche=getListeCaseEauVosin().get(0) ; // juste l'etat init pour ne pas avoir des probleme de compilation !
    int distancemin = Integer.MAX_VALUE;
    Graph graph ;
    for(Case c : getListeCaseEauVosin()){
      if(robot.verifCase(c)){
         // System.out.print("\nl'erreur : "+c);
          graph = new Graph(this.carte,robot);
          graph.dijkstra2(graph,robot.getPosition(),c); 
          if( graph.getCourtDistance() < distancemin ){
            distancemin = graph.getCourtDistance();
            casePlusProche = c;
          }
      }
    }
    return casePlusProche;
}


private boolean goEteindre(Robot robot,Case derniereposition,Incendie incendie,long k){
  Graph graph = new Graph(this.carte,robot);
  graph.dijkstra2(graph,derniereposition,incendie.getCase()); 
  if(graph.getCourtDistance()<Integer.MAX_VALUE){
      for(Direction d : graph.getListeDiretion()){
          ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
          k++;
      }
      ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),this.listeIncendie,incendie,k,this.carte));
      k++;
      ajouteEvenement(new Liberer_Robot(graph.getRobot(),k));
      k++;
      ajouteEvenement(new Liberer_Incendie_Si(robot,incendie,listeIncendie,k));
      robot.setIsLibre(false);
      return true;
  }
  else{
    return false;
  }
}
private Case goRemplir(Robot robot, long k){
  Case plusprocheCaseEau;
  if(robot.getType()=="Drone"){
      plusprocheCaseEau = plusProcheCaseEau(robot);
      Graph graph = new Graph(this.carte,robot);
      graph.dijkstra2(graph,robot.getPosition(),plusprocheCaseEau); 
      for(Direction d : graph.getListeDiretion()){
          ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
          k++;
      }
      ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
      k++;
      return plusprocheCaseEau;
  }
  else if(robot.getType()=="R_Pattes"){
    robot.remplirReservoir(this.carte);
    return robot.getPosition();
  }
  else{
    plusprocheCaseEau = plusProcheCaseEauVoisin(robot);
    Graph graph = new Graph(this.carte,robot);
    graph.dijkstra2(graph,robot.getPosition(),plusprocheCaseEau); 
    for(Direction d : graph.getListeDiretion()){
        ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
        k++;
    }
    ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
    k++;
    return plusprocheCaseEau;
  }
}

}











