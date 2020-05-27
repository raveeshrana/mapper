package com.fedex.priority;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;

import com.fedex.beacon.beans.AccountKey;
import com.fedex.beacon.cache.PriorityCache;
import com.fedex.beacon.database.ConnectionManager;
import com.fedex.beacon.service.AccountService;

public class TrackerIntegrationTest {

	private static AccountService accountService;

	@BeforeClass()
	public static void setup() {
		ConnectionManager.init("jdbc:oracle:thin:@ldap://oid.inf.fedex.com:3060/FXTRACK_SVC1_L0,cn=OracleContext", "oracle.jdbc.OracleDriver", "FXTRACK_RO_APP", "doC4C1TbXcOeBVtOkqLK2igts");
		accountService = new AccountService(PriorityCache.getPriorityCacheInstance());
	}

	//@Test() 
	public void publishAll3() {
		final AccountKey[] detailsList = accountService.getPublishAccountList("12345", "12346", "12348", "01");
		assertEquals(3, detailsList.length);
		assertEquals("12345", detailsList[0].getAccountNumber());
		assertEquals("S", detailsList[0].getAccountType());

		assertEquals("12346", detailsList[1].getAccountNumber());
		assertEquals("R", detailsList[1].getAccountType());

		assertEquals("12348", detailsList[2].getAccountNumber());
		assertEquals("T", detailsList[2].getAccountType());
	}

}
