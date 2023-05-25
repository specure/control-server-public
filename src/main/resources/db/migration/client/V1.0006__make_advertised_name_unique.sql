--
-- Making advertised name unique.
--

TRUNCATE TABLE `package`;
ALTER TABLE `package` ADD UNIQUE (`advertised_name`);