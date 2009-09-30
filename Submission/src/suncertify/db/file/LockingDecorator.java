package suncertify.db.file;

import suncertify.db.DatabaseException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LockingDecorator implements FileDatabaseDecorator
{
   private static final boolean ALLOW_DELETED_ROWS_TO_BE_LOCKED = false;
   private static final boolean ALLOW_DELETED_ROWS_TO_BE_UNLOCKED = true;

   private static final Logger log = Logger.getLogger(LockingDecorator.class.getName());

   private FileDatabaseDecorator database;
   private final Map<Integer, Long> lockedRows = new ConcurrentHashMap<Integer, Long>();

   public LockingDecorator(FileDatabaseDecorator database)
   {
      this.database = database;
   }

   public long lock(int recordNumber) throws RecordNotFoundException
   {
      log.fine(Thread.currentThread().getName() + " attempting to lock " + recordNumber);

      validateRow(recordNumber, ALLOW_DELETED_ROWS_TO_BE_LOCKED);

      long lockId = new Random(System.nanoTime()).nextLong();

      synchronized (lockedRows)
      {
         while (lockedRows.containsKey(recordNumber))
         {
            try
            {
               log.fine(Thread.currentThread().getName() + " waiting for lock on " + recordNumber);
               Long recordToWaitOn = lockedRows.get(recordNumber);
               synchronized (recordToWaitOn)
               {
                  recordToWaitOn.wait();  // no timeout as per spec
               }
            } catch (InterruptedException e)
            {
               log.fine(Thread.currentThread().getName() + " interrupted waiting for lock on " + recordNumber);
               throw new DatabaseException("Forced to stop waiting for lock", e);
            }
         }
         
         log.fine(Thread.currentThread().getName() + " has lock on " + recordNumber);
         lockedRows.put(recordNumber, lockId);
      }

      return lockId;
   }

   public void unlock(int recordNumber, long cookie) throws RecordNotFoundException, SecurityException
   {
      log.fine(Thread.currentThread().getName() + " attempting to unlock " + recordNumber);

      validateRow(recordNumber, ALLOW_DELETED_ROWS_TO_BE_UNLOCKED);
      validateLock(recordNumber, cookie);

      synchronized (lockedRows)
      {
         Long recordToNotifyOn = lockedRows.remove(recordNumber);
         log.fine(Thread.currentThread().getName() + " has unlocked " + recordNumber);

         synchronized (recordToNotifyOn)
         {
            recordToNotifyOn.notify(); // only wake a single thread as per spec
         }
      }
   }

   private void validateRow(int recordNumber, boolean allowDeleted)
           throws RecordNotFoundException
   {
      if (!hasRow(recordNumber, allowDeleted))
         throw new RecordNotFoundException("Record does not exist: " + recordNumber);
   }

   private void validateLock(int recordNumber, long lockCookie)
   {
      log.fine(Thread.currentThread().getName() + " is validating lock on " + recordNumber);

      synchronized (lockedRows)
      {
         if (!lockedRows.containsKey(recordNumber) || lockedRows.get(recordNumber) != lockCookie)
            throw new SecurityException("You do not own the lock for this row");
      }
   }

   public void update(int recordNumber, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      validateRow(recordNumber, false);
      validateLock(recordNumber, lockCookie);
      database.update(recordNumber, data, lockCookie);
   }

   public void delete(int recordNumber, long lockCookie) throws RecordNotFoundException, SecurityException
   {
      validateRow(recordNumber, false);
      validateLock(recordNumber, lockCookie);
      database.delete(recordNumber, lockCookie);
   }

   public boolean hasRow(int recordNumber, boolean allowDeleted)
   {
      return database.hasRow(recordNumber, allowDeleted);
   }

   public List<DatabaseRow> readAll()
   {
      return database.readAll();
   }

   public String[] read(int recordNumber) throws RecordNotFoundException
   {
      return database.read(recordNumber);
   }

   public int create(String[] content)
   {
      return database.create(content);
   }
}
