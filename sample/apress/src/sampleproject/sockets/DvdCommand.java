package sampleproject.sockets;

import sampleproject.db.DVD;

/**
 * A DvdCommand object is an object used in socket implementations
 * that store data as well as the command to be executed.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public class DvdCommand implements java.io.Serializable {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * The type of action this command object represents.
     * This is the action that the DVDDatabase will execute.
     */
    private SocketCommand commandId;

    /**
     * An internal reference to a DVD object.
     */
    private DVD dvd = null;

    /**
     * For the Find action, is the regular expression used to execute the query.
     */
    private String regex = null;

    /**
     * Default constructor.
     */
    public DvdCommand() {
        this(SocketCommand.UNSPECIFIED);
    }

    /**
     * Constructor that requires the type of command to execute as a parameter.
     *
     * @param command The id of the command the server is to perform.
     */
    public DvdCommand(SocketCommand command) {
        this (command, new DVD());
    }

    /**
     * Constructor that requires the type of command and the DVD object.
     *
     * @param command The id of the command the server is to perform.
     * @param dvd the DVD object that the command will process.
     */
    public DvdCommand(SocketCommand command, DVD dvd) {
        setCommandId(command);
        this.dvd = dvd;
    }

    /**
     * Gets the query that was used for searching.
     *
     * @return The string representing the regualr expression to use in find().
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Sets the regular expression.
     *
     * @param re The regular expression to use in find().
     */
    public void setRegex(String re) {
        regex = re;
    }

    /**
     * Sets the id of the method that this object will call in the
     * <code>DBClient</code> interface.
     *
     * @param id An integer indicating the method this object will call in the
     * <code>DBClient</code> interface.
     */
    protected void setCommandId(SocketCommand id) {
        this.commandId = id;
    }

    /**
     * Retrieves the command id specified for this object.
     *
     * @return The integer representing the command id
     */
    protected SocketCommand getCommandId() {
        return commandId;
    }

    /**
     * Gets the <code>DVD</code> object contained in this class.
     *
     * @return A handle to the <code>DVD</code> member.
     */
    protected DVD getDVD() {
        return this.dvd;
    }

    /**
     * Creates a string representing this command for debugging and logging
     * purposes.
     *
     * @return a string representing this DvdCommand.
     */
    public String toString() {
        return "DvdCommand["
               + "SocketCommand: " + commandId + ", "
               + "DVD: " + dvd + ", "
               + "Regex: " + regex
               + "]";
    }
}
