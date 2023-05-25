# ----- measurement -----

create index measurement_open_test_uuid_index
    on measurement (open_test_uuid);

alter table measurement_qos
    add constraint measurement_qos_measurement_open_test_uuid_fk
        foreign key (open_test_uuid) references measurement (open_test_uuid)
            on delete cascade;

alter table geo_location drop foreign key measurement_geo_location_1;
alter table geo_location
    add constraint measurement_geo_location_1
        foreign key (measurement_id) references measurement (id)
            on delete cascade;

alter table ping drop foreign key measurement_ping_1;
alter table ping
    add constraint measurement_ping_1
        foreign key (measurement_id) references measurement (id)
            on delete cascade;

alter table speed_detail drop foreign key measurement_speed_detail_1;
alter table speed_detail
    add constraint measurement_speed_detail_1
        foreign key (measurement_id) references measurement (id)
            on delete cascade;

# ----- measurement qos -----

alter table dns_test_result
    add constraint dns_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table udp_test_result
    add constraint udp_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table voip_test_result
    add constraint voip_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table non_transparent_proxy_test_result
    add constraint non_transparent_proxy_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table http_proxy_test_result
    add constraint http_proxy_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table tcp_test_result
    add constraint tcp_test_result_measurement_qos_id_fk
        foreign key (measurement_qos_id) references measurement_qos (id)
            on delete cascade;

alter table ad_hoc_campaign_downtime drop foreign key ad_hoc_campaign_downtime_ad_hoc_campaign_id_fk;
alter table ad_hoc_campaign_downtime
    add constraint ad_hoc_campaign_downtime_ad_hoc_campaign_id_fk
        foreign key (ad_hoc_campaign_id) references ad_hoc_campaign (id)
            on delete cascade;

