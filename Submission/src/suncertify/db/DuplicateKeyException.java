package suncertify.db;

public class DuplicateKeyException extends Exception
{
  public DuplicateKeyException()
  {
    super();
  }

  public DuplicateKeyException(String message)
  {
    super(message);
  }
}
