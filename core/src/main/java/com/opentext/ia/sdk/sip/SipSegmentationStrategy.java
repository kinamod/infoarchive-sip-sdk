/*
 * Copyright (c) 2016-2017 by OpenText Corporation. All Rights Reserved.
 */
package com.opentext.ia.sdk.sip;

import java.util.Arrays;

/**
 * Strategy for segmenting domain objects into different SIPs.
 * @param <D> The type of domain objects to segment into different SIPs
 */
@FunctionalInterface
public interface SipSegmentationStrategy<D> {

  /**
   * Determine whether to start a new SIP.
   * @param domainObject The domain object to be added to either the current SIP or a new one
   * @param metrics Metrics about the assembly of the current SIP
   * @return <code>true</code> if a new SIP should be started for the given domain object, or <code>false</code> if
   *         the domain object should be stored in the current SIP
   */
  boolean shouldStartNewSip(D domainObject, SipMetrics metrics);

  /**
   * Return a {@linkplain SipSegmentationStrategy} that allows a maximum number of AIUs per SIP.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param maxAiusPerSip The maximum number of AIUs that may go into a SIP
   * @return A {@linkplain SipSegmentationStrategy} that allows a maximum number of AIUs per SIP
   */
  static <D> SipSegmentationStrategy<D> byMaxAius(long maxAiusPerSip) {
    return (domainObject, metrics) -> metrics.numAius() % maxAiusPerSip == 0;
  }

  /**
   * Return a {@linkplain SipSegmentationStrategy} that allows a maximum number of digital objects per SIP.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param maxDigitalObjects The maximum number of AIUs that may go into a SIP
   * @return A {@linkplain SipSegmentationStrategy} that allows a maximum number of digital objects per SIP
   */
  static <D> SipSegmentationStrategy<D> byMaxDigitalObjects(long maxDigitalObjects) {
    return (domainObject, metrics) -> metrics.numDigitalObjects() % maxDigitalObjects == 0;
  }

  /**
   * Return a {@linkplain SipSegmentationStrategy} that allows a maximum size (uncompressed) of the PDI per SIP.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param maxSize The maximum size of the PDI
   * @return A {@linkplain SipSegmentationStrategy} that allows a maximum size of the PDI per SIP
   */
  static <D> SipSegmentationStrategy<D> byMaxPdiSize(long maxSize) {
    return (domainObject, metrics) -> metrics.pdiSize() >= maxSize;
  }

  /**
   * Return a {@linkplain SipSegmentationStrategy} that allows a maximum total size (uncompressed) of digital objects
   * per SIP.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param maxSize The maximum size of the digital objects
   * @return A {@linkplain SipSegmentationStrategy} that allows a maximum total size of digital objects per SIP
   */
  static <D> SipSegmentationStrategy<D> byMaxDigitalObjectsSize(long maxSize) {
    return (domainObject, metrics) -> metrics.digitalObjectsSize() >= maxSize;
  }

  /**
   * Return a {@linkplain SipSegmentationStrategy} that allows a maximum total size (uncompressed) of the SIP.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param maxSize The maximum size of the SIP
   * @return A {@linkplain SipSegmentationStrategy} that allows a maximum total size of the SIP
   */
  static <D> SipSegmentationStrategy<D> byMaxSipSize(long maxSize) {
    return (domainObject, metrics) -> metrics.sipSize() >= maxSize;
  }

  /**
   * Return a {@linkplain SipSegmentationStrategy} that combines a number of partial strategies.
   * @param <D> The type of domain objects to segment into different SIPs
   * @param partialStrategies The partial strategies to combine
   * @return A {@linkplain SipSegmentationStrategy} that combines a number of partial strategies
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <D> SipSegmentationStrategy<D> combining(SipSegmentationStrategy<D>... partialStrategies) {
    return (domainObject, metrics) -> Arrays.stream(partialStrategies)
      .anyMatch(s -> s.shouldStartNewSip(domainObject, metrics));
  }

}
