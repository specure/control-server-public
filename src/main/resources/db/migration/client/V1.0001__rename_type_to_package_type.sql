--
-- Renaming column type to package_type in Package table.
--

ALTER TABLE package CHANGE COLUMN `type` `package_type` varchar(30) NOT NULL;
