package sampleproject.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Logger;
import java.util.Observable;
import javax.swing.*;


/**
 * A common panel used by both the client and server application to specify the
 * configuration options. For the most part these configuration options are the
 * same for both client and server (even if used in different ways). For example
 * the port the server will listen on must be the same port the client will use
 * to connect to the server. So it makes sense to have a common panel so that
 * users can be presented with a familiar layout for all 3 application modes.
 */
public class ConfigOptions extends JPanel {
    // All strings are defined in final static declarations at the start of the
    // class. This will make localisation easier later (if we want to add it).
    // Note that localisation is not required for certification.
    private static final String DB_LOCATION_LABEL = "Database location: ";
    private static final String SERVER_PORT_LABEL = "Server port: ";

    private static final String DB_HD_LOCATION_TOOL_TIP
            = "The location of the database on an accessible hard drive";
    private static final String DB_IP_LOCATION_TOOL_TIP
            = "The server where the database is located (IP address)";
    private static final String SERVER_PORT_TOOL_TIP
            = "The port number the Server uses to listens for requests";

    private static final String DATABASE_EXTENSION = "dvd";
    private static final String DATABASE_FILE_CHOOSER_DESCRIPTION
            = "Database files (*." + DATABASE_EXTENSION + ")";

    private static final String SOCKET_SERVER_TEXT = "Socket server";
    private static final String RMI_SERVER_TEXT = "RMI server";

    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     *
     * Not that we ever serialize this class of course, but JPanel implements
     * Serializable, so therefore by default we do as well.
     */
    private static final long serialVersionUID = 5165L;

    /**
     * An Observable class so interested users of this class can receive
     * automatic updates whenever user options change.
     */
    private ConfigObservable observerConfigOptions = new ConfigObservable();

    // All user modifiable fields are defined here, along with all buttons.
    // This makes it easy to disable the fields and buttons once the
    // configuration is complete
    private JTextField locationField = new JTextField(40);
    private JButton browseButton = new JButton("...");
    private JTextField portNumber = new PositiveIntegerField(5);
    private JRadioButton socketOption = new JRadioButton(SOCKET_SERVER_TEXT);
    private JRadioButton rmiOption = new JRadioButton(RMI_SERVER_TEXT);

    private String location = null;
    private String port = null;
    private ApplicationMode applicationMode = ApplicationMode.STANDALONE_CLIENT;

    /**
     * The Logger instance. All log messages from this class are routed through
     * this member. The Logger namespace is <code>sampleproject.gui</code>.
     */
    private Logger log = Logger.getLogger("sampleproject.gui");

    /**
     * Creates a new instance of ConfigOptions - the panel for configuring
     * database connectivity.
     *
     * @param applicationMode one of <code>STANDALONE_CLIENT_MODE</code>,
     * <code>NETWORK_CLIENT_MODE</code>, or <code>SERVER_MODE</code>.
     * @see ApplicationMode
     */
    public ConfigOptions(ApplicationMode applicationMode) {
        super();
        this.applicationMode = applicationMode;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(gridbag);

        // Standard options
        // ensure there is always a gap between components
        constraints.insets = new Insets(2, 2, 2, 2);


        // Build the Data file location row
        JLabel dbLocationLabel = new JLabel(DB_LOCATION_LABEL);
        gridbag.setConstraints(dbLocationLabel, constraints);
        this.add(dbLocationLabel);

        if (applicationMode == ApplicationMode.NETWORK_CLIENT) {
            locationField.setToolTipText(DB_IP_LOCATION_TOOL_TIP);
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        } else {
            locationField.setToolTipText(DB_HD_LOCATION_TOOL_TIP);
            //next-to-last in row
            constraints.gridwidth = GridBagConstraints.RELATIVE;
        }
        locationField.addFocusListener(new ActionHandler());
        locationField.setName(DB_LOCATION_LABEL);
        gridbag.setConstraints(locationField, constraints);
        this.add(locationField);

        if ((applicationMode == ApplicationMode.SERVER)
                || (applicationMode == ApplicationMode.STANDALONE_CLIENT)) {
            browseButton.addActionListener(new BrowseForDatabase());
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            gridbag.setConstraints(browseButton, constraints);
            this.add(browseButton);
        }


        if ((applicationMode == ApplicationMode.SERVER)
                || (applicationMode == ApplicationMode.NETWORK_CLIENT)) {
            // Build the Server port row if applicable
            constraints.weightx = 0.0;

            JLabel serverPortLabel = new JLabel(SERVER_PORT_LABEL);
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(serverPortLabel, constraints);
            this.add(serverPortLabel);

            portNumber.addFocusListener(new ActionHandler());
            portNumber.setToolTipText(SERVER_PORT_TOOL_TIP);
            portNumber.setName(SERVER_PORT_LABEL);
            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            constraints.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(portNumber, constraints);
            this.add(portNumber);

            // Build the Server type option row 1 if applicable
            constraints.weightx = 0.0;

            JLabel serverTypeLabel = new JLabel("Server Type: ");
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.EAST;
            gridbag.setConstraints(serverTypeLabel, constraints);
            this.add(serverTypeLabel);

            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            constraints.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(socketOption, constraints);
            socketOption.setActionCommand(SOCKET_SERVER_TEXT);
            socketOption.addActionListener(new ActionHandler());
            this.add(socketOption);

            // Build the Server type option row 2 if applicable
            constraints.weightx = 0.0;

            constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
            constraints.anchor = GridBagConstraints.WEST;
            constraints.gridx = 1;
            gridbag.setConstraints(rmiOption, constraints);
            rmiOption.addActionListener(new ActionHandler());
            rmiOption.setActionCommand(RMI_SERVER_TEXT);
            this.add(rmiOption);

            ButtonGroup serverTypesGroup = new ButtonGroup();
            serverTypesGroup.add(socketOption);
            serverTypesGroup.add(rmiOption);
        }
    }

    /**
     * Utility method to inform our observers of any changes the user makes to
     * the parameters on screen.
     *
     * @param updateType Enum specify which field has changed
     * @param payLoad the new data the user just entered.
     */
    private void updateObservers(OptionUpdate.Updates updateType,
                                 Object payLoad) {
        OptionUpdate update = new OptionUpdate(updateType, payLoad);
        observerConfigOptions.setChanged();
        observerConfigOptions.notifyObservers(update);
    }

    /**
     * A utility class to handle user interactions with the panel. These are
     * not processed by the user of this panel, rather they are processed
     * internally, and an update is then sent to any observers.
     */
    private class ActionHandler implements ActionListener, FocusListener {
        /** {@inheritDoc} */
        public void actionPerformed(ActionEvent ae) {
            if (SOCKET_SERVER_TEXT.equals(ae.getActionCommand())) {
                updateObservers(OptionUpdate.Updates.NETWORK_CHOICE_MADE,
                                ConnectionType.SOCKET);
            }

            if (RMI_SERVER_TEXT.equals(ae.getActionCommand())) {
                updateObservers(OptionUpdate.Updates.NETWORK_CHOICE_MADE,
                                ConnectionType.RMI);
            }
        }

        /** {@inheritDoc} */
        public void focusGained(FocusEvent e) {
            // ignored - we don't do anything special when users enter a field
        }

        /** {@inheritDoc} */
        public void focusLost(FocusEvent e) {
            if (DB_LOCATION_LABEL.equals(e.getComponent().getName())
                    && (!locationField.getText().equals(location))) {
                location = locationField.getText();
                updateObservers(OptionUpdate.Updates.DB_LOCATION_CHANGED,
                                location.trim());
            }

            if (SERVER_PORT_LABEL.equals(e.getComponent().getName())
                    && (!portNumber.getText().equals(port))) {
                port = portNumber.getText();
                updateObservers(OptionUpdate.Updates.PORT_CHANGED, port.trim());
            }
        }
    }

    /**
     * A utility class that provides the user with the ability to browse for
     * the database rather than forcing them to remember (and type in) a fully
     * qualified database location.
     */
    private class BrowseForDatabase implements ActionListener {
        /** {@inheritDoc} */
        public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser
                = new JFileChooser(System.getProperty("user.dir"));
            chooser.addChoosableFileFilter(
                new javax.swing.filechooser.FileFilter() {
                    /**
                     * display files ending in ".db" or any other object
                     * (directory or other selectable device).
                     */
                    public boolean accept(File f) {
                        if (f.isFile()) {
                            return f.getName().endsWith(DATABASE_EXTENSION);
                        } else {
                            return true;
                        }
                    }

                    /**
                     * provide a description for the types of files we are
                     * allowing to be selected.
                     */
                    public String getDescription() {
                        return DATABASE_FILE_CHOOSER_DESCRIPTION;
                    }
                }
            );

            // if the user selected a file, update the file name on screen
            if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
                locationField.setText(chooser.getSelectedFile().toString());
                location = locationField.getText();
                updateObservers(OptionUpdate.Updates.DB_LOCATION_CHANGED,
                                location.trim());
            }
        }
    }

    /**
     * Returns the mode the application will be running in
     * (networked, standalone, etc).
     * @return the mode the application will be running in
     * (networked, standalone, etc).
     * @see ApplicationMode
     */
    public ApplicationMode getApplicationMode() {
        return this.applicationMode;
    }

    /**
     * Returns the contents of the database location field.
     * @return the contents of the database location field.
     */
    public String getLocationFieldText() {
        return locationField.getText();
    }

    /**
     * Sets the contents of the database location field.
     *
     * @param locationField the contents of the database location field.
     */
    public void setLocationFieldText(String locationField) {
        location = locationField;
        this.locationField.setText(locationField);
    }

    /**
     * Configures whether the location field is enabled or not.
     *
     * @param enabled true if the location field is enabled.
     */
    public void setLocationFieldEnabled(boolean enabled) {
        this.locationField.setEnabled(enabled);
    }

    /**
     * Configures whether the browse button is enabled or not.
     *
     * @param enabled true if the browse button is enabled.
     */
    public void setBrowseButtonEnabled(boolean enabled) {
        this.browseButton.setEnabled(enabled);
    }

    /**
     * Returns the contents of the port number text field.
     *
     * @return the contents of the port number text field.
     */
    public String getPortNumberText() {
        return portNumber.getText();
    }

    /**
     * Sets the contents of the port number text field.
     *
     * @param portNumber the contents of the port number text field.
     */
    public void setPortNumberText(String portNumber) {
        port = portNumber;
        this.portNumber.setText(portNumber);
    }

    /**
     * Configures whether the port number field is enabled or not.
     *
     * @param enabled true if the port number field is enabled.
     */
    public void setPortNumberEnabled(boolean enabled) {
        this.portNumber.setEnabled(enabled);
    }

    /**
     * Returns the type of network connection the user has chosen.
     *
     * @return the type of network connection the user has chosen.
     */
    public ConnectionType getNetworkConnection() {
        return (socketOption.isSelected()
                ? ConnectionType.SOCKET
                : ConnectionType.RMI);
    }

    /**
     * Sets the type of network connection to be used by default.
     *
     * @param connectionType the type of connection to be used.
     */
    public void setNetworkConnection(ConnectionType connectionType) {
        switch (connectionType) {
            case SOCKET:
                socketOption.setSelected(true);
                break;
            case RMI:
                rmiOption.setSelected(true);
                break;
            default:
                log.warning("Trying to set unknown connection type: "
                            + connectionType);
                break;
        }
    }

    /**
     * Configures whether the socket option radio button is enabled or not.
     *
     * @param enabled true if the socket option radio button is enabled.
     */
    public void setSocketOptionEnabled(boolean enabled) {
        this.socketOption.setEnabled(enabled);
    }

    /**
     * Configures whether the RMI option radio button is enabled or not.
     *
     * @param enabled true if the RMI option radio button is enabled.
     */
    public void setRmiOptionEnabled(boolean enabled) {
        this.rmiOption.setEnabled(enabled);
    }


    /**
     * Our Observabe class - a class that Observers can register themselves
     * with in order to recieve updates as things change within this panel.
     */
    private class ConfigObservable extends Observable {
        /** {@inheritDoc} */
        public void setChanged() {
            super.setChanged();
        }
    }

    /**
     * Returns an instance of the <code>Observable</code> class. Observers can
     * register themselves with this class in order to recieve updates as
     * things change within this panel.
     *
     * @return an instance of the <code>Observable</code> class.
     */
    public Observable getObservable() {
        return observerConfigOptions;
    }
}
