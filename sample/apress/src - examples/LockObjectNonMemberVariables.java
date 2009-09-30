import java.util.*;

public class LockObjectNonMemberVariables
{
    private Vector myVector = new Vector();

    public static void main(String args[]){
        LockObjectNonMemberVariables lonmv =
                new LockObjectNonMemberVariables();
        lonmv.lockTest();
    }

    public synchronized void lockTest(){
        System.out.println("Is the THIS object locked? " +
                            Thread.currentThread().holdsLock(this));

        System.out.println("Is the vector object locked? " +
                            Thread.currentThread().holdsLock(myVector));
    }
}
