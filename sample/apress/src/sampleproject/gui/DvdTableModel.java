package sampleproject.gui;

import java.util.*;
import java.util.logging.*;
import javax.swing.table.*;
import sampleproject.db.*;

/**
 * The custom table model used by the <code>MainWindow</code> instance.
 *
 * @author Denny's DVDs
 * @version 2.0
 * @see sampleproject.gui.MainWindow
 */
public class DvdTableModel extends AbstractTableModel {
    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     *
     * Not that we ever serialize this class of course, but AbstractTableModel
     * implements Serializable, so therefore by default we do as well.
     */
    private static final long serialVersionUID = 5165L;

   /**
    * The Logger instance. All log messages from this class are routed through
    * this member. The Logger namespace is <code>sampleproject.gui</code>.
    */
    private Logger log = Logger.getLogger("sampleproject.gui");

    /**
     * An array of <code>String</code> objects representing the table headers.
     */
    private String [] headerNames = {"UPC", "Movie Title", "Director",
                                    "Lead Actor", "Supporting Actor",
                                    "Composer", "Copies in Stock"};

    /**
     * Holds all Dvd instances displayed in the main table.
     */
    private ArrayList<String[]> dvdRecords = new ArrayList<String[]>(5);

    /**
     * Returns the column count of the table.
     *
     * @return An integer indicating the number or columns in the table.
     */
    public int getColumnCount() {
        return this.headerNames.length;
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return An integer indicating the number of rows in the table.
     */
    public int getRowCount() {
        return this.dvdRecords.size();
    }

    /**
     * Gets a value from a specified index in the table.
     *
     * @param row An integer representing the row index.
     * @param column An integer representing the column index.
     * @return The object located at the specified row and column.
     */
    public Object getValueAt(int row, int column) {
        String[] rowValues = this.dvdRecords.get(row);
        return rowValues[column];
    }

    /**
     * Sets the cell value at a specified index.
     *
     * @param obj The object that is placed in the table cell.
     * @param row The row index.
     * @param column The column index.
     */
    public void setValueAt(Object obj, int row, int column) {
        Object[] rowValues = this.dvdRecords.get(row);
        rowValues[column] = obj;
    }

    /**
     * Returns the name of a column at a given column index.
     *
     * @param column The specified column index.
     * @return A String containing the column name.
     */
    public String getColumnName(int column) {
        return headerNames[column];
    }

    /**
     * Given a row and column index, indicates if a table cell can be edited.
     *
     * @param row Specified row index.
     * @param column Specified column index.
     * @return A boolean indicating if a cell is editable.
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Adds a row of Dvd data to the table.
     *
     * @param upc The Dvd upc.
     * @param name The name of the movie.
     * @param director The movie's director.
     * @param leadActor The movie's lead actor.
     * @param supportingActor The movie's supporting actor.
     * @param composer The movie's composer.
     * @param numberOfCopies The number of DvdDvdDvds in stock.
     */
    public void addDvdRecord(String upc, String name, String director,
                             String leadActor, String supportingActor,
                             String composer, int numberOfCopies) {

        String [] temp = {upc, name, director, leadActor, supportingActor,
                            composer, Integer.toString(numberOfCopies)};
        this.dvdRecords.add(temp);
    }

    /**
     * Adds a Dvd object to the table.
     *
     * @param dvd The Dvd object to add to the table.
     */
    public void addDvdRecord(DVD dvd) {
        addDvdRecord(dvd.getUPC(), dvd.getName(), dvd.getDirector(),
                     dvd.getLeadActor(), dvd.getSupportingActor(),
                     dvd.getComposer(), dvd.getCopy());
    }
}
