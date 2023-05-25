alter table `measurement_server_types`
    add column `port` bigint(20) NULL;

alter table `measurement_server_types`
    add column `port_ssl` bigint(20) NULL;

alter table `measurement_server_types`
    add `encrypted` boolean default false;