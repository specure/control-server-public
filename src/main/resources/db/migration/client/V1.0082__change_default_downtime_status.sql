ALTER TABLE ad_hoc_campaign
    DROP COLUMN downtime_status,
    DROP COLUMN downtime_status_changed;

ALTER TABLE ad_hoc_campaign
    ADD downtime_status         varchar(20),
    ADD downtime_status_changed datetime;