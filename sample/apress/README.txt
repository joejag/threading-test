This README file describes changes to the source files from what might be 
described in the book.


--------------------------------------------------------------------------------

class sampleproject.test.DBTester 

Line 50 has been changed from:
    db = DvdConnector.getRemote();
to:
    db = DvdConnector.getRemote("localhost", "1099");

This now matches the method signature for the getRemote method in the 
DvdConnector classes.

--------------------------------------------------------------------------------

class NotifyVersusNotifyAll

An updated version of this class has been added which shows the differences 
between calling notify() and calling notifyAll() properly.

--------------------------------------------------------------------------------
