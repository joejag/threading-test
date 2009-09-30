package suncertify.db;

import java.io.File;
import java.io.FileNotFoundException;

public class Data implements DB
{
  private DB fileData;

  public Data() throws FileNotFoundException
  {
    fileData = new FileData(new File("Submission/res/db-1x1.db"));
  }

  public String[] read(int recordNumber) throws RecordNotFoundException
  {
    return fileData.read(recordNumber);
  }

  public void update(int recordNumber, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException
  {
    fileData.update(recordNumber, data, lockCookie);
  }

  public void delete(int recordNumber, long lockCookie) throws RecordNotFoundException, SecurityException
  {
    fileData.delete(recordNumber, lockCookie);
  }

  public int[] find(String[] criteria)
  {
    return fileData.find(criteria);
  }

  public int create(String[] data) throws DuplicateKeyException
  {
    return fileData.create(data);
  }

  public long lock(int recordNumber) throws RecordNotFoundException
  {
    return fileData.lock(recordNumber);
  }

  public void unlock(int recordNumber, long cookie) throws RecordNotFoundException, SecurityException
  {
    fileData.unlock(recordNumber, cookie);
  }
}
