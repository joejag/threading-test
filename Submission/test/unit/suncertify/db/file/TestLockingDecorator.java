package suncertify.db.file;

import junit.framework.TestCase;
import suncertify.db.RecordNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class TestLockingDecorator extends TestCase
{
   public MockFileDatabaseDecorator fileDatabaseDecorator;
   public LockingDecorator lockingDecorator;

   protected void setUp() throws Exception
   {
      fileDatabaseDecorator = new MockFileDatabaseDecorator();
      lockingDecorator = new LockingDecorator(fileDatabaseDecorator);
   }

   public void testGoodLockUpdate() throws RecordNotFoundException
   {
      long lockId = lockingDecorator.lock(1);
      lockingDecorator.update(1, new String[]{}, lockId);
      assertEquals(1, fileDatabaseDecorator.updateCalls);
   }

   public void testGoodLockDelete() throws RecordNotFoundException
   {
      long lockId = lockingDecorator.lock(1);
      lockingDecorator.delete(1, lockId);
      assertEquals(1, fileDatabaseDecorator.deleteCalls);
   }

   public void testLockOnBadRecord()
   {
      fileDatabaseDecorator.hasRow = false;
      int noneExistantId = 123;
      try
      {
         lockingDecorator.lock(noneExistantId);
         fail("should fail");
      } catch (RecordNotFoundException e)
      {

      }
   }

   public void testBadLockUpdate() throws RecordNotFoundException
   {
      long lockId = lockingDecorator.lock(1);
      try
      {
         lockingDecorator.update(2, new String[]{}, lockId);
         fail("wrong lock given");
      } catch (SecurityException e)
      {

      }

      try
      {
         lockingDecorator.update(1, new String[]{}, lockId + 1);
         fail("wrong lock given");
      } catch (SecurityException e)
      {

      }
   }

   public void testBadLockDelete() throws RecordNotFoundException
   {
      long lockId = lockingDecorator.lock(1);
      try
      {
         lockingDecorator.delete(2, lockId);
         fail("wrong lock given");
      } catch (SecurityException e)
      {

      }

      try
      {
         lockingDecorator.delete(1, lockId + 1);
         fail("wrong lock given");
      } catch (SecurityException e)
      {

      }
   }

   public void testMakesRandoms() throws RecordNotFoundException
   {
      Set<Long> locks = new HashSet<Long>();
      for (int i = 0; i < 1000; i++)
      {
         long lock = lockingDecorator.lock(i);
         assertFalse(locks.contains(lock));
         locks.add(lock);
      }

   }
}
