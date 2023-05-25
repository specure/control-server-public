CREATE TABLE `ping` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,

    `measurement_id` bigint(20) NOT NULL,
    `value` bigint(20) NULL,
    `value_server` bigint(20) NULL,
    `time_ns` bigint(20) NULL,

    PRIMARY KEY (`id`),
    KEY `measurement_id` (`measurement_id`),
    CONSTRAINT `measurement_ping_1` FOREIGN KEY (`measurement_id`) REFERENCES `measurement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `speed_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `measurement_id` bigint(20) NOT NULL,

    `direction` varchar(10),
    `thread` integer,
    `time` bigint(20) NULL,
    `bytes` integer NULL,

    PRIMARY KEY (`id`),
    KEY `measurement_id` (`measurement_id`),
    CONSTRAINT `measurement_speed_detail_1` FOREIGN KEY (`measurement_id`) REFERENCES `measurement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `geo_location` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `measurement_id` bigint(20) NOT NULL,

    `geo_lat` double precision,
    `geo_long` double precision,
    `accuracy` double precision,
    `altitude` double precision,
    `bearing` double precision,
    `speed` double precision,

    `timestamp` datetime NULL,

    `provider` varchar(60),

    PRIMARY KEY (`id`),
    KEY `measurement_id` (`measurement_id`),
    CONSTRAINT `measurement_geo_location_1` FOREIGN KEY (`measurement_id`) REFERENCES `measurement` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
