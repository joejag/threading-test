package sampleproject.db;

import java.io.*;
import java.util.*;
import java.util.logging.*;

/**A DVD object is a representation of DVD data.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see sampleproject.db.DvdDatabase
 */
public class DVD implements Serializable {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    // The maximum size of each of the fields used to define a Dvd title.
    // These should be checked in the setters and constructors for this
    // class, but have been left out to reduce unnecessary code.

    /**
     * The size of the Universal Product Code for the Dvd.
     */
    static final int UPC_LENGTH = 12;

    /**
     * The maximum length of the title we will store.
     */
    static final int NAME_LENGTH = 50;

    /**
     * The maximum length of the composer's name we will store.
     */
    static final int COMPOSER_LENGTH = 25;

    /**
     * The maximum length of the Director's name we will store.
     */
    static final int DIRECTOR_LENGTH = 25;

    /**
     * The maximum length of the Lead Actor's name we will store.
     */
    static final int LEAD_ACTOR_LENGTH = 25;

    /**
     * The maximum length of the Supporting Actor's name we will store.
     */
    static final int SUPPORTING_ACTOR_LENGTH = 25;

    /**
     * The size of the year the movie was released information in yyyy-mm-dd
     * form.
     */
    static final int YEAR_LENGTH = 10;

    /**
     * The size of the field for containing how many copies of movies are
     * stored. We have arbitrarily chosen 2 digits for this field, giving a
     * maximum of 99 copies of any given Dvd in store at any given time. This
     * potentially could cause data corruption if more than 99 records are
     * returned, however handling this problem is not shown in this code.
     */
    static final int COPIES_LENGTH = 2;


    /**
     * The size of a complete record in the database. Calculated by adding all
     * the previous fields together. Knowing this makes it easy to work with
     * an entire block of data at a time (rather than reading individual
     * fields), reducing the time we need to block on database access.
     */
    static final int RECORD_LENGTH = UPC_LENGTH
                                   + NAME_LENGTH
                                   + COMPOSER_LENGTH
                                   + DIRECTOR_LENGTH
                                   + LEAD_ACTOR_LENGTH
                                   + SUPPORTING_ACTOR_LENGTH
                                   + YEAR_LENGTH
                                   + COPIES_LENGTH;

    /**
     * Stores the Universal Product Code for the Dvd.
     * This number uniquely identifies a Dvd.
     */
    private String upc = ""; // The record UPC identifier

    /**
     * Stores the title name of the movie.
     */
    private String name = ""; // Holds the movie title

    /**
     * Stores the name of the composer of the movie's soundtrack
     * as a single <code>String</code> containing first and last name.
     */
    private String composer = ""; // The music composer

    /**
     * Stores the name of the movie's director as a single <code>String</code>
     * containing first and last name.
     */
    private String director = ""; // The director's name

    /**
     * Stores the name of the movie's main actor as a single <code>String</code>
     * containing first and last name.
     */
    private String leadActor = ""; // The lead actor's name

    /**
     * Stores the name of the movie's supporting actor as a single
     * <code>String</code> containing first and last name.
     */
    private String supportingActor = ""; // The supporting actor's name

    /**
     * Stores the release date of the film in yyyy-mm-dd format.
     */
    private String year = ""; // The movie's release date

    /**
     * An integer value that indicates the number of copies of this Dvd that are
     * currently in stock.
     */
    private int copy = 1; // The number of Dvds in stock

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.db</code>.
     */
    private transient Logger log = Logger.getLogger("sampleproject.db");

    /**
     * Creates an instance of this object with default values.
     */
    public DVD() {
        log.finer("DVD empty constructor called");
    }

    /**
     * Creates an instance of this object with a specified list of
     * initial values. Assumes initial number of copies is 1.
     *
     * @param upc Holds the UPC value of the Dvd.
     * @param name Holds the title of the Dvd.
     * @param composer Holds the name of the movie's composer.
     * @param director Holds the name of the movie's director.
     * @param lead Holds the name of the movie's leading actor.
     * @param supportActor Holds the name of the movie's
     * supporting actor.
     * @param year The date the of the movie's release (yyyy-mm-dd).
     */
    public DVD(String upc, String name, String composer,
               String director, String lead, String supportActor,
               String year) {
        this(upc, name, composer, director, lead, supportActor, year, 1);
    }

    /**
     * Creates an instance of this object with a specified list of initial
     * values.
     *
     * @param upc The UPC value of the Dvd.
     * @param name The title of the Dvd.
     * @param composer The name of the movie's composer.
     * @param director The name of the movie's director.
     * @param leadActor The name of the movie's leading actor.
     * @param supportActor The name of the movie's supporting actor.
     * @param year The date the of the movie's release (yyyy-mm-dd).
     * @param copies The number of copies in stock.
     */
    public DVD(String upc, String name, String composer, String director,
               String leadActor, String supportActor, String year,
               int copies) {
        log.entering("DVD", "DVD",
                     new Object[]{upc, name, composer, director, leadActor,
                                  supportingActor, year, copies});
        this.upc = upc;
        this.name = name;
        this.composer = composer;
        this.director = director;
        this.leadActor = leadActor;
        this.supportingActor = supportActor;
        this.year = year;
        this.copy = copies;
        log.exiting("DVD", "DVD");
    }

    /**
     * Returns the first and last name of the movie's composer.
     *
     * @return A <code>String</code> containing the first and last name of the
     * movie's composer.
     */
    public String getComposer() {
        log.entering("DVD", "getComposer");
        log.exiting("DVD", "getComposer", this.composer);
        return this.composer;
    }

    /**
     * Sets the first and last name of the movie's composer.
     *
     * @param composer A <code>String</code> containing the
     * first and last name of the movie's composer.
     */
    public void setComposer(String composer) {
        log.entering("DVD", "setComposer", composer);
        this.composer = composer;
        log.exiting("DVD", "setComposer", this.composer);
    }

    /**
    * Returns The number of DVDs currently available.
    *
    * @return An <code>int</code> count of the number of available copies of
    * this DVD.
    */
    public int getCopy() {
        log.entering("DVD", "getCopy");
        log.exiting("DVD", "getCopy", this.copy);
        return this.copy;
    }

    /**
     * Sets the number of available DVDs.
     *
     * @param copy The new amount of DVDs available.
     */
    public void setCopy(int copy) {
        log.entering("DVD", "setCopy", copy);
        this.copy = copy;
        log.exiting("DVD", "setCopy", this.copy);
    }

    /**
     * Returns the first and last name of the movie's director.
     *
     * @return A <code>String</code> containing the first and last name of the
     * movie's director.
     */
    public String getDirector() {
        log.entering("DVD", "getDirector");
        log.exiting("DVD", "getDirector", this.director);
        return this.director;
    }

    /**
     * Sets the first and last name of the movie's director.
     *
     * @param director A <code>String</code> containing the first and
     * last name of the movie's director.
     */
    public void setDirector(String director) {
        log.entering("DVD", "setDirector", director);
        this.director = director;
        log.exiting("DVD", "setDirector", this.director);
    }

    /**
     * Returns the DVD's Universal Product Code as a <code>String</code>.
     *
     * @return A <code>String</code> containing the Dvd's UPC.
     * identification code.
     */
    public String getUPC() {
        log.entering("DVD", "getUPC");
        log.exiting("DVD", "getUPC", this.upc);
        return this.upc;
    }

    /**
     * Sets the Dvd's Universal Product Code identification code.
     *
     * @param upc A <code>String</code> containing the Dvd's upc
     * identification code.
     */
    public void setUPC(String upc) {
        log.entering("DVD", "setUPC", upc);
        this.upc = upc;
        log.exiting("DVD", "setUPC", this.upc);
    }

    /**
     * Returns the first and last name of the movie's lead actor.
     *
     * @return A <code>String</code> containing the first and last name of the
     * movie's lead actor.
     */
    public String getLeadActor() {
        log.entering("DVD", "getLeadActor");
        log.exiting("DVD", "getLeadActor", this.leadActor);
        return this.leadActor;
    }

    /**
     * Sets the first and last name of the movie's lead actor.
     *
     * @param leadActor A <code>String</code> containing the first and last
     *  name of the movie's lead actor.
     */
    public void setLeadActor(String leadActor) {
        log.entering("DVD", "setLeadActor", leadActor);
        this.leadActor = leadActor;
        log.exiting("DVD", "setLeadActor", this.leadActor);
    }

    /**
     * Returns this Dvd title.
     *
     * @return A <code>String</code> containing the title of this Dvd.
     */
    public String getName() {
        log.entering("DVD", "getName");
        log.exiting("DVD", "getName", this.name);
        return this.name;
    }

    /**
     * Sets this Dvd's title.
     *
     * @param name A <code>String</code> containing this Dvd's title.
     */
    public void setName(String name) {
        log.entering("DVD", "setName", name);
        this.name = name;
        log.exiting("DVD", "setName", this.name);
    }

    /**
     * Returns the first and last name of the movie's supporting actor.
     *
     * @return A <code>String</code> containing the first and last name of the
     * movie's supporting actor.
     */
    public String getSupportingActor() {
        log.entering("DVD", "getSupportingActor");
        log.exiting("DVD", "getSupportingActor", this.supportingActor);
        return this.supportingActor;
    }

    /**
     * Sets the first and last name of the movie's supporting actor.
     *
     * @param supportingActor A <code>String</code> containing the first and
     * last name of the movie's supporting actor.
     */
    public void setSupportingActor(String supportingActor) {
        log.entering("DVD", "setSupportingActor", supportingActor);
        this.supportingActor = supportingActor;
        log.exiting("DVD", "setSupportingActor", this.supportingActor);
    }

    /**
     * Returns the date of release.
     *
     * @return A <code>String</code> representing the month, day, and year of
     * the movie's release.
     */
    public String getYear() {
        log.entering("DVD", "getYear");
        log.exiting("DVD", "getYear", this.year.toString());
        return year;
    }

    /**
     * Sets the release date of the movie.
     *
     * @param year A <code>String</code> containing the movie's release date.
     */
    public void setYear(String year) {
        log.entering("DVD", "setYear", year);
        this.year = year;
        log.exiting("DVD", "setYear", this.year);
    }

    /**
     * Sets this Dvd's availability status.
     *
     * @param renting <code>boolean</code> dvdRented indicates if this DVD.
     * should rented(true).or returned(false).
     * @return boolean indicating whether the DVD operation was successful;
     */
    public boolean setRented(boolean renting) {
        log.entering("DVD", "setRented", "renting: " + renting);
        boolean retVal = false;

        //handle copy count. Don't rent out more DVDs then we have
        if (!renting) {
            this.copy++;
            retVal = true;
        } else if (this.copy > 0) {
            this.copy--;
            retVal = true;
        }

        log.info("Copies in stock = " + this.copy);
        log.exiting("DVD", "setRented", "rented: " + retVal);
        return retVal;
    }

    /**
     * Checks whether two DVD objects are the same by comparing their UPC.
     * Since a UPC is unique per DVD, if the two UPCs are the same, the two
     * DVD records must be the same object. If we could not use the UPC alone
     * to compare two DVD records, we might have to compare more fields within
     * the two records.
     *
     * @param aDvd the DVD to compare with this DVD.
     * @return true if this DVD and the other DVD have the same UPC.
     */
    public boolean equals(Object aDvd) {
        if (!(aDvd instanceof DVD)) {
            return false;
        }

        DVD otherDvd = (DVD) aDvd;

        return (upc == null) ? (otherDvd.getUPC() == null)
                             : upc.equals(otherDvd.getUPC());
    }

    /**
     * Returns a hashcode for this DVD object that should be reasonably
     * unique amongst DVD objects. As with the <code>equals</code> method,
     * we know that the UPC will be unique amongst DVD records, so all we
     * need do is return the UPC hashcode. If the UPC did not provide a
     * reasonably unique value, we might consider generating a more unique
     * hashcode (possibly by adding all the hashcodes of all the values
     * in this object).
     *
     * @return the hashcode for this instance of DVD.
     */
    public int hashCode() {
        return upc.hashCode();
    }

    /**
     * Returns a <code>String</code> representation of the DVD class.
     *
     * @return A <code>String</code> representation of the DVD class.
     */
    public String toString() {
        String retVal = "["
                      + this.upc + "; "
                      + this.name + "; "
                      + this.director + "; "
                      + this.composer + "; "
                      + this.leadActor + "; "
                      + this.supportingActor + "; "
                      + this.copy
                      + "]";

        return retVal;
    }

    /**
     * When de-serializing this object, make sure we have a logger.
     * Note that the method signature must match <b>exactly</b>.
     *
     * @param in the stream to read data from in order to restore the object
     * @throws IOException if I/O errors occur
     * @throws ClassNotFoundException If the class for an object being
     * restored cannot be found.
     */
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        log = Logger.getLogger("sampleproject.db"); // Log output
        in.defaultReadObject(); // call the standard deserializer
    }
}
