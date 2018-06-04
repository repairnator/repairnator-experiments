alter table CM_COURSE_SET_CANON_ASSOC_T drop constraint FKBFCBD9AE7F976CD6;
alter table CM_COURSE_SET_CANON_ASSOC_T drop constraint FKBFCBD9AE2D306E01;
alter table CM_COURSE_SET_OFFERING_ASSOC_T drop constraint FK5B9A5CFD26827043;
alter table CM_COURSE_SET_OFFERING_ASSOC_T drop constraint FK5B9A5CFD2D306E01;
alter table CM_ENROLLMENT_SET_T drop constraint FK99479DD126827043;
alter table CM_ENROLLMENT_T drop constraint FK7A7F878E456D3EA1;
alter table CM_MEETING_T drop constraint FKE15DCD9BD0506F16;
alter table CM_MEMBERSHIP_T drop constraint FK9FBBBFE067131463;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC6661E50E9;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC6456D3EA1;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC626827043;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC63B0306B1;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC64F7C8841;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC6D05F59F1;
alter table CM_MEMBER_CONTAINER_T drop constraint FKD96A9BC649A68CB6;
alter table CM_OFFICIAL_INSTRUCTORS_T drop constraint FK470F8ACCC28CC1AD;
drop table CM_ACADEMIC_SESSION_T if exists;
drop table CM_COURSE_SET_CANON_ASSOC_T if exists;
drop table CM_COURSE_SET_OFFERING_ASSOC_T if exists;
drop table CM_CROSS_LISTING_T if exists;
drop table CM_ENROLLMENT_SET_T if exists;
drop table CM_ENROLLMENT_T if exists;
drop table CM_MEETING_T if exists;
drop table CM_MEMBERSHIP_T if exists;
drop table CM_MEMBER_CONTAINER_T if exists;
drop table CM_OFFICIAL_INSTRUCTORS_T if exists;
drop table CM_SEC_CATEGORY_T if exists;
create table CM_ACADEMIC_SESSION_T (ACADEMIC_SESSION_ID bigint generated by default as identity (start with 1), VERSION integer not null, LAST_MODIFIED_BY varchar(255), LAST_MODIFIED_DATE date, CREATED_BY varchar(255), CREATED_DATE date, ENTERPRISE_ID varchar(255) not null, TITLE varchar(255) not null, DESCRIPTION varchar(255) not null, START_DATE date, END_DATE date, primary key (ACADEMIC_SESSION_ID), unique (ENTERPRISE_ID));
create table CM_COURSE_SET_CANON_ASSOC_T (COURSE_SET bigint not null, CANON_COURSE bigint not null, primary key (COURSE_SET, CANON_COURSE));
create table CM_COURSE_SET_OFFERING_ASSOC_T (COURSE_SET bigint not null, COURSE_OFFERING bigint not null, primary key (COURSE_SET, COURSE_OFFERING));
create table CM_CROSS_LISTING_T (CROSS_LISTING_ID bigint generated by default as identity (start with 1), VERSION integer not null, LAST_MODIFIED_BY varchar(255), LAST_MODIFIED_DATE date, CREATED_BY varchar(255), CREATED_DATE date, primary key (CROSS_LISTING_ID));
create table CM_ENROLLMENT_SET_T (ENROLLMENT_SET_ID bigint generated by default as identity (start with 1), VERSION integer not null, LAST_MODIFIED_BY varchar(255), LAST_MODIFIED_DATE date, CREATED_BY varchar(255), CREATED_DATE date, ENTERPRISE_ID varchar(255) not null, TITLE varchar(255) not null, DESCRIPTION varchar(255) not null, CATEGORY varchar(255) not null, DEFAULT_CREDITS varchar(255) not null, COURSE_OFFERING bigint, primary key (ENROLLMENT_SET_ID), unique (ENTERPRISE_ID));
create table CM_ENROLLMENT_T (ENROLLMENT_ID bigint generated by default as identity (start with 1), VERSION integer not null, LAST_MODIFIED_BY varchar(255), LAST_MODIFIED_DATE date, CREATED_BY varchar(255), CREATED_DATE date, USER_ID varchar(255) not null, STATUS varchar(255) not null, CREDITS varchar(255) not null, GRADING_SCHEME varchar(255) not null, DROPPED bit, ENROLLMENT_SET bigint, primary key (ENROLLMENT_ID), unique (USER_ID, ENROLLMENT_SET));
create table CM_MEETING_T (MEETING_ID bigint generated by default as identity (start with 1), LOCATION varchar(255), START_TIME time, FINISH_TIME time, NOTES varchar(255), MONDAY bit, TUESDAY bit, WEDNESDAY bit, THURSDAY bit, FRIDAY bit, SATURDAY bit, SUNDAY bit, SECTION_ID bigint not null, primary key (MEETING_ID));
create table CM_MEMBERSHIP_T (MEMBER_ID bigint generated by default as identity (start with 1), VERSION integer not null, USER_ID varchar(255) not null, ROLE varchar(255) not null, MEMBER_CONTAINER_ID bigint, STATUS varchar(255), primary key (MEMBER_ID), unique (USER_ID, MEMBER_CONTAINER_ID));
create table CM_MEMBER_CONTAINER_T (MEMBER_CONTAINER_ID bigint generated by default as identity (start with 1), CLASS_DISCR varchar(100) not null, VERSION integer not null, LAST_MODIFIED_BY varchar(255), LAST_MODIFIED_DATE date, CREATED_BY varchar(255), CREATED_DATE date, ENTERPRISE_ID varchar(100) not null, TITLE varchar(255) not null, DESCRIPTION varchar(255) not null, CATEGORY varchar(255), COURSE_OFFERING bigint, ENROLLMENT_SET bigint, PARENT_SECTION bigint, MAXSIZE integer, PARENT_COURSE_SET bigint, CROSS_LISTING bigint, STATUS varchar(255), START_DATE date, END_DATE date, CANONICAL_COURSE bigint, ACADEMIC_SESSION bigint, primary key (MEMBER_CONTAINER_ID), unique (CLASS_DISCR, ENTERPRISE_ID));
create table CM_OFFICIAL_INSTRUCTORS_T (ENROLLMENT_SET_ID bigint not null, INSTRUCTOR_ID varchar(255), unique (ENROLLMENT_SET_ID, INSTRUCTOR_ID));
create table CM_SEC_CATEGORY_T (CAT_CODE varchar(255) not null, CAT_DESCR varchar(255), primary key (CAT_CODE));
alter table CM_COURSE_SET_CANON_ASSOC_T add constraint FKBFCBD9AE7F976CD6 foreign key (CANON_COURSE) references CM_MEMBER_CONTAINER_T;
alter table CM_COURSE_SET_CANON_ASSOC_T add constraint FKBFCBD9AE2D306E01 foreign key (COURSE_SET) references CM_MEMBER_CONTAINER_T;
alter table CM_COURSE_SET_OFFERING_ASSOC_T add constraint FK5B9A5CFD26827043 foreign key (COURSE_OFFERING) references CM_MEMBER_CONTAINER_T;
alter table CM_COURSE_SET_OFFERING_ASSOC_T add constraint FK5B9A5CFD2D306E01 foreign key (COURSE_SET) references CM_MEMBER_CONTAINER_T;
create index CM_ENR_SET_CO_IDX on CM_ENROLLMENT_SET_T (COURSE_OFFERING);
alter table CM_ENROLLMENT_SET_T add constraint FK99479DD126827043 foreign key (COURSE_OFFERING) references CM_MEMBER_CONTAINER_T;
create index CM_ENR_ENR_SET_IDX on CM_ENROLLMENT_T (ENROLLMENT_SET);
create index CM_ENR_USER on CM_ENROLLMENT_T (USER_ID);
alter table CM_ENROLLMENT_T add constraint FK7A7F878E456D3EA1 foreign key (ENROLLMENT_SET) references CM_ENROLLMENT_SET_T;
alter table CM_MEETING_T add constraint FKE15DCD9BD0506F16 foreign key (SECTION_ID) references CM_MEMBER_CONTAINER_T;
create index CM_MBR_CTR on CM_MEMBERSHIP_T (MEMBER_CONTAINER_ID);
create index CM_MBR_USER on CM_MEMBERSHIP_T (USER_ID);
alter table CM_MEMBERSHIP_T add constraint FK9FBBBFE067131463 foreign key (MEMBER_CONTAINER_ID) references CM_MEMBER_CONTAINER_T;
create index CM_SECTION_PARENT_IDX on CM_MEMBER_CONTAINER_T (PARENT_SECTION);
create index CM_SECTION_ENR_SET_IDX on CM_MEMBER_CONTAINER_T (ENROLLMENT_SET);
create index CM_COURSE_SET_PARENT_IDX on CM_MEMBER_CONTAINER_T (PARENT_COURSE_SET);
create index CM_CO_ACADEMIC_SESS_IDX on CM_MEMBER_CONTAINER_T (ACADEMIC_SESSION);
create index CM_CO_CANON_COURSE_IDX on CM_MEMBER_CONTAINER_T (CANONICAL_COURSE);
create index CM_SECTION_COURSE_IDX on CM_MEMBER_CONTAINER_T (COURSE_OFFERING);
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC6661E50E9 foreign key (ACADEMIC_SESSION) references CM_ACADEMIC_SESSION_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC6456D3EA1 foreign key (ENROLLMENT_SET) references CM_ENROLLMENT_SET_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC626827043 foreign key (COURSE_OFFERING) references CM_MEMBER_CONTAINER_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC63B0306B1 foreign key (PARENT_SECTION) references CM_MEMBER_CONTAINER_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC64F7C8841 foreign key (CROSS_LISTING) references CM_CROSS_LISTING_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC6D05F59F1 foreign key (CANONICAL_COURSE) references CM_MEMBER_CONTAINER_T;
alter table CM_MEMBER_CONTAINER_T add constraint FKD96A9BC649A68CB6 foreign key (PARENT_COURSE_SET) references CM_MEMBER_CONTAINER_T;
create index CM_INSTR_IDX on CM_OFFICIAL_INSTRUCTORS_T (INSTRUCTOR_ID);
alter table CM_OFFICIAL_INSTRUCTORS_T add constraint FK470F8ACCC28CC1AD foreign key (ENROLLMENT_SET_ID) references CM_ENROLLMENT_SET_T;
