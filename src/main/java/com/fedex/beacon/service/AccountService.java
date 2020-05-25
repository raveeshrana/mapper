package com.fedex.beacon.service;

import java.util.ArrayList;
import java.util.List;

import com.fedex.beacon.beans.AccountDetails;
import com.fedex.beacon.beans.AccountKey;
import com.fedex.beacon.cache.PriorityCache;

public class AccountService {

  private static PriorityCache priorityCache = PriorityCache.getPriorityCacheInstance();

  public  static AccountKey[] getPublishAccountList(final String shipperAccountNumber,
    final String recipientAccountNumber, final String thirdPartyAccountNumber,
    final String trackType) {
    final List<AccountKey> accounts = new ArrayList<>();

    final AccountKey shipperAccountKey =
      new AccountKey(shipperAccountNumber, AccountKey.SHIPPER_ACCOUNT);
    final AccountKey recipientAccountKey =
      new AccountKey(recipientAccountNumber, AccountKey.RECIPIENT_ACCOUNT);
    final AccountKey thirdPartyAccountKey =
      new AccountKey(thirdPartyAccountNumber, AccountKey.THIRD_PARTY_ACCOUNT);

    addAccount(shipperAccountKey, trackType, accounts);
    addAccount(recipientAccountKey, trackType, accounts);
    addThirdPartyAccount(thirdPartyAccountKey, trackType, accounts);

    return accounts.toArray(new AccountKey[accounts.size()]);
  }

  private static void addAccount(final AccountKey accountKey, final String trackType,
    final List<AccountKey> accountKeys) {
    final AccountDetails account = priorityCache.get(accountKey);

    if (account == null || !account.getTrackType().contains(trackType)) {
      return;
    }
    accountKeys.add(accountKey);
  }

  private static void addThirdPartyAccount(final AccountKey thirdPartyAccountKey,
    final String trackType, final List<AccountKey> accounts) {
    final AccountDetails account =  priorityCache.get(thirdPartyAccountKey);
    if (account == null || !account.getTrackType().contains(trackType)) {
      return;
    }

    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getAccountNumber() == thirdPartyAccountKey.getAccountNumber()) {
        return;
      }
    }
    accounts.add(thirdPartyAccountKey);
  }
}
