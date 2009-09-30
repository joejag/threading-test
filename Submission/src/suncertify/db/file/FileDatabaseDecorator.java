package suncertify.db.file;

import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;

import java.util.List;

public interface FileDatabaseDecorator
{
  List<DatabaseRow> readAll();

  String[] read(int recordNumber)
          throws RecordNotFoundException;

  int create(String[] content);

  void update(int recordNumber, String[] data, long lockCookie)
          throws RecordNotFoundException, SecurityException;

  void delete(int recordNumber, long lockCookie)
          throws RecordNotFoundException, SecurityException;

  long lock(int recordNumber)
          throws RecordNotFoundException;

  void unlock(int recordNumber, long cookie)
          throws RecordNotFoundException, SecurityException;

  boolean hasRow(int recordNumber, boolean allowDeleted);
}
