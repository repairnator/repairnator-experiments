alter table ACT_HI_COMMENT 
add TYPE_ NVARCHAR2(255);

alter table ACT_HI_COMMENT 
add ACTION_ NVARCHAR2(255);

alter table ACT_HI_COMMENT 
add FULL_MSG_ BLOB;

alter table ACT_HI_TASKINST 
add OWNER_ NVARCHAR2(64);

alter table ACT_HI_TASKINST 
add PARENT_TASK_ID_ NVARCHAR2(64);
