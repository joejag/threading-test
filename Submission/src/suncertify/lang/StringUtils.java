package suncertify.lang;

public class StringUtils
{
  public static String padRight(String input, int length)
  {
    StringBuilder builder = new StringBuilder(input);
    while (builder.length() < length)
      builder.append(" ");

    return builder.toString();
  }

  public static String pluralize(int size, String word)
  {
    if (size == 1)
      return size + " " + word;

    if (size > 1)
      return size + " "  + word + "s";

    return "No " + word + "s";
  }

}
