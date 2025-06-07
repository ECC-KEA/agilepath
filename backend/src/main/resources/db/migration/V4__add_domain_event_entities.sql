-- project_events
CREATE TABLE project_events
(
    id           UUID PRIMARY KEY,
    project_id   UUID        NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    event_type   VARCHAR(50) NOT NULL,
    old_value    VARCHAR(255),
    new_value    VARCHAR(255),
    triggered_by VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- sprint_events
CREATE TABLE sprint_events
(
    id           UUID PRIMARY KEY,
    sprint_id    UUID        NOT NULL REFERENCES sprints (id) ON DELETE CASCADE,
    event_type   VARCHAR(50) NOT NULL,
    old_value    VARCHAR(255),
    new_value    VARCHAR(255),
    triggered_by VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- story_events
CREATE TABLE story_events
(
    id           UUID PRIMARY KEY,
    story_id     UUID        NOT NULL REFERENCES stories (id) ON DELETE CASCADE,
    event_type   VARCHAR(50) NOT NULL,
    old_value    VARCHAR(255),
    new_value    VARCHAR(255),
    triggered_by VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- task_events
CREATE TABLE task_events
(
    id           UUID PRIMARY KEY,
    task_id      UUID        NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    event_type   VARCHAR(50) NOT NULL,
    old_value    VARCHAR(255),
    new_value    VARCHAR(255),
    triggered_by VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
