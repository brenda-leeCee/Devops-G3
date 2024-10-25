FROM mysql/mysql-server:latest
COPY world.sql /docker-entrypoint-initdb.d
ENV MYSQL_ROOT_PASSWORD example
#Yet another fix for Mac
ENV MYSQL_ROOT_HOST=%
