--
-- Adding probe to Package table.
--

ALTER TABLE `package` MODIFY `probe_id` varchar(30);
ALTER TABLE `package` ADD COLUMN `probe_port` varchar(30);