package com.fedex.beacon.priority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AccountDetails implements Serializable {
  /**
   * Serializable UUID
   */
  private static final long serialVersionUID = 6636589016238006452L;
  public static final String SHIPPER_ACCOUNT = "S";
  public static final String RECIPIENT_ACCOUNT = "R";
  public static final String THIRD_PARTY_ACCOUNT = "T";

  private String accountNumber;
  private String trackType;
  private String accountType;
  private transient Set<String> trackTypeList;

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getTrackType() {
    return trackType;
  }

  public void setTrackType(String trackType) {
    this.trackType = trackType;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public Set<String> getTrackTypeList() {
    if (trackTypeList == null) {
      synchronized (this) {
        if (trackTypeList == null) {
          trackTypeList = new HashSet<String>(Arrays.asList(trackType.split(",")));
        }
      }

    }
    return trackTypeList;
  }
}

