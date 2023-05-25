ALTER TABLE client ADD client_type varchar(20) NULL;
ALTER TABLE client ADD terms_and_conditions_accepted bool DEFAULT false;
ALTER TABLE client ADD terms_and_conditions_accepted_timestamp datetime NULL;
ALTER TABLE client ADD terms_and_conditions_accepted_version BIGINT NULL;
ALTER TABLE client ADD last_seen datetime NULL;

ALTER TABLE measurement_server ADD active boolean DEFAULT true;
ALTER TABLE measurement_server ADD selectable boolean DEFAULT true;
ALTER TABLE measurement_server ADD uuid varchar(36) NULL;
ALTER TABLE measurement_server ADD server_type varchar(20) NULL;

CREATE TABLE qos_test_type_desc (
	id BIGINT NOT NULL AUTO_INCREMENT,
	test varchar(50) NULL,
	test_desc varchar(255) NULL,
	test_name varchar(255) NULL,
	CONSTRAINT qos_test_type_desc_pkey PRIMARY KEY (id),
	CONSTRAINT qos_test_type_desc_test_key UNIQUE (test)
);

CREATE TABLE settings (
	id BIGINT NOT NULL AUTO_INCREMENT,
	`key` varchar(255) NOT NULL,
	lang varchar(255) NULL,
	value varchar(255) NOT NULL,
	CONSTRAINT settings_key_lang_key UNIQUE (`key`, lang),
	CONSTRAINT settings_pkey PRIMARY KEY (id)
);

CREATE TABLE test (
	id BIGINT NOT NULL AUTO_INCREMENT,
	uuid varchar(36) NULL,
	model varchar(255) NULL,
	implausible bool NOT NULL DEFAULT false,
	network_type int4 NULL,
	deleted bool NOT NULL DEFAULT false,
	status varchar(100) NULL,
	client_id BIGINT NULL,
	open_test_uuid varchar(36) NULL,
	client_public_ip varchar(100) NULL,
	client_public_ip_anonymized varchar(100) NULL,
	timezone varchar(200) NULL,
	client_time datetime NULL,
	public_ip_asn int8 NULL,
	public_ip_as_name varchar(200) NULL,
	country_asn varchar(2) NULL,
	public_ip_rdns varchar(200) NULL,
	last_sequence_number int4 NULL,
	CONSTRAINT settings_pkey PRIMARY KEY (id)
);

CREATE TABLE qos_test_desc (
	id BIGINT NOT NULL AUTO_INCREMENT,
	desc_key text NULL,
	value text NULL,
	lang text NULL,
	CONSTRAINT qos_test_desc_pkey PRIMARY KEY (id)
);

CREATE TABLE device_map (
	id BIGINT NOT NULL AUTO_INCREMENT,
	codename varchar(200) NULL,
	fullname varchar(200) NULL,
	source varchar(200) NULL,
	CONSTRAINT android_device_map_codename_key UNIQUE (codename),
	CONSTRAINT android_device_map_pkey PRIMARY KEY (id),
	CONSTRAINT device_map_fullname_key UNIQUE (fullname)
);

CREATE TABLE network_type (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name varchar(200) NOT NULL,
	group_name varchar(100) NOT NULL,
	type varchar(100) NOT NULL,
	technology_order int4 NOT NULL DEFAULT 0,
	min_speed_download_kbps int4 NULL,
	max_speed_download_kbps int4 NULL,
	min_speed_upload_kbps int4 NULL,
	max_speed_upload_kbps int4 NULL,
	CONSTRAINT network_type_pkey PRIMARY KEY (id)
);

CREATE TABLE measurement_server_types(
	`measurement_server_id` bigint not null,
	`server_type` varchar(60) NOT NULL
)
