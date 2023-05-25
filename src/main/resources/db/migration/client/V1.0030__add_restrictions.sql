alter table probe add constraint probe_fk
    foreign key (site_id) references site (id);

alter table probe_port add constraint probe_port_fk
    foreign key (probe_id) references probe(id);

alter table probe_port add constraint probe_port_package_fk
    foreign key (package_id) references package(id);

alter table package add constraint package_provider_fk
    foreign key (provider) references provider(id);

alter table measurement add constraint measurement_server_fk
    foreign key (measurement_server_id) references  measurement_server(id)
