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
   Cette classe test le deplacement du robot, etaindre feux + remplire l'eau de maniére semi automatique .....s

*/
public class Test3 {
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

          Graph graph = new Graph(data.getCarte(),data.getListeRobot().get(0));
          k++;
          Simulateurr.ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,data.getCarte()));
          graph.dijkstra2(graph,graph.getRobot().getPosition(),data.getListeIncendie().get(0).getCase());
          for(Direction d : graph.getListeDiretion()){
            Simulateurr.ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,data.getCarte()));
            k++;
          }
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),data.getListeIncendie(),data.getListeIncendie().get(0),k,data.getCarte()));
          graph = new Graph(data.getCarte(),data.getListeRobot().get(0));
          graph.dijkstra2(graph,data.getListeIncendie().get(0).getCase(),data.getCarte().getCase(0,0)); // case de l'eau::!!!
          for(Direction d : graph.getListeDiretion()){
            Simulateurr.ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,data.getCarte()));
            k++;
          }
          Simulateurr.ajouteEvenement(new Remplir_Reservoir(graph.getRobot(),k,data.getCarte()));
          graph = new Graph(data.getCarte(),data.getListeRobot().get(0));
          graph.dijkstra2(graph,data.getCarte().getCase(0,0),data.getListeIncendie().get(1).getCase()); // dernier case du robots
          for(Direction d : graph.getListeDiretion()){
            Simulateurr.ajouteEvenement(new Deplacer_Robot(d,graph.getRobot(),k,data.getCarte()));
            k++;
          }
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(graph.getRobot(),data.getListeIncendie(),data.getListeIncendie().get(1),k,data.getCarte()));



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
        // System.out.println(" la liste est :  "+ this.listeEvenement+"\n");
        if(simulationTerminee()){
          System.out.println("Pas d'event a lancer FFIIINNN \n");
        }
        else{
          int size_liste = getListeEvenements().size();
          for(int k=0;k<size_liste;k++){
             if(getListeEvenements().get(k).getDate()==getDateSimulation()){
                System.out.println("Execution\n");
                getListeEvenements().get(k).execute(getDateSimulation());
                if(getListeEvenements().get(k).getIsExe()){
                  getListeEvenements().remove(getListeEvenements().get(k));
                  size_liste --;
                  k--;
                }
                
              }
          }
          draw();
          // for(Evenement e : getListeEvenements()){
          //   System.out.println("["+getDateSimulation()+"] l'evenement e est de date : "+e.getDate()+"\n");
          //   // System.out.println("liste devent ="+getListeEvenements());
          //   if(e.getDate()==getDateSimulation()){
          //     System.out.println("Execution\n");
          //     e.execute(getDateSimulation());
          //     getListeEvenements().remove(e);
          //     draw();
          //   }
          // }
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
         int t =1;
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
              String s =  " Incendie X : intensité = "+ incendies.getLitresEau();
              gui.addGraphicalElement(new Text(6*factor, 12*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
        
        for(Robot robots : getListeRobot()){
            // if(robots.getTmp()<100) {
            //    System.out.println("heere \n");
            //   robots.setTmp(robots.getTmp() + 10);
            // }
              switch(robots.getType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
            //  System.out.println("colonne : "+robots.getPosition().getColonne()+"\tligne : "+robots.getPosition().getLigne());
              String s = robots.getType() + " : capacite reservoire = "+ robots.getCapActuelle();
              gui.addGraphicalElement(new Text(6*factor, 12*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
   

        //System.out.println("\n FIN AFFICHAGE CARTE !\n");


        // gui.addGraphicalElement(new Text(40,120, Color.decode("#f2ff28"), "MAP"));
    }
}
