package suncertify.db.file.meta;

import suncertify.db.file.meta.LogicalDelete;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetaData
{
  private int magicCookie;
  private int sizeOfRecord;
  private short numberOfFields;

  private LinkedHashMap<String, Short> columns;

  private long dataStartPointer;

  public MetaData(int magicCookie, int sizeOfRecord, short numberOfFields, LinkedHashMap<String, Short> columns, long dataStartPointer)
  {
    this.magicCookie = magicCookie;
    this.sizeOfRecord = sizeOfRecord;
    this.numberOfFields = numberOfFields;
    this.columns = columns;
    this.dataStartPointer = dataStartPointer;
  }

  public List<Short> getFieldSizes()
  {
    return new ArrayList<Short>(this.getColumns().values());
  }

  public int getMagicCookie()
  {
    return magicCookie;
  }

  public int getSizeOfRecord()
  {
    return sizeOfRecord + LogicalDelete.getSizeOfDeletedFlag();
  }

  public short getNumberOfFields()
  {
    return numberOfFields;
  }

  public Map<String, Short> getColumns()
  {
    return columns;
  }

  public long getDataStartPointer()
  {
    return dataStartPointer;
  }
}
