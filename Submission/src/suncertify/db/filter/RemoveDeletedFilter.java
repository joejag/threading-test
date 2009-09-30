package suncertify.db.filter;

import suncertify.db.file.meta.DatabaseRow;
import suncertify.lang.filters.Filter;

public class RemoveDeletedFilter implements Filter<DatabaseRow>
{
  public boolean accepts(String[] criteria, DatabaseRow databaseRow, SearchType type)
  {
    return databaseRow.isValid();
  }
}
