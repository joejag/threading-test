package suncertify.lang.filters;

import suncertify.db.filter.SearchType;

import java.util.ArrayList;
import java.util.List;

public class Filters<T>
{
  private Filter<T>[] filters;

  public Filters(Filter<T>... filters)
  {
    this.filters = filters;
  }

  public List<T> accepts(String[] criteria, List<T> rows, SearchType type)
  {
    List<T> result = new ArrayList<T>();

    for (T row : rows)
    {
      boolean isOk = true;

      for (Filter<T> filter : filters)
        if (!filter.accepts(criteria, row, type))
          isOk = false;

      if (isOk)
        result.add(row);
    }

    return result;
  }
}
