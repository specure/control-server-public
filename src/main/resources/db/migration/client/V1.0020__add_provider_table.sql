--
-- Adding table for storing providers.
--

CREATE TABLE `provider` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

UPDATE package set provider = 1;
UPDATE measurement_server set provider = 1;

ALTER TABLE `package` MODIFY `provider` bigint(20);
ALTER TABLE `measurement_server` MODIFY `provider` bigint(20);
