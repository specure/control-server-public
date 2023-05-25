--
-- Making columns download, upload, throttle, threshold non nullable.
--

UPDATE `package` SET `download` = 0 WHERE `download` IS NULL;
UPDATE `package` SET `upload` = 0 WHERE `upload` IS NULL;
UPDATE `package` SET `throttle` = 0 WHERE `throttle` IS NULL;
UPDATE `package` SET `threshold` = 0 WHERE `threshold` IS NULL;

ALTER TABLE `package` MODIFY `download` int NOT NULL;
ALTER TABLE `package` MODIFY `upload` int NOT NULL;
ALTER TABLE `package` MODIFY `throttle` int NOT NULL;