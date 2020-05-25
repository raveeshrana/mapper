package com.fedex.beacon.utils;

public class StringUtils {
  public static boolean isEmpty(final String str){
    return str == null || str.length() == 0;
  }

  public static boolean isNotEmpty(final String str){
    return !isEmpty(str);
  }
}
