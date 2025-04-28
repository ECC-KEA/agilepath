-- === Insert TASKS ===
INSERT INTO tasks (id, story_id, title, description, sprint_column_id, created_by)
VALUES
-- Tasks for Project 1 Story 1
('22222222-0000-0000-0000-000000000001', '11111111-0000-0000-0000-000000000001', 'Design Board Layout',
 'Design the initial Kanban board layout.', 'c1111111-0000-0000-0000-000000000001', 'dummy-user-1'),
('22222222-0000-0000-0000-000000000002', '11111111-0000-0000-0000-000000000001', 'Setup Database',
 'Set up initial Postgres database.', 'c1111111-0000-0000-0000-000000000001', 'dummy-user-1'),

-- Tasks for Project 2 Story 3
('22222222-0000-0000-0000-000000000003', '11111111-0000-0000-0000-000000000003', 'Write Pair Programming Article',
 'Explain best practices for pair programming.', 'c2222222-0000-0000-0000-000000000001', 'dummy-user-2');