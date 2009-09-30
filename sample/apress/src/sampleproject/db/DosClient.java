package sampleproject.db;

import java.util.List;
import java.io.*;

/**
 *  The main method in the DosClient class allows for a number of functions:
 *  <ul>
 *       <li> Display all Dvds in the database.</li>
 *       <li> Add a Dvd to the database. </li>
 *      <li> Remove a Dvd from the database. </li>
 *      <li> Modify an existing Dvd, by upc, in the database. </li>
 *      <li> Attempt to rent a Dvd. </li>
 *      <li> Return a rented Dvd. </li>
 * </ul>
 *
 * This simple DosClient will eventually be replaced with a SWING GUI.
 * Currently the system output is displayed to a command window.
 * System input is also entered in a command window.
 *
 * Note: This is an inherited application, and been refactored to show
 * good programming practices or to use JDK 5 features.
 *
 * @author Denny's DVDs
 * @version 1.0
 * @see sampleproject.db.DvdDatabase
 */
public class DosClient {
    /**
     * Main menu prompt for screen display.
     */
    private static final String DIRECTIONS
            = "Enter 1 for printing existing records, 2 for adding records, 3 "
            + "for modify, 4 for remove, 5 rent, 6 to return a rental, 7 to "
            + "quit, 8 to generate some dummy records";

    /**
     * Prompt displayed on screen when user is about to create a new DVD.
     */
    private static final String ENTER_DVD_MESSAGE
            = "Enter an upc, name, composer, director, leadActor, supportingActor,"
            + " strDate(ex - 12/06/1980), and number of copies for dvd to modify";

    /**
     * Entry point for our non-graphical user interface. Connects to the
     * database in the current working directory (creating one if necessary),
     * then prompts the user for actions to be performed. It should call
     * appropriate methods for each user action, however all actions are
     * performed within this one monolithic method.
     *
     * @param args command line arguments, which are ignored.
     * @throws IOException if there is a problem accessing the data file.
     */
    public static void main(String args[]) throws IOException {
        DBClient db = new DvdDatabase();
        boolean quitApp = false;

        String upc = null;
        String name = null;
        String composer = null;
        String director = null;
        String leadActor = null;
        String supportingActor = null;
        String strDate = null;
        String strCopies = null;

        DVD dvd = null;

        BufferedReader in
                = new BufferedReader(new InputStreamReader(System.in));

        while (quitApp == false) {
            System.out.println(DIRECTIONS);
            String input = in.readLine();

            switch(Integer.parseInt(input)) {
                case(1):
                    List dvds = db.getDVDs();
                    System.out.println(dvds.toString());
                    break;
                case(2):
                    System.out.println(ENTER_DVD_MESSAGE);

                    upc = in.readLine();
                    name = in.readLine();
                    composer = in.readLine();
                    director = in.readLine();
                    leadActor = in.readLine();
                    supportingActor = in.readLine();
                    strDate = in.readLine();
                    strCopies = in.readLine();

                    dvd = new DVD(upc,
                                  name,
                                  composer,
                                  director,
                                  leadActor,
                                  supportingActor,
                                  strDate,
                                  Integer.parseInt(strCopies));

                    db.addDVD(dvd);
                    break;
                case(3):
                    System.out.println(ENTER_DVD_MESSAGE);

                    upc = in.readLine();
                    name = in.readLine();
                    composer = in.readLine();
                    director = in.readLine();
                    leadActor = in.readLine();
                    supportingActor = in.readLine();
                    strDate = in.readLine();
                    strCopies = in.readLine();

                    dvd = new DVD(upc,
                                  name,
                                  composer,
                                  director,
                                  leadActor,
                                  supportingActor,
                                  strDate,
                                  Integer.parseInt(strCopies));

                    if (db.modifyDVD(dvd)) {
                        System.out.println("recod modified: " + dvd);
                    } else {
                        System.out.println("record not modified."
                                + "May not be unique: " + dvd);
                    }
                    break;
                case(4):
                        System.out.print("to remove item with upc of: " );
                        upc = in.readLine();
                        if (db.removeDVD(upc)) {
                            System.out.println("item removed!");
                        } else {
                            System.out.println("item not removed."
                                    +   "Check that upc is correct.");
                        }
                        break;
                case(5):
                    System.out.print("rent Dvd with upc of: " );
                    upc = in.readLine();
                    try {
                        if (db.reserveDVD(upc)) {
                            dvd = db.getDVD(upc);
                            if (dvd.getCopy() > 0) {
                                dvd.setCopy(dvd.getCopy() - 1);
                                db.modifyDVD(dvd);
                                System.out.println("item rented");
                            } else {
                                System.out.println("item not rented."
                                        +   "Check that upc is correct"
                                        + "and that there are available copies.");
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        db.releaseDVD(upc);
                    }
                    break;
                case(6):
                    System.out.print("return Dvd with upc of: " );
                    upc = in.readLine();
                    try {
                        if (db.reserveDVD(upc)) {
                            dvd = db.getDVD(upc);
                            dvd.setCopy(dvd.getCopy() + 1);
                            db.modifyDVD(dvd);
                            System.out.println("item returned.");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        db.releaseDVD(upc);
                    }
                    break;
                case(7):
                    System.out.println("Thanks for working with Dennys.");
                    quitApp = true;
                    break;
                case(8):
                    dvd = new DVD("794051161727",
                                  "The Hitchhiker's Guide to the Galaxy",
                                  "na",
                                  "J.W Alan",
                                  "Martin Benson",
                                  "Sandra Dickson",
                                  "1981-12-02",20);
                    db.addDVD(dvd);

                    dvd = new DVD("85392246724",
                                  "Harry Potter and the Sorcerer's Stone",
                                  "John Williams",
                                  "Chris Columbus",
                                  "Daniel Radcliffe",
                                  "Emma Watson",
                                  "2001-08-04",1);
                    db.addDVD(dvd);

                    dvd = new DVD("86162118456",
                                  "Office Space",
                                  "John Frizzell",
                                  "Mike Judge",
                                  "Jennifer Aniston",
                                  "Ron Livingston",
                                  "1999-07-16",1);
                    db.addDVD(dvd);

                    dvd = new DVD("27203882703",
                                  "Metropolis",
                                  "Thomas Newman",
                                  "Martha Coolidge",
                                  "Val Kilmer",
                                  "Gabriel Jarret",
                                  "1985-02-25",1);
                    db.addDVD(dvd);

                    dvd = new DVD("32728999302",
                                  "Edward Scissorhands",
                                  "Danny Elfman",
                                  "Tim Burton",
                                  "Johnny Depp",
                                  "Winona Ryder",
                                  "1990-02-05",1);
                    db.addDVD(dvd);

                    dvd = new DVD("78564599302",
                                  "Requiem for a Heavyweight",
                                  "Laurence Rosenthal",
                                  "Ralph Nelson",
                                  "Anthony Quinn",
                                  "Jackie Gleason",
                                  "1962-09-18",1);
                    db.addDVD(dvd);

                    dvd = new DVD("32725349302",
                                  "Night of the Ghouls",
                                  "Edward D. Wood Jr.",
                                  "Edward D. Wood Jr.",
                                  "Kenne Duncan",
                                  "Duke Moore",
                                  "1959-02-25",3);
                    db.addDVD(dvd);

                    dvd = new DVD("327254654435",
                                  "Army of Darkness",
                                  "na",
                                  "Sam Raimi",
                                  "Bruce Campbell",
                                  "Embeth Davidtz",
                                  "1993-11-03",1);
                    db.addDVD(dvd);
                break;
            }
        }
    }
}
