CREATE TABLE "SNAPSHOTS" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  // NULLABLE
  "UUID" VARCHAR(50),
  "CREATED_AT" BIGINT,
  "BUILD_DATE" BIGINT,
  "COMPONENT_UUID" VARCHAR(50) NOT NULL,
  "PARENT_SNAPSHOT_ID" INTEGER,
  "STATUS" VARCHAR(4) NOT NULL DEFAULT 'U',
  "PURGE_STATUS" INTEGER,
  "ISLAST" BOOLEAN NOT NULL DEFAULT FALSE,
  "SCOPE" VARCHAR(3),
  "QUALIFIER" VARCHAR(10),
  "ROOT_SNAPSHOT_ID" INTEGER,
  "VERSION" VARCHAR(500),
  "PATH" VARCHAR(500),
  "DEPTH" INTEGER,
  "ROOT_COMPONENT_UUID" VARCHAR(50) NOT NULL,
  "PERIOD1_MODE" VARCHAR(100),
  "PERIOD1_PARAM" VARCHAR(100),
  "PERIOD1_DATE" BIGINT,
  "PERIOD2_MODE" VARCHAR(100),
  "PERIOD2_PARAM" VARCHAR(100),
  "PERIOD2_DATE" BIGINT,
  "PERIOD3_MODE" VARCHAR(100),
  "PERIOD3_PARAM" VARCHAR(100),
  "PERIOD3_DATE" BIGINT,
  "PERIOD4_MODE" VARCHAR(100),
  "PERIOD4_PARAM" VARCHAR(100),
  "PERIOD4_DATE" BIGINT,
  "PERIOD5_MODE" VARCHAR(100),
  "PERIOD5_PARAM" VARCHAR(100),
  "PERIOD5_DATE" BIGINT
);
