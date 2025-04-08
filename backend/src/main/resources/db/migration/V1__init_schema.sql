-- Users (from Clerk + GitHub)
CREATE TABLE users
(
    id              VARCHAR(50) PRIMARY KEY, -- External ID from Clerk
    github_username VARCHAR(100),
    email           VARCHAR(255) NOT NULL UNIQUE,
    full_name       VARCHAR(255),            -- From GitHub via Clerk; may be split later if needed
    avatar_url      TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_by     VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    modified_at     TIMESTAMP
);

-- Projects
CREATE TABLE projects
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    framework   VARCHAR(50)  NOT NULL, -- ENUM('SCRUM', 'XP', 'NONE')
    created_by  VARCHAR(50)  NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP,
    CHECK (framework IN ('SCRUM', 'XP', 'NONE'))
);

-- Users <-> Projects
CREATE TABLE users_projects
(
    user_id    VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    project_id UUID        NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    role       VARCHAR(50),
    PRIMARY KEY (user_id, project_id)
    CHECK (role IN ('OWNER', 'ADMIN', 'CONTRIBUTOR'))
);

-- Sprints
CREATE TABLE sprints
(
    id          UUID PRIMARY KEY,
    project_id  UUID         NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    name        VARCHAR(255) NOT NULL,
    goal        TEXT,
    start_date  DATE,
    end_date    DATE,
    created_by  VARCHAR(50)  NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

-- Stories
CREATE TABLE stories
(
    id          UUID PRIMARY KEY,
    project_id  UUID         NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    sprint_id   UUID         REFERENCES sprints (id) ON DELETE SET NULL, -- nullable
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(50)  NOT NULL, -- Not an ENUM, but should reflect the current status (and thereby which column it is in)
    priority    INT       DEFAULT 0,
    created_by  VARCHAR(50)  NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

-- Tasks
CREATE TABLE tasks
(
    id              UUID PRIMARY KEY,
    story_id        UUID         NOT NULL REFERENCES stories (id) ON DELETE CASCADE,
    sprint_id       UUID         REFERENCES sprints (id) ON DELETE SET NULL, -- nullable
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    status          VARCHAR(50)  NOT NULL, -- Not an ENUM, but should reflect the current status (and thereby which column it is in)
    estimate_tshirt VARCHAR(50),           -- nullable
    estimate_points INT,                   -- nullable
    created_by      VARCHAR(50)  NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by     VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP,
    CHECK ( estimate_tshirt IS NULL OR estimate_tshirt IN ('XSMALL', 'SMALL', 'MEDIUM', 'LARGE', 'XLARGE') ),
    CHECK ( estimate_points IS NULL OR estimate_points IN (1, 2, 3, 5, 8, 13, 21) ),
);

-- task_assignees
CREATE TABLE task_assignees
(
    task_id UUID        NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    user_id VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, user_id)
);

-- Subtasks
CREATE TABLE subtasks
(
    id          UUID PRIMARY KEY,
    task_id     UUID         NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    is_done     BOOLEAN   DEFAULT FALSE,
    created_by  VARCHAR(50)  NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by VARCHAR(50)  REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

-- Comments (polymorphic relation with CHECK constraint)
CREATE TABLE comments
(
    id          UUID PRIMARY KEY,
    content     TEXT        NOT NULL,
    story_id    UUID REFERENCES stories (id) ON DELETE CASCADE,
    task_id     UUID REFERENCES tasks (id) ON DELETE CASCADE,
    subtask_id  UUID REFERENCES subtasks (id) ON DELETE CASCADE,
    created_by  VARCHAR(50) NOT NULL REFERENCES users (id) ON DELETE SET NULL,
    modified_by VARCHAR(50) REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP,
    CHECK (
        (story_id IS NOT NULL AND task_id IS NULL AND subtask_id IS NULL) OR
        (story_id IS NULL AND task_id IS NOT NULL AND subtask_id IS NULL) OR
        (story_id IS NULL AND task_id IS NULL AND subtask_id IS NOT NULL)
        )
);