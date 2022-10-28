
import robot.*;
import plan.*;
import io.*;
import incendie.*;
import donnees.*;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;



/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affichées.
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des méthodes, inspirées de celles présentes
 * (ou non), qui CREENT les objets au moment adéquat pour construire une
 * instance de la classe DonneesSimulation à partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une méthode qui crée et retourne un objet
 * contenant toutes les données lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class SaveDonnees {
    //private DonneesSimulation data;
     public static DonneesSimulation data;

     public SaveDonnees(){
      this.data = new DonneesSimulation();
     }
    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */
    public static DonneesSimulation creeDonnees(String fichierDonnees)
        throws FileNotFoundException, DataFormatException {
        System.out.println("\n == save du fichier" + fichierDonnees);
        SaveDonnees save = new SaveDonnees(fichierDonnees);

        save.saveCarte();
        save.saveIncendies();
        save.saveRobots();
        scanner.close();
        System.out.println("\n == sauvgarde terminee");
        return data;

    }

    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private SaveDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private void saveCarte() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();

            data.getCarte().setNbLignes(nbLignes);
            data.getCarte().setNbColonnes(nbColonnes);
            data.getCarte().setTailleCases(tailleCases);
            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    saveCase(lig, col);
                }
            }
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }

    /**
     * Lit et affiche les donnees d'une case.
     */
    private void saveCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        try {
            String chaineNature = new String();
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            NatureTerrain nature = NatureTerrain.valueOf(chaineNature);
            verifieLigneTerminee();
            data.getCarte().getCase(lig,col).setNature(nature);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }
    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private void saveIncendies() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                saveIncendie(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
    }

    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private void saveIncendie(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();
            this.data.ajouteIncendie(new Incendie(intensite,new Case(lig,col)));

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private void saveRobots() throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("\nNb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                saveRobot(i);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private void saveRobot(int i) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();
            System.out.print("\t type = " + type);
            switch(type){
                case "DRONE" :  data.ajouteRobot(new Drone(new Case(lig,col))); break;
                case "ROUES" :  data.ajouteRobot(new R_Roue(new Case(lig,col))); break;
                case "CHENILLE" :  data.ajouteRobot(new R_Chenille(new Case(lig,col))); break;
                case "PATTES" :  data.ajouteRobot(new R_Pattes(new Case(lig,col))); break;
            }

            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");
            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                int vitesse = Integer.parseInt(s);
                data.getListeRobot().get(i).setVitesse(vitesse);
                System.out.print(vitesse);
            }
            verifieLigneTerminee();
            // System.out.println();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }

    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
