INSERT INTO `provider`(id, created_date, modified_date, name, country)
SELECT id,
       created_date,
       modified_date,
       raw_name,
       country
FROM `raw_provider`;