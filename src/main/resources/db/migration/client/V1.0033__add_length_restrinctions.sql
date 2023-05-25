alter table `site` modify address varchar(255) charset utf8 not null;
alter table `probe` modify id varchar(31) not null;
alter table `probe` modify comment varchar(255) null;
alter table measurement_server modify name varchar(63) not null;
alter table measurement_server modify web_address varchar(255) not null;
alter table measurement_server modify secret_key varchar(255) null;
alter table measurement_server_description modify email varchar(255) null;
alter table measurement_server_description modify ip_address varchar(255) null;
alter table measurement_server_description modify comment varchar(255) null;
