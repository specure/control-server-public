--
-- Adding table for storing measurement server description.
--
alter table `measurement_server_description` add company varchar(120) null;
alter table `measurement_server_description` add expiration datetime null;
