--
-- Adding `probe_keep_alive` table.
--

CREATE TABLE `probe_keep_alive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `probe_id` varchar(30) NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `port` int NOT NULL,
  `live_time` bigint(30),
  `tested_ip` varchar(30),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;