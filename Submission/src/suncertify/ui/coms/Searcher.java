package suncertify.ui.coms;

import suncertify.db.filter.SearchType;
import suncertify.lang.StringUtils;
import suncertify.service.ServiceSingleton;

import java.util.List;

public class Searcher
{
  private static final String STATUS_FORMAT = "%s is '%s'";

  public String performSearch(ServiceSingleton serviceSingleton, List<String[]> data, String name, String location, SearchType type)
  {
    String[] keys = makeKeys(name, location);

    List<String[]> results = serviceSingleton.search(keys, type);

    data.clear();
    data.addAll(results);

    if (!isValid(name) && !isValid(location))
      return "Showing all records: " + StringUtils.pluralize(data.size(), "result");

    return createStatusResultText(data.size(), name, location, type);
  }

  private String[] makeKeys(String name, String location)
  {
    String[] keys = {null, null, null, null, null, null, null};

    if (isValid(name))
      keys[0] = name;

    if (isValid(location))
      keys[1] = location;

    return keys;
  }

  private String createStatusResultText(int size, String name, String location, SearchType type)
  {
    String result = StringUtils.pluralize(size, "result") + " for ";

    if (isValid(name))
      result += String.format(STATUS_FORMAT, "Name", name);

    if (isValid(name) && isValid(location))
      result += " " + type + " ";

    if (isValid(location))
      result += String.format(STATUS_FORMAT, "Location", location);

    return result;
  }

  private boolean isValid(String text)
  {
    return text.trim().length() > 0;
  }
}
