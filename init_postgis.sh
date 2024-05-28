set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "datebyrate" <<-EOSQL
      CREATE EXTENSION IF NOT EXISTS postgis;
EOSQL