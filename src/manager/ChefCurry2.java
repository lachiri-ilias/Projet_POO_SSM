package manager;

import io.*;
import plan.*;
import evenement.*;
import robot.*;
import incendie.*;
import donnees.*;

import java.util.List;
import java.util.LinkedList;


public class ChefCurry2{

  private Carte carte;
  private LinkedList<Robot> listeRobot;
  private LinkedList<Graph> listeGraph;
  private LinkedList<Incendie> listeIncendie;
  private LinkedList<Evenement> listeEvenement;
  private LinkedList<Case> listeCaseEau;
  private LinkedList<Case> listeCaseEauVosin;

  public ChefCurry2(DonneesSimulation data){
    this.carte = data.getCarte();
    this.listeRobot = data.getListeRobot();
    this.listeIncendie = data.getListeIncendie();
    this.listeGraph = data.getListeGraph();
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
    long k = date +1;
    Case derniereposition; // enregistrer la derniere pos du robot :!!!
    // System.out.print("\n\n\n********** Liste incendie : "+listeIncendie+" \n\n");
    // System.out.print("Liste Evenement : "+listeEvenement);
    for(Incendie incendies : listeIncendie){
      for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && !(incendies.getIsTaken()) && graphs.getRobot().verifCase(incendies.getCase()) ){
              // System.out.print("   in \n");
            incendies.setIsTaken(true);
            if(graphs.getRobot().getCapActuelle() == 0){
              //System.out.println("Robot : ["+ graphs.getRobot() +"] : go Remplire \n");
              derniereposition = goRemplir(graphs,k);
            }
            else{
              derniereposition = graphs.getRobot().getPosition();
            }
            k++;
            if(!goEteindre(graphs,derniereposition,incendies,k)){  // verifier est ce que le robots peut accedes ou pas si oui il part etaindre  sinon rien.
                graphs.getRobot().setIsLibre(true);
                incendies.setIsTaken(false);
             
            }
            else{
                // System.out.println("Robot : ["+ graphs.getRobot() +"] : go Etaindre \n");
                break;
            }
            
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
private Case plusProcheCaseEau(Graph graph ){
    /* Faire la difference entre les robots drone au dessus des cases les autres a cote ! */
    Case casePlusProche=getListeCaseEau().get(0) ;
    int distancemin = Integer.MAX_VALUE;
    for(Case c : getListeCaseEau()){
      Dijkstra dijkstra = new Dijkstra();
      dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),c); 
      if( dijkstra.getCourtDistance() < distancemin ){
        distancemin = dijkstra.getCourtDistance();
        casePlusProche = c;
      }
    }
    return casePlusProche;
}
private Case plusProcheCaseEauVoisin( Graph graph ){
    /* Faire la difference entre les robots drone au dessus des cases les autres a cote ! */
    Case casePlusProche=getListeCaseEauVosin().get(0) ; // juste l'etat init pour ne pas avoir des probleme de compilation !
    int distancemin = Integer.MAX_VALUE;
    for(Case c : getListeCaseEauVosin()){
      if(graph.getRobot().verifCase(c)){
          //System.out.print("\nl'erreur : "+c);
          Dijkstra dijkstra = new Dijkstra();
          dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),c); 
          if( dijkstra.getCourtDistance() < distancemin ){
            distancemin = dijkstra.getCourtDistance();
            casePlusProche = c;
          }
      }
    }
    return casePlusProche;
}


private boolean goEteindre( Graph graph ,Case derniereposition,Incendie incendie,long k){
  Dijkstra dijkstra = new Dijkstra();
  dijkstra.dijkstra2(graph,derniereposition,incendie.getCase()); 
  //System.out.print("la liste direction pour eteindre  est : "+dijkstra.getListeDiretion()+"\n");

  if(dijkstra.getCourtDistance()<Integer.MAX_VALUE){
      for(Direction d : dijkstra.getListeDiretion()){
          ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
          k++;
      }
      ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),this.listeIncendie,incendie,k,this.carte));
      k++;
      ajouteEvenement(new Liberer_Robot(graph.getRobot(),k));
      k++;
      ajouteEvenement(new Liberer_Incendie_Si(graph.getRobot(),incendie,listeIncendie,k));
      graph.getRobot().setIsLibre(false);
      return true;
  }
  else{
    return false;
  }
}
private Case goRemplir( Graph graph , long k){
  Case plusprocheCaseEau;
  if(graph.getRobot().getType()=="Drone"){
      plusprocheCaseEau = plusProcheCaseEau(graph);
      Dijkstra dijkstra = new Dijkstra();
      dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),plusprocheCaseEau); 
     // System.out.print("la liste direction pour remplire  est : "+dijkstra.getListeDiretion()+"\n");
      for(Direction d : dijkstra.getListeDiretion()){
          ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
          k++;
      }
      ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
      k++;
      return plusprocheCaseEau;
  }
  else if(graph.getRobot().getType()=="R_Pattes"){
    graph.getRobot().remplirReservoir(this.carte);
    return graph.getRobot().getPosition();
  }
  else{
    plusprocheCaseEau = plusProcheCaseEauVoisin(graph);
    Dijkstra dijkstra = new Dijkstra();
    dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),plusprocheCaseEau); 
    //System.out.print("la liste direction pour remplire  est : "+dijkstra.getListeDiretion()+"\n");
    for(Direction d : dijkstra.getListeDiretion()){
        ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
        k++;
    }
    ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
    k++;
    return plusprocheCaseEau;
  }
}

}