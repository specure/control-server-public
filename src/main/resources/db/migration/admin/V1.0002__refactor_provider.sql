ALTER TABLE `provider` RENAME `raw_provider`;

ALTER TABLE `raw_provider`
    add column `provider_id` bigint(20);

CREATE TABLE IF NOT EXISTS `provider`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `created_date`  datetime     NOT NULL,
    `modified_date` datetime     NOT NULL,
    `name`          varchar(200) NOT NULL,
    `country`       varchar(200) NOT NULL,
    `active`        boolean DEFAULT TRUE,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;