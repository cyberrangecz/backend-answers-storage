CREATE TABLE sandbox_info (
    sandbox_info_id bigserial NOT NULL PRIMARY KEY,
    sandbox_ref_id  varchar(36),
    user_id         int8        DEFAULT NULL,
    access_token    varchar(63) DEFAULT NULL,
    allocation_id   int8        DEFAULT NULL
);

ALTER SEQUENCE sandbox_info_sandbox_info_id_seq RENAME TO sandbox_info_id_seq;
ALTER SEQUENCE sandbox_info_id_seq INCREMENT 50;

CREATE UNIQUE INDEX sandbox_ref_id_index
    ON sandbox_info (sandbox_ref_id)
    WHERE sandbox_info.access_token IS NULL AND sandbox_info.user_id IS NULL;

CREATE UNIQUE INDEX access_token__index
    ON sandbox_info (access_token, user_id)
    WHERE sandbox_info.sandbox_ref_id IS NULL;


CREATE TABLE sandbox_answer (
    sandbox_answer_id    bigserial     NOT NULL PRIMARY KEY,
    answer_content       varchar(2048) NOT NULL,
    answer_variable_name varchar(255)  NOT NULL,
    sandbox_info_id      int8,
    FOREIGN KEY (sandbox_info_id) REFERENCES sandbox_info
);

ALTER SEQUENCE sandbox_answer_sandbox_answer_id_seq RENAME TO sandbox_answer_id_seq;
ALTER SEQUENCE sandbox_answer_id_seq INCREMENT 50;

CREATE INDEX sandbox_answer_answer_variable_name_index ON sandbox_answer (answer_variable_name);