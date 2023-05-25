--
-- Link site to probe.
--
ALTER TABLE probe
ADD COLUMN site_id bigint(20);
