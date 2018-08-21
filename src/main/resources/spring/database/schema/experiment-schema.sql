
-- ALTER TABLE ExperimentTasks ALTER COLUMN experimentType VARCHAR(20) 

CREATE TABLE IF NOT EXISTS ExperimentTasks (
id int GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
experimentType VARCHAR(20),
matching VARCHAR(50),
annotatorName VARCHAR(100),
datasetName VARCHAR(100),
state int,
lastChanged TIMESTAMP,
version VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Experiments (
  id VARCHAR(300) NOT NULL,
  taskId int NOT NULL,
  PRIMARY KEY (id, taskId)
);

DROP INDEX IF EXISTS ExperimentTaskConfig;
CREATE INDEX ExperimentTaskConfig ON ExperimentTasks (matching,experimentType,annotatorName,datasetName);

UPDATE ExperimentTasks SET experimentType='D2KB' WHERE experimentType='D2W';
UPDATE ExperimentTasks SET experimentType='A2KB' WHERE experimentType='A2W';
UPDATE ExperimentTasks SET experimentType='Sa2KB' WHERE experimentType='Sa2W';
UPDATE ExperimentTasks SET experimentType='C2KB' WHERE experimentType='C2W';
UPDATE ExperimentTasks SET experimentType='Sc2KB' WHERE experimentType='Sc2W';
UPDATE ExperimentTasks SET experimentType='Rc2KB' WHERE experimentType='Rc2W';
UPDATE ExperimentTasks SET annotatorName='Babelfy' WHERE annotatorName='BabelFy';

-- Changes from version 1.1.0 to OKE2015
CREATE TABLE IF NOT EXISTS ExperimentResultsDouble (
resultId int NOT NULL,
taskId int NOT NULL,
value double,
PRIMARY KEY (resultId, taskId)
);

CREATE TABLE IF NOT EXISTS ExperimentResultsInt (
resultId int NOT NULL,
taskId int NOT NULL,
value int,
PRIMARY KEY (resultId, taskId)
);

CREATE TABLE IF NOT EXISTS ExperimentTasks_SubTasks (
taskId int NOT NULL,
subTaskId int NOT NULL,
PRIMARY KEY (taskId, subTaskId)
);
