alter table `measurement` add `wifi_ssid` varchar(200) null;
alter table `measurement` add `wifi_bssid` varchar(200) null;
alter table `measurement` add `sim_count` integer null;
alter table `measurement` add `network_operator_name` varchar(200) null;
alter table `measurement` add `dual_sim` boolean null;
