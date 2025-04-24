ALTER TABLE tasks
    ALTER COLUMN estimate_points TYPE VARCHAR(50);

ALTER TABLE tasks
    DROP CONSTRAINT tasks_estimate_points_check;

ALTER TABLE tasks
    DROP CONSTRAINT tasks_estimate_tshirt_check;