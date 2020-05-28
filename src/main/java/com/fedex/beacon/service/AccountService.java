package com.fedex.beacon.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fedex.beacon.beans.AccountDetails;
import com.fedex.beacon.beans.AccountKey;
import com.fedex.beacon.cache.PriorityCache;

/**
 * Account Service class .. Checks the request account number with database
 * and decides if notification needs to be sent to those accounts.
 */
public class AccountService implements Serializable{

  private PriorityCache priorityCache;

  /**
   * Gets the singleton instance of PriorityCache.
   */
  public AccountService() {
    this(PriorityCache.getPriorityCacheInstance());
  }

  public AccountService(final PriorityCache priorityCache) {
    this.priorityCache = priorityCache;
  }

  /**
   *
   * @param shipperAccountNumber Shipper Account number.
   * @param recipientAccountNumber Recipient Account number.
   * @param thirdPartyAccountNumber Third Party Account number.
   * @param trackType Account type.
   * @return Array of Account key accounts. which is combination of account number and account type.
   */
  public AccountKey[] getPublishAccountList(final String shipperAccountNumber,
    final String recipientAccountNumber, final String thirdPartyAccountNumber,
    final String trackType) {
    final List<AccountKey> accounts = new ArrayList<>();

    /*
     * Make the account key for given input variables.
     */
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

  /**
   * Add the account to list to be returned if account track type is contained in the database.
   *
   * @param accountKey combination of account number and account type.
   * @param trackType track type in request.
   * @param accountKeys keys that need to get the notification.
   */
  private void addAccount(final AccountKey accountKey, final String trackType,
    final List<AccountKey> accountKeys) {
    final AccountDetails account = priorityCache.get(accountKey);

    if (account == null || account.getTrackType() == null || !account.getTrackType().contains(trackType)) {
      return;
    }
    accountKeys.add(accountKey);
  }

  /**
   * Add third party account to the list .. if the account number is not same as shipper or recipientAccount number.
   * @param thirdPartyAccountKey third party account key.
   * @param trackType request track type.
   * @param accounts list of accounts to be notified.
   */
  private void addThirdPartyAccount(final AccountKey thirdPartyAccountKey, final String trackType,
    final List<AccountKey> accounts) {
    final AccountDetails account = priorityCache.get(thirdPartyAccountKey);
    if (account == null || account.getTrackType() == null || !account.getTrackType().contains(trackType)) {
      return;
    }

    /*
     * Check if the notified list already has that account number.
     */
    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getAccountNumber().equals(thirdPartyAccountKey.getAccountNumber())) {
        return;
      }
    }
    accounts.add(thirdPartyAccountKey);
  }
}
