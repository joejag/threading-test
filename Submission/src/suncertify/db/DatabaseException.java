package suncertify.db;

public class DatabaseException extends RuntimeException
{
  public DatabaseException(String message, Exception exception)
  {
    super(message, exception);
  }
}
