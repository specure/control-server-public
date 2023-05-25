-- ALTER DATABASE sah CHARACTER SET utf8 COLLATE utf8_general_ci;

ALTER TABLE `site`
CHANGE COLUMN `address` `address` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL,
CHANGE COLUMN `advertised_id` `advertised_id` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ;
