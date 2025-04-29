-- === Insert SUBTASKS ===
INSERT INTO subtasks (id, task_id, title, description, created_by)
VALUES
-- Subtasks for Task 1
('33333333-0000-0000-0000-000000000001', '22222222-0000-0000-0000-000000000001', 'Draw Wireframes',
 'Sketch the wireframes for the board.', 'dummy-user-1'),
('33333333-0000-0000-0000-000000000002', '22222222-0000-0000-0000-000000000001', 'Create Column Entities',
 'Define entities for board columns.', 'dummy-user-1'),

-- Subtasks for Task 3
('33333333-0000-0000-0000-000000000003', '22222222-0000-0000-0000-000000000003', 'Draft First Section',
 'Write an intro to pair programming.', 'dummy-user-2');
