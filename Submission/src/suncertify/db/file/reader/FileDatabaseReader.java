package suncertify.db.file.reader;

import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;

import java.util.List;

public interface FileDatabaseReader
{
  List<DatabaseRow> readAll();

  int create(String[] content);

  void update(int recordNumber, String[] content) throws RecordNotFoundException;

  void delete(int recordNumber) throws RecordNotFoundException;
}
