package com.fedex.beacon.beans;

import java.io.Serializable;
import java.util.Objects;

public class AccountKey implements Serializable {

	private static final long serialVersionUID = 8556875816945525823L;

	public static final String SHIPPER_ACCOUNT = "S";
	public static final String RECIPIENT_ACCOUNT = "R";
	public static final String THIRD_PARTY_ACCOUNT = "T";

	private String accountNumber;
	private String accountType;

	public AccountKey() {

	}

	public AccountKey(final String accountNumber, final String accountType) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
	}

	/**
	 * Gets accountNumber.
	 *
	 * @return accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets accountNumber.
	 *
	 * @param accountNumber
	 *            accountNumber
	 */
	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets accountType.
	 *
	 * @return accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Sets accountType.
	 *
	 * @param accountType
	 *            accountType
	 */
	public void setAccountType(final String accountType) {
		this.accountType = accountType;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AccountKey))
			return false;
		AccountKey that = (AccountKey) o;
		return Objects.equals(getAccountNumber(), that.getAccountNumber())
				&& Objects.equals(getAccountType(), that.getAccountType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAccountNumber(), getAccountType());
	}
}
