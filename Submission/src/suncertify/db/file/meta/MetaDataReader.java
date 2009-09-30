package suncertify.db.file.meta;

import suncertify.lang.ByteStreamUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class MetaDataReader
{
  private static final Logger log = Logger.getLogger(MetaDataReader.class.getName());

  private int magicCookie;
  private int sizeOfRecord;
  private short numberOfFields;

  private LinkedHashMap<String, Short> columns;

  public MetaData setup(RandomAccessFile accessFile) throws MetaDataException
  {
    getHeaderData(accessFile);
    getSchemaDescription(accessFile, numberOfFields);

    try
    {
      long dataStartPointer = accessFile.getFilePointer();
      return new MetaData(magicCookie, sizeOfRecord, numberOfFields, columns, dataStartPointer);

    } catch (IOException e)
    {
      throw new MetaDataException("Cannot get file pointer for start of data: " + e.getMessage(), e);
    }
  }

  private void getHeaderData(RandomAccessFile accessFile)
  {
    try
    {
      log.fine("Reading in meta data");
      magicCookie = accessFile.readInt();
      sizeOfRecord = accessFile.readInt();
      numberOfFields = accessFile.readShort();
    } catch (IOException e)
    {
      throw new MetaDataException("Cannot read meta data from file because: " + e.getMessage(), e);
    }
  }

  private void getSchemaDescription(RandomAccessFile accessFile, short numberOfFields)
  {
    columns = new LinkedHashMap<String, Short>();

    try
    {
      for (int i = 0; i < numberOfFields; i++)
      {
        short lengthOfFieldName = accessFile.readShort();
        String columnName = ByteStreamUtils.readStringOfSize(accessFile, lengthOfFieldName);
        short columnSize = accessFile.readShort();

        columns.put(columnName, columnSize);
      }
    } catch (IOException e)
    {
      throw new MetaDataException("Cannot read schema data from file because: " + e.getMessage(), e);
    }
  }

}
