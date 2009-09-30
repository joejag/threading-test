import java.util.*;

public class LockObjectNotMemberVariables{
    private List myList = new ArrayList();

    public static void main(String args[]){
        LockObjectNonMemberVariables lonmv =
                new LockObjectNonMemberVariables();
        lonmv.lockTest();
    }

    public synchronized void lockTest(){
        System.out.println("Is the THIS object locked? " +
                            Thread.holdsLock(this));

        System.out.println("Is the list object locked? " +
                            Thread.holdsLock(myList));
    }
}
