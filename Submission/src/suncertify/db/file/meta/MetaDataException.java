package suncertify.db.file.meta;

import suncertify.db.DatabaseException;

import java.io.IOException;

public class MetaDataException extends DatabaseException
{
  public MetaDataException(String message, IOException exception)
  {
    super(message, exception);
  }
}
