package sampleproject.gui;

/** Specifies the modes the application can run in. */
public enum ApplicationMode {
    /** Application will be a standalone client - no network access. */
    STANDALONE_CLIENT,
    /**
     * A networked client via either Sockets or RMI. This is used when the
     * user has not specified any command line parameters when starting the
     * application, so we know that we are going to be making a networkwork
     * connection, but we don't yet know what sort.
     */
    NETWORK_CLIENT,
    /** The server application. */
    SERVER
}
