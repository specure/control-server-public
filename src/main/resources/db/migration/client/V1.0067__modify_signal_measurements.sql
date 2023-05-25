ALTER TABLE `mobile_measurement`
    ADD COLUMN `duration` int,
    ADD COLUMN `network_group_name` varchar(20),
    ADD COLUMN `time` datetime NOT NULL;
