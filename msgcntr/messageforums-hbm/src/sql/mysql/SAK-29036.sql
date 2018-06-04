CREATE TABLE MFR_RANK_INDIVIDUAL_T (
  RANK_ID BIGINT(20) NOT NULL,
  USER_ID VARCHAR(99) NOT NULL,
  PRIMARY KEY (RANK_ID,USER_ID),
  KEY FK_MFR_RANK_INDIVIDUAL (RANK_ID),
  CONSTRAINT FK_MFR_RANK_INDIVIDUAL FOREIGN KEY (RANK_ID) REFERENCES MFR_RANK_T (ID)
);


CREATE TABLE TMP_DIGITS (DIGIT INT(1));

INSERT INTO TMP_DIGITS
VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

CREATE TABLE TMP_SEQUENCE (SEQ INT(3));

INSERT INTO TMP_SEQUENCE (
  SELECT D1.DIGIT + D2.DIGIT * 10
  FROM TMP_DIGITS D1 JOIN TMP_DIGITS D2
);

INSERT INTO MFR_RANK_INDIVIDUAL_T (RANK_ID, USER_ID)
(
  SELECT R.ID, M.USER_ID
  FROM TMP_SEQUENCE S 
    JOIN MFR_RANK_T R
    INNER JOIN SAKAI_USER_ID_MAP M 
      ON TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(R.ASSIGNTO, ';', S.SEQ), ';', -1)) = M.EID
  WHERE S.SEQ BETWEEN 1 AND
    ( SELECT 1 + LENGTH(R.ASSIGNTO) - LENGTH(REPLACE(R.ASSIGNTO, ';', '')))
);

ALTER TABLE MFR_RANK_T
  DROP COLUMN ASSIGNTODISPLAY,
  DROP COLUMN ASSIGNTO;

DROP TABLE TMP_DIGITS;
DROP TABLE TMP_SEQUENCE;
