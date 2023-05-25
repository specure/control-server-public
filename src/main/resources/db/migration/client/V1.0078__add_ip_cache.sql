CREATE TABLE `ip_cache`
(
    `id`           bigint(20) NOT NULL,
    `ip_address` varchar(30) NOT NULL,
    `created_date` datetime   NOT NULL,
    `modified_date` datetime NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
