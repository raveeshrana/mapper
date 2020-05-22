package com.fedex.beacon.priority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCache {
  private static final Map<AccountKey, Set<String>> CACHE =
    new ConcurrentHashMap<AccountKey, Set<String>>();

  public static void put(final String accountNumber, final String accountType, final String trackType) {
    final AccountKey accountKey = new AccountKey(accountNumber, accountType);
    CACHE.put(accountKey,  new HashSet<String>(Arrays.asList(trackType.split(","))));
  }

  public static Set<String> get(final AccountKey key) {
    return CACHE.get(key);
  }
}

