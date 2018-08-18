create table image (id bigint not null auto_increment, created_date datetime, last_modified_date datetime, content longblob, name varchar(255), primary key (id));
create table screenshot (id bigint not null auto_increment, created_date datetime, comment longtext, name varchar(255), sequence bigint, src longtext, status varchar(32), locale_id bigint, screenshot_run_id bigint, primary key (id));
create table screenshot_run (id bigint not null auto_increment, created_date datetime, last_successful_run bit, name varchar(255), repository_id bigint not null, primary key (id));
create table screenshot_text_unit (id bigint not null auto_increment, name varchar(255), number_of_match integer, rendered_target longtext, source longtext, target longtext, screenshot_id bigint not null, tm_text_unit_id bigint, tm_text_unit_variant_id bigint, primary key (id));
alter table image add constraint UK__IMAGE__NAME unique (name);
alter table screenshot add constraint UK__SCREENSHOT__NAME__LOCALE__SCREENSHOT_RUN_ID unique (name, locale_id, screenshot_run_id);
alter table screenshot_run add constraint UK__SCREENSHOT_RUN__NAME unique (name);
alter table screenshot add constraint FK__SCREENSHOT__LOCALE__ID foreign key (locale_id) references locale (id);
alter table screenshot add constraint FK__SCREENSHOT__SCREENSHOT_RUN__ID foreign key (screenshot_run_id) references screenshot_run (id);
alter table screenshot_run add constraint FK__SCREENSHOT_RUN__REPOSITORY__ID foreign key (repository_id) references repository (id);
alter table screenshot_text_unit add constraint FK__SCREENSHOT_TEXT_UNIT__SCREENSHOT__ID foreign key (screenshot_id) references screenshot (id);
alter table screenshot_text_unit add constraint FK__SCREENSHOT_TEXT_UNIT__TM_TEXT_UNIT__ID foreign key (tm_text_unit_id) references tm_text_unit (id);
alter table screenshot_text_unit add constraint FK__SCREENSHOT_TEXT_UNIT__TM_TEXT_UNIT_VARIANT__ID foreign key (tm_text_unit_variant_id) references tm_text_unit_variant (id);
