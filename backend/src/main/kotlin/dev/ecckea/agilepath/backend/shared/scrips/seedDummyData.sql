-- Insert dummy users
INSERT INTO users (id, github_username, email, full_name, avatar_url)
VALUES
    ('user_001', 'agilealice', 'alice@example.com', 'Alice Dev', 'https://example.com/avatar/alice.png'),
    ('user_002', 'agilebob', 'bob@example.com', 'Bob Tester', 'https://example.com/avatar/bob.png'),
    ('user_003', 'agilecharlie', 'charlie@example.com', 'Charlie Manager', 'https://example.com/avatar/charlie.png');

-- Insert dummy projects
INSERT INTO projects (id, name, description, framework, created_by)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'AgilePath Dummy Demo', 'A sample agile project', 'SCRUM', 'user_001');

-- Link users to project
INSERT INTO users_projects (user_id, project_id, role)
VALUES
    ('user_001', '00000000-0000-0000-0000-000000000001', 'OWNER'),
    ('user_002', '00000000-0000-0000-0000-000000000001', 'ADMIN'),
    ('user_003', '00000000-0000-0000-0000-000000000001', 'ADMIN');

-- Insert sprint
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, created_by)
VALUES
    ('00000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000001', 'Sprint 1', 'Deliver MVP', '2025-01-01', '2025-01-14', 'user_003');

-- Insert stories
INSERT INTO stories (id, project_id, sprint_id, title, description, status, priority, created_by)
VALUES
    ('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011',
     'User login', 'As a user, I want to log in with GitHub', 'TO_DO', 1, 'user_001'),
    ('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011',
     'Kanban board', 'As a user, I want to see tasks on a board', 'IN_PROGRESS', 2, 'user_003');

-- Insert tasks
INSERT INTO tasks (id, story_id, sprint_id, title, description, status, estimate_tshirt, estimate_points, created_by)
VALUES
    ('00000000-0000-0000-0000-000000001001', '00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000011',
     'Implement GitHub OAuth', 'Use Clerk to authenticate GitHub users', 'TO_DO', 'MEDIUM', 5, 'user_002'),
    ('00000000-0000-0000-0000-000000001002', '00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000011',
     'Create board UI', 'Display tasks in columns', 'IN_PROGRESS', 'LARGE', 8, 'user_001');

-- Assign users to tasks
INSERT INTO task_assignees (task_id, user_id)
VALUES
    ('00000000-0000-0000-0000-000000001001', 'user_002'),
    ('00000000-0000-0000-0000-000000001002', 'user_001'),
    ('00000000-0000-0000-0000-000000001002', 'user_002');

-- Insert subtasks
INSERT INTO subtasks (id, task_id, title, description, is_done, created_by)
VALUES
    ('00000000-0000-0000-0000-000000002001', '00000000-0000-0000-0000-000000001001',
     'Register Clerk app', 'Set up GitHub login via Clerk dashboard', TRUE, 'user_002'),
    ('00000000-0000-0000-0000-000000002002', '00000000-0000-0000-0000-000000001001',
     'Validate token in backend', 'Ensure valid Clerk JWTs in API', FALSE, 'user_001');

-- Insert comments (one for each type)
INSERT INTO comments (id, content, story_id, created_by)
VALUES
    ('00000000-0000-0000-0000-000000003001', 'Let’s use ClerkJS v4 here.', '00000000-0000-0000-0000-000000000101', 'user_002');

INSERT INTO comments (id, content, task_id, created_by)
VALUES
    ('00000000-0000-0000-0000-000000003002', 'I’ll take care of this task today.', '00000000-0000-0000-0000-000000001002', 'user_001');

INSERT INTO comments (id, content, subtask_id, created_by)
VALUES
    ('00000000-0000-0000-0000-000000003003', 'Is this done? If not, I can help.', '00000000-0000-0000-0000-000000002002', 'user_003');
