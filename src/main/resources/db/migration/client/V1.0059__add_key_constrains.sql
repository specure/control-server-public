ALTER TABLE ad_hoc_campaign_downtime MODIFY ad_hoc_campaign_id varchar(60) CHARACTER SET utf8;
delete from ad_hoc_campaign_downtime where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign );
alter table ad_hoc_campaign_downtime
    add constraint ad_hoc_campaign_downtime_ad_hoc_campaign_id_fk
        foreign key (ad_hoc_campaign_id) references ad_hoc_campaign (id);


ALTER TABLE measurement MODIFY ad_hoc_campaign_id varchar(60) CHARACTER SET utf8;
delete from geo_location where measurement_id in (select id from measurement where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign ));
delete from ping where measurement_id in (select id from measurement where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign ));
delete from speed_detail where measurement_id in (select id from measurement where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign ));
delete from measurement where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign );
alter table measurement
    add constraint measurement_ad_hoc_campaign_id_fk
        foreign key (ad_hoc_campaign_id) references ad_hoc_campaign (id);

delete from probe_history where not probe_id in ( select id from probe );
alter table probe_history
    add constraint probe_history_probe_id_fk
        foreign key (probe_id) references probe (id);

delete from probe_keep_alive where not probe_id in ( select id from probe );
alter table probe_keep_alive
    add constraint probe_keep_alive_probe_id_fk
        foreign key (probe_id) references probe (id);

delete from probe_keep_alive where not site_id in ( select id from site );
alter table probe_keep_alive
    add constraint probe_keep_alive_site_id_fk
        foreign key (site_id) references site (id);

delete from probe_keep_alive where not package_id in ( select id from package );
alter table probe_keep_alive
    add constraint probe_keep_alive_package_id_fk
        foreign key (package_id) references package (id);

ALTER TABLE probe_keep_alive MODIFY ad_hoc_campaign_id varchar(60) CHARACTER SET utf8;
delete from probe_keep_alive where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign );
alter table probe_keep_alive
    add constraint probe_keep_alive_ad_hoc_campaign_id_fk
        foreign key (ad_hoc_campaign_id) references ad_hoc_campaign (id);

ALTER TABLE measurement_qos MODIFY ad_hoc_campaign_id varchar(60) CHARACTER SET utf8;
delete from measurement_qos where not ad_hoc_campaign_id in ( select id from ad_hoc_campaign );
alter table measurement_qos
    add constraint measurement_qos_ad_hoc_campaign_id_fk
        foreign key (ad_hoc_campaign_id) references ad_hoc_campaign (id);
