ALTER TABLE package
    ADD group_active BOOL DEFAULT true NOT NULL;
ALTER TABLE package
    ADD group_id bigint(20);

UPDATE package SET group_id = id where group_id is null;
