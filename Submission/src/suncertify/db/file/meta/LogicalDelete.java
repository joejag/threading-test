package suncertify.db.file.meta;

public enum LogicalDelete
{
   VALID(0), DELETED(1);

   private static final int BYTE_SIZE_OF_DELETE = 1;

   private int databaseValue;

   LogicalDelete(int databaseValue)
   {
      this.databaseValue = databaseValue;
   }

   public byte getValue()
   {
      return (byte) databaseValue;
   }

   public static boolean isDeleted(byte value)
   {
      return (int) value == DELETED.getValue();
   }

   public static int getSizeOfDeletedFlag()
   {
      return BYTE_SIZE_OF_DELETE;
   }
}
