#!/bin/bash

set -eo pipefail

_create_additional_database() {
  docker_process_sql --database=mysql <<-EOSQL
    CREATE DATABASE IF NOT EXISTS \`$1\`;
    GRANT ALL ON \`${1}\`.* TO '$MYSQL_USER'@'%' ;
    FLUSH PRIVILEGES ;
EOSQL
}

mysql_note "Creating database ${DATABASE_TWO}"
_create_additional_database "${DATABASE_TWO}"
