CREATE TABLE retrospectives (
    id              UUID PRIMARY KEY,
    sprint_id       UUID NOT NULL REFERENCES sprints (id) ON DELETE CASCADE,
    completed_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    team_mood       VARCHAR(255)
);

CREATE TABLE retrospective_talking_points (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    prompt              TEXT NOT NULL,
    response            TEXT,
    PRIMARY KEY (retrospective_id, prompt)
);

CREATE TABLE retrospective_keep_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

CREATE TABLE retrospective_stop_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

CREATE TABLE retrospective_start_doing (
    retrospective_id    UUID NOT NULL REFERENCES retrospectives (id) ON DELETE CASCADE,
    item                TEXT NOT NULL,
    PRIMARY KEY (retrospective_id, item)
);

