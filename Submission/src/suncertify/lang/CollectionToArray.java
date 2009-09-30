package suncertify.lang;

import java.util.List;

public class CollectionToArray
{
  public static String[] convert(List<String> input)
  {
    String[] result = new String[input.size()];
    input.toArray(result);
    return result;
  }

  public static int[] convert(List<Integer> input)
  {
    int[] result = new int[input.size()];

    for (int i = 0; i < input.size(); i++)
      result[i] = input.get(i);

    return result;
  }
}
