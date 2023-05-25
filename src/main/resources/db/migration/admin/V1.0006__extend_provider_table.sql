alter table `provider`
    change `active` `mno_active` boolean DEFAULT FALSE;
alter table `provider`
    add `isp_active` boolean DEFAULT FALSE;