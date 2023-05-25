--
-- Adding `probe` table.
--

CREATE TABLE `probe` (
  `id` varchar(30) NOT NULL,
  `created_date` datetime NOT NULL,
  `modified_date` datetime NOT NULL,
  `name` varchar(120) NOT NULL,
  `serial_number` varchar(120) NOT NULL,
  `type` varchar(60) NOT NULL,
  `status` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UC_SerialNumber` (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;