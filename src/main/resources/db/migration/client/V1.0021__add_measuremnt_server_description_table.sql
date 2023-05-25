--
-- Adding table for storing measurement server description.
--

CREATE TABLE `measurement_server_description` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `created_date` datetime NOT NULL,
    `modified_date` datetime NOT NULL,
    `city` varchar(120) NOT NULL,
    `email` varchar(120) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `measurement_server` ADD `measurement_server_description` bigint(20);
