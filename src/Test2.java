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


/*  
    Cette classe est une class de test qui permet de verifier le fonctionment de trouver le plus court chemin
    + on donne juste le robot et la case d'arriver la fct s'encharge a trouver le chemin.

*/
public class Test2 {
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
        //   Direction d =  Direction.NORD ;
          GUISimulator gui = new GUISimulator(X, Y, Color.BLACK);
          Simulateurr Simulateurr = new Simulateurr(gui, data, factor);
        
         
          
          /* on a donner le rebot idem case de deppart : 0  et la case d'arriver : 5 qui est la case d'INcendie*/
          Graph graph = new Graph(data.getCarte(),data.getListeRobot().get(0));
          graph.dijkstra2(graph,graph.getRobot().getPosition(),data.getListeIncendie().get(0).getCase());

          Simulateurr.ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,data.getCarte()));
          k++;
          for(Direction d : graph.getListeDiretion()){
            Simulateurr.ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,data.getCarte()));
            k++;
          }
           System.out.println("la liste d'eveneent est : "+Simulateurr.getListeEvenements());
         /*   Le robot est sur la case d'Incendie on donne l'evenement d'etaindre */

        // j'ai utiliser l'ancien event Eteindre_Incendie car je devais faire des modif test event simple + Eteindre feux Adiscuter avec Robin
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),data.getListeIncendie(),data.getListeIncendie().get(0),k,data.getCarte()));

        Graph graph_drone = new Graph(data.getCarte(),data.getListeRobot().get(1));
        graph_drone.dijkstra2(graph_drone,graph_drone.getRobot().getPosition(),data.getListeIncendie().get(1).getCase());
        for(Direction d : graph_drone.getListeDiretion()){
            Simulateurr.ajouteEvenement(new Deplacer_Robot(d,graph_drone.getRobot(),z,data.getCarte()));
            z++;
          }
           System.out.println("\nSSSUUUIIII  la liste d'eveneent est : "+Simulateurr.getListeEvenements());
        Simulateurr.ajouteEvenement(new Eteindre_Incendie(graph_drone.getRobot(),data.getListeIncendie(),data.getListeIncendie().get(1),z,data.getCarte()));

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
      this.listeEvenement.add(e);
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

    // private void parcourt1() { /* parcours  */
    //     int xMin = 0;
    //     int yMin = 0;
    //     int xMax = 700;
    //     int yMax = 700;
    //     List<Integer> xCoords = new ArrayList<Integer>();
    //     List<Integer> yCoords = new ArrayList<Integer>();

    //   for (int x = xMin ; x <= xMax-400; x += 10) {
    //       xCoords.add(x);
    //       yCoords.add(yMin );
    //   }
    //     this.xIterator = xCoords.iterator();
    //     this.yIterator = yCoords.iterator();
    //     this.x_drone = xMin;
    //     this.y_drone = yMin;
    // }

    // private void parcourt(Case deppart, Case arrive){
    //   /* a copmleter */
    // }
    // private void parcourt_simple() {
    //     int xMin = 0;
    //     int yMin = 0;
    //     int xMax = 700;
    //     int yMax = 700;
    //     // int xMax = gui.getWidth() - xMin ;
    //     // xMax -= xMax % 10;
    //     // int yMax = gui.getHeight() - yMin ;
    //     // yMax -= yMax % 10;

    //     // let's plan the invader displacement!
    //     List<Integer> xCoords = new ArrayList<Integer>();
    //     List<Integer> yCoords = new ArrayList<Integer>();
    //     for(int k =0; k<8;k+=2){
    //           for (int x = xMin ; x <= xMax; x += 10) {
    //               xCoords.add(x);
    //               yCoords.add(yMin+k*100 );
    //           }
    //           for (int y = yMin + k*100 ; y <= (k+1)*100 ; y += 10) {
    //               xCoords.add(xMax);
    //               yCoords.add(y);
    //           }
    //           for (int x = xMax ; x >= xMin; x -= 10) {
    //               xCoords.add(x);
    //               yCoords.add(yMin+(k+1)*100 );
    //           }
    //           if(k!=6){
    //               for (int y = yMin + (k+1)*100 ; y <= (k+2)*100 ; y += 10) {
    //                   xCoords.add(xMin);
    //                   yCoords.add(y);
    //               }
    //           }
    //     }
    //     this.xIterator = xCoords.iterator();
    //     this.yIterator = yCoords.iterator();
    //     // current position
    //     this.x_drone = xMin;
    //     this.y_drone = yMin;
    // }

    @Override
    public void next() {
        incrementeDate();
        // System.out.println("[next] la liste fait "+ this.listeEvenement.size()+"\n");
        if(simulationTerminee()){
          System.out.println("Plus d'event a lancer FFIIINNN \n");
        }
        else{
          for(Evenement e : getListeEvenements()){
            System.out.println("["+getDateSimulation()+"] l'evenement e est de date : "+e.getDate()+"\n");
            // System.out.println("liste devent ="+getListeEvenements());
            if(e.getDate()==getDateSimulation()){
              System.out.println("Execution\n");

              e.execute(getDateSimulation());
              // getListeEvenements().remove(0);
              draw();
            }
          }
          // getListeEvenements().remove(0);
      }
    }
    // @Override
    // public void next() {
    //     if(simulationTerminee()){

    //     }
    //     System.out.println("X  : " + this.x_drone + ";  Y : "+this.y_drone);
    //     if (this.xIterator.hasNext())
    //         this.x_drone = this.xIterator.next();
    //     if (this.yIterator.hasNext())
    //         this.y_drone = this.yIterator.next();
    //     draw();

    // // **************************************************************************
    //             //   /*  COMMENT AJOUTER DES  MOUVEMENT   */
    //             //   if (!this.xIterator.hasNext()){
    //             // //  if(true){  // ajout fct verif deplacement  :  droit par exemple
    //             //       List<Integer> xCoords = new ArrayList<Integer>();
    //             //       for(int i= x_drone;i<=x_drone+100;i+=10){ // +100 : la case  +10 vitesse a modifier apres
    //             //             xCoords.add(i);
    //             //       }
    //             //       this.xIterator = xCoords.iterator();
    //             // //  }
    //             // }
    // // *****************************************************************

    // }

    @Override
    public void restart() {
        //parcourt_simple();
        draw();
    }


    /**
     * Dessine l'invader.
     */
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
            // System.out.println("Case affichee : ligne ="+i+" colonne ="+j);
            // System.out.println("Nature de la Case : "+ this.getCarte().getCase(i,j).getNature());
            switch(this.getCarte().getCase(i,j).getNature()){
              case TERRAIN_LIBRE :
                 // System.out.println("libre ");
                // gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/terrain_libre.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor + (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#af601a"),factor)); break;
              case EAU :
               // System.out.println("eau ");
               //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/eau.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#2e86c1"),factor)); break;
              case HABITAT :
              // System.out.println("habitat ");
             // break;
               //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/maison.png",45,45,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#884ea0"),factor)); break;
              case FORET :
              // System.out.println("foret ");
                //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/foret.png",50,50,gui));  break;
                gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor)); break;
              case ROCHE :
              // System.out.println("roche ");
              //gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/roche.png",50,50,gui));  break;
              gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#717d7e"),factor)); break;
              default : break;
            }
          }
        }
        /*  Incendie  */
        for(Incendie incendies : getListeIncendie()){
            //gui.addGraphicalElement(new Rectangle(( incendies.getCase().getColonne())*factor+ (factor/2), ( incendies.getCase().getLigne())*factor+ (factor/2), Color.decode("#000000"), Color.decode("#00000000"),factor));
              gui.addGraphicalElement(new ImageElement(( incendies.getCase().getColonne())*factor, ( incendies.getCase().getLigne())*factor,"image/feux.gif",factor,factor,gui));

        }
        int t =1;
        for(Robot robots : getListeRobot()){
              switch(robots.getType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              System.out.println("colonne : "+robots.getPosition().getColonne()+"\tligne : "+robots.getPosition().getLigne());
              String s = robots.getType() + " : capacite reservoire = "+ robots.getCapActuelle();
              gui.addGraphicalElement(new Text(5*factor, 10*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
   

        System.out.println("\n FIN AFFICHAGE CARTE !\n");


        // gui.addGraphicalElement(new Text(40,120, Color.decode("#f2ff28"), "MAP"));
    }
}
