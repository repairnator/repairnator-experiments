CREATE TABLE "ORGANIZATIONS" (
  "UUID" VARCHAR(40) NOT NULL PRIMARY KEY,
  "KEE" VARCHAR(32) NOT NULL,
  "NAME" VARCHAR(64) NOT NULL,
  "DESCRIPTION" VARCHAR(256),
  "URL" VARCHAR(256),
  "AVATAR_URL" VARCHAR(256),
  "GUARDED" BOOLEAN NOT NULL,
  "USER_ID" INTEGER,
  "DEFAULT_PERM_TEMPLATE_PROJECT" VARCHAR(40),
  "DEFAULT_PERM_TEMPLATE_VIEW" VARCHAR(40),
  "CREATED_AT" BIGINT NOT NULL,
  "UPDATED_AT" BIGINT NOT NULL
);
CREATE UNIQUE INDEX "PK_ORGANIZATIONS" ON "ORGANIZATIONS" ("UUID");
CREATE UNIQUE INDEX "ORGANIZATION_KEY" ON "ORGANIZATIONS" ("KEE");

CREATE TABLE "INTERNAL_PROPERTIES" (
  "KEE" VARCHAR(50) NOT NULL PRIMARY KEY,
  "IS_EMPTY" BOOLEAN NOT NULL,
  "TEXT_VALUE" VARCHAR(4000),
  "CLOB_VALUE" CLOB,
  "CREATED_AT" BIGINT
);
CREATE UNIQUE INDEX "UNIQ_INTERNAL_PROPERTIES" ON "INTERNAL_PROPERTIES" ("KEE");

CREATE TABLE "RULES" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "PLUGIN_RULE_KEY" VARCHAR(200) NOT NULL,
  "PLUGIN_NAME" VARCHAR(255) NOT NULL,
  "DESCRIPTION" VARCHAR(16777215),
  "DESCRIPTION_FORMAT" VARCHAR(20),
  "PRIORITY" INTEGER,
  "IS_TEMPLATE" BOOLEAN DEFAULT FALSE,
  "TEMPLATE_ID" INTEGER,
  "PLUGIN_CONFIG_KEY" VARCHAR(200),
  "NAME" VARCHAR(200),
  "STATUS" VARCHAR(40),
  "LANGUAGE" VARCHAR(20),
  "NOTE_DATA" CLOB(2147483647),
  "NOTE_USER_LOGIN" VARCHAR(255),
  "NOTE_CREATED_AT" TIMESTAMP,
  "NOTE_UPDATED_AT" TIMESTAMP,
  "REMEDIATION_FUNCTION" VARCHAR(20),
  "DEF_REMEDIATION_FUNCTION" VARCHAR(20),
  "REMEDIATION_GAP_MULT" VARCHAR(20),
  "DEF_REMEDIATION_GAP_MULT" VARCHAR(20),
  "REMEDIATION_BASE_EFFORT" VARCHAR(20),
  "DEF_REMEDIATION_BASE_EFFORT" VARCHAR(20),
  "GAP_DESCRIPTION" VARCHAR(4000),
  "TAGS" VARCHAR(4000),
  "SYSTEM_TAGS" VARCHAR(4000),
  "RULE_TYPE" TINYINT,
  "CREATED_AT" BIGINT,
  "UPDATED_AT" BIGINT
);
CREATE UNIQUE INDEX "RULES_REPO_KEY" ON "RULES" ("PLUGIN_NAME", "PLUGIN_RULE_KEY");

CREATE TABLE "RULES_METADATA" (
  "RULE_ID" INTEGER NOT NULL,
  "ORGANIZATION_UUID" VARCHAR(40) NOT NULL,
  "NOTE_DATA" CLOB(2147483647),
  "NOTE_USER_LOGIN" VARCHAR(255),
  "NOTE_CREATED_AT" BIGINT,
  "NOTE_UPDATED_AT" BIGINT,
  "REMEDIATION_FUNCTION" VARCHAR(20),
  "REMEDIATION_GAP_MULT" VARCHAR(20),
  "REMEDIATION_BASE_EFFORT" VARCHAR(20),
  "TAGS" VARCHAR(4000),
  "CREATED_AT" BIGINT NOT NULL,
  "UPDATED_AT" BIGINT NOT NULL,
  CONSTRAINT PK_RULES_METADATA PRIMARY KEY (RULE_ID,ORGANIZATION_UUID)
);
