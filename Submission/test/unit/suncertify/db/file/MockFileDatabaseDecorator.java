package suncertify.db.file;

import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.RecordNotFoundException;

import java.util.List;

public class MockFileDatabaseDecorator implements FileDatabaseDecorator
{
   public int updateCalls = 0;
   public int deleteCalls = 0;
   public boolean hasRow = true;

   public List<DatabaseRow> readAll()
   {
      return null;
   }

   public String[] read(int recordNumber) throws RecordNotFoundException
   {
      return new String[0];
   }

   public int create(String[] content)
   {
      return 0;
   }

   public void update(int recordNumber, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      updateCalls++;
   }

   public void delete(int recordNumber, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      deleteCalls++;
   }

   public long lock(int recordNumber) throws RecordNotFoundException
   {
      return 0;
   }

   public void unlock(int recordNumber, long cookie) throws RecordNotFoundException, SecurityException
   {
   }

   public boolean hasRow(int recordNumber, boolean allowDeleted)
   {
      return hasRow;
   }
}
