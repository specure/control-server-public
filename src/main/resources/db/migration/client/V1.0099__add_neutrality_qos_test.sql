CREATE TABLE `neutrality_qos_test`
(
    `id`   bigint(20) NOT NULL AUTO_INCREMENT,
    `type` varchar(200) NULL,
    `target` varchar(200) NULL,
    `timeout` bigint NULL,
    `entry_type` varchar(200) NUll,
    `resolver` varchar(200) NULL,
    `dns_status` varchar(200) NULL,
    `dns_entries` varchar(200) NULL,
    `port_number` bigint NULL,
    `num_packets_sent` bigint NULL,
    `min_number_packets_received` bigint NULL,
    `status_code` bigint NULL,
    PRIMARY KEY (id)
);