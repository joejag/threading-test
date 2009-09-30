package suncertify.db.filter;

import junit.framework.TestCase;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.lang.filters.Filter;

public class TestRemovalDeletedFilter extends TestCase
{
  public void testWorks()
  {
    Filter<DatabaseRow> deletedFilter = new RemoveDeletedFilter();
    assertTrue(deletedFilter.accepts(new String[]{""}, new DatabaseRow(1, false, new String[]{}), SearchType.OR));
    assertFalse(deletedFilter.accepts(new String[]{""}, new DatabaseRow(1, true, new String[]{}), SearchType.OR));
  }
}
