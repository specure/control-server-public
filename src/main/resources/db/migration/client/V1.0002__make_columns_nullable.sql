--
-- Making download, upload and throttle nullable.
--

ALTER TABLE `package` MODIFY `download` int;
ALTER TABLE `package` MODIFY `upload` int;
ALTER TABLE `package` MODIFY `throttle` int;