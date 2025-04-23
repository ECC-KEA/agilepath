ALTER TABLE tasks
    DROP COLUMN status;
ALTER TABLE tasks
    ADD COLUMN sprint_column_id UUID NOT NULL REFERENCES sprint_columns (id) ON DELETE CASCADE;