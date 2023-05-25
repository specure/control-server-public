alter table probe add probe_purpose varchar(32) null;

update probe
set probe.probe_purpose = 'SITE'
where probe.current_ad_hoc_campaign_mark is null;

update probe
set probe.probe_purpose = 'CAMPAIGN'
where probe.current_ad_hoc_campaign_mark is not null;
