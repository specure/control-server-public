ALTER TABLE `measurement`
    ADD `mcc_mnc` varchar(50) NULL;
ALTER TABLE `measurement`
    ADD `isp_name` varchar(200) NULL;
ALTER TABLE `measurement`
    ADD `asn` BIGINT NULL;
ALTER TABLE `measurement`
    ADD `is_mno` BOOL NULL;
ALTER TABLE `measurement`
    ADD isp_raw_id varchar(150) null;
