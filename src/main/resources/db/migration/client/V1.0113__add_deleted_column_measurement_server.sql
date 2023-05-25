alter table `measurement_server`
    add `deleted` bool default false;

alter table `measurement_server_description`
    add `deleted` bool default false;

alter table `measurement_server_types`
    add `deleted` bool default false;

alter table `measurement_server_types`
    add `id` bigint(20) NOT NULL AUTO_INCREMENT,
    ADD CONSTRAINT measurement_server_types_pk PRIMARY KEY (id);
