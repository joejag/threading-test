package suncertify.db.file.meta;

public class DatabaseRow
{
  private int id;
  private boolean deleted;
  private String[] data;

  public DatabaseRow(int id, boolean deleted, String[] data)
  {
    this.id = id;
    this.deleted = deleted;
    this.data = data;
  }

  public int getId()
  {
    return id;
  }

  public boolean isValid()
  {
    return !deleted;
  }


  public String[] getData()
  {
    return data;
  }
}
