package suncertify.db;

public interface DB
{
  /**
   * Reads a record from the file. Returns an array where each
   * element is a record value.
   *
   * @param recNo recordNumber
   * @return String array of line in file
   * @throws RecordNotFoundException if invalid record number is given
   */
  public String[] read(int recNo) throws RecordNotFoundException;

  /**
   * Modifies the fields of a record. The new value for field n
   * appears in data[n]. Throws SecurityException
   * if the record is locked with a cookie other than lockCookie.
   *
   * @param recNo      recordNumber
   * @param data       updated data
   * @param lockCookie lock value
   * @throws SecurityException       if lock not in place
   * @throws RecordNotFoundException if record does not exist
   */
  public void update(int recNo, String[] data, long lockCookie)
          throws RecordNotFoundException, SecurityException;

  /**
   * Deletes a record, making the record number and associated disk
   * storage available for reuse.
   * Throws SecurityException if the record is locked with a cookie
   * other than lockCookie.
   *
   * @param recNo      recordNumber
   * @param lockCookie lock value
   * @throws SecurityException       if lock not in place
   * @throws RecordNotFoundException if record does not exist
   */
  public void delete(int recNo, long lockCookie)
          throws RecordNotFoundException, SecurityException;

  /**
   * Returns an array of record numbers that match the specified
   * criteria. Field n in the database file is described by
   * criteria[n]. A null value in criteria[n] matches any field
   * value. A non-null  value in criteria[n] matches any field
   * value that begins with criteria[n]. (For example, "Fred"
   * matches "Fred" or "Freddy".)
   *
   * @param criteria search substrings
   * @return record ids
   */
  public int[] find(String[] criteria);

  /**
   * Creates a new record in the database (possibly reusing a
   * deleted entry). Inserts the given data, and returns the record
   * number of the new record.
   *
   * @param data String array of line in file
   * @return lock value
   * @throws DuplicateKeyException if row already exists
   */
  public int create(String[] data) throws DuplicateKeyException;

  /**
   * Locks a record so that it can only be updated or deleted by this client.
   * Returned value is a cookie that must be used when the record is unlocked,
   * updated, or deleted. If the specified record is already locked by a different
   * client, the current thread gives up the CPU and consumes no CPU cycles until
   * the record is unlocked.
   *
   * @param recNo recordNumber
   * @return local number
   * @throws RecordNotFoundException if record does not exist
   */
  public long lock(int recNo) throws RecordNotFoundException;

  /**
   * Releases the lock on a record. Cookie must be the cookie
   * returned when the record was locked; otherwise throws SecurityException.
   *
   * @param recNo  recordNumber
   * @param cookie lock number
   * @throws SecurityException       if lock not in place
   * @throws RecordNotFoundException if record does not exist
   */
  public void unlock(int recNo, long cookie)
          throws RecordNotFoundException, SecurityException;
}
