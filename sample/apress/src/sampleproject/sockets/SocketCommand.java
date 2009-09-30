package sampleproject.sockets;

/**
 * The enumerated list of possible commands we can send from the client to the
 * server.
 *
 * @author Denny's DVDs
 * @version 2.0
 */
public enum SocketCommand {
    /** indicates that the command object has not been set. */
    UNSPECIFIED,
    /** request will be performing a Find action. */
    FIND,
    /** renting a DVD. */
    RENT,
    /** returning a DVD. */
    RETURN,
    /** updating status of a DVD. */
    MODIFY,
    /** creating a new DVD record. */
    ADD,
    /** delete a DVD record. */
    REMOVE,
    /** retrieve a single DVD from database. */
    GET_DVD,
    /** retrieve multiple DVDs from database. */
    GET_DVDS,
    /** Reserve a DVD. */
    RESERVE,
    /** Release a DVD. */
    RELEASE
}
