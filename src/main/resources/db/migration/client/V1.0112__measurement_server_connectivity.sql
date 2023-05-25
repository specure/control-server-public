ALTER TABLE `measurement_server`
    MODIFY `ip_v4_support` BOOL DEFAULT TRUE,
    MODIFY `ip_v6_support` BOOL DEFAULT TRUE;

UPDATE `measurement_server`
SET `ip_v4_support` = TRUE,
    `ip_v6_support` = TRUE;