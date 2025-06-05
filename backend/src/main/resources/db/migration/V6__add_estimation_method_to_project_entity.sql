ALTER TABLE projects
    ADD COLUMN estimation_method VARCHAR(50) DEFAULT 'story_points' NOT NULL;