--
-- Adding table for storing packages.
--

CREATE TABLE `package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `probe_id` varchar(30) NOT NULL,
  `provider` varchar(60) NOT NULL,
  `package_description` varchar(120) NOT NULL,
  `type` varchar(30) NOT NULL,
  `advertised_name` varchar(120) NOT NULL,
  `technology` varchar(30) NOT NULL,
  `threshold` int NOT NULL,
  `download` int NOT NULL,
  `upload` int NOT NULL,
  `throttle` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;