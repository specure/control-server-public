ALTER TABLE ad_hoc_campaign
    ADD downtime_status         varchar(20) DEFAULT 'UP' NOT NULL,
    ADD downtime_status_changed datetime    DEFAULT now() NOT NULL;