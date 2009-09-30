package suncertify.db.filter;

import suncertify.db.file.meta.DatabaseRow;
import suncertify.lang.filters.Filter;

public abstract class AbstractFilter implements Filter<DatabaseRow>
{
  public boolean accepts(String[] criteria, DatabaseRow databaseRow, SearchType type)
  {
    if (databaseRow.getData().length != criteria.length)
      return false;

    if (areAllCriteriaNull(criteria))
      return true;

    if (SearchType.OR.equals(type))
    {
      for (int i = 0; i < criteria.length; i++)
        if (criteria[i] != null && performTest(criteria, databaseRow, i))
          return true;

      return false;
    } else if (SearchType.AND.equals(type))
    {
      for (int i = 0; i < criteria.length; i++)
        if (criteria[i] != null && !performTest(criteria, databaseRow, i))
          return false;

      return true;
    }

    throw new UnsupportedOperationException("Only supports ANY/ALL ");
  }

  protected abstract boolean performTest(String[] criteria, DatabaseRow databaseRow, int index);

  protected boolean areAllCriteriaNull(String[] criteria)
  {
    for (String term : criteria)
      if (term != null)
        return false;

    return true;
  }
}
