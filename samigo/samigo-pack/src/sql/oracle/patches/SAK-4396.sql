drop table SAM_ATTACHMENT_T cascade constraints;
drop table SAM_PUBLISHEDATTACHMENT_T cascade constraints;
drop sequence SAM_ATTACHMENT_ID_S;
drop sequence SAM_PUBLISHEDATTACHMENT_ID_S;
create table SAM_ATTACHMENT_T (ATTACHMENTID number(19,0) not null, ATTACHMENTTYPE varchar2(255 char) not null, RESOURCEID varchar(255), FILENAME varchar(255), MIMETYPE varchar(80), FILESIZE integer, DESCRIPTION varchar(4000), LOCATION varchar(4000), ISLINK integer, STATUS integer not null, CREATEDBY varchar(36) not null, CREATEDDATE timestamp not null, LASTMODIFIEDBY varchar(36) not null, LASTMODIFIEDDATE timestamp not null, ASSESSMENTID number(19,0), SECTIONID number(19,0), ITEMID number(19,0), primary key (ATTACHMENTID));
create table SAM_PUBLISHEDATTACHMENT_T (ATTACHMENTID number(19,0) not null, ATTACHMENTTYPE varchar2(255 char) not null, RESOURCEID varchar(255), FILENAME varchar(255), MIMETYPE varchar(80), FILESIZE integer, DESCRIPTION varchar(4000), LOCATION varchar(4000), ISLINK integer, STATUS integer not null, CREATEDBY varchar(36) not null, CREATEDDATE timestamp not null, LASTMODIFIEDBY varchar(36) not null, LASTMODIFIEDDATE timestamp not null, ASSESSMENTID number(19,0), SECTIONID number(19,0), ITEMID number(19,0), primary key (ATTACHMENTID));
alter table SAM_ATTACHMENT_T add constraint FK99FA8CB8CAC2365B foreign key (ASSESSMENTID) references SAM_ASSESSMENTBASE_T;
alter table SAM_ATTACHMENT_T add constraint FK99FA8CB83288DBBD foreign key (ITEMID) references SAM_ITEM_T;
alter table SAM_ATTACHMENT_T add constraint FK99FA8CB870CE2BD foreign key (SECTIONID) references SAM_SECTION_T;
alter table SAM_PUBLISHEDATTACHMENT_T add constraint FK270998869482C945 foreign key (ASSESSMENTID) references SAM_PUBLISHEDASSESSMENT_T;
alter table SAM_PUBLISHEDATTACHMENT_T add constraint FK2709988631446627 foreign key (ITEMID) references SAM_PUBLISHEDITEM_T;
alter table SAM_PUBLISHEDATTACHMENT_T add constraint FK27099886895D4813 foreign key (SECTIONID) references SAM_PUBLISHEDSECTION_T;
create sequence SAM_ATTACHMENT_ID_S;
create sequence SAM_PUBLISHEDATTACHMENT_ID_S;
