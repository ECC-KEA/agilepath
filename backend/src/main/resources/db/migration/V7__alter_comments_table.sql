ALTER TABLE comments
    DROP COLUMN subtask_id;

ALTER TABLE comments
    DROP CONSTRAINT IF EXISTS comments_check;

ALTER TABLE comments
    ADD CONSTRAINT comments_check CHECK (
        (story_id IS NOT NULL AND task_id IS NULL) OR
        (story_id IS NULL AND task_id IS NOT NULL)
        );