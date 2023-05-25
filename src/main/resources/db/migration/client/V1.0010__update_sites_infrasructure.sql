--
-- Adding table for storing sites.
--
DROP TABLE IF EXISTS `site`;

CREATE TABLE `site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `advertised_id` varchar(30) NOT NULL,
  `address` varchar(30) NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `name` varchar(120) NOT NULL,
  `latitude` FLOAT NOT NULL,
  `longitude` FLOAT NOT NULL,
  `active` boolean NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
