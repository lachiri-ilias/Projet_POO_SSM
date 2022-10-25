package manager;

import io.*;
import plan.*;
import evenement.*;
import robot.*;
import incendie.*;
import donnees.*;

import java.util.List;
import java.util.LinkedList;


public class Empereur {
  private DonneesSimulation data;
  private Amiral amiral;

  public Empereur(DonneesSimulation data){
    this.data = data;
    this.amiral = new Amiral(data);
  }

  public Amiral getAmiral(){
    return this.amiral;
  }

  public void ordonne(LinkedList<Evenement> listeEvenement, long dateSimulation){
    // TODO : envoyer sur un incendie le robot le + proche de l'incendie
    System.out.println("[Empereur : "+dateSimulation+ "] Ordonne est appele !\n");
    for(Incendie incendie : data.getListeIncendie()){
      /*
      for(Robot robot : data.getListeRobot()){
        if(amiral.estLibre(robot)){
          if(!amiral.estRempli(robot)){
            amiral.goRemplir(robot);
          }
          else{
            amiral.goEteindre(robot, incendie);
            break; // on sort du for sur les robots
          }
        }
      }
      */
      for(Robot robot : data.getListeRobot()){
        if(!getAmiral().estRempli(robot)){
          amiral.goRemplir(robot, listeEvenement, dateSimulation);
        }
      }
      amiral.goEteindre(incendie, listeEvenement, dateSimulation);
    }
  }

}
