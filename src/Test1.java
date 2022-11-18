import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;
import gui.ImageElement;

import manager.*;
import robot.*;
import plan.*;
import io.*;
import donnees.*;
import evenement.*;
import incendie.*;

import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.util.zip.DataFormatException;


/**  
   Cette Class a pour objetif de tester et simuler le deplacement des robots

*/
public class Test1 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }
        
        try {
          DonneesSimulation data = new SaveDonnees().creeDonnees(args[0]);
          int factor = data.getCarte().getTailleCases();
          int X = data.getCarte().getNbColonnes() * factor;
          int Y = data.getCarte().getNbLignes() * factor;
          int k = 1,z=1;
          GUISimulator gui = new GUISimulator(X, Y, Color.BLACK);
          Simulateurr Simulateurr = new Simulateurr(gui, data, factor);

          Simulateurr.ajouteEvenement(new Remplir_Reservoir(data.getListeRobot().get(0),1,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),2,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),3,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),4,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),5,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),7,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),8,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),9,data.getCarte()));
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(data.getListeRobot().get(0),data.getListeIncendie(),data.getListeIncendie().get(0),10,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),14,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),15,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),16,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),17,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),18,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),19,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),20,data.getCarte()));
         
          Simulateurr.ajouteEvenement(new Remplir_Reservoir(data.getListeRobot().get(0),21,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),22,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),23,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),24,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),25,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),27,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),28,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),29,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),30,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.NORD,data.getListeRobot().get(0),31,data.getCarte()));
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(data.getListeRobot().get(0),data.getListeIncendie(),data.getListeIncendie().get(1),33,data.getCarte()));

        /*  FIN DE SIMULATION DONNées */ 
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}



class Simulateurr implements Simulable {
    private GUISimulator gui;
    private Carte carte;
    private LinkedList<Robot> listeRobot;
    private LinkedList<Incendie> listeIncendie;
    private LinkedList<Evenement> listeEvenement;
    private int factor;
    private long dateSimulation;
    private int x_drone;
    private int y_drone;
    private Iterator<Integer> xIterator;
    private Iterator<Integer> yIterator;

    public Simulateurr(GUISimulator gui, DonneesSimulation data, int f) {
        this.factor = f;
        this.gui = gui;
        this.carte = data.getCarte();
        this.listeRobot = data.getListeRobot();
        this.listeIncendie = data.getListeIncendie();
        this.listeEvenement = new LinkedList<Evenement>();
        gui.setSimulable(this);				// association a la gui!
        draw();
    }

    public void ajouteEvenement(Evenement e){
      this.listeEvenement.addLast(e);
    }
    public long getDateSimulation(){
      return this.dateSimulation;
    }
    private void incrementeDate(){
      this.dateSimulation ++;
    }
    private boolean simulationTerminee(){
      return this.listeEvenement.isEmpty();
    }
    public LinkedList<Evenement> getListeEvenements(){
      return this.listeEvenement;
    }


    @Override
    public void next() {
        incrementeDate();
         System.out.println(" la liste est :  "+ this.listeEvenement+"\n");
        if(simulationTerminee()){
          System.out.println("Pas d'event a lancer FFIIINNN \n");
        }
        else{
          int size_liste = getListeEvenements().size();
          for(int k=0;k<size_liste;k++){
             if(getListeEvenements().get(k).getDate()==getDateSimulation()){
                System.out.println("Execution\n");
                getListeEvenements().get(k).execute(getDateSimulation());
                if(getListeEvenements().get(k).getisExecuted()){
                  getListeEvenements().remove(getListeEvenements().get(k));
                  size_liste --;
                  k--;
                }
                
              }
          }
          draw();
        ents().remove(0);
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
    private void draw_2() {
         gui.addGraphicalElement(new ImageElement(x_drone,y_drone,"image/drone.png",factor,factor,gui));
    }
    private void draw() {
         gui.reset();	// clear the window
        for(int i=0; i<this.getCarte().getNbLignes();i++){
          for(int j=0; j<this.getCarte().getNbColonnes();j++){
            switch(this.getCarte().getCase(i,j).getNature()){
              case TERRAIN_LIBRE :
                gui.addGraphicalElement(new Rectangle(j*factor + (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#af601a"),factor)); break;
              case EAU :
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#2e86c1"),factor)); break;
              case HABITAT :
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#884ea0"),factor)); break;
              case FORET :
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor)); break;
              case ROCHE :
              gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#717d7e"),factor)); break;
              default : break;
            }
          }
        }
        /*  Incendie  */
        for(Incendie incendies : getListeIncendie()){
              gui.addGraphicalElement(new ImageElement(( incendies.getCase().getColonne())*factor, ( incendies.getCase().getLigne())*factor,"image/feux.gif",factor,factor,gui));

        }
        int t = 1;
        for(Robot robots : getListeRobot()){
              switch(robots.getRobotType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              String s = robots.getRobotType() + " : capacite reservoire = "+ robots.getCapActuelle();
              gui.addGraphicalElement(new Text(5*factor, 10*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
    }
}
