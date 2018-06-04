
-- Preload with default reports (STAT-35)

--   0) Activity total (Show activity in site, with totals per event.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report0_title}','${predefined_report0_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>event</howChartSource><howChartType>pie</howChartType><howLimitedMaxResults>false</howLimitedMaxResults><howMaxResults>0</howMaxResults><howPresentationMode>how-presentation-both</howPresentationMode><howSort>true</howSort><howSortAscending>true</howSortAscending><howSortBy>event</howSortBy><howTotalsBy><howTotalsBy>event</howTotalsBy></howTotalsBy><siteId/><what>what-events</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-all</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   1) Most accessed files (Show top 10 most accessed files.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report1_title}','${predefined_report1_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>resource</howChartSource><howChartType>pie</howChartType><howLimitedMaxResults>true</howLimitedMaxResults><howMaxResults>10</howMaxResults><howPresentationMode>how-presentation-both</howPresentationMode><howSort>true</howSort><howSortAscending>false</howSortAscending><howSortBy>total</howSortBy><howTotalsBy><howTotalsBy>resource</howTotalsBy></howTotalsBy><siteId/><what>what-resources</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>true</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>read</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-all</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   2) Most active users (Show top 10 users with most activity in site.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report2_title}','${predefined_report2_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>user</howChartSource><howChartType>pie</howChartType><howLimitedMaxResults>true</howLimitedMaxResults><howMaxResults>10</howMaxResults><howPresentationMode>how-presentation-both</howPresentationMode><howSort>true</howSort><howSortAscending>false</howSortAscending><howSortBy>total</howSortBy><howTotalsBy><howTotalsBy>user</howTotalsBy></howTotalsBy><siteId/><what>what-events</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-all</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   3) Less active users (Show top 10 users with less activity in site.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report3_title}','${predefined_report3_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>user</howChartSource><howChartType>bar</howChartType><howLimitedMaxResults>false</howLimitedMaxResults><howMaxResults>0</howMaxResults><howPresentationMode>how-presentation-both</howPresentationMode><howSort>true</howSort><howSortAscending>true</howSortAscending><howSortBy>total</howSortBy><howTotalsBy><howTotalsBy>user</howTotalsBy></howTotalsBy><siteId/><what>what-events</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-all</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   4) Users with more visits (Show top 10 users who have most visited the site.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report4_title}','${predefined_report4_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>user</howChartSource><howChartType>bar</howChartType><howLimitedMaxResults>false</howLimitedMaxResults><howMaxResults>0</howMaxResults><howPresentationMode>how-presentation-both</howPresentationMode><howSort>true</howSort><howSortAscending>false</howSortAscending><howSortBy>total</howSortBy><howTotalsBy><howTotalsBy>user</howTotalsBy></howTotalsBy><siteId/><what>what-visits</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-all</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   5) Users with no visits (Show users who have never visited the site.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report5_title}','${predefined_report5_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesSource>total</howChartSeriesSource><howChartSource>event</howChartSource><howChartType>bar</howChartType><howLimitedMaxResults>false</howLimitedMaxResults><howMaxResults>0</howMaxResults><howPresentationMode>how-presentation-table</howPresentationMode><howSort>false</howSort><howSortAscending>false</howSortAscending><howSortBy>default</howSortBy><howTotalsBy><howTotalsBy>user</howTotalsBy></howTotalsBy><siteId/><what>what-visits</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-none</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
--   6) Users with no activity (Show users with no activity in site.)
insert  into `SST_REPORTS`(`SITE_ID`,`TITLE`,`DESCRIPTION`,`HIDDEN`,`REPORT_DEF`,`CREATED_BY`,`CREATED_ON`,`MODIFIED_BY`,`MODIFIED_ON`) values (NULL,'${predefined_report6_title}','${predefined_report6_description}',0,'<?xml version=\'1.0\' ?><ReportParams><howChartCategorySource>none</howChartCategorySource><howChartSeriesPeriod>byday</howChartSeriesPeriod><howChartSeriesSource>total</howChartSeriesSource><howChartSource>event</howChartSource><howChartType>bar</howChartType><howLimitedMaxResults>false</howLimitedMaxResults><howMaxResults>0</howMaxResults><howPresentationMode>how-presentation-table</howPresentationMode><howSort>false</howSort><howSortAscending>true</howSortAscending><howSortBy>default</howSortBy><howTotalsBy><howTotalsBy>user</howTotalsBy></howTotalsBy><siteId/><what>what-events</what><whatEventIds/><whatEventSelType>what-events-bytool</whatEventSelType><whatLimitedAction>false</whatLimitedAction><whatLimitedResourceIds>false</whatLimitedResourceIds><whatResourceAction>new</whatResourceAction><whatResourceIds/><whatToolIds><whatToolIds>all</whatToolIds></whatToolIds><when>when-all</when><whenFrom/><whenTo/><who>who-none</who><whoGroupId/><whoRoleId>access</whoRoleId><whoUserIds/></ReportParams>','preload',now(),'preload',now());
