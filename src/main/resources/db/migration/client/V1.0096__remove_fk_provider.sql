set @var=if((SELECT true FROM information_schema.TABLE_CONSTRAINTS WHERE
        CONSTRAINT_SCHEMA = DATABASE() AND
        TABLE_NAME        = 'ad_hoc_campaign' AND
        CONSTRAINT_NAME   = 'provider_campaign_fk' AND
        CONSTRAINT_TYPE   = 'FOREIGN KEY') = true,'ALTER TABLE ad_hoc_campaign
            drop foreign key provider_campaign_fk','select 1');
prepare stmt from @var;
execute stmt;
deallocate prepare stmt;


set @var=if((SELECT true FROM information_schema.TABLE_CONSTRAINTS WHERE
        CONSTRAINT_SCHEMA = DATABASE() AND
        TABLE_NAME        = 'package' AND
        CONSTRAINT_NAME   = 'package_provider_fk' AND
        CONSTRAINT_TYPE   = 'FOREIGN KEY') = true,'ALTER TABLE package
            drop foreign key package_provider_fk','select 1');
prepare stmt from @var;
execute stmt;
deallocate prepare stmt;