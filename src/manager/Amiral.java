package manager;

import io.Direction;
import io.NatureTerrain;
import plan.*;


import evenement.*;
//import robot.*;
import donnees.*;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
// import DonneesSimulation;

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
        if(dureeDeplacement(robot, Parcours(robot, case))<dureeDeplacement(robotPlusProche, Parcours(robotPlusProche, case))){
          robotPlusProche = robot;
        }
      }
    }
    // Robot robotPlusProche = plusProcheRobot(incendie.getCase());
    creeDeplacement(robotPlusProche, incendie.getCase(),listeEvenement, dateSimulation);
    listeEvenement.add(new remplirReservoir(robotPlusProche, dateSimulation, data.getCarte()));
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
    for(Case case : data.getListeCaseEau()){
      if(dureeDeplacement(robot, Parcours(robot, case))<dureeDeplacement(robot, Parcours(robot, casePlusProche)))){
        casePlusProche = case;
      }
    }
    return case;
  }



  public long dureeDeplacement(Robot robot, LinkedList<Case> parcours){
    int tailleCase = data.getCarte().getTailleCases();
    long compt_tps = 0;

    for(Case case : parcours){
      long vitesse = robot.getVitesseTerrain(case.getNature())/3.6; // en m/s
      compt_tps += tailleCase/vitesse;
    }

    return compt_tps;

  }

  private void creeDeplacement(Robot robot, Case c, LinkedList<Evenement> listeEvenement, long dateSimulation){
    ArrayList<Case> parcours = Parcours(robot, c);
    Case bufferCase = parcours.next();
    while(parcours.hasNext()){
      Case actuelleCase = parcours.next();
      switch(actuelleCase){

          case (bufferCase.getLigne()==actuelleCase.getLigne()-1):
            listeEvenement.add(new Deplacer_Robot(Direction.SUD, robot, dateSimulation, data.getCarte()));
            break;

          case (bufferCase.getLigne()==actuelleCase.getLigne()+1):
            listeEvenement.add(new Deplacer_Robot(Direction.NORD, robot, dateSimulation, data.getCarte()));
            break;

          case (bufferCase.getColonne()==actuelleCase.getColonne()+1):
            listeEvenement.add(new Deplacer_Robot(Direction.OUEST, robot, dateSimulation, data.getCarte()));
            break;

          case (bufferCase.getColonne()==actuelleCase.getColonne()-1):
            listeEvenement.add(new Deplacer_Robot(Direction.EST, robot, dateSimulation, data.getCarte()));
            break;
      }
      bufferCase = actuelleCase;
    }

    robot.setTempsFin(dateSimulation + dureeDeplacement(robot, parcours));
  }

  private boolean estLibre(Robot robot, long dateSimulation){
    return dateSimulation >= robot.getTempsFin();
  }

  // TODO : AMELIORER GRANDEMENT CETTE FONCTION !
  private ArrayList<Case> Parcours(Robot robot, Case arrivee){
    int lr = robot.getPosition().getLigne();
    int cr = robot.getPosition().getColonne();
    int l = arrivee.getLigne();
    int c = arrivee.getColonne();
    // new Robot impossible car classe abstract !!!  //c'est normal 
    // sauvegarder sa position et l y remettre a la fin !  // oui pourquoi pas
    Robot robCopy = new Robot(robot); 
    boolean b_nord=false, b_sud=false, b_est=false, b_ouest=false;
    ArrayList<Case> parcours = new ArrayList<Case>();

    while((lr != l) && (cr != c)){
      // DEPLACEMENT HORIZONTAL
      if(lr<l && robCopy.verif_depl(Direction.SUD, data.getCarte().getCase(lr+1,cr))){
        lr++;
        parcours.add(data.getCarte().getCase(lr+1,cr));
        robCopy.deplacer(Direction.SUD, data.getCarte());
      }
      else if(lr>l && robCopy.verif_depl(Direction.NORD, data.getCarte().getCase(lr-1,cr))){
        lr--;
        parcours.add(data.getCarte().getCase(lr-1,cr));
        robCopy.deplacer(Direction.NORD, data.getCarte());
      }
      else if(lr==l && (b_est || b_ouest)){
        if(robCopy.verif_depl(Direction.SUD, data.getCarte().getCase(lr+1,cr))){
          lr++;
          parcours.add(data.getCarte().getCase(lr+1,cr));
          robCopy.deplacer(Direction.SUD, data.getCarte());
        }
        else if(robCopy.verif_depl(Direction.NORD, data.getCarte().getCase(lr-1,cr))){
          lr--;
          parcours.add(data.getCarte().getCase(lr-1,cr));
          robCopy.deplacer(Direction.NORD, data.getCarte());
        }
        else System.out.println("[Amiral.Parcours] bloquage du robot "+robot+" en ligne="+lr+" colonne="+cr);
      }

      // DEPLACEMENT VERTICAL
      if(cr<c && robCopy.verif_depl(Direction.EST, data.getCarte().getCase(lr,cr+1))){
        cr++;
        parcours.add(data.getCarte().getCase(lr,cr+1));
        robCopy.deplacer(Direction.EST, data.getCarte());
      }
      else if(cr>c && robCopy.verif_depl(Direction.OUEST, data.getCarte().getCase(lr,cr-1))){
        cr--;
        parcours.add(data.getCarte().getCase(lr,cr-1));
        robCopy.deplacer(Direction.OUEST, data.getCarte());
      }
      else if(cr==c && (b_nord || b_sud)){
        if(robCopy.verif_depl(Direction.EST, data.getCarte().getCase(lr,cr+1))){
          cr++;
          parcours.add(data.getCarte().getCase(lr,cr+1));
          robCopy.deplacer(Direction.EST, data.getCarte());
        }
        else if(robCopy.verif_depl(Direction.OUEST, data.getCarte().getCase(lr,cr-1))){
          cr--;
          parcours.add(data.getCarte().getCase(lr,cr-1));
          robCopy.deplacer(Direction.OUEST, data.getCarte());
        }
        else System.out.println("[Amiral.Parcours] bloquage du robot "+robot+" en ligne="+lr+" colonne="+cr);
      }
    }
    return parcours;
  }

}
