--
-- Adding table for storing measurement server description.
--
alter table measurement_server_description modify city varchar(120) null;
alter table measurement_server_description modify email varchar(120) null;
