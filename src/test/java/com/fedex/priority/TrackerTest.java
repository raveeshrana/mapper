package com.fedex.priority;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fedex.beacon.priority.AccountCache;
import com.fedex.beacon.priority.AccountDetails;
import com.fedex.beacon.priority.MapAccount;

public class TrackerTest {

  @BeforeClass()
  public static void setup(){
    final List<AccountDetails> detailsList = new ArrayList<>();

    AccountDetails details = new AccountDetails();
    details.setAccountNumber("12345");
    details.setAccountType("S");
    details.setTrackType("01,02,03");
    detailsList.add(details);

    details = new AccountDetails();
    details.setAccountNumber("12346");
    details.setAccountType("R");
    details.setTrackType("01");
    detailsList.add(details);


    details = new AccountDetails();
    details.setAccountNumber("12348");
    details.setAccountType("T");
    details.setTrackType("01,04");
    detailsList.add(details);

    details = new AccountDetails();
    details.setAccountNumber("12345");
    details.setAccountType("T");
    details.setTrackType("04");
    detailsList.add(details);


    AccountCache.put(detailsList.toArray(new AccountDetails[detailsList.size()]));
  }

  @Test()
  public void test(){
    final AccountDetails [] detailsList = MapAccount
      .getPublishAccountList("12345", "12346", "12348", "01");
    assertEquals(3, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12346", detailsList[1].getAccountNumber());
    assertEquals("R", detailsList[1].getAccountType());

    assertEquals("12348", detailsList[2].getAccountNumber());
    assertEquals("T", detailsList[2].getAccountType());
  }
}
