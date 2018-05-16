package com.linkedin.thirdeye.anomalydetection.model.merge;

import java.util.Properties;

public abstract class AbstractMergeModel implements MergeModel {
  protected Properties properties;

  @Override
  public void init(Properties properties) {
    this.properties = properties;
  }

  @Override
  public Properties getProperties() {
    return this.properties;
  }
}
