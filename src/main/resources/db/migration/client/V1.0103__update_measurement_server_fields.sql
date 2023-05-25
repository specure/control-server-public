ALTER TABLE measurement_server
    DROP COLUMN active;
ALTER TABLE measurement_server
    DROP COLUMN selectable;
ALTER TABLE measurement_server
    ADD on_net BOOL default false;
ALTER TABLE measurement_server
    ADD dedicated BOOL default false;
