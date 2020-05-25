package com.fedex.priority;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fedex.beacon.beans.AccountKey;
import com.fedex.beacon.service.AccountService;
import com.fedex.beacon.cache.PriorityCache;

public class TrackerTest {

  @BeforeAll()
  public static void setup(){
    final PriorityCache priorityCache = PriorityCache.getPriorityCacheInstance();
    priorityCache.put("12345", "S", "01,02,03");
    priorityCache.put("12346", "R", "01");
    priorityCache.put("12348", "T", "01,04");
    priorityCache.put("12348", "S", "01,04");
    priorityCache.put("12348", "R", "01,04");
    priorityCache.put("12345", "T", "04");
  }

  @Test()
  public void publishAll3(){
    final AccountKey[] detailsList = AccountService
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
    final AccountKey[] detailsList = AccountService
      .getPublishAccountList("12345", "1234", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());
  }

  @Test()
  public void publishRecipientAndThirdParty(){
    final AccountKey[] detailsList = AccountService
      .getPublishAccountList("1234", "12346", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12346", detailsList[0].getAccountNumber());
    assertEquals("R", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());
  }

  @Test()
  public void publishShipperAndRecipientParty(){
    final AccountKey[] detailsList = AccountService
      .getPublishAccountList("12348", "12346", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12348", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12346", detailsList[1].getAccountNumber());
    assertEquals("R", detailsList[1].getAccountType());
  }

  @Test()
  public void publishRecipientAndShipperParty(){
    final AccountKey[] detailsList = AccountService
      .getPublishAccountList("12345", "12348", "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("R", detailsList[1].getAccountType());
  }

  @Test()
  public void testNull(){
     AccountKey[] detailsList = AccountService
      .getPublishAccountList("12345", null, "12348", "01");
    assertEquals(2, detailsList.length);
    assertEquals("12345", detailsList[0].getAccountNumber());
    assertEquals("S", detailsList[0].getAccountType());

    assertEquals("12348", detailsList[1].getAccountNumber());
    assertEquals("T", detailsList[1].getAccountType());

     detailsList = AccountService
      .getPublishAccountList("", "", "", "01");
    assertEquals(0, detailsList.length);

  }
}
