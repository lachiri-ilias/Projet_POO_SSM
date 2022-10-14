
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestSaveDonnees {

    public static void main(String[] args) {
        // System.out.println("ssuuii");
        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            SaveDonnees.creeDonnees(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
    }

}
