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
public  class Test5 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }
        try {
          String fichier = args[0];
          DonneesSimulation data = new SaveDonnees().creeDonnees(fichier);
          GUISimulator gui = new GUISimulator(1400, 930, Color.BLACK);
          int factor = gui.getPanelHeight()/data.getCarte().getNbLignes();
          Simulateur simulateur = new Simulateur(gui, data, factor, fichier);

        /*  FIN DE SIMULATION DONNées */
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }
}



class Simulateur implements Simulable {
    private GUISimulator gui;
    private ChefCurry2 chef;
    private int factor;
    private long dateSimulation;
    private int x_drone;
    private int y_drone;
    private String fichier;
    private Carte carte;
    private LinkedList<Robot> listeRobot;
    private LinkedList<Incendie> listeIncendie;
    private LinkedList<Evenement> listeEvenement;
    private Iterator<Integer> xIterator;
    private Iterator<Integer> yIterator;

    public Simulateur(GUISimulator gui, DonneesSimulation data, int f, String fichier) {
        this.factor = f;
        this.gui = gui;
        this.fichier = fichier;
        this.chef = new ChefCurry2(data);
        this.listeRobot = chef.getListeRobot();
        this.listeIncendie = chef.getListeIncendie();
        this.listeEvenement = chef.getListeEvenements();
        this.carte = chef.getCarte();
        gui.setSimulable(this);
        initDraw();

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
    public void setCarte(Carte resetCarte){
      this.carte = resetCarte;
    }

    @Override
    public void next() {
        incrementeDate();
        this.chef.Simulation(getDateSimulation());
        if(simulationTerminee()){
          System.out.println("Pas d'event a lancer FFIIINNN \n");
        }
        else{
          int size_liste = this.chef.getListeEvenements().size();
          for(int k=0;k<size_liste;k++){
            //System.out.println("liste d'event: "+this.chef.getListeEvenements().get(k).getDate() );
             if(this.chef.getListeEvenements().get(k).getDate()==getDateSimulation()){
                this.chef.getListeEvenements().get(k).execute(getDateSimulation());
                if(this.chef.getListeEvenements().get(k).getIsExe()){
                  this.chef.getListeEvenements().remove(this.chef.getListeEvenements().get(k));
                  size_liste --;
                  k--;
                }

              }
          }
           //System.out.println("\n*********\n");
          draw2();
      }
    }

    @Override
    public void restart() {
      System.out.println("\n\nAPPEL DE RESTRAT!!!\n\n");
      gui.reset();

      try {
        System.out.println("[restart] fichier : "+fichier+"\n");
        DonneesSimulation dataNew = new SaveDonnees().creeDonnees(fichier);
        // setCarte(data.getCarte());
        this.listeRobot = dataNew.getListeRobot();
        this.listeIncendie = dataNew.getListeIncendie();
        this.listeEvenement = new LinkedList<Evenement>();
        this.chef = new ChefCurry2(dataNew);
      } catch (FileNotFoundException e) {
          System.out.println("fichier " + fichier + " inconnu ou illisible");
      } catch (DataFormatException e) {
          System.out.println("\n\t**format du fichier " + fichier + " invalide: " + e.getMessage());
      }

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
      initDraw();
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


    private void initDraw(){
      for(int i=0; i<this.getCarte().getNbLignes();i++){
        for(int j=0; j<this.getCarte().getNbColonnes();j++){
          this.getCarte().getListToDraw().add(this.getCarte().getCase(i,j));
        }
      }
      draw2();
    }

    private void draw2() {
        // gui.reset();	// clear the window
        int etat;
        // for(int i=0; i<this.getCarte().getNbLignes();i++){
          // for(int j=0; j<this.getCarte().getNbColonnes();j++){
            // System.out.println("Case affichee : ligne ="+i+" colonne ="+j);
            // System.out.println("Nature de la Case : "+ this.getCarte().getCase(i,j).getNature());
          System.out.println("[draw2] listToDraw : "+this.getCarte().getListToDraw()+"\n");
          while(this.getCarte().getListToDraw().size()!=0){

            Case caseToDraw = this.getCarte().getListToDraw().removeFirst();
            int i = caseToDraw.getLigne();
            int j = caseToDraw.getColonne();
            System.out.println("[draw2] Dessine la case ("+i+","+j+")\n");
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
              gui.addGraphicalElement(new ImageElement(( incendies.getCase().getColonne())*factor, incendies.getCase().getLigne()*factor-10,"image/feux.gif",factor,factor,gui));
        }
        for(Robot robots : getListeRobot()){
              switch(robots.getRobotType()){
                  case "Drone" :      gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/drone.png",factor,factor,gui));break;
                  case "R_Pattes" :   gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_pattes.png",factor,factor,gui));break;
                  case "R_Roue" :     gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_roue.png",factor,factor,gui));break;
                  case "R_Chenille" : gui.addGraphicalElement(new ImageElement(robots.getPosition().getColonne()*factor,robots.getPosition().getLigne()*factor,"image/r_chenille.png",factor,factor,gui));break;
              }
              System.out.println("colonne : "+robots.getPosition().getColonne()+"\tligne : "+robots.getPosition().getLigne()+"\n");
        }

        System.out.println("\n\t\t FIN AFFICHAGE CARTE !\n\n");

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
