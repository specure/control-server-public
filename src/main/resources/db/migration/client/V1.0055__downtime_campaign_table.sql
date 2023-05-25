CREATE TABLE ad_hoc_campaign_downtime
(
    id bigint auto_increment primary key,
    start datetime NOT NULL,
    finish datetime NULL,

    duration bigint,

    created_date datetime NOT NULL,
    modified_date datetime NOT NULL,

    ad_hoc_campaign_id varchar(60)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

