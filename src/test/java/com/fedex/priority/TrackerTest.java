package com.fedex.priority;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fedex.beacon.priority.AccountCache;
import com.fedex.beacon.priority.AccountKey;
import com.fedex.beacon.priority.MapAccount;

public class TrackerTest {

  @BeforeClass()
  public static void setup(){

    AccountCache.put("12345", "S", "01,02,03");
    AccountCache.put("12346", "R", "01");
    AccountCache.put("12348", "T", "01,04");
    AccountCache.put("12345", "T", "04");
  }

  @Test()
  public void test(){
    final AccountKey[] detailsList = MapAccount
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
