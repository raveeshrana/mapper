package com.fedex.beacon.beans;

import java.io.Serializable;
import java.util.Set;

public class AccountDetails implements Serializable {

  private static final long serialVersionUID = 8556875816945525821L;

  private Set<String> trackType;

  /**
   * Gets trackType.
   *
   * @return trackType
   */
  public Set<String> getTrackType() {
    return trackType;
  }

  /**
   * Sets trackType.
   *
   * @param trackType trackType
   */
  public void setTrackType(final Set<String> trackType) {
    this.trackType = trackType;
  }
}
