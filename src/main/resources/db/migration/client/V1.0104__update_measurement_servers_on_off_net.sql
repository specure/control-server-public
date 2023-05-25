UPDATE measurement_server
SET provider = null
WHERE provider = 6 || provider = 7 || provider = 10;

UPDATE measurement_server
SET on_net = true
WHERE provider is not null;

