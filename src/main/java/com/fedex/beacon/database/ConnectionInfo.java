package com.fedex.beacon.database;

public class ConnectionInfo {
  private String databaseUrl;
  private String jdbcDriver;
  private int connectionTimeOut;
  private int maximumConnections;
  private String userName;
  private String password;

  /**
   * Gets databaseUrl.
   *
   * @return databaseUrl
   */
  public String getDatabaseUrl() {
    return databaseUrl;
  }

  /**
   * Sets databaseUrl.
   *
   * @param databaseUrl databaseUrl
   */
  public void setDatabaseUrl(final String databaseUrl) {
    this.databaseUrl = databaseUrl;
  }

  /**
   * Gets jdbcDriver.
   *
   * @return jdbcDriver
   */
  public String getJdbcDriver() {
    return jdbcDriver;
  }

  /**
   * Sets jdbcDriver.
   *
   * @param jdbcDriver jdbcDriver
   */
  public void setJdbcDriver(final String jdbcDriver) {
    this.jdbcDriver = jdbcDriver;
  }

  /**
   * Gets connectionTimeOut.
   *
   * @return connectionTimeOut
   */
  public int getConnectionTimeOut() {
    return connectionTimeOut;
  }

  /**
   * Sets connectionTimeOut.
   *
   * @param connectionTimeOut connectionTimeOut
   */
  public void setConnectionTimeOut(final int connectionTimeOut) {
    this.connectionTimeOut = connectionTimeOut;
  }

  /**
   * Gets maximumConnections.
   *
   * @return maximumConnections
   */
  public int getMaximumConnections() {
    return maximumConnections;
  }

  /**
   * Sets maximumConnections.
   *
   * @param maximumConnections maximumConnections
   */
  public void setMaximumConnections(final int maximumConnections) {
    this.maximumConnections = maximumConnections;
  }

  /**
   * Gets userName.
   *
   * @return userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets userName.
   *
   * @param userName userName
   */
  public void setUserName(final String userName) {
    this.userName = userName;
  }

  /**
   * Gets password.
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password password
   */
  public void setPassword(final String password) {
    this.password = password;
  }
}
