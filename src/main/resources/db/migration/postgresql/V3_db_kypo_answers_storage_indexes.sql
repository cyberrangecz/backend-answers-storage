CREATE UNIQUE INDEX sandbox_info_sandbox_ref_id_index
ON sandbox_info (sandbox_ref_id);

CREATE INDEX sandbox_answer_answer_identifier_index
ON sandbox_answer (answer_identifier);
