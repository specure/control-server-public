INSERT INTO settings ( `key`, `lang`, `value`)
 SELECT 'num_threads_mobile' as 'key', 'en' as 'lang', value
 FROM settings where `key` = 'num_threads';

INSERT INTO settings ( `key`, `lang`, `value`)
VALUES ('num_threads_web' , 'en' , 5);