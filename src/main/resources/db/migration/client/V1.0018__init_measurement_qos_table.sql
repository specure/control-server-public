create table `measurement_qos` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `open_test_uuid` varchar(120) NOT NULL,
  `voip_result_out_mean_jitter`    bigint(20),
  `voip_result_in_mean_jitter`     bigint(20),
  `voip_objective_delay`           bigint(20),
  `voip_objective_call_duration`   bigint(20),
  `voip_result_out_num_packets`    bigint(20),
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
