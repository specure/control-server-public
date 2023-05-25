CREATE TABLE `cache`
(
    `id`            varchar(200) NOT NULL,
    `value`         varchar(200) NOT NULL,
    `created_date`  datetime     NOT NULL,
    `modified_date` datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;