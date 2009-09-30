package db.ranch;

import junit.framework.TestCase;
import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.FileUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.Enumeration;
import java.util.logging.LogManager;
import java.util.logging.Level;

public class TestLoadData extends TestCase
{

   private static final Random RANDOM = new Random();

   private static final int NO_OF_SEQUENCES = 300;

   private static final String DB_LOCATION_PROPERTY = "suncertify.db.location";

   private static final String PATH = "D:\\jde\\projects\\ac-scjd\\db-1x2-load-test.db";

   private Data data;

   protected void setUp() throws Exception
   {
      FileUtils.setupFreshDatabase();

     // This is my way of initializing the database. You can do it your way.
      System.setProperty(DB_LOCATION_PROPERTY, PATH);
      data = new Data();
      System.setProperty(DB_LOCATION_PROPERTY, "");
   }

   public void testA() {}

   public void atestRunSequences() throws Exception
   {
      for (int i = 0; i < NO_OF_SEQUENCES; i++)
      {
         ThreadSequence ts = new ThreadSequence(data, RANDOM.nextInt(ThreadSequence.FIND_MODE + 1));
         ts.start();
      }

      // let the thread sequences do their jobs.
      Thread.sleep(15000);

      atestWhatsLeftOfIt();
   }

   public void atestWhatsLeftOfIt()
   {
      for (int i = 0; i < 3 * ThreadSequence.MAX_REC_NO; i++)
      {
         try
         {
            String[] record = data.read(i);
            System.out.println(i + " - found record: " + Arrays.toString(record));
         } catch (RecordNotFoundException rnfe)
         {
            System.out.println(i + " - not found record");
         }
      }
   }
}
