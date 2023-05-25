CREATE TABLE ad_hoc_campaign
(
    id varchar(60) NOT NULL primary key,

    status varchar(30),
    location varchar(60),

    created_date datetime NOT NULL,
    modified_date datetime NOT NULL,

    start datetime NOT NULL,
    finish datetime NOT NULL,

    probe_id varchar(32),
    provider_id bigint,
    constraint provider_campaign_fk foreign key (provider_id) references provider(id),
    constraint probe_campaign_fk foreign key (probe_id) references probe(id)

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table measurement_qos add ad_hoc_campaign_id varchar(60) null;

alter table measurement add ad_hoc_campaign_id varchar(60) null;

alter table probe_keep_alive add ad_hoc_campaign_id varchar(60) null;
