alter table ACT_RU_TASK 
    add CATEGORY_ varchar(255);
    
alter table ACT_RU_EXECUTION drop foreign key ACT_FK_EXE_PROCDEF;  	

alter table ACT_RU_EXECUTION drop index ACT_UNIQ_RU_BUS_KEY;  

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF 
    foreign key (PROC_DEF_ID_) 
    references ACT_RE_PROCDEF (ID_);

#  
# ACT-1867: MySQL DATETIME and TIMESTAMP precision
# The way this is done, is by creating a new column, pumping over all data
# and then removing the old column.
#

# DEPLOY_TIME_ in ACT_RE_DEPLOYMENT

ALTER TABLE ACT_RE_DEPLOYMENT ADD DEPLOY_TIME_TEMP_ timestamp(3);
UPDATE ACT_RE_DEPLOYMENT SET DEPLOY_TIME_TEMP_ = DEPLOY_TIME_;
ALTER TABLE ACT_RE_DEPLOYMENT DROP COLUMN DEPLOY_TIME_;
ALTER TABLE ACT_RE_DEPLOYMENT CHANGE DEPLOY_TIME_TEMP_ DEPLOY_TIME_ timestamp(3);


# CREATE_TIME_ in ACT_RE_MODEL

ALTER TABLE ACT_RE_MODEL ADD CREATE_TIME_TEMP_ timestamp(3) null;
UPDATE ACT_RE_MODEL SET CREATE_TIME_TEMP_ = CREATE_TIME_; 
ALTER TABLE ACT_RE_MODEL DROP COLUMN CREATE_TIME_;
ALTER TABLE ACT_RE_MODEL CHANGE CREATE_TIME_TEMP_ CREATE_TIME_ timestamp(3) null;


# LAST_UPDATE_TIME_ in ACT_RE_MODEL

ALTER TABLE ACT_RE_MODEL ADD LAST_UPDATE_TIME_TEMP_ timestamp(3) null;
UPDATE ACT_RE_MODEL SET LAST_UPDATE_TIME_TEMP_ = LAST_UPDATE_TIME_; 
ALTER TABLE ACT_RE_MODEL DROP COLUMN LAST_UPDATE_TIME_;
ALTER TABLE ACT_RE_MODEL CHANGE LAST_UPDATE_TIME_TEMP_ LAST_UPDATE_TIME_ timestamp(3) null;


# LOCK_EXP_TIME_ in ACT_RU_JOB

ALTER TABLE ACT_RU_JOB ADD LOCK_EXP_TIME_TEMP_ timestamp(3) null;
UPDATE ACT_RU_JOB SET LOCK_EXP_TIME_TEMP_ = LOCK_EXP_TIME_; 
ALTER TABLE ACT_RU_JOB DROP COLUMN LOCK_EXP_TIME_;
ALTER TABLE ACT_RU_JOB CHANGE LOCK_EXP_TIME_TEMP_ LOCK_EXP_TIME_ timestamp(3) null;


# DUEDATE_ in ACT_RU_JOB

ALTER TABLE ACT_RU_JOB ADD DUEDATE_TEMP_ timestamp(3) null;
UPDATE ACT_RU_JOB SET DUEDATE_TEMP_ = DUEDATE_; 
ALTER TABLE ACT_RU_JOB DROP COLUMN DUEDATE_;
ALTER TABLE ACT_RU_JOB CHANGE DUEDATE_TEMP_ DUEDATE_ timestamp(3) null;


# CREATE_TIME_ in ACT_RU_TASK

ALTER TABLE ACT_RU_TASK ADD CREATE_TIME_TEMP_ timestamp(3);
UPDATE ACT_RU_TASK SET CREATE_TIME_TEMP_ = CREATE_TIME_; 
ALTER TABLE ACT_RU_TASK DROP COLUMN CREATE_TIME_;
ALTER TABLE ACT_RU_TASK CHANGE CREATE_TIME_TEMP_ CREATE_TIME_ timestamp(3);


# DUE_DATE_ in ACT_RU_TASK

ALTER TABLE ACT_RU_TASK ADD DUE_DATE_TEMP_ datetime(3);
UPDATE ACT_RU_TASK SET DUE_DATE_TEMP_ = DUE_DATE_; 
ALTER TABLE ACT_RU_TASK DROP COLUMN DUE_DATE_;
ALTER TABLE ACT_RU_TASK CHANGE DUE_DATE_TEMP_ DUE_DATE_ datetime(3);


# CREATED_ in ACT_RU_EVENT_SUBSCR

ALTER TABLE ACT_RU_EVENT_SUBSCR ADD CREATED_TEMP_ timestamp(3) not null;
UPDATE ACT_RU_EVENT_SUBSCR SET CREATED_TEMP_ = CREATED_; 
ALTER TABLE ACT_RU_EVENT_SUBSCR DROP COLUMN CREATED_;
ALTER TABLE ACT_RU_EVENT_SUBSCR CHANGE CREATED_TEMP_ CREATED_ timestamp(3) not null DEFAULT CURRENT_TIMESTAMP(3);


alter table ACT_RE_DEPLOYMENT 
    add TENANT_ID_ varchar(255) default ''; 
    
alter table ACT_RE_PROCDEF 
    add TENANT_ID_ varchar(255) default '';
    
alter table ACT_RU_EXECUTION
    add TENANT_ID_ varchar(255) default '';    
    
alter table ACT_RU_TASK
    add TENANT_ID_ varchar(255) default '';  
    
alter table ACT_RU_JOB
    add TENANT_ID_ varchar(255) default ''; 
    
alter table ACT_RE_MODEL
    add TENANT_ID_ varchar(255) default '';   
    
alter table ACT_RU_EVENT_SUBSCR
   add TENANT_ID_ varchar(255) default '';  
   
alter table ACT_RU_EVENT_SUBSCR
   add PROC_DEF_ID_ varchar(64);          
    
alter table ACT_RE_PROCDEF
    drop index ACT_UNIQ_PROCDEF;
    
alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
    unique (KEY_,VERSION_, TENANT_ID_);  


update ACT_GE_PROPERTY set VALUE_ = '5.15' where NAME_ = 'schema.version';
