package suncertify.db;

public class RecordNotFoundException extends Exception
{
  public RecordNotFoundException()
  {
    super();
  }

  public RecordNotFoundException(String message)
  {
    super(message);
  }
}
