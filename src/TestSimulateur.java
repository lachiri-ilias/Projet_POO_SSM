import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;
import gui.ImageElement;


import robot.*;
import plan.*;
import io.*;

import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

/*
TODO :
  - Comprendre pq on ne peut pas fermer les fenetres
  - Dimensionner la fenetre en fonction du nb de lignes et colonnes de la carte
  - Ajouter un titre a la fenetre
  - Ajouter les robots et les Incendies
  - Dimensionner factor en fonction de la taille de la carte (ie fenetre)
*/

public class TestSimulateur {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
          // crée la fenêtre graphique dans laquelle dessiner
         
          // crée l'invader, en l'associant à la fenêtre graphique précédente
          // Invader invader = new Invader(gui, Color.decode("#f2ff28"));
          // TODO : ajouter les verifications (cf TestSaveDonnees)
          DonneesSimulation data = new SaveDonnees().creeDonnees(args[0]);
          int factor = data.getCarte().getTailleCases();
          int X = data.getCarte().getNbColonnes() * factor;
          int Y = data.getCarte().getNbLignes() * factor;
          GUISimulator gui = new GUISimulator(X, Y, Color.BLACK);
          Simulateur simulateur = new Simulateur(gui, data, factor);

        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}

class Simulateur implements Simulable {
    private GUISimulator gui;
    private Carte carte;
    private LinkedList<Robot> listeRobot;
    private LinkedList<Incendie> listeIncendie;
    private int factor;

    private int x_drone;
    private int y_drone;
    private Iterator<Integer> xIterator;
    private Iterator<Integer> yIterator;
    // private Iterator<Integer> xIterator;
    // private Iterator<Integer> yIterator;

    public Simulateur(GUISimulator gui, DonneesSimulation data, int f) {
        this.factor = f;
        this.gui = gui;
        this.carte = data.getCarte();
        this.listeRobot = data.getListeRobot();
        this.listeIncendie = data.getListeIncendie();
        gui.setSimulable(this);				// association a la gui!
        planCoordinates();
        draw();
    }



    private void planCoordinates() {
        // panel must be large enough... unchecked here!
        // total invader size: height == 120, width == 80
        int xMin = 0;
        int yMin = 0;
        int xMax = 600;
        int yMax = 600;
        // int xMax = gui.getWidth() - xMin ;
        // xMax -= xMax % 10;
        // int yMax = gui.getHeight() - yMin ;
        // yMax -= yMax % 10;

        // let's plan the invader displacement!
        List<Integer> xCoords = new ArrayList<Integer>();
        List<Integer> yCoords = new ArrayList<Integer>();
        // going right
        for (int x = xMin ; x <= xMax; x += 10) {
            xCoords.add(x);
            yCoords.add(yMin);
        }
        // going down
        for (int y = yMin ; y <= yMax; y += 10) {
            xCoords.add(xMax);
            yCoords.add(y);
        }
        // going left
        for (int x = xMax ; x >= xMin; x -= 10) {
            xCoords.add(x);
            yCoords.add(yMax);
        }

        this.xIterator = xCoords.iterator();
        this.yIterator = yCoords.iterator();
        // current position
        this.x_drone = xMin;
        this.y_drone = yMin;		
    }
    // @Override
    // public void next() {
    //     draw();
    // }

    // @Override
    // public void restart() {
    //     // planCoordinates();
    //     // draw();
    //     gui.reset();
    // }

    @Override
    public void next() {
        System.out.println("X  : " + this.x_drone + ";  Y : "+this.y_drone);
        if (this.xIterator.hasNext())
            this.x_drone = this.xIterator.next();		
        if (this.yIterator.hasNext())
            this.y_drone = this.yIterator.next();		
        draw();
    }

    @Override
    public void restart() {
        planCoordinates();
        draw();
    }


    /**
     * Dessine l'invader.
     */
    private Carte getCarte(){
       return this.carte;
    }
    private LinkedList<Robot> getListeRobot(){
      return this.listeRobot;
    }
    private LinkedList<Incendie> getListeIncendie(){
      return this.listeIncendie;
    }
    private void draw_2() {
         gui.addGraphicalElement(new ImageElement(x_drone,y_drone,"image/drone.png",factor,factor,gui));
    }
    private void draw() {
         gui.reset();	// clear the window

        for(int i=0; i<this.getCarte().getNbLignes();i++){
          for(int j=0; j<this.getCarte().getNbColonnes();j++){
            System.out.println("Case affichee : ligne ="+i+" colonne ="+j);
            System.out.println("Nature de la Case : "+ this.getCarte().getCase(i,j).getNature());
            switch(this.getCarte().getCase(i,j).getNature()){
              case TERRAIN_LIBRE :
                 System.out.println("libre ");
                // gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/terrain_libre.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor + (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#af601a"),factor)); break;
              case EAU :
               System.out.println("eau ");
               //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/eau.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#2e86c1"),factor)); break;
              case HABITAT :
              System.out.println("habitat ");
             // break;
               //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/maison.png",45,45,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#884ea0"),factor)); break;
              case FORET :
              System.out.println("foret ");
                //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/foret.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor)); break;
              case ROCHE :
              System.out.println("roche ");
              //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/roche.png",50,50,gui));  break;
              gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#717d7e"),factor)); break;
              default : break;
            }
          }
        }
         gui.addGraphicalElement(new ImageElement(x_drone,y_drone,"image/drone.png",factor,factor,gui));
        System.out.println("\n FIN AFFICHAGE CARTE !\n");
        

        // gui.addGraphicalElement(new Text(40,120, Color.decode("#f2ff28"), "MAP"));
    }
}
