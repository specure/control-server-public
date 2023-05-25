INSERT INTO settings ( `key`, `lang`, `value`)
SELECT 'num_threads_android' as 'key', 'en' as 'lang', value
FROM settings where `key` = 'num_threads_mobile';

INSERT INTO settings ( `key`, `lang`, `value`)
SELECT 'num_threads_ios' as 'key', 'en' as 'lang', value
FROM settings where `key` = 'num_threads_mobile';