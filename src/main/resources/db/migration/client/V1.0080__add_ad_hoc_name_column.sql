ALTER TABLE ad_hoc_campaign
    ADD campaign_name varchar(60) UNIQUE;

UPDATE ad_hoc_campaign SET campaign_name = id where campaign_name is null;

ALTER TABLE ad_hoc_campaign MODIFY campaign_name varchar(60) UNIQUE NOT NULL;
