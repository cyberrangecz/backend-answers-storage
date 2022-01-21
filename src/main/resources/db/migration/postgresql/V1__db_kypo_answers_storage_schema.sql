CREATE TABLE sandbox_info (
                              sandbox_info_id bigserial NOT NULL PRIMARY KEY,
                              sandbox_ref_id  int8      NOT NULL UNIQUE
);
ALTER SEQUENCE sandbox_info_sandbox_info_id_seq RENAME TO sandbox_info_id_seq;
ALTER SEQUENCE sandbox_info_id_seq INCREMENT 50;

CREATE TABLE sandbox_answer (
                                sandbox_answer_id bigserial    NOT NULL PRIMARY KEY,
                                answer_content    varchar(255) NOT NULL,
                                answer_variable_name varchar(255) NOT NULL,
                                sandbox_info_id   int8,
                                FOREIGN KEY (sandbox_info_id) REFERENCES sandbox_info
);
ALTER SEQUENCE sandbox_answer_sandbox_answer_id_seq RENAME TO sandbox_answer_id_seq;
ALTER SEQUENCE sandbox_answer_id_seq INCREMENT 50;

CREATE UNIQUE INDEX sandbox_info_sandbox_ref_id_index ON sandbox_info (sandbox_ref_id);
CREATE INDEX sandbox_answer_answer_variable_name_index ON sandbox_answer (answer_variable_name);