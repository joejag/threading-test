package suncertify.db.filter;

import suncertify.db.file.meta.DatabaseRow;

public class StartsWithFilter extends AbstractFilter
{
  protected boolean performTest(String[] criteria, DatabaseRow databaseRow, int index)
  {
    return databaseRow.getData()[index].toLowerCase().startsWith(criteria[index].toLowerCase());
  }
}
