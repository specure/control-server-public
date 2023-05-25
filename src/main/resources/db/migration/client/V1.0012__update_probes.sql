--
-- Updating probe table with new fields/probe history.
--
ALTER TABLE `probe`
    ADD COLUMN operator VARCHAR(200),
    ADD COLUMN modem_count INT,
    ADD COLUMN comment VARCHAR(250);

CREATE TABLE `probe_history` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  probe_id varchar(30) NOT NULL,
  created_date datetime NOT NULL,
  modified_date datetime NOT NULL,
  type varchar(60) NOT NULL,
  status varchar(60) NOT NULL,
  comment varchar(200),
  modem_count INT,
  operator VARCHAR(200),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
