package com.fedex.beacon.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapAccount {

  public static AccountKey[] getPublishAccountList(final String shipperAccountNumber,
    final String recipientAccountNumber, final String thirdPartyAccountNumber,
    final String trackType) {
    final List<AccountKey> accounts = new ArrayList<AccountKey>();

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

  private static void addAccount(final AccountKey shipperAccountKey, final String trackType,
    final List<AccountKey> accountKeys) {
    if (shipperAccountKey == null) {
      return;
    }
    final Set<String> shipperTrackType = AccountCache.get(shipperAccountKey);
    if (shipperTrackType == null || !shipperTrackType.contains(trackType)) {
      return;
    }
    accountKeys.add(shipperAccountKey);
  }

  private static void addThirdPartyAccount(final AccountKey thirdPartyAccountKey,
    final String trackType, final List<AccountKey> accounts) {
    if (thirdPartyAccountKey == null || !AccountCache.get(thirdPartyAccountKey)
      .contains(trackType)) {
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

