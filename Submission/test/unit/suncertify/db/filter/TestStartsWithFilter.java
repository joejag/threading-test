package suncertify.db.filter;

import junit.framework.TestCase;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.lang.filters.Filter;

public class TestStartsWithFilter extends TestCase
{
  public void testInit()
  {
    Filter<DatabaseRow> filter = new StartsWithFilter();

    assertTrue(filter.accepts(new String[]{null, null, null}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));

    assertTrue(filter.accepts(new String[]{"A", null, null}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{"A", "B", null}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{null, null, "C"}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));

    assertFalse(filter.accepts(new String[]{"B", null, null}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));
    assertFalse(filter.accepts(new String[]{"B", "C", null}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));
    assertFalse(filter.accepts(new String[]{"D", null, "A"}, new DatabaseRow(1, true, new String[]{"A", "B", "C"}), SearchType.OR));

    assertTrue(filter.accepts(new String[]{"A", null, null}, new DatabaseRow(1, true, new String[]{"Aaaaaa", "B", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{"A", "B", null}, new DatabaseRow(1, true, new String[]{"Aaaa", "Bbbb", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{null, null, "C"}, new DatabaseRow(1, true, new String[]{"A", "B", "Casdasd"}), SearchType.OR));

    assertFalse(filter.accepts(new String[]{"B", null, null}, new DatabaseRow(1, true, new String[]{"Ab", "B", "C"}), SearchType.OR));
    assertFalse(filter.accepts(new String[]{"B", "C", null}, new DatabaseRow(1, true, new String[]{"Ab", "Bccc", "C"}), SearchType.OR));
    assertFalse(filter.accepts(new String[]{"D", null, "A"}, new DatabaseRow(1, true, new String[]{"Addd", "B", "Caa"}), SearchType.OR));

    assertTrue(filter.accepts(new String[]{"Aa", null, null}, new DatabaseRow(1, true, new String[]{"Aaaaaa", "B", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{"A", "B", null}, new DatabaseRow(1, true, new String[]{"BAaaa", "Bbbb", "C"}), SearchType.OR));
    assertTrue(filter.accepts(new String[]{null, null, "Cas"}, new DatabaseRow(1, true, new String[]{"A", "B", "Casdasd"}), SearchType.OR));
  }
}
