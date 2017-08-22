CREATE TABLE "RESOURCE_INDEX" (
  "ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "KEE" VARCHAR(400) NOT NULL,
  "POSITION" INTEGER NOT NULL,
  "NAME_SIZE" INTEGER NOT NULL,
  "COMPONENT_UUID" VARCHAR(50) NOT NULL,
  "ROOT_COMPONENT_UUID" VARCHAR(50) NOT NULL,
  "QUALIFIER" VARCHAR(10) NOT NULL
);
CREATE INDEX "RESOURCE_INDEX_KEY" ON "RESOURCE_INDEX" ("KEE");
CREATE INDEX "RESOURCE_INDEX_COMPONENT" ON "RESOURCE_INDEX" ("COMPONENT_UUID");

