-- Users (from Clerk + GitHub)
CREATE TABLE users
(
    id              UUID PRIMARY KEY,
    clerk_id        VARCHAR(50)  NOT NULL UNIQUE, -- External ID from Clerk
    github_username VARCHAR(100),
    email           VARCHAR(255) NOT NULL UNIQUE,
    full_name       VARCHAR(255),                 -- From GitHub via Clerk; may be split later if needed
    avatar_url      TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_by     UUID REFERENCES users (id),
    modified_at     TIMESTAMP

);

-- Projects
CREATE TABLE projects
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_by  UUID         NOT NULL REFERENCES users (id),
    modified_by UUID REFERENCES users (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

-- Users <-> Projects
CREATE TABLE users_projects
(
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    project_id UUID NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    role       VARCHAR(50),
    PRIMARY KEY (user_id, project_id)
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
    created_by  UUID         NOT NULL REFERENCES users (id),
    modified_by UUID REFERENCES users (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP
);

-- Boards
CREATE TABLE boards
(
    id         UUID PRIMARY KEY,
    project_id UUID         NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    sprint_id  UUID         REFERENCES sprints (id) ON DELETE SET NULL,
    name       VARCHAR(255) NOT NULL,
    type       VARCHAR(50)  NOT NULL DEFAULT 'SPRINT',
    created