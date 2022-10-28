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

public class Amiral {
  private DonneesSimulation data;
  private LinkedList<Case> listeCaseEau;

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
  }

  private LinkedList<Case> getListeCaseEau(){
    return this.listeCaseEau;
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
    creeDeplacement(robot, plusProcheCase(robot), listeEvenement, dateSimulation);
  }

  public boolean estRempli(Robot robot){
    return robot.getCapActuelle() == robot.getCapMax();
  }

  private Case plusProcheCase(Robot robot){
    Case casePlusProche = getListeCaseEau().getFirst();
    for(Case c : getListeCaseEau()){
      if(dureeDeplacement(robot, Parcours(robot, c))<dureeDeplacement(robot, Parcours(robot, casePlusProche))){
        casePlusProche = c;
      }
    }
    return casePlusProche;
  }

  public long dureeDeplacement(Robot robot, List<Case> parcours){
    int tailleCase = data.getCarte().getTailleCases();
    long compt_tps = 0;

    for(Case c : parcours){
      long vitesse =(long) (robot.getVitesseTerrain(c.getNature()) /3.6) ;// en m/s *************************
      compt_tps += tailleCase/vitesse;
    }
    return compt_tps;

  }

//  changer le next par une parcours liste simple (2 case par 2 case )
  private void creeDeplacement(Robot robot, Case c, LinkedList<Evenement> listeEvenement, long dateSimulation){
    LinkedList<Case> parcours = Parcours(robot, c);
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
    robot.setTempsFin(dateSimulation + dureeDeplacement(robot, Parcours(robot, c)));
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
    while((lr != l) && (cr != c)){
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
