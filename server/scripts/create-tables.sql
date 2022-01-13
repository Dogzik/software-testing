CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS Users
(
    login     VARCHAR(255) NOT NULL PRIMARY KEY,
    pass_hash TEXT         NOT NULL
);

CREATE TABLE IF NOT EXISTS Letters
(
    email_id   UUID         NOT NULL PRIMARY KEY,
    src        VARCHAR(255) NOT NULL,
    dst        VARCHAR(255) NOT NULL,
    subject    TEXT         NOT NULL,
    time       TIMESTAMP    NOT NULL,
    email_text TEXT         NOT NULL
);

CREATE INDEX IF NOT EXISTS sent ON Letters USING btree (src, time);
CREATE INDEX IF NOT EXISTS inbox ON Letters USING btree (dst, time);
