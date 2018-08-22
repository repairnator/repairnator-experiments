CREATE TABLE LEGO_SET ( id BIGINT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(30));
CREATE TABLE MANUAL ( id BIGINT AUTO_INCREMENT PRIMARY KEY, LEGO_SET BIGINT, CONTENT VARCHAR(2000));

ALTER TABLE MANUAL ADD FOREIGN KEY (LEGO_SET)
REFERENCES LEGO_SET(id);
