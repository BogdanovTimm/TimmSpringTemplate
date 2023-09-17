--liquibase formatted sql



--changeset bogdanovtim:1
CREATE TABLE IF NOT EXISTS table1 ( /*? One to Many */
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name       VARCHAR (255) NOT NULL UNIQUE,
    enumeration VARCHAR (255),
    PRIMARY KEY (id)
);
--rollback DROP TABLE table1;


--changeset bogdanovtim:2
CREATE TABLE IF NOT EXISTS table2 ( /*? Many to One */
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name       VARCHAR (255) NOT NULL UNIQUE,
    table1_id  BIGINT UNSIGNED,
    FOREIGN KEY (table1_id) REFERENCES table1 (id),
    PRIMARY KEY (id)
);
--rollback DROP TABLE table2;