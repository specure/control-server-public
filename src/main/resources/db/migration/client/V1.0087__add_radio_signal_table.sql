CREATE TABLE radio_signal
(
    id              bigint(20)   NOT NULL AUTO_INCREMENT,
    open_test_uuid  varchar(120) NOT NULL,
    time            datetime,
    bit_error_rate  integer,
    cell_uuid       varchar(120),
    network_type_id integer,
    signal_strength integer,
    time_ns_last    bigint(30),
    time_ns         bigint(30),
    wifi_link_speed integer,
    lte_rsrp        integer,
    lte_rsrq        integer,
    lte_rssnr       integer,
    lte_cqi         integer,
    timing_advance  integer,
    PRIMARY KEY (id)
);
