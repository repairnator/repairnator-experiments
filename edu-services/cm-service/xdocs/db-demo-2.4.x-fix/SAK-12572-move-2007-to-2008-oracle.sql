-- Emergency fix to enable course site creation in 2.4.x demo systems.

update CM_ACADEMIC_SESSION_T set START_DATE='01-JAN-2008', END_DATE='01-APR-2008' where START_DATE='01-JAN-2007';
update CM_ACADEMIC_SESSION_T set START_DATE='01-APR-2008', END_DATE='01-JUN-2008' where START_DATE='01-APR-2007';
update CM_ACADEMIC_SESSION_T set START_DATE='01-JUN-2008', END_DATE='01-SEP-2008' where START_DATE='01-JUN-2007';
update CM_ACADEMIC_SESSION_T set START_DATE='01-SEP-2008', END_DATE='01-JAN-2009' where START_DATE='01-SEP-2007';

update CM_MEMBER_CONTAINER_T set START_DATE='01-JAN-2008', END_DATE='01-APR-2008' where START_DATE='01-JAN-2007';
update CM_MEMBER_CONTAINER_T set START_DATE='01-APR-2008', END_DATE='01-JUN-2008' where START_DATE='01-APR-2007';
update CM_MEMBER_CONTAINER_T set START_DATE='01-JUN-2008', END_DATE='01-SEP-2008' where START_DATE='01-JUN-2007';
update CM_MEMBER_CONTAINER_T set START_DATE='01-SEP-2008', END_DATE='01-JAN-2009' where START_DATE='01-SEP-2007';
