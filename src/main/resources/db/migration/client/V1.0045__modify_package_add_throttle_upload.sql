alter table package change throttle throttle_speed_download bigint not null;
alter table package add throttle_speed_upload bigint null;
