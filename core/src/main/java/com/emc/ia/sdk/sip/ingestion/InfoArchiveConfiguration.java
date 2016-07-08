/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.ingestion;

public interface InfoArchiveConfiguration {

  String PREFIX = "ia.";
  String NAME = "name";

  String SERVER_PREFIX = PREFIX + "server.";
  String SERVER_AUTENTICATON_TOKEN = SERVER_PREFIX + "authentication.token";
  String SERVER_URI = SERVER_PREFIX + "uri";

  String TENANT_NAME = PREFIX + "tenant." + NAME;

  String XDB_PREFIX = PREFIX + "xdb.";
  String FEDERATION_PREFIX = XDB_PREFIX + "federation.";
  String FEDERATION_NAME = FEDERATION_PREFIX + NAME;
  String FEDERATION_SUPERUSER_PASSWORD = FEDERATION_PREFIX + "superuser.password";
  String FEDERATION_BOOTSTRAP = FEDERATION_PREFIX + "bootstrap.uri";

  String DATABASE = XDB_PREFIX + "database.";
  String DATABASE_NAME = DATABASE + NAME;
  String DATABASE_ADMIN_PASSWORD = DATABASE + "admin.password";

  String APPLICATION_PREFIX = PREFIX + "applicaton.";
  String APPLICATION_NAME = APPLICATION_PREFIX + NAME;

  String HOLDING_PREFIX = PREFIX + "holding.";
  String HOLDING_NAME = HOLDING_PREFIX + NAME;

  String AIC_PREFIX = PREFIX + "aic.";
  String AIC_NAME = AIC_PREFIX + NAME;

  String CRITERIA_PREFIX = AIC_PREFIX + "criteria.";
  String CRITERIA_NAME = CRITERIA_PREFIX + NAME;
  String CRITERIA_LABEL = CRITERIA_PREFIX + "label";
  String CRITERIA_TYPE = CRITERIA_PREFIX + "type";
  String CRITERIA_PKEYMINATTR = CRITERIA_PREFIX + "pkeyminattr";
  String CRITERIA_PKEYMAXATTR = CRITERIA_PREFIX + "pkeymaxattr";
  String CRITERIA_PKEYVALUESATTR = CRITERIA_PREFIX + "pkeyvaluesattr";
  String CRITERIA_INDEXED = CRITERIA_PREFIX + "indexed";

  String QUERY_PREFIX = PREFIX + "query.";
  String QUERY_NAME = QUERY_PREFIX + NAME;
  String QUERY_NAMESPACE_PREFIX_TEMPLATE = QUERY_PREFIX + "%s.namespace.prefix";
  String QUERY_NAMESPACE_URI_TEMPLATE = QUERY_PREFIX + "%s.namespace.uri";

  String QUERY_RESULT_ROOT_ELEMENT_TEMPLATE = QUERY_PREFIX + "%s.result.root.element";
  String QUERY_RESULT_ROOT_NS_ENABLED_TEMPLATE = QUERY_PREFIX + "%s.result.root.ns.enabled";
  String QUERY_RESULT_SCHEMA_TEMPLATE = QUERY_PREFIX + "%s.result.schema";

  String QUERY_XDBPDI_ENTITY_PATH_TEMPLATE = QUERY_PREFIX + "%s.xdbpdi.entity.path";
  String QUERY_XDBPDI_SCHEMA_TEMPLATE = QUERY_PREFIX + "%s.xdbpdi.schema";
  String QUERY_XDBPDI_TEMPLATE_TEMPLATE = QUERY_PREFIX + "%s.xdbpdi.template";

  String QUERY_XDBPDI_OPERAND_PREFIX = QUERY_PREFIX + "%s.xdbpdi[%s].operand.";
  String QUERY_XDBPDI_OPERAND_NAME = QUERY_XDBPDI_OPERAND_PREFIX + NAME;
  String QUERY_XDBPDI_OPERAND_PATH = QUERY_XDBPDI_OPERAND_PREFIX + "path";
  String QUERY_XDBPDI_OPERAND_TYPE = QUERY_XDBPDI_OPERAND_PREFIX + "type";
  String QUERY_XDBPDI_OPERAND_INDEX = QUERY_XDBPDI_OPERAND_PREFIX + "index";

  String QUOTA_PREFIX = PREFIX + "quota.";
  String QUOTA_NAME = QUOTA_PREFIX + NAME;
  String QUOTA_AIU = QUOTA_PREFIX + "aiu";
  String QUOTA_AIP = QUOTA_PREFIX + "aip";
  String QUOTA_DIP = QUOTA_PREFIX + "dip";

  String RESULT_HELPER_PREFIX = PREFIX + "result.helper.";
  String RESULT_HELPER_SCHEMA_TEMPLATE = RESULT_HELPER_PREFIX + "%s.result.schema";
  String RESULT_HELPER_NAME = RESULT_HELPER_PREFIX + NAME;

  String RETENTION_POLICY_NAME = PREFIX + "retention.policy.name";

  String PDI_PREFIX = PREFIX + "pdi.";
  String PDI_XML = PDI_PREFIX + "xml";
  String PDI_SCHEMA = PDI_PREFIX + "schema";
  String PDI_SCHEMA_NAME = PDI_SCHEMA + ".name";

  String JOB_PREFIX = PREFIX + "job.";
  String JOB_DEFINITION_NAME = JOB_PREFIX + "definition";
  String JOB_CONFIRMATION = "Confirmation";

  String INGEST_XML = PREFIX + "ingest.xml";

  String HTTP_CLIENT_CLASSNAME = PREFIX + "http.client";
}