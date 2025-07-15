\set ON_ERROR_STOP off
-- delete database
DROP DATABASE IF EXISTS eletrohub;

-- create database
CREATE DATABASE eletrohub;

-- create USER

DROP OWNED BY db_admin;
DROP USER IF EXISTS db_admin;
DROP USER IF EXISTS db_user; -- user created by liquibase
CREATE USER db_admin WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE "eletrohub" to db_admin;
ALTER USER db_admin WITH SUPERUSER;
