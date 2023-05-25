create table `website_test_result` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `qos_test_uid` bigint(20)NOT NULL,
      `measurement_qos_id` bigint(20),
      `duration_ns` bigint(20),
      `start_time_ns`  bigint(20),
      `website_result_info` varchar(150),
      `website_objective_url` varchar(150),
      `website_result_status` varchar(150),
      `website_result_duration` bigint(20),
      `website_result_rx_bytes` bigint(20),
      `website_result_tx_bytes` bigint(20),
      `website_objective_timeout` bigint(20),
      PRIMARY KEY (`id`)
);

create table `traceroute_test_result` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `qos_test_uid` bigint(20)NOT NULL,
      `measurement_qos_id` bigint(20),
      `duration_ns` bigint(20),
      `start_time_ns`  bigint(20),
      `traceroute_objective_host` varchar(150),
      `traceroute_result_status` varchar(150),
      `traceroute_result_duration` bigint(20),
      `traceroute_objective_timeout` bigint(20),
      `traceroute_objective_max_hops` int(11),
      `traceroute_result_hops` int(11),
      PRIMARY KEY (`id`)
);

CREATE TABLE path_element_entries_result (
	id BIGINT NOT NULL AUTO_INCREMENT,
	traceroute_test_result_id BIGINT NULL,
	time datetime NULL,
    host varchar(500) NULL,
	CONSTRAINT path_element_entries_result_pkey PRIMARY KEY (id)
);

alter table `dns_test_result` add `failure_count` integer DEFAULT 0;
alter table `dns_test_result` add `success_count` integer DEFAULT 0;

alter table `http_proxy_test_result` add `failure_count` integer DEFAULT 0;
alter table `http_proxy_test_result` add `success_count` integer DEFAULT 0;

alter table `non_transparent_proxy_test_result` add `failure_count` integer DEFAULT 0;
alter table `non_transparent_proxy_test_result` add `success_count` integer DEFAULT 0;

alter table `tcp_test_result` add `failure_count` integer DEFAULT 0;
alter table `tcp_test_result` add `success_count` integer DEFAULT 0;

alter table `udp_test_result` add `failure_count` integer DEFAULT 0;
alter table `udp_test_result` add `success_count` integer DEFAULT 0;

alter table `voip_test_result` add `failure_count` integer DEFAULT 0;
alter table `voip_test_result` add `success_count` integer DEFAULT 0;

alter table `traceroute_test_result` add `failure_count` integer DEFAULT 0;
alter table `traceroute_test_result` add `success_count` integer DEFAULT 0;

alter table `website_test_result` add `failure_count` integer DEFAULT 0;
alter table `website_test_result` add `success_count` integer DEFAULT 0;
