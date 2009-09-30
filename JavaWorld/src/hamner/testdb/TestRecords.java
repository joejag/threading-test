package hamner.testdb;

import java.util.*;
import hamner.db.*;


/**
 * Simple test class for the RecordsFile example. To run the test, 
 * set you CLASSPATH and then type "java hamner.dbtest.TestRecords"
 */
public class TestRecords {

  static void log(String s) {
    System.out.println(s);
  }

  public static void main (String[] args) throws Exception {
    
    log("creating records file...");
    RecordsFile recordsFile = new RecordsFile("sampleFile.records", 64);
    
    log("adding a record...");
    RecordWriter rw = new RecordWriter("foo.lastAccessTime");
    rw.writeObject(new Date());
    recordsFile.insertRecord(rw);
    
    log("reading record...");
    RecordReader rr = recordsFile.readRecord("foo.lastAccessTime");
    Date d = (Date)rr.readObject();
    System.out.println("\tlast access was at: " + d.toString());
    
    log("updating record...");
    rw = new RecordWriter("foo.lastAccessTime");
    rw.writeObject(new Date());
    recordsFile.updateRecord(rw);

    log("reading record...");
    rr = recordsFile.readRecord("foo.lastAccessTime");
    d = (Date)rr.readObject();
    System.out.println("\tlast access was at: " + d.toString());

    log("deleting record...");
    recordsFile.deleteRecord("foo.lastAccessTime");
    if (recordsFile.recordExists("foo.lastAccessTime")) {
      throw new Exception("Record not deleted");
    } else {
      log("record successfully deleted.");
    }
    
    log("test completed.");
  }
}

