--
-- Update zero values for download, upload, throttle and threshold.
--

UPDATE `package` SET `download` = 1 WHERE `download` = 0;
UPDATE `package` SET `upload` = 1 WHERE `upload` = 0;
UPDATE `package` SET `throttle` = 1 WHERE `throttle` = 0;
UPDATE `package` SET `threshold` = 1 WHERE `threshold` = 0;