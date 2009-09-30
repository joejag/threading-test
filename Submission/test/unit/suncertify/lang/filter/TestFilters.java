package suncertify.lang.filter;

import junit.framework.TestCase;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.filter.RemoveDeletedFilter;
import suncertify.db.filter.StartsWithFilter;
import suncertify.db.filter.SearchType;
import suncertify.lang.filters.Filters;

import java.util.Arrays;

public class TestFilters extends TestCase
{
  public void testAllFilters()
  {
    DatabaseRow del2 = new DatabaseRow(1, true, new String[]{});
    DatabaseRow del3 = new DatabaseRow(1, true, new String[]{"A", "B", "C"});
    DatabaseRow del1 = new DatabaseRow(1, false, new String[]{"B"});
    DatabaseRow good1 = new DatabaseRow(1, false, new String[]{"A", "B", "C"});

    Filters<DatabaseRow> filters = new Filters<DatabaseRow>(new RemoveDeletedFilter(), new StartsWithFilter());
    assertEquals(1, filters.accepts(new String[]{"a", "b", "c"}, Arrays.asList(del1, del2, del3, good1), SearchType.OR).size());
  }
}
