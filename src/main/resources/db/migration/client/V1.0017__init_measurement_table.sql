--
-- Adding table for storing packages.
--

CREATE TABLE measurement (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    open_test_uuid varchar(120) NOT NULL,
    `time` datetime,
    token VARCHAR(500),
    test_slot int,
    ping_median bigint,
    signal_strength int,
    network_operator varchar(200),
    speed_upload integer,
    speed_download integer,
    lte_rsrp integer,
    device varchar(200),
    tag varchar(512),
    network_type integer,
    voip_result_jitter varchar(20),
    voip_result_packet_loss varchar(20),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
