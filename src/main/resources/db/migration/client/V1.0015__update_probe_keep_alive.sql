--
-- Updating `probe_keep_alive` table. Update port to varchar
--

ALTER TABLE `probe_keep_alive` MODIFY `port` varchar(30) NOT NULL;