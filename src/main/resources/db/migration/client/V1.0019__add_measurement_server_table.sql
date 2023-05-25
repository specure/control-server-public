--
-- Adding table for storing packages.
--

CREATE TABLE `measurement_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `name` varchar(60) NOT NULL,
  `web_address` varchar(30) NOT NULL,
  `port` int,
  `port_ssl` int,
  `secret_key` varchar(120),
  `provider` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
