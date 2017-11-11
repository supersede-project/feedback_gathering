FROM mysql:5.7
MAINTAINER Ronnie Schaniel <ronnieschaniel@gmail.com>

# Contents of /docker-entrypoint-initdb.d are run on mysqld startup
ADD src/main/resources/db/migrations/ /docker-entrypoint-initdb.d/