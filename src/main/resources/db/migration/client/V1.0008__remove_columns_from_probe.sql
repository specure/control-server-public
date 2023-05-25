--
-- Removing `name` and `serial_number` columns.
--

ALTER TABLE `probe`
    DROP COLUMN `name`,
    DROP COLUMN `serial_number`,
    DROP INDEX `UC_SerialNumber`;

TRUNCATE TABLE `probe`;
