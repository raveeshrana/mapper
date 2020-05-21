package com.fedex.beacon.priority;

import java.util.ArrayList;
import java.util.List;

public class MapAccount {

  public static AccountDetails[] getPublishAccountList(final String shipperAccountNumber,
    final String recipientAccountNumber, final String thirdPartyAccountNumber,
    final String trackType) {
    final List<AccountDetails> accounts = new ArrayList<AccountDetails>();

    final AccountDetails shipperAccountDetails =
      AccountCache.get(shipperAccountNumber, AccountDetails.SHIPPER_ACCOUNT);
    if (shipperAccountDetails != null && shipperAccountDetails.getTrackTypeList()
      .contains(trackType)) {
      accounts.add(shipperAccountDetails);
    }

    final AccountDetails recipientAccountDetails =
      AccountCache.get(recipientAccountNumber, AccountDetails.RECIPIENT_ACCOUNT);
    if (recipientAccountDetails != null && recipientAccountDetails.getTrackTypeList()
      .contains(trackType)) {
      accounts.add(recipientAccountDetails);
    }

    final AccountDetails thirdPartyAccountDetails =
      AccountCache.get(thirdPartyAccountNumber, AccountDetails.THIRD_PARTY_ACCOUNT);
    addThirdPartyAccount(trackType, accounts, shipperAccountDetails, recipientAccountDetails,
      thirdPartyAccountDetails);

    return accounts.toArray(new AccountDetails[accounts.size()]);
  }

  private static void addThirdPartyAccount(final String trackType,
    final List<AccountDetails> accounts, final AccountDetails shipperAccountDetails,
    final AccountDetails recipientAccountDetails, final AccountDetails thirdPartyAccountDetails) {
    if (thirdPartyAccountDetails == null || !thirdPartyAccountDetails.getTrackTypeList()
      .contains(trackType)) {
      return;
    }

    if (shipperAccountDetails != null) {
      if (accounts.size() > 0 && thirdPartyAccountDetails.getAccountNumber()
        .equals(shipperAccountDetails.getAccountNumber())) {
        return;
      }
    }

    if (recipientAccountDetails != null) {
      if (accounts.size() > 0 && thirdPartyAccountDetails.getAccountNumber()
        .equals(recipientAccountDetails.getAccountNumber())) {
        return;
      }
    }

    accounts.add(thirdPartyAccountDetails);
  }
}

