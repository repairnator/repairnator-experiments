CREATE TABLE "ORGANIZATIONS" (
  "UUID" VARCHAR(40) NOT NULL PRIMARY KEY,
  "KEE" VARCHAR(32) NOT NULL,
  "NAME" VARCHAR(64) NOT NULL,
  "DESCRIPTION" VARCHAR(256),
  "URL" VARCHAR(256),
  "AVATAR_URL" VARCHAR(256),
  "GUARDED" BOOLEAN,
  "DEFAULT_PERM_TEMPLATE_PROJECT" VARCHAR(40),
  "DEFAULT_PERM_TEMPLATE_VIEW" VARCHAR(40),
  "CREATED_AT" BIGINT NOT NULL,
  "UPDATED_AT" BIGINT NOT NULL
);
CREATE UNIQUE INDEX "PK_ORGANIZATIONS" ON "ORGANIZATIONS" ("UUID");
CREATE UNIQUE INDEX "ORGANIZATION_KEY" ON "ORGANIZATIONS" ("KEE");
