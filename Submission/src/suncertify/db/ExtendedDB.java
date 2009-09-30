package suncertify.db;

import suncertify.db.filter.SearchType;

public interface ExtendedDB
{
  int[] find(String[] criteria, SearchType type);
}
