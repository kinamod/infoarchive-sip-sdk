/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.client;

import static com.emc.ia.sdk.configurer.InfoArchiveConfiguration.APPLICATION_NAME;
import static com.emc.ia.sdk.configurer.InfoArchiveConfiguration.HTTP_CLIENT_CLASSNAME;
import static com.emc.ia.sdk.configurer.InfoArchiveConfiguration.SERVER_URI;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import com.emc.ia.sdk.configurer.InfoArchiveConfiguration;
import com.emc.ia.sdk.configurer.InfoArchiveConfigurers;
import com.emc.ia.sdk.sip.client.dto.Aics;
import com.emc.ia.sdk.sip.client.dto.Application;
import com.emc.ia.sdk.sip.client.dto.Applications;
import com.emc.ia.sdk.sip.client.dto.Services;
import com.emc.ia.sdk.sip.client.dto.Tenant;
import com.emc.ia.sdk.sip.client.rest.ArchiveOperationsByApplicationResourceCache;
import com.emc.ia.sdk.sip.client.rest.InfoArchiveLinkRelations;
import com.emc.ia.sdk.sip.client.rest.InfoArchiveRestClient;
import com.emc.ia.sdk.support.NewInstance;
import com.emc.ia.sdk.support.http.HttpClient;
import com.emc.ia.sdk.support.http.apache.ApacheHttpClient;
import com.emc.ia.sdk.support.io.RuntimeIoException;
import com.emc.ia.sdk.support.rest.RestClient;

/**
 * Factory methods for creating ArchiveClient.
 */
public final class ArchiveClients {

  private ArchiveClients() {
  }

  /**
   * Installs, of necessary, the application and holding artifacts based on the details in the configuration map then
   * returns an ArchiveClient instance.
   * @param configuration The configuration map.
   * @return An ArchiveClient
   */
  public static ArchiveClient withPropertyBasedAutoConfiguration(Map<String, String> configuration) {
    return withPropertyBasedAutoConfiguration(configuration, Optional.empty());
  }

  /**
   * Installs, of necessary, the application and holding artifacts based on the details in the configuration map then
   * returns an ArchiveClient instance.
   * @param configuration The configuration map.
   * @param restClient The RestClient used to interact with the InfoArchive REST api.
   * @return An ArchiveClient
   */
  public static ArchiveClient withPropertyBasedAutoConfiguration(Map<String, String> configuration,
      RestClient restClient) {
    return withPropertyBasedAutoConfiguration(configuration, Optional.of(restClient));
  }

  private static ArchiveClient withPropertyBasedAutoConfiguration(Map<String, String> configuration,
      Optional<RestClient> potentialClient) {
    RestClient client = potentialClient.orElseGet(() -> createRestClient(configuration));
    InfoArchiveConfigurers.propertyBased(configuration, client)
      .configure();
    String billboardUrl = getBillboadUrl(configuration);
    String applicationName = getApplicationName(configuration);
    return client(client, billboardUrl, applicationName);
  }

  /**
   * Creates a new ArchiveClient instance without installing any artifacts in the archive using the default RestClient.
   * @param billboardUrl The URL entry point to the InfoArchive REST api.
   * @param applicationName The name of the application to create the ArchiveClient for.
   * @return An ArchiveClient
   */
  public static ArchiveClient client(String billboardUrl, String applicationName) {
    RestClient restClient = createDefaultRestClient();
    return new InfoArchiveRestClient(restClient, appResourceCache(restClient, billboardUrl, applicationName));
  }

  /**
   * Creates a new ArchiveClient instance without installing any artifacts in the archive using the sdk configuration
   * file.
   * @return An ArchiveClient
   */
  public static ArchiveClient client() {
    Map<String, String> configuration = getSdkConfiguration();
    RestClient restClient = createRestClient(configuration);
    String billboardUrl = getBillboadUrl(configuration);
    String applicationName = getApplicationName(configuration);
    return new InfoArchiveRestClient(restClient, appResourceCache(restClient, billboardUrl, applicationName));
  }

  /**
   * Creates a new ArchiveClient instance without installing any artifacts in the archive.
   * @param configuration The configuration map.
   * @return An ArchiveClient
   */
  public static ArchiveClient client(Map<String, String> configuration) {
    RestClient restClient = createRestClient(configuration);
    String billboardUrl = getBillboadUrl(configuration);
    String applicationName = getApplicationName(configuration);
    return new InfoArchiveRestClient(restClient, appResourceCache(restClient, billboardUrl, applicationName));
  }

  /**
   * Creates a new ArchiveClient instance without installing any artifacts in the archive.
   * @param restClient The RestClient used to interact with the InfoArchive REST api.
   * @param billboardUrl The URL entry point to the InfoArchive REST api.
   * @param applicationName The name of the application to create the ArchiveClient for.
   * @return An ArchiveClient
   */
  public static ArchiveClient client(RestClient restClient, String billboardUrl, String applicationName) {
    return new InfoArchiveRestClient(restClient, appResourceCache(restClient, billboardUrl, applicationName));
  }

  private static ArchiveOperationsByApplicationResourceCache appResourceCache(RestClient restClient,
      String billboardUrl, String applicationName) {
    try {
      ArchiveOperationsByApplicationResourceCache resourceCache =
          new ArchiveOperationsByApplicationResourceCache(applicationName);
      Services services = restClient.get(billboardUrl, Services.class);
      Tenant tenant = getTenant(restClient, services);
      Application application = getApplication(restClient, tenant, applicationName);
      cacheResourceUris(resourceCache, restClient, application);
      return resourceCache;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  private static Tenant getTenant(RestClient restClient, Services services) throws IOException {
    return Objects.requireNonNull(restClient.follow(services, InfoArchiveLinkRelations.LINK_TENANT, Tenant.class),
        "Tenant not found.");
  }

  private static Application getApplication(RestClient restClient, Tenant tenant, String applicationName)
      throws IOException {
    Applications applications =
        restClient.follow(tenant, InfoArchiveLinkRelations.LINK_APPLICATIONS, Applications.class);
    return Objects.requireNonNull(applications.byName(applicationName),
        "Application named " + applicationName + " not found.");
  }

  private static void cacheResourceUris(ArchiveOperationsByApplicationResourceCache resourceCache,
      RestClient restClient, Application application) throws IOException {
    Aics aics = restClient.follow(application, InfoArchiveLinkRelations.LINK_AICS, Aics.class);

    Map<String, String> dipResourceUriByAicName = new HashMap<>();
    aics.getItems()
      .forEach(aic -> dipResourceUriByAicName.put(aic.getName(), aic.getUri(InfoArchiveLinkRelations.LINK_DIP)));
    resourceCache.setDipResourceUriByAicName(dipResourceUriByAicName);
    resourceCache.setCiResourceUri(application.getUri(InfoArchiveLinkRelations.LINK_CI));
    resourceCache.setAipResourceUri(application.getUri(InfoArchiveLinkRelations.LINK_AIPS));
  }

  private static RestClient createDefaultRestClient() {
    Map<String, String> configuration = getSdkConfiguration();
    return createRestClient(configuration);
  }

  private static Map<String, String> getSdkConfiguration() {
    try (InputStream stream = ClientConfigurationFinder.find()) {
      return asMap(stream);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  private static Map<String, String> asMap(InputStream stream) throws IOException {
    Properties properties = new Properties();
    properties.load(stream);
    Map<String, String> map = new HashMap<String, String>();
    for (Map.Entry<Object, Object> e : properties.entrySet()) {
      map.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
    }
    return map;
  }

  private static RestClient createRestClient(Map<String, String> configuration) {
    HttpClient httpClient =
        NewInstance.fromConfiguration(configuration, HTTP_CLIENT_CLASSNAME, ApacheHttpClient.class.getName())
          .as(HttpClient.class);
    RestClient client = new RestClient(httpClient);
    client.init(configuration.get(InfoArchiveConfiguration.SERVER_AUTENTICATON_TOKEN));
    return client;
  }

  private static String getApplicationName(Map<String, String> configuration) {
    return Objects.requireNonNull(configuration.get(APPLICATION_NAME),
        "The property " + APPLICATION_NAME + " cannot be null or empty.");
  }

  private static String getBillboadUrl(Map<String, String> configuration) {
    return Objects.requireNonNull(configuration.get(SERVER_URI),
        "The property " + SERVER_URI + " cannot be null or empty.");
  }
}
