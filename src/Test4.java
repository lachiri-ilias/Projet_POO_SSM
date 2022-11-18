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
   Cette classe test le fct automatique des pompier .....

*/
public class Test4 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }
        try {
          DonneesSimulation data = new SaveDonnees().creeDonnees(args[0]);
          int factor = data.getCarte().getTailleCases()/6;
          int X = data.getCarte().getNbColonnes() * factor;
          int Y = data.getCarte().getNbLignes() * factor;
          GUISimulator gui = new GUISimulator(X, Y, Color.BLACK);
          Simulateurr Simulateurr = new Simulateurr(gui, data, factor);

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
    private ChefPompier chef;
    private int factor;
    private long dateSimulation;
    private int x_drone;
    private int y_drone;
    private Iterator<Integer> xIterator;
    private Iterator<Integer> yIterator;

    public Simulateurr(GUISimulator gui, DonneesSimulation data, int f) {
        this.factor = f;
        this.gui = gui;
        this.chef = new ChefPompier(data);
        gui.setSimulable(this);				
        draw();
    }

    
    public long getDateSimulation(){
      return this.dateSimulation;
    }
    private void incrementeDate(){
      this.dateSimulation ++;
    }
    private boolean simulationTerminee(){
      return this.chef.getListeEvenements().isEmpty() && this.chef.getListeIncendie().isEmpty();
    }


    @Override
    public void next() {
        incrementeDate();
        // c'est la version la plus optimise
        this.chef.SimulationV04(getDateSimulation()); 
        if(simulationTerminee()){
          System.out.println("Pas d'event a lancer FFIIINNN \n");
        }
        else{
          int size_liste = this.chef.getListeEvenements().size();
          for(int k=0;k<size_liste;k++){
             if(this.chef.getListeEvenements().get(k).getDate()==getDateSimulation()){
                this.chef.getListeEvenements().get(k).execute(getDateSimulation());
                if(this.chef.getListeEvenements().get(k).getisExecuted()){
                  this.chef.getListeEvenements().remove(this.chef.getListeEvenements().get(k));
                  size_liste --;
                  k--;
                }
                
              }
          }
          draw();
      }
    }

    @Override
    public void restart() {
        draw();
    }

    public Carte getCarte(){
       return this.chef.getCarte();
    }
    public LinkedList<Robot> getListeRobot(){
      return this.chef.getListeRobot();
    }
    public LinkedList<Incendie> getListeIncendie(){
      return this.chef.getListeIncendie();
    }
    private void draw() {
         gui.reset();
         int t =1;
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
        for(Incendie incendies : getListeIncendie()){
              gui.addGraphicalElement(new ImageElement(( incendies.getCase().getColonne())*factor, ( incendies.getCase().getLigne())*factor,"image/feux.gif",factor,factor,gui));
              String s =  " Incendie X : intensité = "+ incendies.getLitresEau();
              gui.addGraphicalElement(new Text(1500, 15*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
        
        for(Robot robots : getListeRobot()){
              switch(robots.getRobotType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              String s = robots.getRobotType() + " : capacite reservoire = "+ robots.getCapActuelle();
              gui.addGraphicalElement(new Text(1500, 15*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
    }
}
