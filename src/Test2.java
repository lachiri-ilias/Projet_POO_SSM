import java.awt.Color;

import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;
import gui.Text;
import gui.ImageElement;


import robot.*;
import plan.*;
import io.*;
import donnees.*;
import evenement.*;
import incendie.*;

import java.io.*;
import java.util.LinkedList;
import java.util.zip.DataFormatException;


/**  
   Cette Class a pour objetif de tester et simuler le deplacement des robots

*/
public class Test2 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }
        
        try {
          String fichier = args[0];
          DonneesSimulation data = new SaveDonnees().creeDonnees(args[0]);
          int factor = data.getCarte().getTailleCases();
          int X = data.getCarte().getNbColonnes() * factor;
          int Y = data.getCarte().getNbLignes() * factor;
          GUISimulator gui = new GUISimulator(X, Y, Color.BLACK);
          Simulateurr Simulateurr = new Simulateurr(gui, data, factor, fichier);

          
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),2,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),3,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.EST,data.getListeRobot().get(0),5,data.getCarte()));
          Simulateurr.ajouteEvenement(new Remplir_Reservoir(data.getListeRobot().get(0),6,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),7,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),8,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.SUD,data.getListeRobot().get(0),9,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),10,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),11,data.getCarte()));
          Simulateurr.ajouteEvenement(new Deplacer_Robot(Direction.OUEST,data.getListeRobot().get(0),12,data.getCarte()));
          Simulateurr.ajouteEvenement(new Eteindre_Incendie(data.getListeRobot().get(0),data.getListeIncendie(),data.getListeIncendie().get(0),13,data.getCarte()));
          
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
    private String fichier;

    public Simulateurr(GUISimulator gui, DonneesSimulation data, int f, String fichier) {
        this.factor = f;
        this.gui = gui;
        this.carte = data.getCarte();
        this.listeRobot = data.getListeRobot();
        this.listeIncendie = data.getListeIncendie();
        this.listeEvenement = new LinkedList<Evenement>();
        this.fichier = fichier;
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
        if(simulationTerminee()){
          System.out.println("Pas d'event a lancer FFIIINNN \n");
        }
        else{
          int size_liste = getListeEvenements().size();
          for(int k=0;k<size_liste;k++){
             if(getListeEvenements().get(k).getDate()==getDateSimulation()){
                getListeEvenements().get(k).execute(getDateSimulation());
                if(getListeEvenements().get(k).getisExecuted()){
                  getListeEvenements().remove(getListeEvenements().get(k));
                  size_liste --;
                  k--;
                }
                
              }
          }
          draw();
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
              String s = robots.getRobotType() + " : capacite reservoir = "+ robots.getCapActuelle();
              gui.addGraphicalElement(new Text(5*factor, 10*t, Color.decode("#FFFFFF"), s));
              t ++;
        }
    }

    private void initDraw(){
      for(int i=0; i<this.getCarte().getNbLignes();i++){
        for(int j=0; j<this.getCarte().getNbColonnes();j++){
          this.getCarte().getListToDraw().add(this.getCarte().getCase(i,j));
        }
      }
      draw();
    }

    @Override
    public void restart() {
      gui.reset();

      try {
        DonneesSimulation dataNew = new SaveDonnees().creeDonnees(fichier);
        this.listeRobot = dataNew.getListeRobot();
        this.listeIncendie = dataNew.getListeIncendie();
        this.listeEvenement = new LinkedList<Evenement>();
      } catch (FileNotFoundException e) {
          System.out.println("fichier " + fichier + " inconnu ou illisible");
      } catch (DataFormatException e) {
          System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
      }
      initDraw();
      
    }
}
