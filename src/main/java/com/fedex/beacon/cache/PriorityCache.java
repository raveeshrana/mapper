package com.fedex.beacon.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import com.fedex.beacon.beans.AccountDetails;
import com.fedex.beacon.beans.AccountKey;
import com.fedex.beacon.database.ConnectionManager;
import com.fedex.beacon.utils.StringUtils;

/**
 * Priority cache is a singleton class.
 * Uses Ehache for caching the data. if the data is not present in cache it calls loadValue method.
 *
 * loadValue method calls the database query and puts in the cache.
 */
public class PriorityCache {

  /**
   * Get track types from database query.
   */
  private static final String ACCOUNT_TRACK_TYPE_QUERY = "SELECT LISTAGG(CN.EVENT_CD, ',') WITHIN GROUP (ORDER BY A.ACCOUNT_ID) \"TRACK_TYPE\""+ 
  " FROM FXTRACK_SCHEMA.CUST_ACCT_NBR A, FXTRACK_SCHEMA.CUST_NAME_EVENT_LIST CN, FXTRACK_SCHEMA.CONSIGNEE_ACCT_NUMBER_LIST CA" +
  " WHERE A.ACCOUNT_ACTIVE_FLG='Y'"+
  "   AND A.ACCOUNT_ID = CN.ACCOUNT_ID"+
  "   AND CA.ACCOUNT_ID = A.ACCOUNT_ID"+
  "   AND A.ACCOUNT_ID = ?"+
  "   AND CA.CON_TYPE = ?"+
  " GROUP BY A.ACCOUNT_ID,CA.CON_TYPE";

  private static Cache<AccountKey, AccountDetails> pCache;
  private static PriorityCache priorityCache;
  
  private PriorityCache() {
    final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
      .withCache("priority", CacheConfigurationBuilder
        .newCacheConfigurationBuilder(AccountKey.class, AccountDetails.class,
          ResourcePoolsBuilder.heap(1000).offheap(200, MemoryUnit.MB))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(360)))
        .withLoaderWriter(new CacheLoaderWriter<AccountKey, AccountDetails>() {
          @Override
          public AccountDetails load(final AccountKey accountKey) throws Exception {
            return loadValue(accountKey);
          }

          @Override
          public void write(final AccountKey accountKey, final AccountDetails accountDetails)
            throws Exception {
            return;
          }

          @Override
          public void delete(final AccountKey accountKey) throws Exception {
            return;
          }
        })).build();
    cacheManager.init();
    pCache = cacheManager.getCache("priority", AccountKey.class, AccountDetails.class);
  }

  /**
   * function that can be called to refresh the cache.
   */
  public void refresh(){
    pCache.clear();
  }

  /**
   * put the account in cache.
   *
   * @param accountNumber account number.
   * @param accountType accountType.
   * @param trackType trackType comma separated values.
   */
  public void put(final String accountNumber, final String accountType, final String trackType) {
    final AccountKey accountKey = new AccountKey(accountNumber, accountType);
    final AccountDetails accountDetails = new AccountDetails();
    accountDetails.setTrackType(new HashSet<>(Arrays.asList(trackType.split(","))));
    pCache.put(accountKey, accountDetails);
  }

  /**
   * gets the track types from list.
   *
   * @param key account key.
   * @return account details that has track type.
   */
  public AccountDetails get(final AccountKey key) {
    if (key == null || StringUtils.isEmpty(key.getAccountNumber()) || StringUtils
      .isEmpty(key.getAccountType())) {
      return null;
    }
    return pCache.get(key);
  }

  /**
   * makes the class singleton having single instance.
   * @return PriorityCache.
   */
  public static PriorityCache getPriorityCacheInstance() {
    if (priorityCache != null) {
      return priorityCache;
    }
    synchronized (PriorityCache.class) {
      if (priorityCache == null) {
        priorityCache = new PriorityCache();
      }
      return priorityCache;
    }
  }

  /**
   * function gets called if the vlaue is not found in cache. Query is hit to database and put in cache.
   * @param key Account Key.
   * @return AccountDetails.
   */
  public AccountDetails loadValue(final AccountKey key) {
    if (key == null || key.getAccountNumber() == null) {
      return null;
    }
    final String accountType = getConnectoinType(key.getAccountType());
    if(accountType == null){
      return null;
    }

    try (Connection con = ConnectionManager.getConnection()){
      final PreparedStatement statement = con.prepareStatement(ACCOUNT_TRACK_TYPE_QUERY);
      statement.setString(1, key.getAccountNumber());
      statement.setString(2, accountType);
      final ResultSet resultSet = statement.executeQuery();
      final AccountDetails accountDetails = new AccountDetails();
      while(resultSet.next()){
      final String result = resultSet.getString(1);
      final List<String> string = Arrays.asList(result.split(","));
      accountDetails.setTrackType(new HashSet<>(string));
      }
      return accountDetails;
    } catch (SQLException sql) {
      throw new RuntimeException("Error Executing query ", sql);
    } catch (Exception ex) {
      throw new RuntimeException("Could not get connection ", ex);
    }
    
  }

  /**
   * converts the connection Type from BW type to Database type.
   *  e.g S --> Shipper for query.
   * @param accountType
   * @return Database value of conn type.
   */
  private String getConnectoinType(final String accountType){
	  if(StringUtils.isEmpty(accountType)){
		  return null;
	  }
	  switch(accountType){
	  case AccountKey.SHIPPER_ACCOUNT:
		  return "Shipper";
	  case AccountKey.RECIPIENT_ACCOUNT:
		  return "Recipient";
	  case AccountKey.THIRD_PARTY_ACCOUNT:
		  return "Third Party";
	  default:
		  return null;
	  }
  }
}
