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
TODO :
  - Comprendre pq on ne peut pas fermer les fenetres
  - Dimensionner la fenetre en fonction du nb de lignes et colonnes de la carte
  - Ajouter un titre a la fenetre
  - Ajouter les robots et les Incendies
  - Dimensionner factor en fonction de la taille de la carte (ie fenetre)
*/

public class TestResolution {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
          String fichier = args[0];
          DonneesSimulation data = new SaveDonnees().creeDonnees(fichier);
          // DonneesSimulation dataZero = new SaveDonnees().getDataZero();

          //int factor = data.getCarte().getTailleCases()/data.getCarte().getNbColonnes();
          // int X = data.getCarte().getNbColonnes() * factor;
          // int Y = data.getCarte().getNbLignes() * factor;
          GUISimulator gui = new GUISimulator(1400, 930, Color.BLACK);
          int factor = gui.getPanelHeight()/data.getCarte().getNbLignes();
          Simulateur simulateur = new Simulateur(gui, data, factor, fichier);
          // simulateur.getChefPompier().ordonne(simulateur.getListeEvenements(), simulateur.getDateSimulation());


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
    private LinkedList<Evenement> listeEvenement;
    private int factor;
    private DonneesSimulation dataZero;
    private int factorAsset;
    private long dateSimulation;
    private ChefCurry2 chefPompier;
    private String fichier;



    public Simulateur(GUISimulator gui, DonneesSimulation data, int f, String fichier) {
        // this.dataZero = dataZero;
        this.fichier = fichier;
        this.factor = f;
        this.factorAsset = f*2;
        this.gui = gui;
        this.carte = data.getCarte();
        this.listeRobot = data.getListeRobot();
        this.listeIncendie = data.getListeIncendie();
        this.listeEvenement = new LinkedList<Evenement>();
        this.chefPompier = new ChefCurry2(data);
        gui.setSimulable(this);				// association a la gui!
        initDraw(2);
    }

    public void ajouteEvenement(Evenement e){
      this.listeEvenement.add(e);
    }
    public long getDateSimulation(){
      return this.dateSimulation;
    }
    public ChefCurry2 getChefPompier(){
      return this.chefPompier;
    }
    private void incrementeDate(){
      this.dateSimulation ++;
    }
    private boolean simulationTerminee(){
      return this.listeEvenement.isEmpty();
    }
    public Carte getCarte(){
       return this.carte;
    }

    public void setCarte(Carte resetCarte){
      this.carte = resetCarte;
    }
    public void setlisteRobot(LinkedList<Robot> resetListeRobot){
      this.listeRobot = resetListeRobot;
    }
    public void setListeIncendie(LinkedList<Incendie> resetListeIncendie){
      this.listeIncendie = resetListeIncendie;
    }

    public LinkedList<Evenement> getListeEvenements(){
      return this.listeEvenement;
    }
    public LinkedList<Incendie> getListeIncendie(){
      return this.listeIncendie;
    }
    public LinkedList<Robot> getListeRobot(){
      return this.listeRobot;
    }

    @Override
    public void next() {
        System.out.println("["+dateSimulation+"] Appel de next : la liste d'evenement est : \n"+listeEvenement);
        // incrementeDate();
        // getChefPompier().ordonne(getListeEvenements(), getDateSimulation());
        getChefPompier().Simulation(getDateSimulation());
        // System.out.println("[next] la liste fait "+ this.listeEvenement.size()+"\n");
        if(simulationTerminee()){
          System.out.println("Plus d'event a lancer FFIIINNN \n");
        }
        else{
          for(Evenement e : getListeEvenements()){
            System.out.println("["+getDateSimulation()+"] l'evenement "+ e +" est de date : "+e.getDate()+"\n");
            // System.out.println("liste devent ="+getListeEvenements());
            if(e.getDate()==getDateSimulation()){
              System.out.println("["+getDateSimulation()+"] l'evenement "+e+" s'execute !\n");
              e.execute(getDateSimulation(), gui);
              draw2();
            }
          }
      }
      incrementeDate();
    }


    @Override
    public void restart() {
        System.out.println("\n\nAPPEL DE RESET!!!\n\n");
        gui.reset();


        try {
          DonneesSimulation data = new SaveDonnees().creeDonnees(fichier);
          setCarte(data.getCarte());
          this.listeRobot = data.getListeRobot();
          this.listeIncendie = data.getListeIncendie();
          this.listeEvenement = new LinkedList<Evenement>();
          this.chefPompier = new ChefCurry2(data);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + fichier + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
        }

        // DonneesSimulation data = new SaveDonnees().creeDonnees(fichier);

        /*
        // setCarte(dataZero.getCarte()); // normalement inutile car la carte ne change pas
        dateSimulation = 0;
        System.out.println("[data] listeRobot : " + getListeRobot()+"\n");
        System.out.println("[data] listeIncendie : " + getListeIncendie()+"\n");
        System.out.println("[dataZero] listeRobot : " + dataZero.getListeRobot()+"\n");
        System.out.println("[dataZero] listeIncendie : " + dataZero.getListeIncendie()+"\n");
        setlisteRobot(dataZero.getListeRobot());
        setListeIncendie(dataZero.getListeIncendie());
        */
        draw2();
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
              // gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/roche.png",factor,factor,gui));  break;
              // gui.addGraphicalElement(new ImageElement(i*factor,j*factor,"image/3D/roche/stone_largeF_NE.png",50,50,gui));  break;
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
        for(Robot robots : getListeRobot()){
              switch(robots.getType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              System.out.println("colonne : "+robots.getPosition().getColonne()+"\tligne : "+robots.getPosition().getLigne());
        }

        System.out.println("\n FIN AFFICHAGE CARTE !\n");

    }


    private void initDraw(int k){


        for(int i=0; i<this.getCarte().getNbLignes();i++){
          for(int j=0; j<this.getCarte().getNbColonnes();j++){
            this.getCarte().getListToDraw().add(this.getCarte().getCase(i,j));
          }

        if(k==1) draw();
        else draw2();
      }


    }

    // TODO : utiliser etat pour faire des foret d'arbres et de rochers et d'habitations
    private void draw2() {
        // gui.reset();	// clear the window
        int etat;
        // for(int i=0; i<this.getCarte().getNbLignes();i++){
          // for(int j=0; j<this.getCarte().getNbColonnes();j++){
            // System.out.println("Case affichee : ligne ="+i+" colonne ="+j);
            // System.out.println("Nature de la Case : "+ this.getCarte().getCase(i,j).getNature());
          while(this.getCarte().getListToDraw().size()!=0){
            Case caseToDraw = this.getCarte().getListToDraw().removeFirst();
            int i = caseToDraw.getLigne();
            int j = caseToDraw.getColonne();
            int f = 4;
            int dec = 2;
            // gui.addGraphicalElement(new ImageElement(0,0,"image/sol/grass.png",factor*this.getCarte().getNbColonnes(),factor*this.getCarte().getNbLignes(),gui));
            etat = situation(i,j);
            switch(this.getCarte().getCase(i,j).getNature()){
              case TERRAIN_LIBRE :
                // gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#2fe6c4"),factor)); break;
                gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_grass_NE.png",factor-1,factor-1,gui));  break;
              case HABITAT :
                // gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor));
                gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_grass_NE.png",factor-1,factor-1,gui));
                // gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3,"image/3D/habitat/tent_smallOpen_NW.png",f*factor,f*factor,gui));  break;

                if(etat==0){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/habitat/tent_detailedClosed_SW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11001){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/habitat/tent_detailedClosed_NW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11100){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/habitat/tent_detailedClosed_NE.png", f*factor, f*factor, gui)); break;
                }
                if(etat==10110){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/habitat/tent_detailedOpen_SE.png", f*factor, f*factor, gui)); break;
                }
                else{
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/habitat/tent_detailedOpen_SW.png", f*factor, f*factor, gui)); break;
                }

              case FORET :
                // gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor));
                gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_grass_NE.png",factor-1,factor-1,gui));
                // gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2,"image/3D/foret/tree_blocks_dark_NE.png",f*factor,f*factor,gui));  break;

                if(etat==0){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2, "image/3D/foret/tree_tall_dark_SE.png", f*factor, f*factor, gui)); break;
                }
                if(etat==10110){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2, "image/3D/foret/tree_thin_dark_SE.png", f*factor, f*factor, gui)); break;
                }
                if(etat==10011){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2, "image/3D/foret/tree_thin_dark_SW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11001){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2, "image/3D/foret/tree_thin_dark_NW.png", f*factor, f*factor, gui)); break;
                }
                else{
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/2, "image/3D/foret/tree_thin_dark_NE.png", f*factor, f*factor, gui)); break;
                }



              case ROCHE :
                // gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#229954"),factor));
                gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_grass_NE.png",factor-1,factor-1,gui));
                // gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/statue_head_SW.png", f*factor, f*factor, gui)); break;

                if(etat==0){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/statue_head_SW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11001){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallA_SE.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11100){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallA_SW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==10110){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallB_NE.png", f*factor, f*factor, gui)); break;
                }
                if(etat==10011){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallB_NW.png", f*factor, f*factor, gui)); break;
                }
                if(etat==11111){
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallG_SE.png", f*factor, f*factor, gui)); break;
                }
                else{
                  gui.addGraphicalElement(new ImageElement(j*factor-f*factor/dec+factor/2,i*factor-f*factor/dec+factor/3, "image/3D/roche/stone_tallB_NE.png", f*factor, f*factor, gui)); break;
                }



                // gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/3D/terrain_libre/ground_grass_NW.png",factor,factor,gui));  break;
              case EAU :
                //gui.addGraphicalElement(new Rectangle(j*factor+ (factor/2), i*factor+ (factor/2), Color.decode("#000000"), Color.decode("#2e86c1"),factor)); break;
                // gui.addGraphicalElement(new ImageElement(j*factor, i*factor, "image/3D/bordu.png", factor, factor, gui)); break;


                if(etat==0){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverTile_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==11000){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverEndClosed_SE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==10100){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverEndClosed_SO.png",factor-1,factor-1,gui));  break;
                }
                if(etat==10010){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverEndClosed_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==10001){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverEndClosed_NS.png",factor-1,factor-1,gui));  break;
                }

                if(etat==11100){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCorner_EE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==11010){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverRocks_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==11001){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCorner_SE.png",factor-1,factor-1,gui));  break;
                }

                if(etat==10110){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCorner_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==10101){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverRocks_NW.png",factor-1,factor-1,gui));  break;
                }

                if(etat==10011){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCorner_NS.png",factor-1,factor-1,gui));  break;
                }

                if(etat==11110){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverSide_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==11101){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverSide_NO.png",factor-1,factor-1,gui));  break;
                }
                if(etat==11011){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverSide_SO.png",factor-1,factor-1,gui));  break;
                }

                if(etat==10111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverSide_SE.png",factor-1,factor-1,gui));  break;
                }

                if(etat==11111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverOpen_NE.png",factor-1,factor-1,gui));  break;
                }

                // SPECIALS CASE

                if(etat==21111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCrossroads.png",factor-1,factor-1,gui));  break;
                }

                if(etat==21001){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverBend_NS.png",factor-1,factor-1,gui));  break;
                }
                if(etat==21100){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverBend_NW.png",factor-1,factor-1,gui));  break;
                }
                if(etat==20110){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverBend_NN.png",factor-1,factor-1,gui));  break;
                }
                if(etat==20011){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverBend_NE.png",factor-1,factor-1,gui));  break;
                }

                if(etat==31111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCornerSmall_NW.png",factor-1,factor-1,gui));  break;
                }
                if(etat==41111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCornerSmall_NE.png",factor-1,factor-1,gui));  break;
                }
                if(etat==51111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCornerSmall_NN.png",factor-1,factor-1,gui));  break;
                }
                if(etat==61111){
                  gui.addGraphicalElement(new ImageElement(j*factor,i*factor,"image/sol/ground_riverCornerSmall_NS.png",factor-1,factor-1,gui));  break;
                }

              default : break;
            }
        //  }
        }
        /*  Incendie  */
        for(Incendie incendies : getListeIncendie()){
            //gui.addGraphicalElement(new Rectangle(( incendies.getCase().getColonne())*factor+ (factor/2), ( incendies.getCase().getLigne())*factor+ (factor/2), Color.decode("#000000"), Color.decode("#00000000"),factor));
              gui.addGraphicalElement(new ImageElement(( incendies.getCase().getColonne())*factor, incendies.getCase().getLigne()*factor-10,"image/feux.gif",factor,factor,gui));

        }
        for(Robot robots : getListeRobot()){
              switch(robots.getType()){
                  case "Drone" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" :  gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              System.out.println("colonne : "+robots.getPosition().getColonne()+"\tligne : "+robots.getPosition().getLigne());
        }

        System.out.println("\n FIN AFFICHAGE CARTE !\n");

    }

    public boolean memeNature(Case a, Case b){
      return a.getNature() == b.getNature();
    }

    public int situation(int lig, int col){
      Case caseActuelle = carte.getCase(lig, col);
      boolean voisinNord=false, voisinEst=false, voisinSud=false, voisinOuest=false;
      if(carte.voisinExiste(caseActuelle, Direction.NORD) && memeNature(caseActuelle, carte.getCase(lig-1, col))) voisinNord = true;
      if(carte.voisinExiste(caseActuelle, Direction.EST) && memeNature(caseActuelle, carte.getCase(lig, col+1))) voisinEst = true;
      if(carte.voisinExiste(caseActuelle, Direction.SUD) && memeNature(caseActuelle, carte.getCase(lig+1, col))) voisinSud = true;
      if(carte.voisinExiste(caseActuelle, Direction.OUEST) && memeNature(caseActuelle, carte.getCase(lig, col-1))) voisinOuest = true;

      // TESTS centraux
      if(voisinNord){
        if(voisinEst){
          if(voisinSud){
            if(voisinOuest){
              if(!memeNature(caseActuelle, carte.getCase(lig-1, col-1)) &&
                 !memeNature(caseActuelle, carte.getCase(lig+1, col+1)) &&
                 !memeNature(caseActuelle, carte.getCase(lig-1, col+1)) &&
                 !memeNature(caseActuelle, carte.getCase(lig+1, col-1))
                ){
                  return 21111;
                }
              else if(!memeNature(caseActuelle, carte.getCase(lig-1, col-1))) {
                return 31111;
              }
              else if(!memeNature(caseActuelle, carte.getCase(lig+1, col+1))) {
                return 41111;
              }
              else if(!memeNature(caseActuelle, carte.getCase(lig-1, col+1))) {
                return 51111;
              }
              else if(!memeNature(caseActuelle, carte.getCase(lig+1, col-1))) {
                return 61111;
              }
              return 11111;
            }
            return 11110;
          }
          if(voisinOuest){
            return 11101;
          }
          if(!memeNature(caseActuelle, carte.getCase(lig-1, col+1))) {
            return 21100;
          }
          return 11100;
        }
        if(voisinSud){
          if(voisinOuest){
            return 11011;
          }
          return 11010;
        }
        if(voisinOuest){
          if(!memeNature(caseActuelle, carte.getCase(lig-1, col-1))) {
            return 21001;
          }
          return 11001;
        }
        return 11000;
      }

      if(voisinEst){
        if(voisinSud){
          if(voisinOuest){
            return 10111;
          }
          if(!memeNature(caseActuelle, carte.getCase(lig+1, col+1))) {
            return 20110;
          }
          return 10110;
        }
        if(voisinOuest){
          return 10101;
        }
        return 10100;
      }

      if(voisinSud){
        if(voisinOuest){
          if(!memeNature(caseActuelle, carte.getCase(lig+1, col-1))) {
            return 20011;
          }
          return 10011;
        }
        return 10010;
      }

      if(voisinOuest){
        return 10001;
      }

      return 0;
  }
}


/*
Explication de la fonction situation : return | nombre de voisins de memenature | position des voisins
1er chiffre -> inutile mais obligatoire (ne peut pas commencer par 0)
2e chiffre : NORD -> 1 = OUI / 0 = NON
3e chiffre : EST
4e chiffe : SUD
5e chiffre : OUEST
0 | 0 | ø
11000 | 1 | NORD
10100 | 1 | EST
10010 | 1 | SUD
10001 | 1 | OUEST

11100 | 2 | NORD, EST
11010 | 2 | NORD, SUD
11001 | 2 | NORD, OUEST

10110 | 2 | EST, SUD
10101 | 2 | EST, OUEST

10011 | 2 | SUD, OUEST

11110 | 3 | NORD, EST, SUD
11101 | 3 | NORD, EST, OUEST
11011 | 3 | NORD, SUD, OUEST

10111 | 3 | EST, SUD, OUEST

11111 | 4 | NORD, EST, SUD, OUEST


SPECIALS

21111 | 4 | NORD, EST, SUD, OUEST

21001 | 2 | NORD, OUEST
21100 | 2 | NORD, EST
20110 | 2 | EST, SUD
20011 | 2 | SUD, OUEST

31111 | 4 | NORD, EST, SUD, OUEST haut gauche
41111 | 4 | NORD, EST, SUD, OUEST bas droit
51111 | 4 | NORD, EST, SUD, OUEST haut droit
61111 | 4 | NORD, EST, SUD, OUEST bas gauche


*/
