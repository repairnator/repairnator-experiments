DROP SCHEMA IF EXISTS `dbtbl_0_slave_0`;
CREATE SCHEMA `dbtbl_0_slave_0`;
DROP TABLE IF EXISTS `t_order_0`;
DROP TABLE IF EXISTS `t_order_1`;
DROP TABLE IF EXISTS `t_order_2`;
DROP TABLE IF EXISTS `t_order_3`;
DROP TABLE IF EXISTS `t_order_item_0`;
DROP TABLE IF EXISTS `t_order_item_1`;
DROP TABLE IF EXISTS `t_order_item_2`;
DROP TABLE IF EXISTS `t_order_item_3`;
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_order_0` (`order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_id`));
CREATE TABLE `t_order_1` (`order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_id`));
CREATE TABLE `t_order_2` (`order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_id`));
CREATE TABLE `t_order_3` (`order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_id`));
CREATE TABLE `t_order_item_0` (`order_item_id` INT NOT NULL, `order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_item_id`));
CREATE TABLE `t_order_item_1` (`order_item_id` INT NOT NULL, `order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_item_id`));
CREATE TABLE `t_order_item_2` (`order_item_id` INT NOT NULL, `order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_item_id`));
CREATE TABLE `t_order_item_3` (`order_item_id` INT NOT NULL, `order_id` INT NOT NULL, `user_id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`order_item_id`));
CREATE TABLE `t_config` (`id` INT NOT NULL, `status` VARCHAR(45) NULL, PRIMARY KEY (`id`));
