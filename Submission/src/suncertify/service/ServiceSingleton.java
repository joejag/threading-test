package suncertify.service;

import suncertify.db.FileData;
import suncertify.db.RecordNotFoundException;
import suncertify.db.filter.SearchType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ServiceSingleton
{
  private static ServiceSingleton ourInstance = new ServiceSingleton();  // early inst

  public static ServiceSingleton getInstance()
  {
    return ourInstance;
  }

  private FileData data;

  private ServiceSingleton()
  {
    try
    {
      data = new FileData(new File("Submission/res/db-1x1.db"));
    } catch (FileNotFoundException e)
    {
      throw new RuntimeException();
    }
  }

  public List<String[]> getAll()
  {
    return search(new String[]{null, null, null, null, null, null, null}, SearchType.OR);
  }

  public List<String[]> search(String[] keys, SearchType type)
  {
    List<String[]> result = new ArrayList<String[]>();

    for (int key : data.find(keys, type))
    {
      try
      {
        result.add(data.read(key));
      } catch (RecordNotFoundException e)
      {
        // data must have changed post find
      }
    }

    return result;
  }

}
