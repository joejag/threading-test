package sampleproject.gui;

import java.io.Serializable;

/**
 * Value object used to transfer information about changes to our
 * <code>ConfigOptions</code> panel to any interested observers.<p>
 *
 * Even though we don't currently intend for this value object to be sent over
 * the wire, it might be desirable in the future. Therefore this object
 * implements Serializable. It should be noted that just because it implements
 * Serializable does not imply that serialization takes place - we are safe to
 * do this without contravening any assignment instructions.
 */
public class OptionUpdate implements Serializable {
    /**
     * The enumerated list of possible updates that can be sent from the
     * ConfigOptions panel. Only one of the following options can possibly be
     * passed with this value object.
     */
    public enum Updates {
        /** The user has swapped between RMI and Sockets network options. */
        NETWORK_CHOICE_MADE,
        /**
         * The user has specified the location of the data file or the address
         * of the server.
         */
        DB_LOCATION_CHANGED,
        /**
         * The user has changed the port number the server is expected to be
         * listening on.
         */
        PORT_CHANGED;
    }

    /**
     * A version number for this class so that serialization can occur
     * without worrying about the underlying class changing between
     * serialization and deserialization.
     */
    private static final long serialVersionUID = 5165L;

    /*
     * The values that will be transfered.
     */
    private Updates updateType = null;
    private Object payload = null;

    /**
     * Empty constructor to conform with JavaBean requirements.
     */
    public OptionUpdate() {
    }

    /**
     * A far more useful constructor - one that allows us to specify the
     * update type and any relevant information.<p>
     *
     * @param updateType the tyepe of update that has occurred.
     * @param payload any relevant information that we would like to pass at
     * the same time.
     */
    public OptionUpdate(Updates updateType, Object payload) {
        this.updateType = updateType;
        this.payload = payload;
    }

    /**
     * Sets the type of update that has occurred.
     *
     * @param updateType the tyepe of update that has occurred.
     */
    public void setUpdateType(Updates updateType) {
        this.updateType = updateType;
    }

    /**
     * Gets the type of update that has occurred.
     *
     * @return the tyepe of update that has occurred.
     */
    public Updates getUpdateType() {
        return this.updateType;
    }

    /**
     * Sets any information considered relevant to this update.
     *
     * @param payload any relevant information that we would like to pass at
     * the same time.
     */
    public void getPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * Gets any information considered relevant to this update.
     *
     * @return any relevant information that we would like to pass at
     * the same time.
     */
    public Object getPayload() {
        return payload;
    }
}
