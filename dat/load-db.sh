#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

export PGPASSWORD=pgpw
export PGUSER=postgres
export PGHOST=localhost
export PGDATABASE=postgres

psql -a -f ./data.sql