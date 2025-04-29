-- === Insert STORIES ===
INSERT INTO stories (id, project_id, title, description, status, priority, created_by)
VALUES
-- Project 1 stories
('11111111-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Setup Kanban Board',
 'Initial setup of project board', 'TODO', 1, 'dummy-user-1'),
('11111111-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'User Authentication',
 'Implement Clerk login', 'TODO', 2, 'dummy-user-1'),

-- Project 2 stories
('11111111-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', 'Pair Programming Guide',
 'Write a guide for pair programming', 'TODO', 1, 'dummy-user-2'),
('11111111-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002', 'CI/CD Setup',
 'Set up continuous integration workflows', 'TODO', 2, 'dummy-user-2');
