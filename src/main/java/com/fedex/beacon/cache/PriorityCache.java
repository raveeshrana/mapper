package com.fedex.beacon.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

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

public class PriorityCache {

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

  public void refresh(){
    pCache.clear();
  }

  public void put(final String accountNumber, final String accountType, final String trackType) {
    final AccountKey accountKey = new AccountKey(accountNumber, accountType);
    final AccountDetails accountDetails = new AccountDetails();
    accountDetails.setTrackType(new HashSet<>(Arrays.asList(trackType.split(","))));
    pCache.put(accountKey, accountDetails);
  }

  public AccountDetails get(final AccountKey key) {
    if (key == null || StringUtils.isEmpty(key.getAccountNumber()) || StringUtils
      .isEmpty(key.getAccountType())) {
      return null;
    }
    return pCache.get(key);
  }

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

  public AccountDetails loadValue(final AccountKey key) {
    if (key == null || key.getAccountNumber() == null || key.getAccountType() == null) {
      return null;
    }
    try (Connection con = ConnectionManager.getConnection()){
      final PreparedStatement statement = con.prepareStatement("");
      final ResultSet resultSet = statement.executeQuery();
      final String result = resultSet.getString(1);
      final AccountDetails accountDetails = new AccountDetails();
      accountDetails.setTrackType(new HashSet<>(Arrays.asList(result.split(","))));
      return accountDetails;
    } catch (SQLException sql) {
      throw new RuntimeException("Error Executing query ", sql);
    } catch (Exception ex) {
      throw new RuntimeException("Could not get connection ", ex);
    }
  }
}
