/*
 * Copyright (c) 2016-2017 by OpenText Corporation. All Rights Reserved.
 */
package com.opentext.ia.sdk.sip;

import java.util.Objects;
import java.util.function.Supplier;


/**
 * Generate Data Submission Session (DSS) IDs from a fixed prefix and a variable part.
 */
public abstract class PrefixDssIdSupplier implements Supplier<String> {

  private final String prefix;

  /**
   * Create an instance.
   * @param prefix The prefix for all generated DSS IDs
   */
  public PrefixDssIdSupplier(String prefix) {
    this.prefix = Objects.requireNonNull(prefix);
  }

  @Override
  public final String get() {
    return prefix + postfix();
  }

  protected abstract String postfix();

}
