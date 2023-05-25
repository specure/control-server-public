CREATE TABLE news (
	uid serial NOT NULL,
	time datetime NOT NULL,
	title_en varchar(255) NULL,
	title_de varchar(255) NULL,
	text_en varchar(255) NULL,
	text_de varchar(255) NULL,
	active boolean NOT NULL DEFAULT false,
	`force` boolean NOT NULL DEFAULT false,
	plattform varchar(255) NULL,
	max_software_version_code bigint NULL,
	min_software_version_code bigint NULL,
	`uuid` binary(255) NULL,
	CONSTRAINT uid PRIMARY KEY (uid)
);
