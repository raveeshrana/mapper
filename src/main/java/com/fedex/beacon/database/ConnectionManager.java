package com.fedex.beacon.database;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionManager {

  private static HikariDataSource ds;

  public static void init(final ConnectionInfo info) {
    try {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(info.getDatabaseUrl());
      config.setUsername(info.getUserName());
      config.setPassword(info.getPassword());
      config.setConnectionTimeout(60*1000);
      config.setDriverClassName(info.getJdbcDriver());
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
