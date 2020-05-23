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
    AccountCache.put("12348", "S", "01,04");
    AccountCache.put("12348", "R", "01,04");
    AccountCache.put("12345", "T", "04");
  }

  @Test()
  public void publishAll3(){
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

  @Test()
  public void publishShipperAndThirdParty(){
    final AccountKey[] detailsList = MapAccount
      .getPublishAccountList("12345", "1234", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());
  }

  @Test()
  public void publishRecipientAndThirdParty(){
    final AccountKey[] detailsList = MapAccount
      .getPublishAccountList("1234", "12346", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12346", detailsList[0].getAccountNumber());
    assertEquals("R", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());
  }

  @Test()
  public void publishShipperAndRecipientParty(){
    final AccountKey[] detailsList = MapAccount
      .getPublishAccountList("12348", "12346", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12348", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12346", detailsList[1].getAccountNumber());
    assertEquals("R", detailsList[1].getAccountType());
  }

  @Test()
  public void publishRecipientAndShipperParty(){
    final AccountKey[] detailsList = MapAccount
      .getPublishAccountList("12345", "12348", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("R", detailsList[1].getAccountType());
  }

  @Test()
  public void testNull(){
     AccountKey[] detailsList = MapAccount
      .getPublishAccountList("12345", null, "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());

     detailsList = MapAccount
      .getPublishAccountList("", "", "", "01");
    assertEquals(0, detailsList.length);

  }
}
