alter table `measurement` add `client_language` varchar(20) null;
alter table `measurement` add `client_name` varchar (20) null;
alter table `measurement` add `client_version` varchar(10) null;
alter table `measurement` add `model` varchar(200) null;
alter table `measurement` add `platform` varchar(200) null;
alter table `measurement` add `product` varchar(200) null;
alter table `measurement` add `timezone` varchar(200) null;
alter table `measurement` add `type` varchar(200) null;
alter table `measurement` add `version_code` varchar(200) null;

alter table `measurement` add `test_bytes_download` bigint null;
alter table `measurement` add `test_bytes_upload` bigint null;
alter table `measurement` add `test_nsec_download` bigint null;
alter table `measurement` add `test_nsec_upload` bigint null;

alter table `measurement` add `test_ping_shortest` bigint null;

alter table `measurement` add `test_num_threads` integer null;
alter table `measurement` add `num_threads_ul` integer null;

alter table `measurement` add `test_speed_download` integer null;
alter table `measurement` add `test_speed_upload` integer null;


