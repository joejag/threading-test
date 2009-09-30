package sampleproject.gui;

import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;
import sampleproject.db.*;


/**
 * Handles all interactions between the GUI layer and the data layer.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see DBClient
 * @see GuiControllerException
 */
public class GuiController {
    /**
     * Holds a reference to the client interface of the <code>DvdMediator</code>.
     */
    private DBClient connection;

    /**
    * The Logger instance. All log messages from this class are routed through
    * this member. The Logger namespace is <code>sampleproject.gui</code>.
    */
    private Logger log = Logger.getLogger("sampleproject.gui");

    /**
     * Creates a <code>GuiController</code> instance with a specified connection
     * type.
     *
     * @param connectionType the method of connecting the client and database.
     * @param dbLocation the path to the data file, or the network address of
     * the server hosting the data file.
     * @param port the port the network server is listening on.
     * @throws GuiControllerException on communication error with database.
     */
    public GuiController(ConnectionType connectionType,
                         String dbLocation, String port)
            throws GuiControllerException {
        try{
            switch (connectionType) {
                case DIRECT:
                    connection = sampleproject.direct.DvdConnector.getLocal(dbLocation);
                    break;
                case RMI:
                    connection = sampleproject.remote.DvdConnector.getRemote(dbLocation, port);
                    break;
                case SOCKET:
                    connection = sampleproject.sockets.DvdConnector.getRemote(dbLocation, port);
                    break;
                default:
                    throw new IllegalArgumentException
                            ("Invalid connection type specified");
            }
        } catch(ClassNotFoundException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.rmi.RemoteException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.io.IOException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
    }

    /**
     * Locates a Dvd record by and UPC number.
     *
     * @param upc A String representing the UPC identifying the Dvd record.
     * @return A DvdTableModel containing all found Dvd records.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public DvdTableModel findDvd(String upc) throws GuiControllerException{
        DvdTableModel out = new DvdTableModel();
        ArrayList dvdArray = new ArrayList();
        try{
            dvdArray = (ArrayList) this.connection.findDVD(upc);
            out.addDvdRecord((DVD) dvdArray.get(0));
        }

        catch(PatternSyntaxException pse){
            log.log(Level.WARNING, pse.getMessage(), pse);
            throw new GuiControllerException(pse);
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Locates a Dvd based on a user defined search query.
     *
     * @param searchString The user defined search String
     * @return A DvdTableModel containing all Dvd records that met the search
     * criteria.
     * @throws GuiControllerException Indicates a database or network
     * level exception.
     */
    public DvdTableModel find(String searchString) throws
                                                    GuiControllerException{
        DvdTableModel out = new DvdTableModel();
        ArrayList dvdArray = new ArrayList();

        try {
            dvdArray = (ArrayList)this.connection.findDVD(searchString);
            Iterator it = dvdArray.iterator();
            while(it.hasNext()){
                out.addDvdRecord((DVD)it.next());
            }
        }
        catch(PatternSyntaxException pse){
            log.log(Level.WARNING, pse.getMessage(), pse);
            throw new GuiControllerException(pse);
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Retrieves all Dvd records from the database.
     *
     * @return A DvdTableModel containing all Dvd Records.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public DvdTableModel getDvds() throws GuiControllerException{
        DvdTableModel out = new DvdTableModel();
        ArrayList dvdArray = new ArrayList();
        try {
            dvdArray = (ArrayList)this.connection.getDVDs();
            Iterator it = dvdArray.iterator();
            while(it.hasNext()){
                out.addDvdRecord((DVD)it.next());
            }
        }
        catch(Exception e){
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        }
        return out;
    }

    /**
     * Decrements the number of Dvd's in stock identified by their UPC number.
     *
     * @param upc The UPC of the Dvd to rent.
     * @return A boolean indicating if the rent operation was successful.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public boolean rent(String upc) throws GuiControllerException{
        boolean returnValue = false;
        try {
            if (this.connection.reserveDVD(upc)) {
                DVD dvd = this.connection.getDVD(upc);
                if (dvd.getCopy() > 0) {
                    dvd.setCopy(dvd.getCopy() - 1);
                    this.connection.modifyDVD(dvd);
                    returnValue = true;
                }
            }
        } catch(InterruptedIOException ie) {
            log.log(Level.SEVERE, ie.getMessage(), ie);
            throw new GuiControllerException(ie);
        } catch(Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } finally {
            try {
                this.connection.releaseDVD(upc);
            } catch(IOException ie) {
                log.log(Level.SEVERE, ie.getMessage(), ie);
                throw new GuiControllerException(ie);
            }
        }
        return returnValue;
    }

    /**
     * Increments the number of Dvd's in stock identified by their UPC number.
     *
     * @param upc The UPC of the Dvd to return.
     * @return A boolean indicating if the return operation was successful.
     * @throws GuiControllerException Indicates a database or
     * network level exception.
     */
    public boolean returnRental(String upc) throws GuiControllerException{
        boolean returnValue = false;
        try {
            if (this.connection.reserveDVD(upc)) {
                DVD dvd = this.connection.getDVD(upc);
                dvd.setCopy(dvd.getCopy() + 1);
                this.connection.modifyDVD(dvd);
                returnValue = true;
            }
        } catch(InterruptedException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } catch(java.io.IOException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw new GuiControllerException(e);
        } finally {
            try {
                this.connection.releaseDVD(upc);
            } catch(IOException ie) {
                log.log(Level.SEVERE, ie.getMessage(), ie);
                throw new GuiControllerException(ie);
            }
        }
        return returnValue;
    }
}
