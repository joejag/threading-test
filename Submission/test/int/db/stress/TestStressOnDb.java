package db.stress;

import junit.framework.TestCase;
import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.FileUtils;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class TestStressOnDb extends TestCase
{
  public void testLocksAreObeyed() throws Exception
  {
    FileUtils.setupFreshDatabase();

    Enumeration<String> loggerNames = LogManager.getLogManager().getLoggerNames();
    while (loggerNames.hasMoreElements())
      LogManager.getLogManager().getLogger(loggerNames.nextElement()).setLevel(Level.FINEST);

    Data data = new Data();

    Thread thread = new Thread(new Client(data));
    thread.start();
    thread.join();

    Thread thread1 = new Thread(new Client(data));
    thread1.start();
    thread1.join();
  }

  public static class Client implements Runnable
  {
    private Data data;

    public Client(Data data)
    {
      this.data = data;
    }

    public void run()
    {
      try
      {
        System.out.println("working");
        long lock = data.lock(1);
        try
        {
          Thread.sleep(1000);
        } catch (InterruptedException e)
        {
          fail("Should not of got exception");
        }
//            data.delete(1, lock);
        data.unlock(1, lock);
        System.out.println("finished");
      } catch (RecordNotFoundException e)
      {
        fail("Should not of got exception");
      }
    }
  }
}


