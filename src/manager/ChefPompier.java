package manager;

import io.*;
import plan.*;
import evenement.*;
import robot.*;
import incendie.*;
import donnees.*;

import java.util.List;
import java.util.LinkedList;


public class ChefPompier{

  private Carte carte;
  private LinkedList<Robot> listeRobot;
  private LinkedList<Graph> listeGraph;
  private LinkedList<Incendie> listeIncendie;
  private LinkedList<Evenement> listeEvenement;
  private LinkedList<Case> listeCaseEau;
  private LinkedList<Case> listeCaseEauVosin;

/**
Fills all the linked lists given as attributes
 */
  public ChefPompier(DonneesSimulation data){
    this.carte = data.getCarte();
    this.listeIncendie = data.getListeIncendie();
    this.listeRobot = data.getListeRobot();
    this.listeGraph = data.getListeGraph();
    this.listeEvenement = new LinkedList<Evenement>();
    this.listeCaseEau = new LinkedList<Case>();
    this.listeCaseEauVosin = new LinkedList<Case>();
    for (int lig = 0; lig < this.carte.getNbLignes(); lig++) {
        for (int col = 0; col < this.carte.getNbColonnes(); col++) {
            if(carte.getCase(lig, col).getNature()==NatureTerrain.EAU){
                listeCaseEau.add(carte.getCase(lig, col));

                if(carte.voisinExiste(carte.getCase(lig, col), Direction.NORD)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.NORD).getNature() !=NatureTerrain.EAU) 
                    && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.NORD))))

                          listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.NORD));
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.SUD)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.SUD).getNature() !=NatureTerrain.EAU) 
                    && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.SUD))))

                           listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.SUD));
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.EST)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.EST).getNature() !=NatureTerrain.EAU)
                    && !(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.EST))))

                           listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.EST));
                }
                if(carte.voisinExiste(carte.getCase(lig, col),Direction.OUEST)){
                  if( (carte.getVoisin(carte.getCase(lig, col),Direction.OUEST).getNature() !=NatureTerrain.EAU)
                    &&!(listeCaseEauVosin.contains(carte.getVoisin(carte.getCase(lig, col),Direction.OUEST))))

                           listeCaseEauVosin.add(carte.getVoisin(carte.getCase(lig, col),Direction.OUEST));
                }
            }
        }
    }
  }
//******       VERSION §!!!!!!!!!!!!!!!!!!!!!!!!!!! */
public void SimulationV04(long date){
    long k = date +1;
    Case derniereposition; 
    LinkedList<Direction> listeDirection=null;
    int courtDistance = Integer.MAX_VALUE;
    Incendie procheIncendie=null;
    boolean isDispo;

    for(Graph graphs : listeGraph){
        isDispo = false;
        if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() == 0){
            goRemplir(graphs,k);
        }
        else if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() != 0){
            for(Incendie incendies : listeIncendie){
              if( (graphs.getRobot().getRobotType()=="Drone" && incendies.getLitresEau()>= graphs.getRobot().getCapActuelle()) 
                || (graphs.getRobot().getRobotType()!="Drone")  
                ){
                    if(graphs.getRobot().verifCase(incendies.getCase())){
                        Dijkstra dijkstra = new Dijkstra();
                        dijkstra.dijkstra2(graphs,graphs.getRobot().getPosition(),incendies.getCase()); 
                          if(dijkstra.getCourtDistance() < courtDistance){
                            courtDistance = dijkstra.getCourtDistance();
                            listeDirection = dijkstra.getListeDiretion(); 
                            procheIncendie = incendies;
                            isDispo = true;
                        }
                    }
                }
            }
        }
      if(isDispo){
         k++;
         for(Direction d : listeDirection){
            ajouteEvenement(new Deplacer_Robot(d,graphs.getRobot(),k,this.carte));
            k++;
          }
          ajouteEvenement(new Eteindre_Incendie(graphs.getRobot(),this.listeIncendie,procheIncendie,k,this.carte));
          k++;
          ajouteEvenement(new Liberer_Robot(graphs.getRobot(),k));
          k++;
          graphs.getRobot().setIsLibre(false);
      }          
    }
}




/**
Called every date to fill the given events
Version optimised 
 */
public void SimulationV02(long date){
    long k = date +1;
    Case derniereposition; 
    LinkedList<Direction> listeDirection=null;
    int courtDistance = Integer.MAX_VALUE;
    Graph procheGraph;
    boolean isDispo;

    for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() == 0){
            goRemplir(graphs,k);
        }
    }
    for(Incendie incendies : listeIncendie){
      isDispo = false;
      procheGraph = listeGraph.get(0);
      for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && !(incendies.getIsTaken())  && graphs.getRobot().verifCase(incendies.getCase())
          && graphs.getRobot().getCapActuelle() > 0 ) {
          Dijkstra dijkstra = new Dijkstra();
          dijkstra.dijkstra2(graphs,graphs.getRobot().getPosition(),incendies.getCase()); 
          if(dijkstra.getCourtDistance() < courtDistance){
            courtDistance = dijkstra.getCourtDistance();
            listeDirection = dijkstra.getListeDiretion(); 
            procheGraph = graphs;
            isDispo = true;
          }
        }
      }
      if(isDispo){
         incendies.setIsTaken(true);
         k++;
         for(Direction d : listeDirection){
            ajouteEvenement(new Deplacer_Robot(d,procheGraph.getRobot(),k,this.carte));
            k++;
          }
          ajouteEvenement(new Eteindre_Incendie(procheGraph.getRobot(),this.listeIncendie,incendies,k,this.carte));
          k++;
          ajouteEvenement(new Liberer_Robot(procheGraph.getRobot(),k));
          k++;
          ajouteEvenement(new Liberer_Incendie_Si(procheGraph.getRobot(),incendies,listeIncendie,k));
          procheGraph.getRobot().setIsLibre(false);
      }          
    }
  }
/**
Called every date to fill the given events
Version optimised 
 */
public void SimulationV03(long date){
    long k = date +1;
    Case derniereposition; 
    LinkedList<Direction> listeDirection=null;
    int courtDistance = Integer.MAX_VALUE;
    Incendie procheIncendie=null;
    boolean isDispo;
    for(Graph graphs : listeGraph){
        isDispo = false;
        if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() == 0){
            goRemplir(graphs,k);
        }
        else if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() != 0){
            for(Incendie incendies : listeIncendie){
              if(graphs.getRobot().verifCase(incendies.getCase())){
                   Dijkstra dijkstra = new Dijkstra();
                   dijkstra.dijkstra2(graphs,graphs.getRobot().getPosition(),incendies.getCase()); 
                    if(dijkstra.getCourtDistance() < courtDistance){
                      courtDistance = dijkstra.getCourtDistance();
                      listeDirection = dijkstra.getListeDiretion(); 
                      procheIncendie = incendies;
                      isDispo = true;
                  }
              }
            }
        }
      if(isDispo){
         k++;
         for(Direction d : listeDirection){
            ajouteEvenement(new Deplacer_Robot(d,graphs.getRobot(),k,this.carte));
            k++;
          }
          ajouteEvenement(new Eteindre_Incendie(graphs.getRobot(),this.listeIncendie,procheIncendie,k,this.carte));
          k++;
          ajouteEvenement(new Liberer_Robot(graphs.getRobot(),k));
          k++;
          graphs.getRobot().setIsLibre(false);
      }          
    }
}

/**
Called every date to fill the given events
Version not yet optimised V01
 */
  public void SimulationV01(long date){
    long k = date +1;
    Case derniereposition;
    for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && graphs.getRobot().getCapActuelle() == 0){
            goRemplir(graphs,k);
        }
    }
    for(Incendie incendies : listeIncendie){
      for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && !(incendies.getIsTaken()) && graphs.getRobot().verifCase(incendies.getCase()) ){
            incendies.setIsTaken(true);
            k++;
            if(!goEteindre(graphs,graphs.getRobot().getPosition(),incendies,k)){  
                graphs.getRobot().setIsLibre(true);
                incendies.setIsTaken(false);
            }
            else{
                break;
            }
        }
      }
    }
 }


/**
Called every date to fill the given events
Version not yet optimised V00
 */
  public void Simulation_V00(long date){
    long k = date +1;
    Case derniereposition; 
    for(Incendie incendies : listeIncendie){
      for(Graph graphs : listeGraph){
        if(graphs.getRobot().getIsLibre() && !(incendies.getIsTaken()) && graphs.getRobot().verifCase(incendies.getCase()) ){
            incendies.setIsTaken(true);
            if(graphs.getRobot().getCapActuelle() == 0){
              derniereposition = goRemplir(graphs,k);
            }
            else{
              derniereposition = graphs.getRobot().getPosition();
            }
            k++;
            if(!goEteindre(graphs,derniereposition,incendies,k)){ 
                graphs.getRobot().setIsLibre(true);
                incendies.setIsTaken(false);

            }
            else{
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

public void setListeRobot(LinkedList<Robot> listR ){
      this.listeRobot=listR;
}
public void setListeIncendie(LinkedList<Incendie> listI){
  this.listeIncendie=listI;
}
public void setListeEvenements(LinkedList<Evenement> listE){
  this.listeEvenement=listE;
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
/**
In case the robot concerned is Drone
  */
private Case plusProcheCaseEau(Graph graph ){
    Case casePlusProche=getListeCaseEau().get(0) ; // just initialising
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
/**
For all robots except Drone
 */
private Case plusProcheCaseEauVoisin( Graph graph ){
    Case casePlusProche=getListeCaseEauVosin().get(0) ; // just initialising
    int distancemin = Integer.MAX_VALUE;
    int i =0;
    for(Case c : getListeCaseEauVosin()){
      i++;
      if(graph.getRobot().verifCase(c) && i>4){
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

/**
Order to put off the fire
 */
private boolean goEteindre( Graph graph ,Case derniereposition,Incendie incendie,long k){
  Dijkstra dijkstra = new Dijkstra();
  dijkstra.dijkstra2(graph,derniereposition,incendie.getCase()); 
  graph.getRobot().setIsLibre(false);
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
/**
Order to fill the tank from the closest field of water
@k : date where the order should be started
 */
private Case goRemplir( Graph graph , long k){
  Case plusprocheCaseEau;
  graph.getRobot().setIsLibre(false);
  if(graph.getRobot().getRobotType()=="Drone"){
      plusprocheCaseEau = plusProcheCaseEau(graph);
      Dijkstra dijkstra = new Dijkstra();
      dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),plusprocheCaseEau); 
      for(Direction d : dijkstra.getListeDiretion()){
          ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
          k++;
      }
      ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));

  }
  else if(graph.getRobot().getRobotType()=="R_Pattes"){ 
    graph.getRobot().remplirReservoir(this.carte);
    plusprocheCaseEau =  graph.getRobot().getPosition();
  }
  else{
    plusprocheCaseEau = plusProcheCaseEauVoisin(graph);
    Dijkstra dijkstra = new Dijkstra();
    dijkstra.dijkstra2(graph,graph.getRobot().getPosition(),plusprocheCaseEau); 
    for(Direction d : dijkstra.getListeDiretion()){
        ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,this.carte));
        k++;
    }
    ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,this.carte));
  }
  ajouteEvenement(new Liberer_Robot(graph.getRobot(),k));
  k++;
  return plusprocheCaseEau;
}




private boolean Eteindre( Graph graph ,Case derniereposition,Incendie incendie,long k){
  Dijkstra dijkstra = new Dijkstra();
  dijkstra.dijkstra2(graph,derniereposition,incendie.getCase()); 
  graph.getRobot().setIsLibre(false);
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

}
