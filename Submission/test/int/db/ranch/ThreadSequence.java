package db.ranch;


import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

import java.util.Arrays;
import java.util.Random;

public class ThreadSequence
{

   /**
    * The create mode.
    */
   public static final int CREATE_MODE = 0;

   /**
    * The read mode.
    */
   public static final int READ_MODE = 1;

   /**
    * The update mode.
    */
   public static final int UPDATE_MODE = 2;

   /**
    * The delete mode.
    */
   public static final int DELETE_MODE = 3;

   /**
    * The find mode.
    */
   public static final int FIND_MODE = 4;

   /**
    * The maximum recNo.
    */
   public static final int MAX_REC_NO = 100;

   /**
    * A random generator.
    */
   private static final Random RANDOM = new Random();

   /**
    * A "random" record.
    */
   private String[] record = new String[]{
           "Excelsior", "Smallville", "2", "N", "$210.00", "2005/03/23", String.valueOf(RANDOM.nextInt(100000000))
   };

   /**
    * A "random" criteria.
    */
   private String[] criteria = new String[]{
           null, "Small", null, RANDOM.nextBoolean() ? "Y" : "N", null, null, null
   };

   /**
    * The current mode of this sequence.
    */
   private int mode;

   /**
    * The locker thread.
    */
   private SequenceStep lockerThread;

   /**
    * The do-something thread.
    */
   private SequenceStep doSomethingThread;

   /**
    * The unlocker thread.
    */
   private SequenceStep unlockerThread;

   /**
    * Constructor. Creates the three threads.
    *
    * @param data The {@link Data} instance.
    * @param mode When in CREATE_MODE, READ_MODE or FIND_MODE, this will only
    *             be able to start one thread (because no locking is involved).
    */
   public ThreadSequence(final Data data, final int mode)
   {
      this.mode = mode;

      lockerThread = new SequenceStep()
      {
         {
            recNo = RANDOM.nextInt(MAX_REC_NO);
         }

         public void run()
         {
            try
            {
               Thread.yield();
               System.out.println("Locking record: " + recNo);
               lockingCookie = data.lock(recNo);
               withoutRnfe = true;
               Thread.yield();
            } catch (RecordNotFoundException re)
            {
               withoutRnfe = false;
            }
         }

         ;
      };

      doSomethingThread = new SequenceStep()
      {
         public void run()
         {
            recNo = lockerThread.recNo;

            try
            {
               if (mode == UPDATE_MODE || mode == DELETE_MODE)
               {
                  // only in these modes we need a join
                  lockerThread.join();
                  Thread.yield();
                  // we perform the tasks only if the record was found
                  // (it's a random number, so it is possible to be
                  // already deleted or outside database length).
                  if (lockerThread.withoutRnfe)
                  {
                     lockingCookie = lockerThread.lockingCookie;
                     Thread.yield();
                     if (mode == UPDATE_MODE)
                     {
                        System.out.println("Updating record: " + recNo);
                        data.update(recNo, record, lockingCookie);
                     } else if (mode == DELETE_MODE)
                     {
                        System.out.println("Deleting record: " + recNo);

                        // if you're reusing deleted records, you can comment out this line
                        // and see how the deleted records disappear and the find returns all records.
                        data.delete(recNo, lockingCookie);
                     }
                  } else
                  {
                     Thread.yield();
                     System.out.println("Could not find record: " + recNo);
                     // at least do something else:
                     System.out.println("Creating record: " + recNo);
                     data.create(record);
                     Thread.yield();
                  }
               } else
               {
                  switch (mode)
                  {
                     case CREATE_MODE:
                        Thread.yield();
                        System.out.println("Creating record");
                        data.create(record);
                        Thread.yield();
                        break;
                     case READ_MODE:
                        Thread.yield();
                        System.out.println("Reading record: " + recNo);
                        data.read(recNo);
                        Thread.yield();
                        break;
                     case FIND_MODE:
                        // no yielding, this is a slow operation anyway.
                        int[] found = data.find(criteria);
                        System.out.println("Found records: " + Arrays.toString(found));
                        break;
                     default:
                        System.err.println("Unknown mode " + mode);
                  }
               }
            } catch (RecordNotFoundException re)
            {
               // in my implementation, the update/delete methods can throw
               // RecordNotFoundException
               System.out.println("Attempt to read a deleted record, or the record was deleted "
                       + "while update/delete were waiting for the lock: " + recNo);
               withoutRnfe = false;
            } catch (InterruptedException ie)
            {
               // should not happen
               System.err.println("InterruptedException in doSomethingThread");
            } catch (DuplicateKeyException dke)
            {
               // in my implementation, it should not happen
               System.err.println("DuplicateKeyException in doSomethingThread");
            }
         }
      };

      unlockerThread = new SequenceStep()
      {

         public void run()
         {
            try
            {
               doSomethingThread.join();
               Thread.yield();
               recNo = lockerThread.recNo;
               lockingCookie = lockerThread.lockingCookie;

               if (lockerThread.withoutRnfe)
               {
                  Thread.yield();
                  data.unlock(recNo, lockingCookie);
                  System.out.println("Unlocked record: " + recNo);
                  Thread.yield();
               }
            } catch (RecordNotFoundException re)
            {
               // in my implementation, it should not happen
               System.err.println("The unlocker thread must not throw RecordNotFoundException.");
            } catch (InterruptedException ie)
            {
               // should not happen
               System.err.println("InterruptedException in doSomethingThread");
            }
         }
      };
   }

   public void start() throws Exception
   {
      if (mode == UPDATE_MODE || mode == DELETE_MODE)
      {
         lockerThread.start();
      }

      doSomethingThread.start();

      if (mode == UPDATE_MODE || mode == DELETE_MODE)
      {
         unlockerThread.start();
      }
   }

   private static class SequenceStep extends Thread
   {
      public long lockingCookie;
      public int recNo;
      public boolean withoutRnfe = false;
   }

}
