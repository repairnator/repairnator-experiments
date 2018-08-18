-- rename column, need to rename FK too
alter table translation_kit_text_unit change column tm_text_unit_variant_id imported_tm_text_unit_variant_id bigint;
alter table translation_kit_text_unit drop foreign key `FK__TRANSLATION_KIT_TEXT_UNIT__TM_TEXT_UNIT_VARIANT__ID`;
alter table translation_kit_text_unit add constraint FK__TRANSLATION_KIT_TEXT_UNIT__IMPORTED_TM_TEXT_UNIT_VARIANT__ID foreign key (imported_tm_text_unit_variant_id) references tm_text_unit_variant (id);

-- Add new column
alter table translation_kit_text_unit add column exported_tm_text_unit_variant_id bigint;
alter table translation_kit_text_unit add constraint FK__TRANSLATION_KIT_TEXT_UNIT__EXPORTED_TM_TEXT_UNIT_VARIANT__ID foreign key (exported_tm_text_unit_variant_id) references tm_text_unit_variant (id);