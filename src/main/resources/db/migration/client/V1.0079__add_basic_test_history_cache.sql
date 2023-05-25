CREATE TABLE `measurement_cache`
(
    `open_test_uuid` varchar(36) NOT NULL,
    `basic_test_history` text NOT NULL,
    `created_date` datetime   NOT NULL,
    `modified_date` datetime NOT NULL,
    PRIMARY KEY (`open_test_uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
