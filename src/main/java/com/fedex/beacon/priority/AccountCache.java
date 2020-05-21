package com.fedex.beacon.priority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCache {
  private static final Map<String, AccountDetails> CACHE =
    new ConcurrentHashMap<String, AccountDetails>();

  public static void put(final AccountDetails accountDetails) {
    CACHE.put(accountDetails.getAccountNumber() + ":" + accountDetails.getAccountType(),
      accountDetails);
  }

  public static void put(final AccountDetails[] accountDetailsArray) {
    for (int i = 0; i < accountDetailsArray.length; i++) {
      final AccountDetails accountDetails = accountDetailsArray[i];
      put(accountDetails);
    }
  }

  public static AccountDetails get(final String accountKey, final String accountType) {
    return CACHE.get(accountKey + ":" + accountType);
  }
}

