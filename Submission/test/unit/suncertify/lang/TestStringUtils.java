package suncertify.lang;

import junit.framework.TestCase;

public class TestStringUtils extends TestCase
{
  public void testPadString()
  {
    String paddedString = StringUtils.padRight("Name", 64);
    assertEquals(64, paddedString.length());
  }
}
