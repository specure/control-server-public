drop table measurement_qos;

create table `measurement_qos` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `test_token` varchar(120) NOT NULL,
    `open_test_uuid` varchar(120) NOT NULL,
    `client_uuid` varchar(120) NOT NULL,
    `client_version` varchar(120),
    `client_name` varchar(120),
    `client_language` varchar(120),
    `time` datetime,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `tcp_test_result` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `qos_test_uid` bigint(20)NOT NULL,
    `measurement_qos_id` bigint(20),

    `tcp_result_out` varchar(120),
    `tcp_objective_timeout` bigint(20),
    `duration_ns` bigint(20),
    `tcp_result_out_response` varchar(120),
    `tcp_objective_out_port` int(11),
    `start_time_ns` bigint(20),


    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `voip_test_result` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `qos_test_uid` bigint(20)NOT NULL,
    `measurement_qos_id` bigint(20),

    `voip_result_out_short_seq` int(11),
    `voip_result_in_sequence_error` int(11),
    `voip_objective_delay` bigint(20),
    `voip_objective_sample_rate` int(11),
    `voip_result_out_num_packets` bigint(20),
    `voip_objective_bits_per_sample` int(11),
    `voip_result_out_long_seq` int(11),
    `voip_result_out_max_jitter` bigint(20),
    `duration_ns` bigint(20),
    `voip_result_out_mean_jitter` bigint(20),
    `voip_result_status` varchar(120),
    `voip_result_in_num_packets` int(11),
    `voip_result_out_sequence_error` int(11),
    `voip_result_jitter` varchar(120),
    `voip_result_in_max_jitter` bigint(20),
    `voip_result_in_skew` bigint(20),
    `voip_result_out_max_delta` bigint(20),
    `voip_objective_in_port` int(11),
    `voip_objective_call_duration` bigint(20),
    `voip_result_in_short_seq` int(11),
    `start_time_ns` bigint(20),
    `voip_objective_payload` int(11),
    `voip_result_in_max_delta` bigint(20),
    `voip_objective_out_port` int(11),
    `voip_result_in_mean_jitter` bigint(20),
    `voip_result_out_skew` bigint(20),
    `voip_result_in_long_seq` int(11),
    `voip_result_packet_loss` varchar(120),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `udp_test_result` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `qos_test_uid` bigint(20)NOT NULL,
    `measurement_qos_id` bigint(20),

    `udp_result_out_num_packets` int(11),
    `duration_ns` bigint(20),
    `udp_result_out_response_num_packets` int(11),
    `udp_objective_out_num_packets` int(11),
    `udp_result_out_packet_loss_rate` varchar(120),
    `udp_objective_out_port` int(11),
    `udp_objective_delay` bigint(20),
    `udp_objective_timeout` bigint(20),
    `start_time_ns`  bigint(20),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `http_proxy_test_result` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT,
       `qos_test_uid` bigint(20)NOT NULL,
       `measurement_qos_id` bigint(20),

       `http_objective_url` varchar(350),
       `http_result_duration` bigint(20),
       `http_result_header` varchar(350),
       `duration_ns` bigint(20),
       `http_result_length` int(11),

       `start_time_ns`  bigint(20),
       `http_result_hash` varchar(150),
       `http_result_status` int(11),

       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `non_transparent_proxy_test_result` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `qos_test_uid` bigint(20)NOT NULL,
      `measurement_qos_id` bigint(20),

      `duration_ns` bigint(20),
      `non_transparent_proxy_result` varchar(150),
      `non_transparent_proxy_objective_request` varchar(150),
      `non_transparent_proxy_objective_timeout` bigint(20),
      `non_transparent_proxy_objective_port` int(11),
      `non_transparent_proxy_result_response` varchar(150),
      `start_time_ns`  bigint(20),

      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `dns_test_result` (
         `id` bigint(20) NOT NULL AUTO_INCREMENT,
         `qos_test_uid` bigint(20)NOT NULL,
         `measurement_qos_id` bigint(20),

         `dns_objective_timeout` bigint(20),
         `start_time_ns`  bigint(20),
         `dns_objective_dns_record` varchar(120),
         `duration_ns` bigint(20),
         `dns_objective_host`  varchar(150),
         `dns_objective_resolver`  varchar(150),
         `dns_result_info` varchar(150),
         `dns_result_status`  varchar(150),
         `dns_result_duration` bigint(20),
         `dns_result_entries_found`  int(11),

         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table `dns_result_entries` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `dns_test_result_id` bigint(20),

   `dns_result_address`  varchar(150),
   `dns_result_ttl`  varchar(150),

   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
