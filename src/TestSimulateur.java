import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;

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
          GUISimulator gui = new GUISimulator(800, 800, Color.BLACK);
          // crée l'invader, en l'associant à la fenêtre graphique précédente
          // Invader invader = new Invader(gui, Color.decode("#f2ff28"));
          // TODO : ajouter les verifications (cf TestSaveDonnees)
          DonneesSimulation data = new SaveDonnees().creeDonnees(args[0]);
          Simulateur simulateur = new Simulateur(gui, data, 80);

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
    // private Iterator<Integer> xIterator;
    // private Iterator<Integer> yIterator;

    public Simulateur(GUISimulator gui, DonneesSimulation data, int f) {
        this.factor = f;
        this.gui = gui;
        this.carte = data.getCarte();
        this.listeRobot = data.getListeRobot();
        this.listeIncendie = data.getListeIncendie();
        gui.setSimulable(this);				// association a la gui!
        // planCoordinates();
        draw();
    }

    /**
     * Programme les déplacements de l'invader.

    private void planCoordinates() {
        // panel must be large enough... unchecked here!
        // total invader size: height == 120, width == 80
        int xMin = 60;
        int yMin = 40;
        int xMax = gui.getWidth() - xMin - 80;
        xMax -= xMax % 10;
        int yMax = gui.getHeight() - yMin - 120;
        yMax -= yMax % 10;

        // let's plan the invader displacement!
        List<Integer> xCoords = new ArrayList<Integer>();
        List<Integer> yCoords = new ArrayList<Integer>();
        // going right
        for (int x = xMin + 10; x <= xMax; x += 10) {
            xCoords.add(x);
            yCoords.add(yMin);
        }
        // going down
        for (int y = yMin + 10; y <= xMin + 150; y += 10) {
            xCoords.add(xMax);
            yCoords.add(y);
        }
        // going left
        for (int x = xMax - 10; x >= xMin; x -= 10) {
            xCoords.add(x);
            yCoords.add(yMin + 170);
        }

        this.xIterator = xCoords.iterator();
        this.yIterator = yCoords.iterator();
        // current position
        this.x = xMin;
        this.y = yMin;
    }
    */
    @Override
    public void next() {
        draw();
    }

    @Override
    public void restart() {
        // planCoordinates();
        // draw();
        gui.reset();
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

    private void draw() {
        // gui.reset();	// clear the window

        for(int i=0; i<this.getCarte().getNbColonnes();i++){
          for(int j=0; j<this.getCarte().getNbLignes();j++){
            System.out.println("Case affichee : ligne ="+i+" colonne ="+j);
            switch(this.getCarte().getCase(i,j).getNature()){
              case TERRAIN_LIBRE :
                gui.addGraphicalElement(new Rectangle(i*factor, j*factor, Color.decode("#af601a"), Color.decode("#af601a"), factor)); break;
              case EAU :
                gui.addGraphicalElement(new Rectangle(i*factor, j*factor, Color.decode("#2e86c1"), Color.decode("#2e86c1"), factor)); break;
              case HABITAT :
                gui.addGraphicalElement(new Rectangle(i*factor, j*factor, Color.decode("#884ea0"), Color.decode("#884ea0"), factor)); break;
              case FORET :
                gui.addGraphicalElement(new Rectangle(i*factor, j*factor, Color.decode("#229954"), Color.decode("#229954"), factor)); break;
              case ROCHE :
                gui.addGraphicalElement(new Rectangle(i*factor, j*factor, Color.decode("#717d7e"), Color.decode("#717d7e"), factor)); break;
            }
          }
        }
        System.out.println("\n FIN AFFICHAGE CARTE !\n");
        // gui.close;

        // gui.addGraphicalElement(new Text(40,120, Color.decode("#f2ff28"), "MAP"));
    }
}
