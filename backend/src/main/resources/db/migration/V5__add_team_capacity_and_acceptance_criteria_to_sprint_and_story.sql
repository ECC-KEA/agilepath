ALTER TABLE sprints
    ADD COLUMN capacity INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE stories
    ADD COLUMN acceptance_criteria TEXT;
