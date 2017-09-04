FROM mysql/mysql-server
MAINTAINER Ronnie Schaniel <ronnieschaniel@gmail.com>

# Contents of /docker-entrypoint-initdb.d are run on mysqld startup
ADD src/main/resources/db/migrations/ /docker-entrypoint-initdb.d/