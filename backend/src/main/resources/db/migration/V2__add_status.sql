ALTER TABLE sprint_columns
    DROP CONSTRAINT sprint_columns_status_check,
    ADD CONSTRAINT sprint_columns_status_check CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE', 'ARCHIVED'));