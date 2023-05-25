CREATE TABLE loop_mode_settings
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    test_uuid    varchar(120) NOT NULL UNIQUE,
    client_uuid  varchar(120),
    max_movement integer,
    max_delay    integer,
    max_tests    integer,
    test_counter integer,
    loop_uuid    varchar(120),
    PRIMARY KEY (id)
);
