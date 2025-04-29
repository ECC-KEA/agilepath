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