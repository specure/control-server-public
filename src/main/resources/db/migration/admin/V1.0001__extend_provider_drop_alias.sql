DROP TABLE `provider_aliases`;

alter table `provider`
    add column `alias` varchar(200) NULL;
alter table `provider`
    add column `asn` varchar(100) NULL;
alter table `provider`
    add column `mcc_mnc` varchar(50) NULL;
alter table provider change name raw_name varchar(200) NOT NULL;
