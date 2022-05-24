ALTER TABLE sandbox_info DROP CONSTRAINT sandbox_info_sandbox_ref_id_key;
DROP INDEX sandbox_info_sandbox_ref_id_index;
ALTER TABLE sandbox_info ALTER COLUMN sandbox_ref_id DROP NOT NULL;;

ALTER TABLE sandbox_info ADD COLUMN user_id int8 default (null);
ALTER TABLE sandbox_info ADD COLUMN  access_token varchar(63) default (null);

CREATE UNIQUE INDEX sandbox_ref_id_index ON sandbox_info (sandbox_ref_id)
    WHERE sandbox_info.access_token IS NULL AND sandbox_info.user_id IS NULL;

CREATE UNIQUE INDEX access_token__index ON sandbox_info (access_token, user_id)
    WHERE sandbox_info.sandbox_ref_id IS NULL;