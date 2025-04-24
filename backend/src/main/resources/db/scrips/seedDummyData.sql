-- This file contains SQL commands to seed the database with dummy data.
-- Please ensure that the database is empty before running this script.
-- This script is intended for development purposes only as test has its own via FlyWay.

-- === Insert users ===
INSERT INTO users (id, github_username, email, full_name, avatar_url)
VALUES ('dummy-user-1', 'gh-dummy-1', 'test1@example.com', 'Dummy User',
        'https://avatars.githubusercontent.com/u/1?v=4'),
       ('dummy-user-2', 'gh-dummy-2', 'test2@example.com', 'Test User',
        'https://avatars.githubusercontent.com/u/2?v=4');

-- === Insert projects ===
INSERT INTO projects (id, name, description, framework, created_by)
VALUES ('00000000-0000-0000-0000-000000000001', 'Agile Kanban Board', 'A test project for agile collaboration', 'SCRUM',
        'dummy-user-1'),
       ('00000000-0000-0000-0000-000000000002', 'Learning XP Together', 'XP-focused dev collaboration', 'XP',
        'dummy-user-2');

-- === Link users to projects with roles ===
INSERT INTO users_projects (user_id, project_id, role)
VALUES ('dummy-user-1', '00000000-0000-0000-0000-000000000001', 'OWNER'),
       ('dummy-user-2', '00000000-0000-0000-0000-000000000001', 'CONTRIBUTOR'),
       ('dummy-user-2', '00000000-0000-0000-0000-000000000002', 'OWNER'),
       ('dummy-user-1', '00000000-0000-0000-0000-000000000002', 'CONTRIBUTOR');

-- === SPRINTS for Project 1 ===
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, created_by)
VALUES ('11111111-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Sprint 1',
        'Get started with Kanban board', CURRENT_DATE, CURRENT_DATE + INTERVAL '14 DAY', 'dummy-user-1'),
       ('11111111-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'Sprint 2',
        'Improve UI and add login', CURRENT_DATE + INTERVAL '15 DAY', CURRENT_DATE + INTERVAL '28 DAY', 'dummy-user-1'),
       ('11111111-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'Sprint 3',
        'Enable agile roles and task flow', CURRENT_DATE + INTERVAL '29 DAY', CURRENT_DATE + INTERVAL '42 DAY',
        'dummy-user-1');

-- === SPRINT COLUMNS for Project 1 sprints ===
INSERT INTO sprint_columns (id, sprint_id, name, status, column_index)
VALUES
-- Sprint 1
('c1111111-0000-0000-0000-000000000001', '11111111-0000-0000-0000-000000000001', 'To Do', 'TODO', 0),
('c1111111-0000-0000-0000-000000000002', '11111111-0000-0000-0000-000000000001', 'In Progress', 'IN_PROGRESS', 1),
('c1111111-0000-0000-0000-000000000003', '11111111-0000-0000-0000-000000000001', 'Done', 'DONE', 2),
-- Sprint 2
('c1111111-0000-0000-0000-000000000004', '11111111-0000-0000-0000-000000000002', 'To Do', 'TODO', 0),
('c1111111-0000-0000-0000-000000000005', '11111111-0000-0000-0000-000000000002', 'In Progress', 'IN_PROGRESS', 1),
('c1111111-0000-0000-0000-000000000006', '11111111-0000-0000-0000-000000000002', 'Done', 'DONE', 2),
-- Sprint 3
('c1111111-0000-0000-0000-000000000007', '11111111-0000-0000-0000-000000000003', 'To Do', 'TODO', 0),
('c1111111-0000-0000-0000-000000000008', '11111111-0000-0000-0000-000000000003', 'In Progress', 'IN_PROGRESS', 1),
('c1111111-0000-0000-0000-000000000009', '11111111-0000-0000-0000-000000000003', 'Done', 'DONE', 2);

-- === SPRINTS for Project 2 ===
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, created_by)
VALUES ('22222222-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 'Sprint 1',
        'Pair programming kickoff', CURRENT_DATE, CURRENT_DATE + INTERVAL '14 DAY', 'dummy-user-2'),
       ('22222222-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Sprint 2',
        'Continuous integration setup', CURRENT_DATE + INTERVAL '15 DAY', CURRENT_DATE + INTERVAL '28 DAY',
        'dummy-user-2'),
       ('22222222-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', 'Sprint 3',
        'Refactor and test coverage', CURRENT_DATE + INTERVAL '29 DAY', CURRENT_DATE + INTERVAL '42 DAY',
        'dummy-user-2');

-- === SPRINT COLUMNS for Project 2 sprints ===
INSERT INTO sprint_columns (id, sprint_id, name, status, column_index)
VALUES
-- Sprint 1
('c2222222-0000-0000-0000-000000000001', '22222222-0000-0000-0000-000000000001', 'To Do', 'TODO', 0),
('c2222222-0000-0000-0000-000000000002', '22222222-0000-0000-0000-000000000001', 'In Progress', 'IN_PROGRESS', 1),
('c2222222-0000-0000-0000-000000000003', '22222222-0000-0000-0000-000000000001', 'Done', 'DONE', 2),
-- Sprint 2
('c2222222-0000-0000-0000-000000000004', '22222222-0000-0000-0000-000000000002', 'To Do', 'TODO', 0),
('c2222222-0000-0000-0000-000000000005', '22222222-0000-0000-0000-000000000002', 'In Progress', 'IN_PROGRESS', 1),
('c2222222-0000-0000-0000-000000000006', '22222222-0000-0000-0000-000000000002', 'Done', 'DONE', 2),
-- Sprint 3
('c2222222-0000-0000-0000-000000000007', '22222222-0000-0000-0000-000000000003', 'To Do', 'TODO', 0),
('c2222222-0000-0000-0000-000000000008', '22222222-0000-0000-0000-000000000003', 'In Progress', 'IN_PROGRESS', 1),
('c2222222-0000-0000-0000-000000000009', '22222222-0000-0000-0000-000000000003', 'Done', 'DONE', 2);
