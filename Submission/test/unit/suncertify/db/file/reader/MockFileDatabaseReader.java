package suncertify.db.file.reader;

import suncertify.db.file.reader.FileDatabaseReader;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.RecordNotFoundException;

import java.util.List;
import java.util.ArrayList;

public class MockFileDatabaseReader implements FileDatabaseReader
{
  public int readCalls = 0;
  public List<DatabaseRow> databaseRows= new ArrayList<DatabaseRow>();

  public List<DatabaseRow> readAll()
  {
    readCalls++;
    
    return databaseRows;
  }

  public int create(String[] content)
  {
    return 0;
  }

  public void update(int recordNumber, String[] content) throws RecordNotFoundException
  {

  }

  public void delete(int recordNumber) throws RecordNotFoundException
  {
  }
}
