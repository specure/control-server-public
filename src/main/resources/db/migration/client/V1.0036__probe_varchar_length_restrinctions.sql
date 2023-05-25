alter table probe modify id varchar(32) not null;
alter table probe_keep_alive modify probe_id varchar(32) not null;
alter table probe_port modify probe_id varchar(32) not null;
alter table package modify probe_id varchar(32) null;
