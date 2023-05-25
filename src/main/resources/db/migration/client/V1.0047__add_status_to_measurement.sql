alter table measurement add status varchar(10) null;
update measurement set status = 'STARTED' where speed_download is null and speed_upload is null and ping_median is null;
update measurement set status = 'FINISHED' where speed_download is not null and speed_upload is not null and ping_median is not null;