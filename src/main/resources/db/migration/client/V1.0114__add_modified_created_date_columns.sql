alter table `measurement_server_types`
    add `created_date`  datetime NOT NULL,
    add `modified_date` datetime NOT NULL;

UPDATE `measurement_server_types`
SET `created_date`  = NOW(),
    `modified_date` = NOW();
