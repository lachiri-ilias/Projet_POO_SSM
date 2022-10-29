package manager;

import io.Direction;
import io.NatureTerrain;
import plan.*;
import evenement.*;
import robot.*;
import donnees.*;
import incendie.*;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


/*
TODO : gros soucis sur le temps d'une opération :
dans cette classe on a acces a la duree d'un deplacement complet
OR le Robot ne doit etre occupe qu'a partir du moment ou on appelle
execute de la classe Evenement SAUF qu'a ce moment la on n'a plus acces
au temps global que va mettre le robot pour effectuer son operation
*/

/*
Renommer verif_depl en verifCase !!! et mettre en parametre uniquement la case
*/

public class Amiral {
  private DonneesSimulation data;
  private LinkedList<Case> listeCaseEau;
  private Graph graphDrone;
  private Graph graphR_Chenille;
  private Graph graphR_Pattes;
  private Graph graphR_Roue;

  public Amiral(DonneesSimulation data){
    this.data = data;
    this.listeCaseEau = new LinkedList<Case>();
    for (int lig = 0; lig < data.getCarte().getNbLignes(); lig++) {
        for (int col = 0; col < data.getCarte().getNbColonnes(); col++) {
            if(data.getCarte().getCase(lig, col).getNature()==NatureTerrain.EAU){
              listeCaseEau.add(data.getCarte().getCase(lig, col));
            }
        }
    }
    // Construction des graph
    graphDrone = makeGraph("Drone");
    graphR_Chenille = makeGraph("R_Chenille");
    graphR_Pattes = makeGraph("R_Pattes");
    graphR_Roue = makeGraph("R_Roue");
  }

  private LinkedList<Case> getListeCaseEau(){
    return this.listeCaseEau;
  }

  private Graph makeGraph(String type){
    Graph graphSpecial = new Graph();
    Carte carte = data.getCarte();
    int tailleCase = data.getCarte().getTailleCases();
    Case caseij, voisin;

    // On pourrait facilement compacter tout ca
    // soucis : mettre type (de type String le convertir en ??? pour l'utiliser comme une classe)

    /*
    remplissage du graph :
    Si la case est accessible par le robot -> ajout de la case
    Verification pour chaque direction :
      1) que la case existe
      2) que cette case est accessible par notre robot
      -> ajout de la connection entre notre robot et la case
    */
    switch(type){

      case "Drone" :
        for(int i=0; i<carte.getNbLignes();i++){
          for(int j=0; j<carte.getNbColonnes();j++){
            // pas de condition particuliere
            caseij = carte.getCase(i,j);
            graphSpecial.addVertex(caseij);
            if(carte.voisinExiste(caseij, Direction.NORD) && Drone.verifCase(carte.getVoisin(Direction.NORD,caseij))){
              voisin = carte.getCase(i-1,j);
              tempsCaseDepart = tailleCase/((long) (Drone.getVitesseTerrain(caseij.getNature()) /3.6));
              tempsCaseDestination = tailleCase/((long) (Drone.getVitesseTerrain(voisin.getNature()) /3.6));
              graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
            }
            if(carte.voisinExiste(caseij, Direction.SUD) && Drone.verifCase(carte.getVoisin(Direction.SUD,caseij))){
              voisin = carte.getCase(i+1,j);
              tempsCaseDepart = tailleCase/((long) (Drone.getVitesseTerrain(caseij.getNature()) /3.6));
              tempsCaseDestination = tailleCase/((long) (Drone.getVitesseTerrain(voisin.getNature()) /3.6));
              graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
            }
            if(carte.voisinExiste(caseij, Direction.EST) && Drone.verifCase(carte.getVoisin(Direction.EST,caseij))){
              voisin = carte.getCase(i,j+1);
              tempsCaseDepart = tailleCase/((long) (Drone.getVitesseTerrain(caseij.getNature()) /3.6));
              tempsCaseDestination = tailleCase/((long) (Drone.getVitesseTerrain(voisin.getNature()) /3.6));
              graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
            }
            if(carte.voisinExiste(caseij, Direction.OUEST) && Drone.verifCase(carte.getVoisin(Direction.OUEST,caseij))){
              voisin = carte.getCase(i,j-1);
              tempsCaseDepart = tailleCase/((long) (Drone.getVitesseTerrain(caseij.getNature()) /3.6));
              tempsCaseDestination = tailleCase/((long) (Drone.getVitesseTerrain(voisin.getNature()) /3.6));
              graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
            }
          }
        }
        break;

      case "R_Chenille" :
        for(int i=0; i<carte.getNbLignes();i++){
          for(int j=0; j<carte.getNbColonnes();j++){
            // pas de condition particuliere
            caseij = carte.getCase(i,j);
            // si la ligne ci dessous ne marche pas mettre new R_chenille.(etc)
            if(R_Chenille.verifCase(caseij){ // regarder le commentaire ligne 27
              graphSpecial.addVertex(caseij);
              if(carte.voisinExiste(caseij, Direction.NORD) && R_Chenille.verifCase(carte.getVoisin(Direction.NORD,caseij))){
                voisin = carte.getCase(i-1,j);
                tempsCaseDepart = tailleCase/((long) (R_Chenille.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Chenille.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.SUD) && R_Chenille.verifCase(carte.getVoisin(Direction.SUD,caseij))){
                voisin = carte.getCase(i+1,j);
                tempsCaseDepart = tailleCase/((long) (R_Chenille.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Chenille.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.EST) && R_Chenille.verifCase(carte.getVoisin(Direction.EST,caseij))){
                voisin = carte.getCase(i,j+1);
                tempsCaseDepart = tailleCase/((long) (R_Chenille.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Chenille.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.OUEST) && R_Chenille.verifCase(carte.getVoisin(Direction.OUEST,caseij))){
                voisin = carte.getCase(i,j-1);
                tempsCaseDepart = tailleCase/((long) (R_Chenille.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Chenille.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
            }
          }
        }
        break;

      case "R_Pattes" :
        for(int i=0; i<carte.getNbLignes();i++){
          for(int j=0; j<carte.getNbColonnes();j++){
            // pas de condition particuliere
            caseij = carte.getCase(i,j);
            if(R_Pattes.verif_depl(Direction.NORD, caseij){ // regarder le commentaire ligne 27
              graphSpecial.addVertex(caseij);
              if(carte.voisinExiste(caseij, Direction.NORD) && R_Pattes.verifCase(carte.getVoisin(Direction.NORD,caseij))){
                voisin = carte.getCase(i-1,j);
                tempsCaseDepart = tailleCase/((long) (R_Pattes.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Pattes.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.SUD) && R_Pattes.verifCase(carte.getVoisin(Direction.SUD,caseij))){
                voisin = carte.getCase(i+1,j);
                tempsCaseDepart = tailleCase/((long) (R_Pattes.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Pattes.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.EST) && R_Pattes.verifCase(carte.getVoisin(Direction.EST,caseij))){
                voisin = carte.getCase(i,j+1);
                tempsCaseDepart = tailleCase/((long) (R_Pattes.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Pattes.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.OUEST) && R_Pattes.verifCase(carte.getVoisin(Direction.OUEST,caseij))){
                voisin = carte.getCase(i,j-1);
                tempsCaseDepart = tailleCase/((long) (R_Pattes.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Pattes.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
            }
          }
        }
        break;

      case "R_Roue" :
        for(int i=0; i<carte.getNbLignes();i++){
          for(int j=0; j<carte.getNbColonnes();j++){
            // pas de condition particuliere
            caseij = carte.getCase(i,j);
            if(R_Roue.verif_depl(Direction.NORD, caseij){ // regarder le commentaire ligne 27
              graphSpecial.addVertex(caseij);
              if(carte.voisinExiste(caseij, Direction.NORD) && R_Roue.verifCase(carte.getVoisin(Direction.NORD,caseij)){
                voisin = carte.getCase(i-1,j);
                tempsCaseDepart = tailleCase/((long) (R_Roue.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Roue.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.SUD) && R_Roue.verifCase(carte.getVoisin(Direction.SUD,caseij)){
                voisin = carte.getCase(i+1,j);
                tempsCaseDepart = tailleCase/((long) (R_Roue.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Roue.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.EST) && R_Roue.verifCase(carte.getVoisin(Direction.EST,caseij)){
                voisin = carte.getCase(i,j+1);
                tempsCaseDepart = tailleCase/((long) (R_Roue.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Roue.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
              if(carte.voisinExiste(caseij, Direction.OUEST) && R_Roue.verifCase(carte.getVoisin(Direction.OUEST,caseij)){
                voisin = carte.getCase(i,j-1);
                tempsCaseDepart = tailleCase/((long) (R_Roue.getVitesseTerrain(caseij.getNature()) /3.6));
                tempsCaseDestination = tailleCase/((long) (R_Roue.getVitesseTerrain(voisin.getNature()) /3.6));
                graphSpecial.addEdge(caseij, voisin, tempsCaseDepart, tempsCaseDestination);
              }
            }
          }
        }
        break;
    }
    /* // A ESSAYER (type mis a la place d'un switch/case)
    for(int i=0; i<carte.getNbLignes();i++){
      for(int j=0; j<carte.getNbColonnes();j++){
        // pas de condition particuliere
        Case caseij = carte.getCase(i,j);
        if(type.verif_depl(Direction.NORD, caseij) // regarder le commentaire ligne 27
          graphSpecial.addVertex(caseij);
        if(carte.voisinExiste(caseij, Direction.NORD)){
          graphSpecial.addEdge(caseij, carte.getCase(i-1,j));
        }
        if(carte.voisinExiste(caseij, Direction.SUD)){
          graphSpecial.addEdge(caseij, carte.getCase(i+1,j));
        }
        if(carte.voisinExiste(caseij, Direction.EST)){
          graphSpecial.addEdge(caseij, carte.getCase(i,j+1));
        }
        if(carte.voisinExiste(caseij, Direction.OUEST)){
          adjVertices.addEdge(caseij, carte.getCase(i,j-1));
        }
      }
    }
    */
    return specialGraph;
  }

  public void goEteindre(Incendie incendie, LinkedList<Evenement> listeEvenement, long dateSimulation){
    Robot robotPlusProche = data.getListeRobot().getFirst();
    for(Robot robot : data.getListeRobot()){
      if(estLibre(robot, dateSimulation)){
        if(dureeDeplacement(robot, Parcours(robot, incendie.getCase()))<dureeDeplacement(robotPlusProche, Parcours(robotPlusProche, incendie.getCase()))){  // ICI case PAR incendie.getCase()
          robotPlusProche = robot;
        }
      }
    }
    // Robot robotPlusProche = plusProcheRobot(incendie.getCase());
    creeDeplacement(robotPlusProche, incendie.getCase(),listeEvenement, dateSimulation);
    listeEvenement.add(new Remplir_Reservoir(robotPlusProche, dateSimulation, data.getCarte()));
  }

  /*
  public Robot plusProcheRobot(Case case, long dateSimulation){
    Robot robotPlusProche = data.getListeRobot().getFirst();
    for(Robot robot : data.getListeRobot()){
      if(estLibre(robot, dateSimulation)){
        if(dureeDeplacement(robot, Parcours(robot, case))<dureeDeplacement(robotPlusProche, Parcours(robotPlusProche, case))){
          robotPlusProche = robot;
        }
      }
    }
    return robotPlusProche;
  }
  */

  public void goRemplir(Robot robot, LinkedList<Evenement> listeEvenement, long dateSimulation){
    creeDeplacement(robot, plusProcheCaseEau(robot), listeEvenement, dateSimulation);
  }

  public boolean estRempli(Robot robot){
    return robot.getCapActuelle() == robot.getCapMax();
  }

  private Case plusProcheCaseEau(Robot robot){
    Case casePlusProche = getListeCaseEau().getFirst();
    for(Case c : getListeCaseEau()){
      if(dureeDeplacement(robot, Parcours(robot, c))<dureeDeplacement(robot, Parcours(robot, casePlusProche))){
        casePlusProche = c;
      }
    }
    return casePlusProche;
  }

  public long dureeDeplacement(Robot robot, LinkedList<Case> parcours){
    int tailleCase = data.getCarte().getTailleCases();
    long compt_tps = 0;

    for(Case c : parcours){
      long vitesse =(long) (robot.getVitesseTerrain(c.getNature()) /3.6) ;// en m/s *************************
      compt_tps += tailleCase/vitesse;
    }
    return compt_tps;

  }

//  changer le next par une parcours liste simple (2 case par 2 case )
  private void creeDeplacement(Robot robot, Case DestinationCase, LinkedList<Evenement> listeEvenement, long dateSimulation){
    LinkedList<Case> parcours = Parcours(robot, DestinationCase);
    Case bufferCase, actuelleCase;
    for(int i=1; i<parcours.size(); i++){
      bufferCase = parcours.get(i-1);
      actuelleCase = parcours.get(i);
      if(bufferCase.getLigne()==actuelleCase.getLigne()-1){
        listeEvenement.add(new Deplacer_Robot(Direction.SUD, robot, dateSimulation, data.getCarte()));
        System.out.println("["+dateSimulation+"] AJOUT event Deplacement SUD du robot : "+robot);
      }
      else if (bufferCase.getLigne()==actuelleCase.getLigne()+1){
        listeEvenement.add(new Deplacer_Robot(Direction.NORD, robot, dateSimulation, data.getCarte()));
        System.out.println("["+dateSimulation+"] AJOUT event Deplacement NORD du robot : "+robot);
      }
      else if  (bufferCase.getColonne()==actuelleCase.getColonne()+1){
        listeEvenement.add(new Deplacer_Robot(Direction.OUEST, robot, dateSimulation, data.getCarte()));
        System.out.println("["+dateSimulation+"] AJOUT event Deplacement OUEST du robot : "+robot);
      }
      else if  (bufferCase.getColonne()==actuelleCase.getColonne()-1){
        listeEvenement.add(new Deplacer_Robot(Direction.EST, robot, dateSimulation, data.getCarte()));
        System.out.println("["+dateSimulation+"] AJOUT event Deplacement EST du robot : "+robot);
      }
    }
    /*
    while(parcours.hasNext()){
      Case actuelleCase = parcours.next();
      if(bufferCase.getLigne()==actuelleCase.getLigne()-1){
            listeEvenement.add(new Deplacer_Robot(Direction.SUD, robot, dateSimulation, data.getCarte()));
          }
      else if (bufferCase.getLigne()==actuelleCase.getLigne()+1){
            listeEvenement.add(new Deplacer_Robot(Direction.NORD, robot, dateSimulation, data.getCarte()));
          }
      else if  (bufferCase.getColonne()==actuelleCase.getColonne()+1){
        listeEvenement.add(new Deplacer_Robot(Direction.OUEST, robot, dateSimulation, data.getCarte()));
      }
      else if  (bufferCase.getColonne()==actuelleCase.getColonne()-1){
        listeEvenement.add(new Deplacer_Robot(Direction.EST, robot, dateSimulation, data.getCarte()));
      }

      bufferCase = actuelleCase;
    }
    */
   // robot.setTempsFin(dateSimulation + dureeDeplacement(robot, parcours));  // A DISCUTER !!!!!
    robot.setTempsFin(dateSimulation + dureeDeplacement(robot, Parcours(robot, DestinationCase)));
  }

  private boolean estLibre(Robot robot, long dateSimulation){
    return dateSimulation >= robot.getTempsFin();
  }

  // TODO : AMELIORER GRANDEMENT CETTE FONCTION !
  private LinkedList<Case> Parcours(Robot robot, Case arrivee){
    int lr = robot.getPosition().getLigne();
    int cr = robot.getPosition().getColonne();
    int l = arrivee.getLigne();
    int c = arrivee.getColonne();
    // new Robot impossible car classe abstract !!!  //c'est normal
    // sauvegarder sa position et l y remettre a la fin !  // oui pourquoi pas
    Case init_pos = robot.getPosition();
    //Robot robCopy = new Robot(robot);
    boolean b_nord=false, b_sud=false, b_est=false, b_ouest=false;
    LinkedList<Case> parcours = new LinkedList<Case>();
    // while((lr != l) && (cr != c)){
    for(int i=0; i<2; i++){
      // DEPLACEMENT HORIZONTAL
      if(lr<l && robot.verif_depl(Direction.SUD, data.getCarte().getCase(lr+1,cr))){
        lr++;
        parcours.add(data.getCarte().getCase(lr+1,cr));
        robot.deplacer(Direction.SUD, data.getCarte());
      }
      else if(lr>l && robot.verif_depl(Direction.NORD, data.getCarte().getCase(lr-1,cr))){
        lr--;
        parcours.add(data.getCarte().getCase(lr-1,cr));
        robot.deplacer(Direction.NORD, data.getCarte());
      }
      else if(lr==l && (b_est || b_ouest)){
        if(robot.verif_depl(Direction.SUD, data.getCarte().getCase(lr+1,cr))){
          lr++;
          parcours.add(data.getCarte().getCase(lr+1,cr));
          robot.deplacer(Direction.SUD, data.getCarte());
        }
        else if(robot.verif_depl(Direction.NORD, data.getCarte().getCase(lr-1,cr))){
          lr--;
          parcours.add(data.getCarte().getCase(lr-1,cr));
          robot.deplacer(Direction.NORD, data.getCarte());
        }
        else System.out.println("[Amiral.Parcours] bloquage du robot "+robot+" en ligne="+lr+" colonne="+cr);
      }

      // DEPLACEMENT VERTICAL
      if(cr<c && robot.verif_depl(Direction.EST, data.getCarte().getCase(lr,cr+1))){
        cr++;
        parcours.add(data.getCarte().getCase(lr,cr+1));
        robot.deplacer(Direction.EST, data.getCarte());
      }
      else if(cr>c && robot.verif_depl(Direction.OUEST, data.getCarte().getCase(lr,cr-1))){
        cr--;
        parcours.add(data.getCarte().getCase(lr,cr-1));
        robot.deplacer(Direction.OUEST, data.getCarte());
      }
      else if(cr==c && (b_nord || b_sud)){
        if(robot.verif_depl(Direction.EST, data.getCarte().getCase(lr,cr+1))){
          cr++;
          parcours.add(data.getCarte().getCase(lr,cr+1));
          robot.deplacer(Direction.EST, data.getCarte());
        }
        else if(robot.verif_depl(Direction.OUEST, data.getCarte().getCase(lr,cr-1))){
          cr--;
          parcours.add(data.getCarte().getCase(lr,cr-1));
          robot.deplacer(Direction.OUEST, data.getCarte());
        }
        else System.out.println("[Amiral.Parcours] bloquage du robot "+robot+" en ligne="+lr+" colonne="+cr);
      }
    }
    robot.getPosition().setLigne(init_pos.getLigne());
    robot.getPosition().setColonne(init_pos.getColonne());
    return parcours;
  }

}
