alter table `measurement_details`
    add `sim_mcc_mnc` varchar(20);
alter table `measurement_details`
    add `sim_operator_name` varchar(255);
alter table `measurement_details`
    add `network_is_roaming` bool;
alter table `measurement_details`
    change `telephony_network_operator_code` `network_mcc_mnc` varchar(120);
alter table `measurement_details`
    add `network_operator_name` varchar(255);
alter table `measurement_details`
    add `network_country` varchar(255);
alter table `measurement_details`
    add `sim_country` varchar(255);