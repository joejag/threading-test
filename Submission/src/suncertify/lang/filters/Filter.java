package suncertify.lang.filters;

import suncertify.db.filter.SearchType;

public interface Filter<T>
{
  boolean accepts(String[] criteria, T databaseRow, SearchType type);
}
