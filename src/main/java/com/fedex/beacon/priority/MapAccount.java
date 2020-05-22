package com.fedex.beacon.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapAccount {

	public static AccountKey[] getPublishAccountList(final String shipperAccountNumber,
			final String recipientAccountNumber, final String thirdPartyAccountNumber, final String trackType) {
		final List<AccountKey> accounts = new ArrayList<AccountKey>();

		final AccountKey shipperAccountKey = new AccountKey(shipperAccountNumber, AccountKey.SHIPPER_ACCOUNT);
		final AccountKey recipientAccountKey = new AccountKey(recipientAccountNumber, AccountKey.RECIPIENT_ACCOUNT);
		final AccountKey thirdPartyAccountKey = new AccountKey(thirdPartyAccountNumber, AccountKey.THIRD_PARTY_ACCOUNT);

		addAccount(shipperAccountKey, trackType, accounts);
		addAccount(recipientAccountKey, trackType, accounts);
		addThirdPartyAccount(thirdPartyAccountKey, trackType, accounts);

		return accounts.toArray(new AccountKey[accounts.size()]);
	}

	private static void addAccount(final AccountKey accountKey, final String trackType,
			final List<AccountKey> accountKeys) {
		final Set<String> trackTypeList = AccountCache.get(accountKey);

		if (trackTypeList == null || !trackTypeList.contains(trackType)) {
			return;
		}
		accountKeys.add(accountKey);
	}

	private static void addThirdPartyAccount(final AccountKey thirdPartyAccountKey, final String trackType,
			final List<AccountKey> accounts) {
		if (thirdPartyAccountKey.getAccountNumber() == null) {
			return;
		}
		final Set<String> trackTypeList = AccountCache.get(thirdPartyAccountKey);
		if (trackTypeList == null || !trackTypeList.contains(trackType)) {
			return;
		}

		for (int i = 0; i < accounts.size(); i++) {
			if (thirdPartyAccountKey.getAccountNumber().equals(accounts.get(i).getAccountNumber())) {
				return;
			}
		}
		accounts.add(thirdPartyAccountKey);
	}
}
