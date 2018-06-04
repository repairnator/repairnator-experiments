-- Gradebook table changes between Sakai 2.3.* and 2.4.

-- Add grade commments.
create table GB_COMMENT_T (
	ID bigint generated by default as identity (start with 1),
	VERSION integer not null,
	GRADER_ID varchar(255) not null,
	STUDENT_ID varchar(255) not null,
	COMMENT_TEXT longvarchar,
	DATE_RECORDED timestamp not null,
	GRADABLE_OBJECT_ID bigint not null,
	primary key (ID),
	unique (STUDENT_ID, GRADABLE_OBJECT_ID));

-- Remove database-caching of calculated course grades.
alter table GB_GRADE_RECORD_T drop column SORT_GRADE;
