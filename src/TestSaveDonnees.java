import robot.*;
import plan.*;
import io.*;
import donnees.*;
import incendie.*;
import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

/*
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
*/

public class TestSaveDonnees {

    public static void main(String[] args) {
        // System.out.println("ssuuii");
        // private DonneesSimulation storage;
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            DonneesSimulation storage = new SaveDonnees().creeDonnees(args[0]);
            System.out.println("\n\n \t VERIFICATION DE LA LECTURE \n\n");

            System.out.println("Il y a "+storage.getNbIncendie()+ " incendies.\n");
            System.out.println("Il y a "+storage.getNbRobot() +" robots.\n");
            System.out.println("La liste de robot est : "+storage.getListeRobot()+"\n");
            System.out.println("La liste d incendies est : "+storage.getListeIncendie()+"\n");

            System.out.println("\n \t AJOUT D UN ROBOT (DRONE) \n");
            storage.ajouteRobot(new Drone(new Case(10, 10)));
            System.out.println("La nouvelle liste de robot est : "+storage.getListeRobot()+"\n");
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }

        

    }

}
