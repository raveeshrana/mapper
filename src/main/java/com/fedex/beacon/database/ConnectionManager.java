package com.fedex.beacon.database;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Connection Manager singleton class.
 */
public class ConnectionManager {

  private static HikariDataSource ds;

  public static void init(final String databaseUrl, final String driverClass, final String userName, final String password) {
    try {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(databaseUrl);
      config.setUsername(userName);
      config.setPassword(password);
      config.setConnectionTimeout(60*1000);
      config.setDriverClassName(driverClass);
      config.setMaximumPoolSize(128);
      config.setMaximumPoolSize(10);
      config.setIdleTimeout(10 * 60 * 1000);
      config.setReadOnly(true);
      ds = new HikariDataSource(config);
    } catch (Exception ex) {
      throw new RuntimeException("Not able to connect to database."+ ex.getCause());
    }
  }

  public static Connection getConnection() throws Exception {
    return ds.getConnection();
  }
}
