update measurement set platform = 'PROBE' where model = 'bhprobe';
update measurement set platform = 'BROWSER' where platform = 'RMBTws';
update measurement set platform = 'UNKNOWN' where platform is null;