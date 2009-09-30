package sampleproject.gui;

/** Specifies the types of connections that can be made to the server. */
public enum ConnectionType {
    /** a Serialized Objects over standard sockets based server. */
    SOCKET,
    /** an RMI based server. */
    RMI,
    /** direct connect - no network involved. */
    DIRECT
}
