package suncertify.db.file;

import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.file.reader.FileDatabaseReader;

import java.util.ArrayList;
import java.util.List;

public class CachedDecorator implements FileDatabaseDecorator
{
   private FileDatabaseReader database;

   private final List<DatabaseRow> cache = new ArrayList<DatabaseRow>();

   public CachedDecorator(FileDatabaseReader database)
   {
      this.database = database;
      regenerateCache();
   }

   private void regenerateCache()
   {
      synchronized (cache)
      {
         cache.clear();
         cache.addAll(database.readAll());
      }
   }

   public String[] read(int recordNumber) throws RecordNotFoundException
   {
      for (DatabaseRow databaseRow : readAll())
         if (databaseRow.getId() == recordNumber && databaseRow.isValid())
            return databaseRow.getData();

      throw new RecordNotFoundException("Record does not exist: " + recordNumber);
   }

   public boolean hasRow(int recordNumber, boolean allowDeleted)
   {
      for (DatabaseRow databaseRow : readAll())
         if (databaseRow.getId() == recordNumber)
            return allowDeleted || databaseRow.isValid();

      return false;
   }

   public List<DatabaseRow> readAll()
   {
      synchronized (cache)
      {
         return new ArrayList<DatabaseRow>(cache);    // prevent concurrent modifications errors elsewhere by returning a new list
      }
   }

   public int create(String[] content)
   {
      int result = database.create(content);
      regenerateCache();

      return result;
   }

   public void update(int recordNumber, String[] content, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      database.update(recordNumber, content);
      regenerateCache();
   }

   public void delete(int recordNumber, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      database.delete(recordNumber);
      regenerateCache();
   }

   public long lock(int recordNumber) throws RecordNotFoundException
   {
      return 0;
   }

   public void unlock(int recordNumber, long cookie) throws RecordNotFoundException, SecurityException
   {
   }

}
