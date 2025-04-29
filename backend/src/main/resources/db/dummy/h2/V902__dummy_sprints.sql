-- === SPRINTS for Project 1 ===
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, created_by)
VALUES ('11111111-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Sprint 1',
        'Get started with Kanban board', CURRENT_DATE, CURRENT_DATE + 14, 'dummy-user-1'),
       ('11111111-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'Sprint 2',
        'Improve UI and add login', CURRENT_DATE + 15, CURRENT_DATE + 28, 'dummy-user-1'),
       ('11111111-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'Sprint 3',
        'Enable agile roles and task flow', CURRENT_DATE + 29, CURRENT_DATE + 42, 'dummy-user-1');

-- === SPRINTS for Project 2 ===
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, created_by)
VALUES ('22222222-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 'Sprint 1',
        'Pair programming kickoff', CURRENT_DATE, CURRENT_DATE + 14, 'dummy-user-2'),
       ('22222222-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Sprint 2',
        'Continuous integration setup', CURRENT_DATE + 15, CURRENT_DATE + 28, 'dummy-user-2'),
       ('22222222-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', 'Sprint 3',
        'Refactor and test coverage', CURRENT_DATE + 29, CURRENT_DATE + 42, 'dummy-user-2');