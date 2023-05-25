CREATE TABLE IF NOT EXISTS `provider`
(
    `id`                      bigint(20)   NOT NULL AUTO_INCREMENT,
    `created_date`            datetime     NOT NULL,
    `modified_date`           datetime     NOT NULL,
    `name`                    varchar(200) NOT NULL,
    `country`                 varchar(200) NOT NULL,
    `mobile_network_operator` BOOL DEFAULT false,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

CREATE TABLE IF NOT EXISTS `provider_aliases`
(
    `provider_id` bigint(20)   NOT NULL AUTO_INCREMENT,
    `alias`       varchar(200) NOT NULL,
    CONSTRAINT `provider_id_fk` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;