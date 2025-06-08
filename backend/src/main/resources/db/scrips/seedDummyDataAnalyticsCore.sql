-- Comprehensive seed data script for analytics testing
-- a Single project with rich event history and realistic workflows
-- This script is intended for development purposes only.

-- === Insert users ===
INSERT INTO users (id, github_username, email, full_name, avatar_url)
VALUES ('alice-123', 'alice-dev', 'alice@company.com', 'Alice Johnson',
        'https://avatars.githubusercontent.com/u/1?v=4'),
       ('bob-456', 'bob-fullstack', 'bob@company.com', 'Bob Smith',
        'https://avatars.githubusercontent.com/u/2?v=4'),
       ('carol-789', 'carol-ui', 'carol@company.com', 'Carol Williams',
        'https://avatars.githubusercontent.com/u/3?v=4'),
       ('david-012', 'david-backend', 'david@company.com', 'David Brown',
        'https://avatars.githubusercontent.com/u/4?v=4'),
       ('eve-345', 'eve-qa', 'eve@company.com', 'Eve Davis',
        'https://avatars.githubusercontent.com/u/5?v=4');

-- == INSERT ASSISTANTS ==
INSERT INTO llm_assistants (id, name, description, model, prompt, temperature, top_p, max_tokens)
VALUES
    -- Assistant 1
    ('e1111111-0000-0000-0000-000000000001', 'Task Helper',
     'Helps break down tasks, while remaining suggestive and encouraging reflection', 'gpt-4o-mini',
     '##Role\n You are a helpful assistant that breaks down student tasks into small, manageable subtasks.\n## Goal\n Your goal is to encourage the students to think for themselves, so give hints and suggestions on ideas of thinking, instead of simply breaking the tasks down.\n##Input Format\n You receive data in JSON format like this:\n{\"task_header\": \"Example Header\", \"task_description\": \"Example description\"}',
     0.7, 1.0, 1000);

-- === Insert project ===
INSERT INTO projects (id, name, description, framework, estimation_method, created_by)
VALUES ('00000000-0000-0000-0000-000000000003', 'AgileFlow Platform',
        'Complete agile project management platform with advanced analytics and team collaboration features',
        'SCRUM', 'STORY_POINTS', 'alice-123');

-- === Link users to project ===
INSERT INTO users_projects (user_id, project_id, role)
VALUES ('alice-123', '00000000-0000-0000-0000-000000000003', 'OWNER'),
       ('bob-456', '00000000-0000-0000-0000-000000000003', 'ADMIN'),
       ('carol-789', '00000000-0000-0000-0000-000000000003', 'CONTRIBUTOR'),
       ('david-012', '00000000-0000-0000-0000-000000000003', 'CONTRIBUTOR'),
       ('eve-345', '00000000-0000-0000-0000-000000000003', 'CONTRIBUTOR');

-- === SPRINTS (4 completed, 1 in progress) ===
INSERT INTO sprints (id, project_id, name, goal, start_date, end_date, capacity, created_by)
VALUES
-- Sprint 1: COMPLETED (60 days ago to 46 days ago)
('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'Foundation Sprint',
 'Set up project infrastructure and core authentication system',
 CURRENT_DATE - INTERVAL '60 DAY', CURRENT_DATE - INTERVAL '46 DAY', 85, 'alice-123'),

-- Sprint 2: COMPLETED (45 days ago to 31 days ago)
('10000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'User Management Sprint',
 'Implement user profiles, team management, and project creation',
 CURRENT_DATE - INTERVAL '45 DAY', CURRENT_DATE - INTERVAL '31 DAY', 90, 'alice-123'),

-- Sprint 3: COMPLETED (30 days ago to 16 days ago)
('10000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', 'Sprint Management Sprint',
 'Build sprint creation, kanban boards, and task management features',
 CURRENT_DATE - INTERVAL '30 DAY', CURRENT_DATE - INTERVAL '16 DAY', 95, 'alice-123'),

-- Sprint 4: COMPLETED (15 days ago to 1 day ago)
('10000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000003', 'Analytics Foundation Sprint',
 'Implement event tracking, basic analytics, and reporting infrastructure',
 CURRENT_DATE - INTERVAL '15 DAY', CURRENT_DATE - INTERVAL '1 DAY', 100, 'alice-123'),

-- Sprint 5: IN PROGRESS (today to 14 days from now)
('10000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', 'Advanced Analytics Sprint',
 'Build comprehensive analytics dashboard with charts and insights',
 CURRENT_DATE, CURRENT_DATE + INTERVAL '14 DAY', 105, 'alice-123');

-- === SPRINT COLUMNS ===
INSERT INTO sprint_columns (id, sprint_id, name, status, column_index)
VALUES
-- Sprint 1 columns
('11000000-0100-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'To Do', 'TODO', 0),
('11000000-0100-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'In Progress', 'IN_PROGRESS', 1),
('11000000-0100-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Done', 'DONE', 2),

-- Sprint 2 columns
('11000000-0200-0000-0000-000000000001', '10000000-0000-0000-0000-000000000002', 'To Do', 'TODO', 0),
('11000000-0200-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', 'In Progress', 'IN_PROGRESS', 1),
('11000000-0200-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', 'Done', 'DONE', 2),

-- Sprint 3 columns
('11000000-0300-0000-0000-000000000001', '10000000-0000-0000-0000-000000000003', 'To Do', 'TODO', 0),
('11000000-0300-0000-0000-000000000002', '10000000-0000-0000-0000-000000000003', 'In Progress', 'IN_PROGRESS', 1),
('11000000-0300-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', 'Done', 'DONE', 2),

-- Sprint 4 columns
('11000000-0400-0000-0000-000000000001', '10000000-0000-0000-0000-000000000004', 'To Do', 'TODO', 0),
('11000000-0400-0000-0000-000000000002', '10000000-0000-0000-0000-000000000004', 'In Progress', 'IN_PROGRESS', 1),
('11000000-0400-0000-0000-000000000003', '10000000-0000-0000-0000-000000000004', 'Done', 'DONE', 2),

-- Sprint 5 columns (current)
('11000000-0500-0000-0000-000000000001', '10000000-0000-0000-0000-000000000005', 'To Do', 'TODO', 0),
('11000000-0500-0000-0000-000000000002', '10000000-0000-0000-0000-000000000005', 'In Progress', 'IN_PROGRESS', 1),
('11000000-0500-0000-0000-000000000003', '10000000-0000-0000-0000-000000000005', 'Done', 'DONE', 2);

-- === STORIES (10 stories across all sprints) ===
INSERT INTO stories (id, project_id, title, description, status, priority, acceptance_criteria, created_by)
VALUES
-- Sprint 1 Stories (Foundation)
('11100000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'User Authentication System',
 'As a user, I want to securely log in and manage my account so that my data is protected', 'DONE', 1,
 'GIVEN a new user visits the application
WHEN they register with valid credentials
THEN they should receive email confirmation
AND be able to log in successfully

GIVEN an existing user
WHEN they enter correct credentials
THEN they should be authenticated and redirected to dashboard', 'alice-123'),

('11100000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'Project Infrastructure Setup',
 'As a developer, I want a solid foundation with CI/CD, database, and monitoring so that we can develop efficiently',
 'DONE', 1,
 'GIVEN the development environment
WHEN code is pushed to main branch
THEN automated tests should run
AND deployment should happen automatically
AND monitoring should track application health', 'bob-456'),

-- Sprint 2 Stories (User Management)
('11100000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', 'User Profile Management',
 'As a user, I want to manage my profile information and preferences so that the system works best for me', 'DONE', 1,
 'GIVEN a logged-in user
WHEN they access profile settings
THEN they should be able to update personal information
AND save notification preferences
AND see changes reflected immediately', 'carol-789'),

('11100000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000003', 'Team and Project Management',
 'As a project owner, I want to create teams and manage project access so that collaboration is organized', 'DONE', 1,
 'GIVEN a project owner
WHEN they create a new project
THEN they should be able to invite team members
AND assign roles (owner, admin, contributor)
AND manage permissions appropriately', 'alice-123'),

-- Sprint 3 Stories (Sprint Management)
('11100000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', 'Sprint Creation and Management',
 'As a scrum master, I want to create and manage sprints so that work is organized in time-boxed iterations', 'DONE', 1,
 'GIVEN a project with team members
WHEN creating a new sprint
THEN I should set start/end dates and capacity
AND be able to add stories to the sprint
AND track sprint progress over time', 'bob-456'),

('11100000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000003', 'Kanban Board Interface',
 'As a team member, I want to use a kanban board to visualize and manage task progress during sprints', 'DONE', 1,
 'GIVEN a sprint with tasks
WHEN viewing the kanban board
THEN I should see columns (To Do, In Progress, Done)
AND be able to drag tasks between columns
AND see real-time updates from other team members', 'carol-789'),

-- Sprint 4 Stories (Analytics Foundation)
('11100000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000003', 'Event Tracking System',
 'As a product manager, I want comprehensive event tracking so that we can analyze team performance and workflow',
 'DONE', 1,
 'GIVEN any user action in the system
WHEN they create, update, or move items
THEN events should be logged with timestamps
AND include user attribution and old/new values
AND be available for analytics queries', 'david-012'),

('11100000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000003', 'Basic Analytics Dashboard',
 'As a team lead, I want basic analytics about sprint performance so that I can identify improvement opportunities',
 'DONE', 2,
 'GIVEN completed sprints with tracked events
WHEN viewing the analytics dashboard
THEN I should see sprint completion rates
AND task cycle times and team velocity
AND be able to filter by time periods', 'alice-123'),

-- Sprint 5 Stories (Advanced Analytics - Current)
('11100000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000003', 'Advanced Analytics Charts',
 'As a stakeholder, I want rich visual analytics so that I can understand team performance trends and patterns',
 'IN_PROGRESS', 1,
 'GIVEN historical sprint and task data
WHEN viewing advanced analytics
THEN I should see burndown charts and velocity trends
AND task distribution and completion patterns
AND be able to export reports', 'eve-345'),

('11100000-0000-0000-0000-000000000010', '00000000-0000-0000-0000-000000000003', 'AI-Powered Project Insights',
 'As a project manager, I want AI-generated insights about team performance so that I can make data-driven decisions',
 'TODO', 2,
 'GIVEN sufficient historical data
WHEN viewing the insights panel
THEN I should see AI recommendations for sprint planning
AND predictions about delivery timelines
AND suggestions for process improvements', 'alice-123');

-- === TASKS (50+ tasks total, 5+ per story) ===
-- Sprint 1 Tasks (Foundation) - All DONE
INSERT INTO tasks (id, story_id, title, description, sprint_column_id, estimate_tshirt, estimate_points, created_by)
VALUES
-- Authentication System Tasks
('11110000-0000-0000-0000-000000000001', '11100000-0000-0000-0000-000000000001', 'Set up Clerk Authentication',
 'Integrate Clerk for user authentication and session management', '11000000-0100-0000-0000-000000000003', null,
 'POINT_8', 'alice-123'),
('11110000-0000-0000-0000-000000000002', '11100000-0000-0000-0000-000000000001', 'Create Login/Register Pages',
 'Build responsive login and registration forms with validation', '11000000-0100-0000-0000-000000000003', null,
 'POINT_5', 'carol-789'),
('11110000-0000-0000-0000-000000000003', '11100000-0000-0000-0000-000000000001', 'Implement Protected Routes',
 'Add route protection and authentication middleware', '11000000-0100-0000-0000-000000000003', null, 'POINT_3',
 'bob-456'),
('11110000-0000-0000-0000-000000000004', '11100000-0000-0000-0000-000000000001', 'User Session Management',
 'Handle user sessions, logout, and token refresh', '11000000-0100-0000-0000-000000000003', null, 'POINT_5',
 'alice-123'),
('11110000-0000-0000-0000-000000000005', '11100000-0000-0000-0000-000000000001', 'Email Verification Flow',
 'Implement email verification for new user accounts', '11000000-0100-0000-0000-000000000003', null, 'POINT_5',
 'david-012'),

-- Infrastructure Tasks
('11110000-0000-0000-0000-000000000006', '11100000-0000-0000-0000-000000000002', 'Database Schema Design',
 'Design and implement PostgreSQL schema with Flyway migrations', '11000000-0100-0000-0000-000000000003', null,
 'POINT_8', 'bob-456'),
('11110000-0000-0000-0000-000000000007', '11100000-0000-0000-0000-000000000002', 'CI/CD Pipeline Setup',
 'Configure GitHub Actions for testing and deployment', '11000000-0100-0000-0000-000000000003', null, 'POINT_8',
 'david-012'),
('11110000-0000-0000-0000-000000000008', '11100000-0000-0000-0000-000000000002', 'Docker Configuration',
 'Create Docker containers for development and production', '11000000-0100-0000-0000-000000000003', null, 'POINT_5',
 'bob-456'),
('11110000-0000-0000-0000-000000000009', '11100000-0000-0000-0000-000000000002', 'Monitoring and Logging',
 'Set up application monitoring with health checks', '11000000-0100-0000-0000-000000000003', null, 'POINT_5',
 'david-012'),
('11110000-0000-0000-0000-000000000010', '11100000-0000-0000-0000-000000000002', 'Environment Configuration',
 'Configure development, staging, and production environments', '11000000-0100-0000-0000-000000000003', null, 'POINT_3',
 'alice-123'),

-- Sprint 2 Tasks (User Management) - All DONE
-- Profile Management Tasks
('11110000-0000-0000-0000-000000000011', '11100000-0000-0000-0000-000000000003', 'User Profile API',
 'Create REST endpoints for profile CRUD operations', '11000000-0200-0000-0000-000000000003', null, 'POINT_5',
 'david-012'),
('11110000-0000-0000-0000-000000000012', '11100000-0000-0000-0000-000000000003', 'Profile Settings UI',
 'Build user-friendly profile management interface', '11000000-0200-0000-0000-000000000003', null, 'POINT_8',
 'carol-789'),
('11110000-0000-0000-0000-000000000013', '11100000-0000-0000-0000-000000000003', 'Avatar Upload Feature',
 'Implement profile picture upload with image processing', '11000000-0200-0000-0000-000000000003', null, 'POINT_5',
 'carol-789'),
('11110000-0000-0000-0000-000000000014', '11100000-0000-0000-0000-000000000003', 'Notification Preferences',
 'Add email and in-app notification preference controls', '11000000-0200-0000-0000-000000000003', null, 'POINT_3',
 'alice-123'),
('11110000-0000-0000-0000-000000000015', '11100000-0000-0000-0000-000000000003', 'Profile Validation',
 'Implement client and server-side profile data validation', '11000000-0200-0000-0000-000000000003', null, 'POINT_2',
 'david-012'),

-- Team Management Tasks
('11110000-0000-0000-0000-000000000016', '11100000-0000-0000-0000-000000000004', 'Project Creation Workflow',
 'Build project creation wizard with team setup', '11000000-0200-0000-0000-000000000003', null, 'POINT_8', 'alice-123'),
('11110000-0000-0000-0000-000000000017', '11100000-0000-0000-0000-000000000004', 'Team Invitation System',
 'Implement email invitations for team members', '11000000-0200-0000-0000-000000000003', null, 'POINT_8', 'bob-456'),
('11110000-0000-0000-0000-000000000018', '11100000-0000-0000-0000-000000000004', 'Role-Based Access Control',
 'Implement RBAC with owner/admin/contributor permissions', '11000000-0200-0000-0000-000000000003', null, 'POINT_13',
 'david-012'),
('11110000-0000-0000-0000-000000000019', '11100000-0000-0000-0000-000000000004', 'Team Member Management UI',
 'Create interface for managing team members and roles', '11000000-0200-0000-0000-000000000003', null, 'POINT_5',
 'carol-789'),
('11110000-0000-0000-0000-000000000020', '11100000-0000-0000-0000-000000000004', 'Project Settings Panel',
 'Build comprehensive project configuration interface', '11000000-0200-0000-0000-000000000003', null, 'POINT_5',
 'alice-123'),

-- Sprint 3 Tasks (Sprint Management) - All DONE
-- Sprint Management Tasks
('11110000-0000-0000-0000-000000000021', '11100000-0000-0000-0000-000000000005', 'Sprint CRUD Operations',
 'Implement create, read, update, delete for sprints', '11000000-0300-0000-0000-000000000003', null, 'POINT_8',
 'bob-456'),
('11110000-0000-0000-0000-000000000022', '11100000-0000-0000-0000-000000000005', 'Sprint Planning Interface',
 'Build sprint planning workflow with capacity management', '11000000-0300-0000-0000-000000000003', null, 'POINT_13',
 'alice-123'),
('11110000-0000-0000-0000-000000000023', '11100000-0000-0000-0000-000000000005', 'Sprint Goals and Tracking',
 'Implement sprint goal setting and progress tracking', '11000000-0300-0000-0000-000000000003', null, 'POINT_5',
 'david-012'),
('11110000-0000-0000-0000-000000000024', '11100000-0000-0000-0000-000000000005', 'Sprint Timeline View',
 'Create visual timeline for sprint planning and review', '11000000-0300-0000-0000-000000000003', null, 'POINT_5',
 'carol-789'),
('11110000-0000-0000-0000-000000000025', '11100000-0000-0000-0000-000000000005', 'Sprint Capacity Estimation',
 'Add team capacity planning with story point estimation', '11000000-0300-0000-0000-000000000003', null, 'POINT_8',
 'alice-123'),

-- Kanban Board Tasks
('11110000-0000-0000-0000-000000000026', '11100000-0000-0000-0000-000000000006', 'Drag and Drop Interface',
 'Implement smooth drag-and-drop for task management', '11000000-0300-0000-0000-000000000003', null, 'POINT_13',
 'carol-789'),
('11110000-0000-0000-0000-000000000027', '11100000-0000-0000-0000-000000000006', 'Real-time Updates',
 'Add WebSocket support for live collaboration', '11000000-0300-0000-0000-000000000003', null, 'POINT_13', 'bob-456'),
('11110000-0000-0000-0000-000000000028', '11100000-0000-0000-0000-000000000006', 'Column Customization',
 'Allow teams to customize kanban column names and workflow', '11000000-0300-0000-0000-000000000003', null, 'POINT_8',
 'david-012'),
('11110000-0000-0000-0000-000000000029', '11100000-0000-0000-0000-000000000006', 'Task Filtering and Search',
 'Add filtering by assignee, priority, and search functionality', '11000000-0300-0000-0000-000000000003', null,
 'POINT_5', 'eve-345'),
('11110000-0000-0000-0000-000000000030', '11100000-0000-0000-0000-000000000006', 'Mobile Responsive Design',
 'Ensure kanban board works well on mobile devices', '11000000-0300-0000-0000-000000000003', null, 'POINT_5',
 'carol-789'),

-- Sprint 4 Tasks (Analytics Foundation) - All DONE
-- Event Tracking Tasks
('11110000-0000-0000-0000-000000000031', '11100000-0000-0000-0000-000000000007', 'Event Logger Infrastructure',
 'Build comprehensive event logging system', '11000000-0400-0000-0000-000000000003', null, 'POINT_13', 'david-012'),
('11110000-0000-0000-0000-000000000032', '11100000-0000-0000-0000-000000000007', 'Domain Event Integration',
 'Integrate event logging into all business operations', '11000000-0400-0000-0000-000000000003', null, 'POINT_8',
 'bob-456'),
('11110000-0000-0000-0000-000000000033', '11100000-0000-0000-0000-000000000007', 'Event Data Models',
 'Design and implement event storage schema', '11000000-0400-0000-0000-000000000003', null, 'POINT_5', 'david-012'),
('11110000-0000-0000-0000-000000000034', '11100000-0000-0000-0000-000000000007', 'Event Query API',
 'Create API endpoints for querying event data', '11000000-0400-0000-0000-000000000003', null, 'POINT_5', 'alice-123'),
('11110000-0000-0000-0000-000000000035', '11100000-0000-0000-0000-000000000007', 'Event Data Validation',
 'Implement validation and cleanup for event data', '11000000-0400-0000-0000-000000000003', null, 'POINT_3',
 'david-012'),

-- Basic Analytics Tasks
('11110000-0000-0000-0000-000000000036', '11100000-0000-0000-0000-000000000008', 'Analytics Data Service',
 'Build service layer for analytics data aggregation', '11000000-0400-0000-0000-000000000003', null, 'POINT_8',
 'alice-123'),
('11110000-0000-0000-0000-000000000037', '11100000-0000-0000-0000-000000000008', 'Sprint Metrics Calculator',
 'Implement sprint completion and velocity calculations', '11000000-0400-0000-0000-000000000003', null, 'POINT_8',
 'david-012'),
('11110000-0000-0000-0000-000000000038', '11100000-0000-0000-0000-000000000008', 'Basic Dashboard UI',
 'Create initial analytics dashboard with key metrics', '11000000-0400-0000-0000-000000000003', null, 'POINT_8',
 'eve-345'),
('11110000-0000-0000-0000-000000000039', '11100000-0000-0000-0000-000000000008', 'Date Range Filtering',
 'Add time period filters for analytics views', '11000000-0400-0000-0000-000000000003', null, 'POINT_5', 'eve-345'),
('11110000-0000-0000-0000-000000000040', '11100000-0000-0000-0000-000000000008', 'Performance Optimization',
 'Optimize analytics queries for large datasets', '11000000-0400-0000-0000-000000000003', null, 'POINT_5', 'david-012'),

-- Sprint 5 Tasks (Advanced Analytics) - Mixed status
-- Charts Tasks (IN_PROGRESS)
('11110000-0000-0000-0000-000000000041', '11100000-0000-0000-0000-000000000009', 'Chart Library Integration',
 'Integrate Recharts for advanced data visualization', '11000000-0500-0000-0000-000000000003', null, 'POINT_5',
 'eve-345'),
('11110000-0000-0000-0000-000000000042', '11100000-0000-0000-0000-000000000009', 'Burndown Chart Component',
 'Build interactive burndown chart with sprint data', '11000000-0500-0000-0000-000000000003', null, 'POINT_8',
 'eve-345'),
('11110000-0000-0000-0000-000000000043', '11100000-0000-0000-0000-000000000009', 'Velocity Trend Analysis',
 'Create velocity tracking across multiple sprints', '11000000-0500-0000-0000-000000000002', null, 'POINT_8',
 'alice-123'),
('11110000-0000-0000-0000-000000000044', '11100000-0000-0000-0000-000000000009', 'Task Distribution Charts',
 'Visualize task distribution by assignee and status', '11000000-0500-0000-0000-000000000002', null, 'POINT_5',
 'eve-345'),
('11110000-0000-0000-0000-000000000045', '11100000-0000-0000-0000-000000000009', 'Export and Reporting',
 'Add PDF/CSV export capabilities for analytics', '11000000-0500-0000-0000-000000000001', null, 'POINT_5', 'bob-456'),

-- AI Insights Tasks (TODO)
('11110000-0000-0000-0000-000000000046', '11100000-0000-0000-0000-000000000010', 'Data Analysis Pipeline',
 'Build data pipeline for AI model input preparation', '11000000-0500-0000-0000-000000000001', null, 'POINT_13',
 'david-012'),
('11110000-0000-0000-0000-000000000047', '11100000-0000-0000-0000-000000000010', 'Performance Prediction Model',
 'Implement ML model for sprint outcome prediction', '11000000-0500-0000-0000-000000000001', null, 'POINT_21',
 'alice-123'),
('11110000-0000-0000-0000-000000000048', '11100000-0000-0000-0000-000000000010', 'Insights UI Component',
 'Create interface for displaying AI-generated insights', '11000000-0500-0000-0000-000000000001', null, 'POINT_8',
 'eve-345'),
('11110000-0000-0000-0000-000000000049', '11100000-0000-0000-0000-000000000010', 'Recommendation Engine',
 'Build recommendation system for process improvements', '11000000-0500-0000-0000-000000000001', null, 'POINT_21',
 'david-012'),
('11110000-0000-0000-0000-000000000050', '11100000-0000-0000-0000-000000000010', 'Insights API Integration',
 'Integrate OpenAI API for natural language insights', '11000000-0500-0000-0000-000000000001', null, 'POINT_13',
 'alice-123');

-- === TASK ASSIGNEES ===
INSERT INTO task_assignees (task_id, user_id)
VALUES
-- Sprint 1 assignments
('11110000-0000-0000-0000-000000000001', 'alice-123'),
('11110000-0000-0000-0000-000000000002', 'carol-789'),
('11110000-0000-0000-0000-000000000003', 'bob-456'),
('11110000-0000-0000-0000-000000000004', 'alice-123'),
('11110000-0000-0000-0000-000000000005', 'david-012'),
('11110000-0000-0000-0000-000000000006', 'bob-456'),
('11110000-0000-0000-0000-000000000007', 'david-012'),
('11110000-0000-0000-0000-000000000008', 'bob-456'),
('11110000-0000-0000-0000-000000000009', 'david-012'),
('11110000-0000-0000-0000-000000000010', 'alice-123'),

-- Sprint 2 assignments
('11110000-0000-0000-0000-000000000011', 'david-012'),
('11110000-0000-0000-0000-000000000012', 'carol-789'),
('11110000-0000-0000-0000-000000000013', 'carol-789'),
('11110000-0000-0000-0000-000000000014', 'alice-123'),
('11110000-0000-0000-0000-000000000015', 'david-012'),
('11110000-0000-0000-0000-000000000016', 'alice-123'),
('11110000-0000-0000-0000-000000000017', 'bob-456'),
('11110000-0000-0000-0000-000000000018', 'david-012'),
('11110000-0000-0000-0000-000000000019', 'carol-789'),
('11110000-0000-0000-0000-000000000020', 'alice-123'),

-- Sprint 3 assignments
('11110000-0000-0000-0000-000000000021', 'bob-456'),
('11110000-0000-0000-0000-000000000022', 'alice-123'),
('11110000-0000-0000-0000-000000000023', 'david-012'),
('11110000-0000-0000-0000-000000000024', 'carol-789'),
('11110000-0000-0000-0000-000000000025', 'alice-123'),
('11110000-0000-0000-0000-000000000026', 'carol-789'),
('11110000-0000-0000-0000-000000000027', 'bob-456'),
('11110000-0000-0000-0000-000000000028', 'david-012'),
('11110000-0000-0000-0000-000000000029', 'eve-345'),
('11110000-0000-0000-0000-000000000030', 'carol-789'),

-- Sprint 4 assignments
('11110000-0000-0000-0000-000000000031', 'david-012'),
('11110000-0000-0000-0000-000000000032', 'bob-456'),
('11110000-0000-0000-0000-000000000033', 'david-012'),
('11110000-0000-0000-0000-000000000034', 'alice-123'),
('11110000-0000-0000-0000-000000000035', 'david-012'),
('11110000-0000-0000-0000-000000000036', 'alice-123'),
('11110000-0000-0000-0000-000000000037', 'david-012'),
('11110000-0000-0000-0000-000000000038', 'eve-345'),
('11110000-0000-0000-0000-000000000039', 'eve-345'),
('11110000-0000-0000-0000-000000000040', 'david-012'),

-- Sprint 5 assignments (current)
('11110000-0000-0000-0000-000000000041', 'eve-345'),
('11110000-0000-0000-0000-000000000042', 'eve-345'),
('11110000-0000-0000-0000-000000000043', 'alice-123'),
('11110000-0000-0000-0000-000000000044', 'eve-345'),
('11110000-0000-0000-0000-000000000045', 'bob-456'),
('11110000-0000-0000-0000-000000000046', 'david-012'),
('11110000-0000-0000-0000-000000000047', 'alice-123'),
('11110000-0000-0000-0000-000000000048', 'eve-345'),
('11110000-0000-0000-0000-000000000049', 'david-012'),
('11110000-0000-0000-0000-000000000050', 'alice-123'),

-- Some tasks with multiple assignees (pair programming)
('11110000-0000-0000-0000-000000000027', 'david-012'),
('11110000-0000-0000-0000-000000000032', 'alice-123'),
('11110000-0000-0000-0000-000000000037', 'alice-123'),
('11110000-0000-0000-0000-000000000043', 'eve-345'),
('11110000-0000-0000-0000-000000000047', 'david-012');

-- === SUBTASKS (varying amounts: 0-7 per task) ===
INSERT INTO subtasks (id, task_id, title, description, is_done, created_by)
VALUES
-- 11110000-0000-0000-0000-000000000001 (7 subtasks)
('11111000-0000-0000-0000-000000000001', '11110000-0000-0000-0000-000000000001', 'Research Clerk Documentation',
 'Review Clerk setup and integration guide', true, 'alice-123'),
('11111000-0000-0000-0000-000000000002', '11110000-0000-0000-0000-000000000001', 'Create Clerk Application',
 'Set up Clerk app with proper configuration', true, 'alice-123'),
('11111000-0000-0000-0000-000000000003', '11110000-0000-0000-0000-000000000001', 'Install Clerk SDK',
 'Add Clerk dependencies to project', true, 'alice-123'),
('11111000-0000-0000-0000-000000000004', '11110000-0000-0000-0000-000000000001', 'Configure Environment Variables',
 'Set up Clerk API keys and settings', true, 'alice-123'),
('11111000-0000-0000-0000-000000000005', '11110000-0000-0000-0000-000000000001', 'Implement ClerkProvider',
 'Wrap application with Clerk provider', true, 'alice-123'),
('11111000-0000-0000-0000-000000000006', '11110000-0000-0000-0000-000000000001', 'Add Authentication Middleware',
 'Protect API routes with Clerk middleware', true, 'alice-123'),
('11111000-0000-0000-0000-000000000007', '11110000-0000-0000-0000-000000000001', 'Test Authentication Flow',
 'Verify login/logout works correctly', true, 'alice-123'),

-- 11110000-0000-0000-0000-000000000002 (5 subtasks)
('11111000-0000-0000-0000-000000000008', '11110000-0000-0000-0000-000000000002', 'Design Login Form',
 'Create wireframes for authentication forms', true, 'carol-789'),
('11111000-0000-0000-0000-000000000009', '11110000-0000-0000-0000-000000000002', 'Build Login Component',
 'Implement React login form with validation', true, 'carol-789'),
('11111000-0000-0000-0000-000000000010', '11110000-0000-0000-0000-000000000002', 'Build Register Component',
 'Implement registration form with error handling', true, 'carol-789'),
('11111000-0000-0000-0000-000000000011', '11110000-0000-0000-0000-000000000002', 'Add Form Validation',
 'Client-side validation for all input fields', true, 'carol-789'),
('11111000-0000-0000-0000-000000000012', '11110000-0000-0000-0000-000000000002', 'Style Authentication Pages',
 'Apply consistent styling and responsive design', true, 'carol-789'),

-- 11110000-0000-0000-0000-000000000003 (3 subtasks)
('11111000-0000-0000-0000-000000000013', '11110000-0000-0000-0000-000000000003', 'Create ProtectedRoute Component',
 'Higher-order component for route protection', true, 'bob-456'),
('11111000-0000-0000-0000-000000000014', '11110000-0000-0000-0000-000000000003', 'Update Router Configuration',
 'Apply protection to sensitive routes', true, 'bob-456'),
('11111000-0000-0000-0000-000000000015', '11110000-0000-0000-0000-000000000003', 'Handle Unauthorized Access',
 'Redirect to login for unauthorized users', true, 'bob-456'),

-- 11110000-0000-0000-0000-000000000004 (4 subtasks)
('11111000-0000-0000-0000-000000000016', '11110000-0000-0000-0000-000000000004', 'Implement Session Hooks',
 'Custom hooks for session management', true, 'alice-123'),
('11111000-0000-0000-0000-000000000017', '11110000-0000-0000-0000-000000000004', 'Add Logout Functionality',
 'Clean logout with session cleanup', true, 'alice-123'),
('11111000-0000-0000-0000-000000000018', '11110000-0000-0000-0000-000000000004', 'Token Refresh Mechanism',
 'Automatic token refresh for long sessions', true, 'alice-123'),
('11111000-0000-0000-0000-000000000019', '11110000-0000-0000-0000-000000000004', 'Session Persistence',
 'Remember login state across browser sessions', true, 'alice-123'),

-- 11110000-0000-0000-0000-000000000005 (2 subtasks)
('11111000-0000-0000-0000-000000000020', '11110000-0000-0000-0000-000000000005', 'Configure Email Templates',
 'Set up verification email templates', true, 'david-012'),
('11111000-0000-0000-0000-000000000021', '11110000-0000-0000-0000-000000000005', 'Test Email Verification',
 'End-to-end testing of email flow', true, 'david-012'),

-- 11110000-0000-0000-0000-000000000006 (6 subtasks)
('11111000-0000-0000-0000-000000000022', '11110000-0000-0000-0000-000000000006', 'Design Entity Relationships',
 'Create ER diagram for all entities', true, 'bob-456'),
('11111000-0000-0000-0000-000000000023', '11110000-0000-0000-0000-000000000006', 'Create Initial Migration',
 'V1 migration with core tables', true, 'bob-456'),
('11111000-0000-0000-0000-000000000024', '11110000-0000-0000-0000-000000000006', 'Set up Flyway Configuration',
 'Configure migration tool and scripts', true, 'bob-456'),
('11111000-0000-0000-0000-000000000025', '11110000-0000-0000-0000-000000000006', 'Add Database Indexes',
 'Optimize query performance with indexes', true, 'bob-456'),
('11111000-0000-0000-0000-000000000026', '11110000-0000-0000-0000-000000000006', 'Create Seed Data Script',
 'Development database seeding', true, 'bob-456'),
('11111000-0000-0000-0000-000000000027', '11110000-0000-0000-0000-000000000006', 'Validate Schema Integrity',
 'Test all constraints and relationships', true, 'bob-456'),

-- 11110000-0000-0000-0000-000000000007 (0 subtasks - David worked solo on this)

-- 11110000-0000-0000-0000-000000000008 (4 subtasks)
('11111000-0000-0000-0000-000000000028', '11110000-0000-0000-0000-000000000008', 'Create Development Dockerfile',
 'Multi-stage build for development', true, 'bob-456'),
('11111000-0000-0000-0000-000000000029', '11110000-0000-0000-0000-000000000008', 'Create Production Dockerfile',
 'Optimized production container build', true, 'bob-456'),
('11111000-0000-0000-0000-000000000030', '11110000-0000-0000-0000-000000000008', 'Docker Compose Setup',
 'Development environment with database', true, 'bob-456'),
('11111000-0000-0000-0000-000000000031', '11110000-0000-0000-0000-000000000008', 'Container Registry Configuration',
 'Set up image registry and deployment', true, 'bob-456'),

-- 11110000-0000-0000-0000-000000000009 (5 subtasks)
('11111000-0000-0000-0000-000000000032', '11110000-0000-0000-0000-000000000009', 'Set up Application Logging',
 'Structured logging with log levels', true, 'david-012'),
('11111000-0000-0000-0000-000000000033', '11110000-0000-0000-0000-000000000009', 'Health Check Endpoints',
 'API endpoints for monitoring services', true, 'david-012'),
('11111000-0000-0000-0000-000000000034', '11110000-0000-0000-0000-000000000009', 'Performance Metrics',
 'Application performance monitoring setup', true, 'david-012'),
('11111000-0000-0000-0000-000000000035', '11110000-0000-0000-0000-000000000009', 'Error Tracking Integration',
 'Connect to external error monitoring', true, 'david-012'),
('11111000-0000-0000-0000-000000000036', '11110000-0000-0000-0000-000000000009', 'Alerting Configuration',
 'Set up alerts for critical issues', true, 'david-012'),

-- 11110000-0000-0000-0000-000000000010 (1 subtask)
('11111000-0000-0000-0000-000000000037', '11110000-0000-0000-0000-000000000010', 'Environment Variable Management',
 'Secure configuration for all environments', true, 'alice-123'),

-- Some Sprint 2 subtasks
-- 11110000-0000-0000-0000-000000000011 (6 subtasks)
('11111000-0000-0000-0000-000000000038', '11110000-0000-0000-0000-000000000011', 'Design Profile API Schema',
 'Define request/response models', true, 'david-012'),
('11111000-0000-0000-0000-000000000039', '11110000-0000-0000-0000-000000000011', 'Implement Profile Controller',
 'REST endpoints for profile operations', true, 'david-012'),
('11111000-0000-0000-0000-000000000040', '11110000-0000-0000-0000-000000000011', 'Add Profile Validation',
 'Server-side validation rules', true, 'david-012'),
('11111000-0000-0000-0000-000000000041', '11110000-0000-0000-0000-000000000011', 'Profile Security Checks',
 'Ensure users can only edit own profiles', true, 'david-012'),
('11111000-0000-0000-0000-000000000042', '11110000-0000-0000-0000-000000000011', 'API Error Handling',
 'Comprehensive error responses', true, 'david-012'),
('11111000-0000-0000-0000-000000000043', '11110000-0000-0000-0000-000000000011', 'Profile API Testing',
 'Unit and integration tests', true, 'david-012'),

-- 11110000-0000-0000-0000-000000000018 (7 subtasks - complex RBAC)
('11111000-0000-0000-0000-000000000044', '11110000-0000-0000-0000-000000000018', 'Define Permission Matrix',
 'Map roles to specific permissions', true, 'david-012'),
('11111000-0000-0000-0000-000000000045', '11110000-0000-0000-0000-000000000018', 'Create Permission Annotations',
 'Spring Security method-level security', true, 'david-012'),
('11111000-0000-0000-0000-000000000046', '11110000-0000-0000-0000-000000000018', 'Implement Role Hierarchy',
 'Owner > Admin > Contributor permissions', true, 'david-012'),
('11111000-0000-0000-0000-000000000047', '11110000-0000-0000-0000-000000000018', 'Add Permission Middleware',
 'Automatic permission checking', true, 'david-012'),
('11111000-0000-0000-0000-000000000048', '11110000-0000-0000-0000-000000000018', 'Frontend Permission Guards',
 'UI elements based on user permissions', true, 'david-012'),
('11111000-0000-0000-0000-000000000049', '11110000-0000-0000-0000-000000000018', 'Permission Testing Suite',
 'Comprehensive security testing', true, 'david-012'),
('11111000-0000-0000-0000-000000000050', '11110000-0000-0000-0000-000000000018', 'Role Migration Scripts',
 'Safe role updates for existing users', true, 'david-012'),

-- Some Sprint 3 subtasks
-- 11110000-0000-0000-0000-000000000026 (7 subtasks - complex drag & drop)
('11111000-0000-0000-0000-000000000051', '11110000-0000-0000-0000-000000000026', 'Research Drag & Drop Libraries',
 'Evaluate react-beautiful-dnd vs alternatives', true, 'carol-789'),
('11111000-0000-0000-0000-000000000052', '11110000-0000-0000-0000-000000000026', 'Implement Basic Drag & Drop',
 'Core functionality for moving tasks', true, 'carol-789'),
('11111000-0000-0000-0000-000000000053', '11110000-0000-0000-0000-000000000026', 'Add Visual Feedback',
 'Hover states and drag indicators', true, 'carol-789'),
('11111000-0000-0000-0000-000000000054', '11110000-0000-0000-0000-000000000026', 'Handle Drop Validation',
 'Prevent invalid moves and show errors', true, 'carol-789'),
('11111000-0000-0000-0000-000000000055', '11110000-0000-0000-0000-000000000026', 'Optimize Performance',
 'Virtual scrolling for large boards', true, 'carol-789'),
('11111000-0000-0000-0000-000000000056', '11110000-0000-0000-0000-000000000026', 'Add Keyboard Navigation',
 'Accessibility for keyboard users', true, 'carol-789'),
('11111000-0000-0000-0000-000000000057', '11110000-0000-0000-0000-000000000026', 'Mobile Touch Support',
 'Touch gestures for mobile devices', true, 'carol-789'),

-- 11110000-0000-0000-0000-000000000027 (5 subtasks)
('11111000-0000-0000-0000-000000000058', '11110000-0000-0000-0000-000000000027', 'Set up WebSocket Server',
 'Real-time communication infrastructure', true, 'bob-456'),
('11111000-0000-0000-0000-000000000059', '11110000-0000-0000-0000-000000000027', 'Implement Real-time Events',
 'Broadcast task movements to all users', true, 'bob-456'),
('11111000-0000-0000-0000-000000000060', '11110000-0000-0000-0000-000000000027', 'Handle Connection Management',
 'Reconnection and error handling', true, 'bob-456'),
('11111000-0000-0000-0000-000000000061', '11110000-0000-0000-0000-000000000027', 'Add Conflict Resolution',
 'Handle simultaneous edits gracefully', true, 'bob-456'),
('11111000-0000-0000-0000-000000000062', '11110000-0000-0000-0000-000000000027', 'Test Concurrent Users',
 'Load testing with multiple users', true, 'bob-456'),

-- Some Sprint 4 subtasks
-- 11110000-0000-0000-0000-000000000031 (6 subtasks)
('11111000-0000-0000-0000-000000000063', '11110000-0000-0000-0000-000000000031', 'Design Event Data Model',
 'Flexible schema for all event types', true, 'david-012'),
('11111000-0000-0000-0000-000000000064', '11110000-0000-0000-0000-000000000031', 'Create Event Logger Interface',
 'Generic interface for different domains', true, 'david-012'),
('11111000-0000-0000-0000-000000000065', '11110000-0000-0000-0000-000000000031', 'Implement Event Storage',
 'Efficient storage and retrieval system', true, 'david-012'),
('11111000-0000-0000-0000-000000000066', '11110000-0000-0000-0000-000000000031', 'Add Event Serialization',
 'JSON serialization for complex objects', true, 'david-012'),
('11111000-0000-0000-0000-000000000067', '11110000-0000-0000-0000-000000000031', 'Create Event Query Builder',
 'Flexible querying for analytics', true, 'david-012'),
('11111000-0000-0000-0000-000000000068', '11110000-0000-0000-0000-000000000031', 'Performance Optimization',
 'Indexing and query optimization', true, 'david-012'),

-- 11110000-0000-0000-0000-000000000037 (4 subtasks)
('11111000-0000-0000-0000-000000000069', '11110000-0000-0000-0000-000000000037', 'Sprint Completion Rate Logic',
 'Calculate percentage of completed tasks', true, 'david-012'),
('11111000-0000-0000-0000-000000000070', '11110000-0000-0000-0000-000000000037', 'Velocity Calculation',
 'Story points completed per sprint', true, 'david-012'),
('11111000-0000-0000-0000-000000000071', '11110000-0000-0000-0000-000000000037', 'Cycle Time Analysis',
 'Time from start to completion', true, 'david-012'),
('11111000-0000-0000-0000-000000000072', '11110000-0000-0000-0000-000000000037', 'Team Performance Metrics',
 'Individual and team productivity', true, 'david-012'),

-- Current Sprint 5 subtasks (some incomplete)
-- 11110000-0000-0000-0000-000000000042 (3 subtasks)
('11111000-0000-0000-0000-000000000073', '11110000-0000-0000-0000-000000000042', 'Burndown Data Processing',
 'Calculate ideal vs actual burndown', true, 'eve-345'),
('11111000-0000-0000-0000-000000000074', '11110000-0000-0000-0000-000000000042', 'Interactive Chart Component',
 'Recharts implementation with tooltips', true, 'eve-345'),
('11111000-0000-0000-0000-000000000075', '11110000-0000-0000-0000-000000000042', 'Chart Responsiveness',
 'Mobile-friendly chart display', false, 'eve-345'),

-- 11110000-0000-0000-0000-000000000043 (2 subtasks)
('11111000-0000-0000-0000-000000000076', '11110000-0000-0000-0000-000000000043', 'Historical Velocity Data',
 'Aggregate velocity across sprints', false, 'alice-123'),
('11111000-0000-0000-0000-000000000077', '11110000-0000-0000-0000-000000000043', 'Trend Line Analysis',
 'Statistical trend analysis and prediction', false, 'alice-123'),

-- 11110000-0000-0000-0000-000000000047 (1 subtask - AI is complex, minimal breakdown)
('11111000-0000-0000-0000-000000000078', '11110000-0000-0000-0000-000000000047', 'ML Model Architecture Design',
 'Design neural network for sprint prediction', false, 'alice-123');

-- === COMMENTS (showing collaboration) ===
INSERT INTO comments (id, content, story_id, task_id, created_by, created_at)
VALUES
-- Comments on stories (cross-sprint discussions)
('11111100-0000-0000-0000-000000000001', 'Great foundation work! The authentication system is rock solid.',
 '11100000-0000-0000-0000-000000000001', null, 'bob-456', CURRENT_DATE - INTERVAL '45 DAY'),
('11111100-0000-0000-0000-000000000002',
 'Should we consider adding OAuth providers like Google/GitHub in the next iteration?',
 '11100000-0000-0000-0000-000000000001', null, 'carol-789', CURRENT_DATE - INTERVAL '44 DAY'),
('11111100-0000-0000-0000-000000000003', 'Good idea! Let''s add that to the backlog for Sprint 6.',
 '11100000-0000-0000-0000-000000000001', null, 'alice-123', CURRENT_DATE - INTERVAL '44 DAY'),

('11111100-0000-0000-0000-000000000004',
 'The team management features are working great. Onboarding new members is super smooth now.',
 '11100000-0000-0000-0000-000000000004', null, 'eve-345', CURRENT_DATE - INTERVAL '25 DAY'),
('11111100-0000-0000-0000-000000000005',
 'Analytics are looking fantastic! The velocity trends are really helpful for sprint planning.',
 '11100000-0000-0000-0000-000000000008', null, 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),

-- Comments on tasks (detailed technical discussions)
('11111100-0000-0000-0000-000000000006',
 'Clerk integration went smoothly. The JWT handling is much cleaner than our previous solution.', null,
 '11110000-0000-0000-0000-000000000001', 'alice-123', CURRENT_DATE - INTERVAL '55 DAY'),
('11111100-0000-0000-0000-000000000007', 'Agreed! The middleware setup was straightforward too.', null,
 '11110000-0000-0000-0000-000000000001', 'bob-456', CURRENT_DATE - INTERVAL '55 DAY'),

('11111100-0000-0000-0000-000000000008', 'The drag and drop feels really smooth. Great work on the visual feedback!',
 null, '11110000-0000-0000-0000-000000000026', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('11111100-0000-0000-0000-000000000009', 'Thanks! The mobile touch support was the trickiest part.', null,
 '11110000-0000-0000-0000-000000000026', 'carol-789', CURRENT_DATE - INTERVAL '20 DAY'),

('11111100-0000-0000-0000-000000000010',
 'WebSocket integration is working perfectly with the drag & drop. No conflicts detected during testing.', null,
 '11110000-0000-0000-0000-000000000027', 'david-012', CURRENT_DATE - INTERVAL '18 DAY'),
('11111100-0000-0000-0000-000000000011', 'The reconnection logic handles network issues really well.', null,
 '11110000-0000-0000-0000-000000000027', 'eve-345', CURRENT_DATE - INTERVAL '18 DAY'),

('11111100-0000-0000-0000-000000000012',
 'Event logging is capturing everything we need. The analytics data looks comprehensive.', null,
 '11110000-0000-0000-0000-000000000031', 'alice-123', CURRENT_DATE - INTERVAL '10 DAY'),
('11111100-0000-0000-0000-000000000013',
 'Performance is good even with thousands of events. The indexing strategy is working.', null,
 '11110000-0000-0000-0000-000000000031', 'david-012', CURRENT_DATE - INTERVAL '10 DAY'),

('11111100-0000-0000-0000-000000000014',
 'The burndown chart is looking great! The ideal vs actual comparison is very insightful.', null,
 '11110000-0000-0000-0000-000000000042', 'alice-123', CURRENT_DATE - INTERVAL '3 DAY'),
('11111100-0000-0000-0000-000000000015',
 'Working on the mobile responsiveness now. Should be ready for testing tomorrow.', null,
 '11110000-0000-0000-0000-000000000042', 'eve-345', CURRENT_DATE - INTERVAL '2 DAY'),

('11111100-0000-0000-0000-000000000016',
 'The velocity trend analysis is more complex than expected. Might need an extra day.', null,
 '11110000-0000-0000-0000-000000000043', 'alice-123', CURRENT_DATE - INTERVAL '1 DAY'),
('11111100-0000-0000-0000-000000000017',
 'No problem, quality is more important than speed. Let me know if you need help with the statistical calculations.',
 null, '11110000-0000-0000-0000-000000000043', 'david-012', CURRENT_DATE - INTERVAL '1 DAY');

-- === PROJECT EVENTS ===
INSERT INTO project_events (id, project_id, event_type, old_value, new_value, triggered_by, created_at)
VALUES ('00000000-0000-0000-0000-100000000001', '00000000-0000-0000-0000-000000000003', 'CREATED', null,
        'AgileFlow Platform', 'alice-123', CURRENT_DATE - INTERVAL '75 DAY'),
       ('00000000-0000-0000-0000-100000000002', '00000000-0000-0000-0000-000000000003', 'TEAM_MEMBER_ADDED', null,
        'bob-456 as ADMIN', 'alice-123', CURRENT_DATE - INTERVAL '74 DAY'),
       ('00000000-0000-0000-0000-100000000003', '00000000-0000-0000-0000-000000000003', 'TEAM_MEMBER_ADDED', null,
        'carol-789 as CONTRIBUTOR', 'alice-123', CURRENT_DATE - INTERVAL '74 DAY'),
       ('00000000-0000-0000-0000-100000000004', '00000000-0000-0000-0000-000000000003', 'TEAM_MEMBER_ADDED', null,
        'david-012 as CONTRIBUTOR', 'alice-123', CURRENT_DATE - INTERVAL '73 DAY'),
       ('00000000-0000-0000-0000-100000000005', '00000000-0000-0000-0000-000000000003', 'TEAM_MEMBER_ADDED', null,
        'eve-345 as CONTRIBUTOR', 'alice-123', CURRENT_DATE - INTERVAL '72 DAY');

-- === SPRINT EVENTS ===
INSERT INTO sprint_events (id, sprint_id, event_type, old_value, new_value, triggered_by, created_at)
VALUES
-- Sprint 1 Events (Foundation Sprint)
('00000000-0000-0000-0000-110000000001', '10000000-0000-0000-0000-000000000001', 'CREATED', null, 'Foundation Sprint',
 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000002', '10000000-0000-0000-0000-000000000001', 'STARTED', null,
 'Task Setup Authentication Service moved to In Progress', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0000-110000000003', '10000000-0000-0000-0000-000000000001', 'COMPLETED', 'STARTED', 'COMPLETED',
 'alice-123', CURRENT_DATE - INTERVAL '46 DAY'),
('00000000-0000-0000-0000-110000000004', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Set up Clerk Authentication', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000005', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Create Login/Register Pages', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000006', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Implement Protected Routes', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000007', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'User Session Management', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000008', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Email Verification Flow', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000009', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Database Schema Design', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000010', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'CI/CD Pipeline Setup', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000011', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Docker Configuration', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000012', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Monitoring and Logging', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0000-110000000013', '10000000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Environment Configuration', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
-- Sprint 2 Events (User Management Sprint)
('00000000-0000-0000-0000-110000000014', '10000000-0000-0000-0000-000000000002', 'CREATED', null,
 'User Management Sprint', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000015', '10000000-0000-0000-0000-000000000002', 'STARTED', null,
 'Task User Profile API moved to In Progress', 'david-012', CURRENT_DATE - INTERVAL '45 DAY'),
('00000000-0000-0000-0000-110000000016', '10000000-0000-0000-0000-000000000002', 'COMPLETED', 'STARTED', 'COMPLETED',
 'alice-123', CURRENT_DATE - INTERVAL '31 DAY'),
('00000000-0000-0000-0000-110000000017', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null, 'User Profile API',
 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000018', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Profile Settings UI', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000019', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Avatar Upload Feature', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000020', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Notification Preferences', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000021', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Profile Validation', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000022', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Project Creation Workflow', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000023', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Team Invitation System', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000024', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Role-Based Access Control', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000025', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Team Member Management UI', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0000-0000-0000-110000000026', '10000000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Project Settings Panel', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
-- Sprint 3 Events (Sprint Management Sprint)
('00000000-0000-0000-0000-110000000027', '10000000-0000-0000-0000-000000000003', 'CREATED', null,
 'Sprint Management Sprint', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000028', '10000000-0000-0000-0000-000000000003', 'STARTED', null,
 'Task Sprint CRUD Operations moved to In Progress', 'bob-456', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0000-110000000029', '10000000-0000-0000-0000-000000000003', 'COMPLETED', 'STARTED', 'COMPLETED',
 'alice-123', CURRENT_DATE - INTERVAL '16 DAY'),
('00000000-0000-0000-0000-110000000030', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Sprint CRUD Operations', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000031', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Sprint Planning Interface', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000032', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Sprint Goals and Tracking', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000033', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Sprint Timeline View', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000034', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Sprint Capacity Estimation', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000035', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000036', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000037', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Column Customization', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000038', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Task Filtering and Search', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0000-0000-0000-110000000039', '10000000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Mobile Responsive Design', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),

-- Sprint 4 Events (Analytics Foundation Sprint)
('00000000-0000-0000-0000-110000000041', '10000000-0000-0000-0000-000000000004', 'CREATED', null,
 'Analytics Foundation Sprint', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000042', '10000000-0000-0000-0000-000000000004', 'STARTED', null,
 'Task Event Logger Infrastructure moved to In Progress', 'david-012', CURRENT_DATE - INTERVAL '15 DAY'),
('00000000-0000-0000-0000-110000000043', '10000000-0000-0000-0000-000000000004', 'COMPLETED', 'STARTED', 'COMPLETED',
 'alice-123', CURRENT_DATE - INTERVAL '1 DAY'),
('00000000-0000-0000-0000-110000000044', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Event Logger Infrastructure', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000045', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Domain Event Integration', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000046', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Event Data Models', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000047', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null, 'Event Query API',
 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000048', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Event Data Validation', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0000-0000-0000-110000000049', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Analytics Data Service', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0000-0000-0000-110000000050', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Sprint Metrics Calculator', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0000-0000-0000-110000000051', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Basic Dashboard UI', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000052', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Date Range Filtering', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0000-0000-0000-110000000053', '10000000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Performance Optimization', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),

-- Sprint 5 Events (Advanced Analytics Sprint - Current - In Progress)
('00000000-0000-0000-0000-110000000054', '10000000-0000-0000-0000-000000000005', 'CREATED', null,
 'Chart Library Integration', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000055', '10000000-0000-0000-0000-000000000005', 'STARTED', null,
 'Task Chart Library Integration moved to In Progress', 'eve-345', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000056', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Chart Library Integration', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000057', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Burndown Chart Component', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000058', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Velocity Trend Analysis', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000059', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Task Distribution Charts', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000060', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Export and Reporting', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000061', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Data Analysis Pipeline', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000062', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Performance Prediction Model', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000063', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Insights UI Component', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000064', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Recommendation Engine', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0000-0000-0000-110000000065', '10000000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Insights API Integration', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY');

-- === STORY EVENTS ===
INSERT INTO story_events (id, story_id, event_type, old_value, new_value, triggered_by, created_at)
VALUES
-- User Authentication System Story Events
('00000000-0000-0000-0001-111000000001', '11100000-0000-0000-0000-000000000001', 'CREATED', null,
 'User Authentication System', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0000-0000-0001-111000000002', '11100000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Set up Clerk Authentication', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000003', '11100000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Create Login/Register Pages', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000002', '11100000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Set up Clerk Authentication', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000003', '11100000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Create Login/Register Pages', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000002', '11100000-0000-0000-0000-000000000001', 'TASK_ADDED', null,
 'Set up Clerk Authentication', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000004', '11100000-0000-0000-0000-000000000001', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000005', '11100000-0000-0000-0000-000000000001', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'alice-123', CURRENT_DATE - INTERVAL '46 DAY'),
('00000000-0000-0000-0001-111000000006', '11100000-0000-0000-0000-000000000001', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '46 DAY'),
('00000000-0000-0000-0001-111000000007', '11100000-0000-0000-0000-000000000001', 'COMMENT_ADDED', null,
 'Great foundation work! The authentication system is rock solid.', 'bob-456', CURRENT_DATE - INTERVAL '49 DAY'),

-- Project Infrastructure Setup Story Events
('00000000-0000-0000-0001-111000000009', '11100000-0000-0000-0000-000000000002', 'CREATED', null,
 'Team and Project Management', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000010', '11100000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Project Creation Workflow', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000011', '11100000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Team Invitation System', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000012', '11100000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Project Creation Workflow', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000013', '11100000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Team Invitation System', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000014', '11100000-0000-0000-0000-000000000002', 'TASK_ADDED', null,
 'Project Creation Workflow', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000015', '11100000-0000-0000-0000-000000000002', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'alice-123', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0000-0000-0001-111000000016', '11100000-0000-0000-0000-000000000002', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'alice-123', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0000-0000-0001-111000000017', '11100000-0000-0000-0000-000000000002', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '33 DAY'),

-- User Profile Management Story Events
('00000000-0000-0000-0001-111000000018', '11100000-0000-0000-0000-000000000003', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000019', '11100000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000020', '11100000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000021', '11100000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000022', '11100000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000023', '11100000-0000-0000-0000-000000000003', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000024', '11100000-0000-0000-0000-000000000003', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000025', '11100000-0000-0000-0000-000000000003', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000026', '11100000-0000-0000-0000-000000000003', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- User Profile Management Story Events
('00000000-0000-0000-0001-111000000027', '11100000-0000-0000-0000-000000000004', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000028', '11100000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000029', '11100000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000030', '11100000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000031', '11100000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000032', '11100000-0000-0000-0000-000000000004', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000033', '11100000-0000-0000-0000-000000000004', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000034', '11100000-0000-0000-0000-000000000004', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000035', '11100000-0000-0000-0000-000000000004', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- Sprint Creation and Management Story Events
('00000000-0000-0000-0001-111000000036', '11100000-0000-0000-0000-000000000005', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000037', '11100000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000038', '11100000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000039', '11100000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000040', '11100000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000041', '11100000-0000-0000-0000-000000000005', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000042', '11100000-0000-0000-0000-000000000005', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000043', '11100000-0000-0000-0000-000000000005', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000044', '11100000-0000-0000-0000-000000000005', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- Kanban Board Interface Story Events
('00000000-0000-0000-0001-111000000045', '11100000-0000-0000-0000-000000000006', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000046', '11100000-0000-0000-0000-000000000006', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000047', '11100000-0000-0000-0000-000000000006', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000048', '11100000-0000-0000-0000-000000000006', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000049', '11100000-0000-0000-0000-000000000006', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000050', '11100000-0000-0000-0000-000000000006', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000051', '11100000-0000-0000-0000-000000000006', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000052', '11100000-0000-0000-0000-000000000006', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000053', '11100000-0000-0000-0000-000000000006', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- Event Tracking System Story Events
('00000000-0000-0000-0001-111000000054', '11100000-0000-0000-0000-000000000007', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000055', '11100000-0000-0000-0000-000000000007', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000056', '11100000-0000-0000-0000-000000000007', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000057', '11100000-0000-0000-0000-000000000007', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000058', '11100000-0000-0000-0000-000000000007', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000059', '11100000-0000-0000-0000-000000000007', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000060', '11100000-0000-0000-0000-000000000007', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000061', '11100000-0000-0000-0000-000000000007', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000062', '11100000-0000-0000-0000-000000000007', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- Basic Analytics Dashboard Story Events
('00000000-0000-0000-0001-111000000063', '11100000-0000-0000-0000-000000000008', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000064', '11100000-0000-0000-0000-000000000008', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000065', '11100000-0000-0000-0000-000000000008', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000066', '11100000-0000-0000-0000-000000000008', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000067', '11100000-0000-0000-0000-000000000008', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000068', '11100000-0000-0000-0000-000000000008', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000069', '11100000-0000-0000-0000-000000000008', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000070', '11100000-0000-0000-0000-000000000008', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000071', '11100000-0000-0000-0000-000000000008', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- Advanced Analytics Charts Story Events
('00000000-0000-0000-0001-111000000072', '11100000-0000-0000-0000-000000000009', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000073', '11100000-0000-0000-0000-000000000009', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000074', '11100000-0000-0000-0000-000000000009', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000075', '11100000-0000-0000-0000-000000000009', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000076', '11100000-0000-0000-0000-000000000009', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000077', '11100000-0000-0000-0000-000000000009', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000078', '11100000-0000-0000-0000-000000000009', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000079', '11100000-0000-0000-0000-000000000009', 'STATUS_CHANGED', 'IN_PROGRESS',
 'DONE', 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0000-0000-0001-111000000080', '11100000-0000-0000-0000-000000000009', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '18 DAY'),

-- AI-Powered Project Insights Story Events
('00000000-0000-0000-0001-111000000081', '11100000-0000-0000-0000-000000000010', 'CREATED', null,
 'Kanban Board Interface', 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0000-0000-0001-111000000082', '11100000-0000-0000-0000-000000000010', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000083', '11100000-0000-0000-0000-000000000010', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000084', '11100000-0000-0000-0000-000000000010', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000085', '11100000-0000-0000-0000-000000000010', 'TASK_ADDED', null,
 'Real-time Updates', 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0000-0000-0001-111000000086', '11100000-0000-0000-0000-000000000010', 'TASK_ADDED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0000-0000-0001-111000000087', '11100000-0000-0000-0000-000000000010', 'STATUS_CHANGED', 'TODO',
 'IN_PROGRESS', 'carol-789', CURRENT_DATE - INTERVAL '29 DAY');

-- === TASK EVENTS (comprehensive task lifecycle tracking) ===
INSERT INTO task_events (id, task_id, event_type, old_value, new_value, triggered_by, created_at)
VALUES

-- ===== SPRINT 1 TASK EVENTS (All DONE) =====

-- Task: Set up Clerk Authentication (11110000-0000-0000-0000-000000000001)
('00000000-0001-0000-0000-111100000001', '11110000-0000-0000-0000-000000000001', 'CREATED', null,
 'Set up Clerk Authentication', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0001-0000-0000-111100000002', '11110000-0000-0000-0000-000000000001', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0001-0000-0000-111100000003', '11110000-0000-0000-0000-000000000001', 'ESTIMATED', null, '8 points',
 'alice-123', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0001-0000-0000-111100000004', '11110000-0000-0000-0000-000000000001', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '59 DAY'),
('00000000-0001-0000-0000-111100000005', '11110000-0000-0000-0000-000000000001', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '54 DAY'),

-- Task: Create Login/Register Pages (11110000-0000-0000-0000-000000000002)
('00000000-0002-0000-0000-111100000001', '11110000-0000-0000-0000-000000000002', 'CREATED', null,
 'Create Login/Register Pages', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0002-0000-0000-111100000002', '11110000-0000-0000-0000-000000000002', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0002-0000-0000-111100000003', '11110000-0000-0000-0000-000000000002', 'ESTIMATED', null, '5 points',
 'carol-789', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0002-0000-0000-111100000004', '11110000-0000-0000-0000-000000000002', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '58 DAY'),
('00000000-0002-0000-0000-111100000005', '11110000-0000-0000-0000-000000000002', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '52 DAY'),

-- Task: Implement Protected Routes (11110000-0000-0000-0000-000000000003)
('00000000-0003-0000-0000-111100000001', '11110000-0000-0000-0000-000000000003', 'CREATED', null,
 'Implement Protected Routes', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0003-0000-0000-111100000002', '11110000-0000-0000-0000-000000000003', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0003-0000-0000-111100000003', '11110000-0000-0000-0000-000000000003', 'ESTIMATED', null, '3 points',
 'bob-456', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0003-0000-0000-111100000004', '11110000-0000-0000-0000-000000000003', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '57 DAY'),
('00000000-0003-0000-0000-111100000005', '11110000-0000-0000-0000-000000000003', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '53 DAY'),

-- Task: User Session Management (11110000-0000-0000-0000-000000000004)
('00000000-0004-0000-0000-111100000001', '11110000-0000-0000-0000-000000000004', 'CREATED', null,
 'User Session Management', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0004-0000-0000-111100000002', '11110000-0000-0000-0000-000000000004', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0004-0000-0000-111100000003', '11110000-0000-0000-0000-000000000004', 'ESTIMATED', null, '5 points',
 'alice-123', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0004-0000-0000-111100000004', '11110000-0000-0000-0000-000000000004', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '56 DAY'),
('00000000-0004-0000-0000-111100000005', '11110000-0000-0000-0000-000000000004', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '51 DAY'),

-- Task: Email Verification Flow (11110000-0000-0000-0000-000000000005)
('00000000-0005-0000-0000-111100000001', '11110000-0000-0000-0000-000000000005', 'CREATED', null,
 'Email Verification Flow', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0005-0000-0000-111100000002', '11110000-0000-0000-0000-000000000005', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0005-0000-0000-111100000003', '11110000-0000-0000-0000-000000000005', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0005-0000-0000-111100000004', '11110000-0000-0000-0000-000000000005', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '55 DAY'),
('00000000-0005-0000-0000-111100000005', '11110000-0000-0000-0000-000000000005', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '50 DAY'),

-- Task: Database Schema Design (11110000-0000-0000-0000-000000000006)
('00000000-0006-0000-0000-111100000001', '11110000-0000-0000-0000-000000000006', 'CREATED', null,
 'Database Schema Design', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0006-0000-0000-111100000002', '11110000-0000-0000-0000-000000000006', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0006-0000-0000-111100000003', '11110000-0000-0000-0000-000000000006', 'ESTIMATED', null, '8 points',
 'bob-456', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0006-0000-0000-111100000004', '11110000-0000-0000-0000-000000000006', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '60 DAY'),
('00000000-0006-0000-0000-111100000005', '11110000-0000-0000-0000-000000000006', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '49 DAY'),

-- Task: CI/CD Pipeline Setup (11110000-0000-0000-0000-000000000007)
('00000000-0007-0000-0000-111100000001', '11110000-0000-0000-0000-000000000007', 'CREATED', null,
 'CI/CD Pipeline Setup', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0007-0000-0000-111100000002', '11110000-0000-0000-0000-000000000007', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0007-0000-0000-111100000003', '11110000-0000-0000-0000-000000000007', 'ESTIMATED', null, '8 points',
 'david-012', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0007-0000-0000-111100000004', '11110000-0000-0000-0000-000000000007', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '58 DAY'),
('00000000-0007-0000-0000-111100000005', '11110000-0000-0000-0000-000000000007', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '48 DAY'),

-- Task: Docker Configuration (11110000-0000-0000-0000-000000000008)
('00000000-0008-0000-0000-111100000001', '11110000-0000-0000-0000-000000000008', 'CREATED', null,
 'Docker Configuration', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0008-0000-0000-111100000002', '11110000-0000-0000-0000-000000000008', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0008-0000-0000-111100000003', '11110000-0000-0000-0000-000000000008', 'ESTIMATED', null, '5 points',
 'bob-456', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0008-0000-0000-111100000004', '11110000-0000-0000-0000-000000000008', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '56 DAY'),
('00000000-0008-0000-0000-111100000005', '11110000-0000-0000-0000-000000000008', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '49 DAY'),

-- Task: Monitoring and Logging (11110000-0000-0000-0000-000000000009)
('00000000-0009-0000-0000-111100000001', '11110000-0000-0000-0000-000000000009', 'CREATED', null,
 'Monitoring and Logging', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0009-0000-0000-111100000002', '11110000-0000-0000-0000-000000000009', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0009-0000-0000-111100000003', '11110000-0000-0000-0000-000000000009', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0009-0000-0000-111100000004', '11110000-0000-0000-0000-000000000009', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '54 DAY'),
('00000000-0009-0000-0000-111100000005', '11110000-0000-0000-0000-000000000009', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '47 DAY'),

-- Task: Environment Configuration (11110000-0000-0000-0000-000000000010)
('00000000-0010-0000-0000-111100000001', '11110000-0000-0000-0000-000000000010', 'CREATED', null,
 'Environment Configuration', 'alice-123', CURRENT_DATE - INTERVAL '65 DAY'),
('00000000-0010-0000-0000-111100000002', '11110000-0000-0000-0000-000000000010', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '64 DAY'),
('00000000-0010-0000-0000-111100000003', '11110000-0000-0000-0000-000000000010', 'ESTIMATED', null, '3 points',
 'alice-123', CURRENT_DATE - INTERVAL '63 DAY'),
('00000000-0010-0000-0000-111100000004', '11110000-0000-0000-0000-000000000010', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '53 DAY'),
('00000000-0010-0000-0000-111100000005', '11110000-0000-0000-0000-000000000010', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '46 DAY'),

-- ===== SPRINT 2 TASK EVENTS (All DONE) =====

-- Task: User Profile API (11110000-0000-0000-0000-000000000011)
('00000000-0011-0000-0000-111100000001', '11110000-0000-0000-0000-000000000011', 'CREATED', null, 'User Profile API',
 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0011-0000-0000-111100000002', '11110000-0000-0000-0000-000000000011', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0011-0000-0000-111100000003', '11110000-0000-0000-0000-000000000011', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0011-0000-0000-111100000004', '11110000-0000-0000-0000-000000000011', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '44 DAY'),
('00000000-0011-0000-0000-111100000005', '11110000-0000-0000-0000-000000000011', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '39 DAY'),

-- Task: Profile Settings UI (11110000-0000-0000-0000-000000000012)
('00000000-0012-0000-0000-111100000001', '11110000-0000-0000-0000-000000000012', 'CREATED', null, 'Profile Settings UI',
 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0012-0000-0000-111100000002', '11110000-0000-0000-0000-000000000012', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0012-0000-0000-111100000003', '11110000-0000-0000-0000-000000000012', 'ESTIMATED', null, '8 points',
 'carol-789', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0012-0000-0000-111100000004', '11110000-0000-0000-0000-000000000012', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '43 DAY'),
('00000000-0012-0000-0000-111100000005', '11110000-0000-0000-0000-000000000012', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '36 DAY'),

-- Task: Avatar Upload Feature (11110000-0000-0000-0000-000000000013)
('00000000-0013-0000-0000-111100000001', '11110000-0000-0000-0000-000000000013', 'CREATED', null,
 'Avatar Upload Feature', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0013-0000-0000-111100000002', '11110000-0000-0000-0000-000000000013', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0013-0000-0000-111100000003', '11110000-0000-0000-0000-000000000013', 'ESTIMATED', null, '5 points',
 'carol-789', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0013-0000-0000-111100000004', '11110000-0000-0000-0000-000000000013', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '42 DAY'),
('00000000-0013-0000-0000-111100000005', '11110000-0000-0000-0000-000000000013', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '37 DAY'),

-- Task: Notification Preferences (11110000-0000-0000-0000-000000000014)
('00000000-0014-0000-0000-111100000001', '11110000-0000-0000-0000-000000000014', 'CREATED', null,
 'Notification Preferences', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0014-0000-0000-111100000002', '11110000-0000-0000-0000-000000000014', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0014-0000-0000-111100000003', '11110000-0000-0000-0000-000000000014', 'ESTIMATED', null, '3 points',
 'alice-123', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0014-0000-0000-111100000004', '11110000-0000-0000-0000-000000000014', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '41 DAY'),
('00000000-0014-0000-0000-111100000005', '11110000-0000-0000-0000-000000000014', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '38 DAY'),

-- Task: Profile Validation (11110000-0000-0000-0000-000000000015)
('00000000-0015-0000-0000-111100000001', '11110000-0000-0000-0000-000000000015', 'CREATED', null, 'Profile Validation',
 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0015-0000-0000-111100000002', '11110000-0000-0000-0000-000000000015', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0015-0000-0000-111100000003', '11110000-0000-0000-0000-000000000015', 'ESTIMATED', null, '2 points',
 'david-012', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0015-0000-0000-111100000004', '11110000-0000-0000-0000-000000000015', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '40 DAY'),
('00000000-0015-0000-0000-111100000005', '11110000-0000-0000-0000-000000000015', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '36 DAY'),

-- Task: Project Creation Workflow (11110000-0000-0000-0000-000000000016)
('00000000-0016-0000-0000-111100000001', '11110000-0000-0000-0000-000000000016', 'CREATED', null,
 'Project Creation Workflow', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0016-0000-0000-111100000002', '11110000-0000-0000-0000-000000000016', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0016-0000-0000-111100000003', '11110000-0000-0000-0000-000000000016', 'ESTIMATED', null, '8 points',
 'alice-123', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0016-0000-0000-111100000004', '11110000-0000-0000-0000-000000000016', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '45 DAY'),
('00000000-0016-0000-0000-111100000005', '11110000-0000-0000-0000-000000000016', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),

-- Task: Team Invitation System (11110000-0000-0000-0000-000000000017)
('00000000-0017-0000-0000-111100000001', '11110000-0000-0000-0000-000000000017', 'CREATED', null,
 'Team Invitation System', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0017-0000-0000-111100000002', '11110000-0000-0000-0000-000000000017', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0017-0000-0000-111100000003', '11110000-0000-0000-0000-000000000017', 'ESTIMATED', null, '8 points',
 'bob-456', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0017-0000-0000-111100000004', '11110000-0000-0000-0000-000000000017', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '44 DAY'),
('00000000-0017-0000-0000-111100000005', '11110000-0000-0000-0000-000000000017', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '35 DAY'),

-- Task: Role-Based Access Control (11110000-0000-0000-0000-000000000018)
('00000000-0018-0000-0000-111100000001', '11110000-0000-0000-0000-000000000018', 'CREATED', null,
 'Role-Based Access Control', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0018-0000-0000-111100000002', '11110000-0000-0000-0000-000000000018', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0018-0000-0000-111100000003', '11110000-0000-0000-0000-000000000018', 'ESTIMATED', null, '13 points',
 'david-012', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0018-0000-0000-111100000004', '11110000-0000-0000-0000-000000000018', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '43 DAY'),
('00000000-0018-0000-0000-111100000005', '11110000-0000-0000-0000-000000000018', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '32 DAY'),

-- Task: Team Member Management UI (11110000-0000-0000-0000-000000000019)
('00000000-0019-0000-0000-111100000001', '11110000-0000-0000-0000-000000000019', 'CREATED', null,
 'Team Member Management UI', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0019-0000-0000-111100000002', '11110000-0000-0000-0000-000000000019', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0019-0000-0000-111100000003', '11110000-0000-0000-0000-000000000019', 'ESTIMATED', null, '5 points',
 'carol-789', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0019-0000-0000-111100000004', '11110000-0000-0000-0000-000000000019', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '42 DAY'),
('00000000-0019-0000-0000-111100000005', '11110000-0000-0000-0000-000000000019', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '33 DAY'),

-- Task: Project Settings Panel (11110000-0000-0000-0000-000000000020)
('00000000-0020-0000-0000-111100000001', '11110000-0000-0000-0000-000000000020', 'CREATED', null,
 'Project Settings Panel', 'alice-123', CURRENT_DATE - INTERVAL '50 DAY'),
('00000000-0020-0000-0000-111100000002', '11110000-0000-0000-0000-000000000020', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '49 DAY'),
('00000000-0020-0000-0000-111100000003', '11110000-0000-0000-0000-000000000020', 'ESTIMATED', null, '5 points',
 'alice-123', CURRENT_DATE - INTERVAL '48 DAY'),
('00000000-0020-0000-0000-111100000004', '11110000-0000-0000-0000-000000000020', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '41 DAY'),
('00000000-0020-0000-0000-111100000005', '11110000-0000-0000-0000-000000000020', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '31 DAY'),

-- ===== SPRINT 3 TASK EVENTS (All DONE) =====

-- Task: Sprint CRUD Operations (11110000-0000-0000-0000-000000000021)
('00000000-0021-0000-0000-111100000001', '11110000-0000-0000-0000-000000000021', 'CREATED', null,
 'Sprint CRUD Operations', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0021-0000-0000-111100000002', '11110000-0000-0000-0000-000000000021', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0021-0000-0000-111100000003', '11110000-0000-0000-0000-000000000021', 'ESTIMATED', null, '8 points',
 'bob-456', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0021-0000-0000-111100000004', '11110000-0000-0000-0000-000000000021', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0021-0000-0000-111100000005', '11110000-0000-0000-0000-000000000021', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '24 DAY'),

-- Task: Sprint Planning Interface (11110000-0000-0000-0000-000000000022)
('00000000-0022-0000-0000-111100000001', '11110000-0000-0000-0000-000000000022', 'CREATED', null,
 'Sprint Planning Interface', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0022-0000-0000-111100000002', '11110000-0000-0000-0000-000000000022', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0022-0000-0000-111100000003', '11110000-0000-0000-0000-000000000022', 'ESTIMATED', null, '13 points',
 'alice-123', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0022-0000-0000-111100000004', '11110000-0000-0000-0000-000000000022', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '28 DAY'),
('00000000-0022-0000-0000-111100000005', '11110000-0000-0000-0000-000000000022', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),

-- Task: Sprint Goals and Tracking (11110000-0000-0000-0000-000000000023)
('00000000-0023-0000-0000-111100000001', '11110000-0000-0000-0000-000000000023', 'CREATED', null,
 'Sprint Goals and Tracking', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0023-0000-0000-111100000002', '11110000-0000-0000-0000-000000000023', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0023-0000-0000-111100000003', '11110000-0000-0000-0000-000000000023', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0023-0000-0000-111100000004', '11110000-0000-0000-0000-000000000023', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '27 DAY'),
('00000000-0023-0000-0000-111100000005', '11110000-0000-0000-0000-000000000023', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '22 DAY'),

-- Task: Sprint Timeline View (11110000-0000-0000-0000-000000000024)
('00000000-0024-0000-0000-111100000001', '11110000-0000-0000-0000-000000000024', 'CREATED', null,
 'Sprint Timeline View', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0024-0000-0000-111100000002', '11110000-0000-0000-0000-000000000024', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0024-0000-0000-111100000003', '11110000-0000-0000-0000-000000000024', 'ESTIMATED', null, '5 points',
 'carol-789', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0024-0000-0000-111100000004', '11110000-0000-0000-0000-000000000024', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '26 DAY'),
('00000000-0024-0000-0000-111100000005', '11110000-0000-0000-0000-000000000024', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '21 DAY'),

-- Task: Sprint Capacity Estimation (11110000-0000-0000-0000-000000000025)
('00000000-0025-0000-0000-111100000001', '11110000-0000-0000-0000-000000000025', 'CREATED', null,
 'Sprint Capacity Estimation', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0025-0000-0000-111100000002', '11110000-0000-0000-0000-000000000025', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0025-0000-0000-111100000003', '11110000-0000-0000-0000-000000000025', 'ESTIMATED', null, '8 points',
 'alice-123', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0025-0000-0000-111100000004', '11110000-0000-0000-0000-000000000025', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '25 DAY'),
('00000000-0025-0000-0000-111100000005', '11110000-0000-0000-0000-000000000025', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '18 DAY'),

-- Task: Drag and Drop Interface (11110000-0000-0000-0000-000000000026)
('00000000-0026-0000-0000-111100000001', '11110000-0000-0000-0000-000000000026', 'CREATED', null,
 'Drag and Drop Interface', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0026-0000-0000-111100000002', '11110000-0000-0000-0000-000000000026', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0026-0000-0000-111100000003', '11110000-0000-0000-0000-000000000026', 'ESTIMATED', null, '13 points',
 'carol-789', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0026-0000-0000-111100000004', '11110000-0000-0000-0000-000000000026', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '30 DAY'),
('00000000-0026-0000-0000-111100000005', '11110000-0000-0000-0000-000000000026', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '17 DAY'),

-- Task: Real-time Updates (11110000-0000-0000-0000-000000000027)
('00000000-0027-0000-0000-111100000001', '11110000-0000-0000-0000-000000000027', 'CREATED', null, 'Real-time Updates',
 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0027-0000-0000-111100000002', '11110000-0000-0000-0000-000000000027', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0027-0000-0000-111100000003', '11110000-0000-0000-0000-000000000027', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0027-0000-0000-111100000004', '11110000-0000-0000-0000-000000000027', 'ESTIMATED', null, '13 points',
 'bob-456', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0027-0000-0000-111100000005', '11110000-0000-0000-0000-000000000027', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '29 DAY'),
('00000000-0027-0000-0000-111100000006', '11110000-0000-0000-0000-000000000027', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '17 DAY'),

-- Task: Column Customization (11110000-0000-0000-0000-000000000028)
('00000000-0028-0000-0000-111100000001', '11110000-0000-0000-0000-000000000028', 'CREATED', null,
 'Column Customization', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0028-0000-0000-111100000002', '11110000-0000-0000-0000-000000000028', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0028-0000-0000-111100000003', '11110000-0000-0000-0000-000000000028', 'ESTIMATED', null, '8 points',
 'david-012', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0028-0000-0000-111100000004', '11110000-0000-0000-0000-000000000028', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '27 DAY'),
('00000000-0028-0000-0000-111100000005', '11110000-0000-0000-0000-000000000028', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '20 DAY'),

-- Task: Task Filtering and Search (11110000-0000-0000-0000-000000000029)
('00000000-0029-0000-0000-111100000001', '11110000-0000-0000-0000-000000000029', 'CREATED', null,
 'Task Filtering and Search', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0029-0000-0000-111100000002', '11110000-0000-0000-0000-000000000029', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0029-0000-0000-111100000003', '11110000-0000-0000-0000-000000000029', 'ESTIMATED', null, '5 points',
 'eve-345', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0029-0000-0000-111100000004', '11110000-0000-0000-0000-000000000029', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '26 DAY'),
('00000000-0029-0000-0000-111100000005', '11110000-0000-0000-0000-000000000029', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'eve-345', CURRENT_DATE - INTERVAL '19 DAY'),

-- Task: Mobile Responsive Design (11110000-0000-0000-0000-000000000030)
('00000000-0030-0000-0000-111100000001', '11110000-0000-0000-0000-000000000030', 'CREATED', null,
 'Mobile Responsive Design', 'alice-123', CURRENT_DATE - INTERVAL '35 DAY'),
('00000000-0030-0000-0000-111100000002', '11110000-0000-0000-0000-000000000030', 'ASSIGNED', null, 'carol-789',
 'alice-123', CURRENT_DATE - INTERVAL '34 DAY'),
('00000000-0030-0000-0000-111100000003', '11110000-0000-0000-0000-000000000030', 'ESTIMATED', null, '5 points',
 'carol-789', CURRENT_DATE - INTERVAL '33 DAY'),
('00000000-0030-0000-0000-111100000004', '11110000-0000-0000-0000-000000000030', 'STARTED', 'TODO', 'IN_PROGRESS',
 'carol-789', CURRENT_DATE - INTERVAL '25 DAY'),
('00000000-0030-0000-0000-111100000005', '11110000-0000-0000-0000-000000000030', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'carol-789', CURRENT_DATE - INTERVAL '16 DAY'),

-- ===== SPRINT 4 TASK EVENTS (All DONE) =====

-- Task: Event Logger Infrastructure (11110000-0000-0000-0000-000000000031)
('00000000-0031-0000-0000-111100000001', '11110000-0000-0000-0000-000000000031', 'CREATED', null,
 'Event Logger Infrastructure', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0031-0000-0000-111100000002', '11110000-0000-0000-0000-000000000031', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0031-0000-0000-111100000003', '11110000-0000-0000-0000-000000000031', 'ESTIMATED', null, '13 points',
 'david-012', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0031-0000-0000-111100000004', '11110000-0000-0000-0000-000000000031', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '14 DAY'),
('00000000-0031-0000-0000-111100000005', '11110000-0000-0000-0000-000000000031', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '6 DAY'),

-- Task: Domain Event Integration (11110000-0000-0000-0000-000000000032)
('00000000-0032-0000-0000-111100000001', '11110000-0000-0000-0000-000000000032', 'CREATED', null,
 'Domain Event Integration', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0032-0000-0000-111100000002', '11110000-0000-0000-0000-000000000032', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0032-0000-0000-111100000003', '11110000-0000-0000-0000-000000000032', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0032-0000-0000-111100000004', '11110000-0000-0000-0000-000000000032', 'ESTIMATED', null, '8 points',
 'bob-456', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0032-0000-0000-111100000005', '11110000-0000-0000-0000-000000000032', 'STARTED', 'TODO', 'IN_PROGRESS',
 'bob-456', CURRENT_DATE - INTERVAL '13 DAY'),
('00000000-0032-0000-0000-111100000006', '11110000-0000-0000-0000-000000000032', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'bob-456', CURRENT_DATE - INTERVAL '7 DAY'),

-- Task: Event Data Models (11110000-0000-0000-0000-000000000033)
('00000000-0033-0000-0000-111100000001', '11110000-0000-0000-0000-000000000033', 'CREATED', null, 'Event Data Models',
 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0033-0000-0000-111100000002', '11110000-0000-0000-0000-000000000033', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0033-0000-0000-111100000003', '11110000-0000-0000-0000-000000000033', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0033-0000-0000-111100000004', '11110000-0000-0000-0000-000000000033', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0033-0000-0000-111100000005', '11110000-0000-0000-0000-000000000033', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '8 DAY'),

-- Task: Event Query API (11110000-0000-0000-0000-000000000034)
('00000000-0034-0000-0000-111100000001', '11110000-0000-0000-0000-000000000034', 'CREATED', null, 'Event Query API',
 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0034-0000-0000-111100000002', '11110000-0000-0000-0000-000000000034', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0034-0000-0000-111100000003', '11110000-0000-0000-0000-000000000034', 'ESTIMATED', null, '5 points',
 'alice-123', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0034-0000-0000-111100000004', '11110000-0000-0000-0000-000000000034', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '11 DAY'),
('00000000-0034-0000-0000-111100000005', '11110000-0000-0000-0000-000000000034', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),

-- Task: Event Data Validation (11110000-0000-0000-0000-000000000035)
('00000000-0035-0000-0000-111100000001', '11110000-0000-0000-0000-000000000035', 'CREATED', null,
 'Event Data Validation', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0035-0000-0000-111100000002', '11110000-0000-0000-0000-000000000035', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0035-0000-0000-111100000003', '11110000-0000-0000-0000-000000000035', 'ESTIMATED', null, '3 points',
 'david-012', CURRENT_DATE - INTERVAL '11 DAY'),
('00000000-0035-0000-0000-111100000004', '11110000-0000-0000-0000-000000000035', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '10 DAY'),
('00000000-0035-0000-0000-111100000005', '11110000-0000-0000-0000-000000000035', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Analytics Data Service (11110000-0000-0000-0000-000000000036)
('00000000-0036-0000-0000-111100000001', '11110000-0000-0000-0000-000000000036', 'CREATED', null,
 'Analytics Data Service', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0036-0000-0000-111100000002', '11110000-0000-0000-0000-000000000036', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0036-0000-0000-111100000003', '11110000-0000-0000-0000-000000000036', 'ESTIMATED', null, '8 points',
 'alice-123', CURRENT_DATE - INTERVAL '11 DAY'),
('00000000-0036-0000-0000-111100000004', '11110000-0000-0000-0000-000000000036', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '10 DAY'),
('00000000-0036-0000-0000-111100000005', '11110000-0000-0000-0000-000000000036', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'alice-123', CURRENT_DATE - INTERVAL '3 DAY'),

-- Task: Sprint Metrics Calculator (11110000-0000-0000-0000-000000000037)
('00000000-0037-0000-0000-111100000001', '11110000-0000-0000-0000-000000000037', 'CREATED', null,
 'Sprint Metrics Calculator', 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0037-0000-0000-111100000002', '11110000-0000-0000-0000-000000000037', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0037-0000-0000-111100000003', '11110000-0000-0000-0000-000000000037', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '11 DAY'),
('00000000-0037-0000-0000-111100000004', '11110000-0000-0000-0000-000000000037', 'ESTIMATED', null, '8 points',
 'david-012', CURRENT_DATE - INTERVAL '10 DAY'),
('00000000-0037-0000-0000-111100000005', '11110000-0000-0000-0000-000000000037', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '10 DAY'),
('00000000-0037-0000-0000-111100000006', '11110000-0000-0000-0000-000000000037', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '2 DAY'),

-- Task: Basic Dashboard UI (11110000-0000-0000-0000-000000000038)
('00000000-0038-0000-0000-111100000001', '11110000-0000-0000-0000-000000000038', 'CREATED', null, 'Basic Dashboard UI',
 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0038-0000-0000-111100000002', '11110000-0000-0000-0000-000000000038', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0038-0000-0000-111100000003', '11110000-0000-0000-0000-000000000038', 'ESTIMATED', null, '8 points',
 'eve-345', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0038-0000-0000-111100000004', '11110000-0000-0000-0000-000000000038', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '13 DAY'),
('00000000-0038-0000-0000-111100000005', '11110000-0000-0000-0000-000000000038', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'eve-345', CURRENT_DATE - INTERVAL '2 DAY'),

-- Task: Date Range Filtering (11110000-0000-0000-0000-000000000039)
('00000000-0039-0000-0000-111100000001', '11110000-0000-0000-0000-000000000039', 'CREATED', null,
 'Date Range Filtering', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0039-0000-0000-111100000002', '11110000-0000-0000-0000-000000000039', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0039-0000-0000-111100000003', '11110000-0000-0000-0000-000000000039', 'ESTIMATED', null, '5 points',
 'eve-345', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0039-0000-0000-111100000004', '11110000-0000-0000-0000-000000000039', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '12 DAY'),
('00000000-0039-0000-0000-111100000005', '11110000-0000-0000-0000-000000000039', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'eve-345', CURRENT_DATE - INTERVAL '3 DAY'),

-- Task: Performance Optimization (11110000-0000-0000-0000-000000000040)
('00000000-0040-0000-0000-111100000001', '11110000-0000-0000-0000-000000000040', 'CREATED', null,
 'Performance Optimization', 'alice-123', CURRENT_DATE - INTERVAL '20 DAY'),
('00000000-0040-0000-0000-111100000002', '11110000-0000-0000-0000-000000000040', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '19 DAY'),
('00000000-0040-0000-0000-111100000003', '11110000-0000-0000-0000-000000000040', 'ESTIMATED', null, '5 points',
 'david-012', CURRENT_DATE - INTERVAL '18 DAY'),
('00000000-0040-0000-0000-111100000004', '11110000-0000-0000-0000-000000000040', 'STARTED', 'TODO', 'IN_PROGRESS',
 'david-012', CURRENT_DATE - INTERVAL '11 DAY'),
('00000000-0040-0000-0000-111100000005', '11110000-0000-0000-0000-000000000040', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'david-012', CURRENT_DATE - INTERVAL '1 DAY'),

-- ===== SPRINT 5 TASK EVENTS (Current Sprint - Mixed Status) =====

-- Task: Chart Library Integration (11110000-0000-0000-0000-000000000041) - DONE
('00000000-0041-0000-0000-111100000001', '11110000-0000-0000-0000-000000000041', 'CREATED', null,
 'Chart Library Integration', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0041-0000-0000-111100000002', '11110000-0000-0000-0000-000000000041', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0041-0000-0000-111100000003', '11110000-0000-0000-0000-000000000041', 'ESTIMATED', null, '5 points',
 'eve-345', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0041-0000-0000-111100000004', '11110000-0000-0000-0000-000000000041', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0041-0000-0000-111100000005', '11110000-0000-0000-0000-000000000041', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'eve-345', CURRENT_DATE - INTERVAL '2 DAY'),

-- Task: Burndown Chart Component (11110000-0000-0000-0000-000000000042) - DONE
('00000000-0042-0000-0000-111100000001', '11110000-0000-0000-0000-000000000042', 'CREATED', null,
 'Burndown Chart Component', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0042-0000-0000-111100000002', '11110000-0000-0000-0000-000000000042', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0042-0000-0000-111100000003', '11110000-0000-0000-0000-000000000042', 'ESTIMATED', null, '8 points',
 'eve-345', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0042-0000-0000-111100000004', '11110000-0000-0000-0000-000000000042', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '3 DAY'),
('00000000-0042-0000-0000-111100000005', '11110000-0000-0000-0000-000000000042', 'COMPLETED', 'IN_PROGRESS', 'DONE',
 'eve-345', CURRENT_DATE - INTERVAL '1 DAY'),

-- Task: Velocity Trend Analysis (11110000-0000-0000-0000-000000000043) - IN_PROGRESS
('00000000-0043-0000-0000-111100000001', '11110000-0000-0000-0000-000000000043', 'CREATED', null,
 'Velocity Trend Analysis', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0043-0000-0000-111100000002', '11110000-0000-0000-0000-000000000043', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0043-0000-0000-111100000003', '11110000-0000-0000-0000-000000000043', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0043-0000-0000-111100000004', '11110000-0000-0000-0000-000000000043', 'ESTIMATED', null, '8 points',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0043-0000-0000-111100000005', '11110000-0000-0000-0000-000000000043', 'STARTED', 'TODO', 'IN_PROGRESS',
 'alice-123', CURRENT_DATE - INTERVAL '2 DAY'),

-- Task: Task Distribution Charts (11110000-0000-0000-0000-000000000044) - IN_PROGRESS
('00000000-0044-0000-0000-111100000001', '11110000-0000-0000-0000-000000000044', 'CREATED', null,
 'Task Distribution Charts', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0044-0000-0000-111100000002', '11110000-0000-0000-0000-000000000044', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0044-0000-0000-111100000003', '11110000-0000-0000-0000-000000000044', 'ESTIMATED', null, '5 points',
 'eve-345', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0044-0000-0000-111100000004', '11110000-0000-0000-0000-000000000044', 'STARTED', 'TODO', 'IN_PROGRESS',
 'eve-345', CURRENT_DATE - INTERVAL '1 DAY'),

-- Task: Export and Reporting (11110000-0000-0000-0000-000000000045) - TODO
('00000000-0045-0000-0000-111100000001', '11110000-0000-0000-0000-000000000045', 'CREATED', null,
 'Export and Reporting', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0045-0000-0000-111100000002', '11110000-0000-0000-0000-000000000045', 'ASSIGNED', null, 'bob-456',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0045-0000-0000-111100000003', '11110000-0000-0000-0000-000000000045', 'ESTIMATED', null, '5 points',
 'bob-456', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Data Analysis Pipeline (11110000-0000-0000-0000-000000000046) - TODO
('00000000-0046-0000-0000-111100000001', '11110000-0000-0000-0000-000000000046', 'CREATED', null,
 'Data Analysis Pipeline', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0046-0000-0000-111100000002', '11110000-0000-0000-0000-000000000046', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0046-0000-0000-111100000003', '11110000-0000-0000-0000-000000000046', 'ESTIMATED', null, '13 points',
 'david-012', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Performance Prediction Model (11110000-0000-0000-0000-000000000047) - TODO
('00000000-0047-0000-0000-111100000001', '11110000-0000-0000-0000-000000000047', 'CREATED', null,
 'Performance Prediction Model', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0047-0000-0000-111100000002', '11110000-0000-0000-0000-000000000047', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0047-0000-0000-111100000003', '11110000-0000-0000-0000-000000000047', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0047-0000-0000-111100000004', '11110000-0000-0000-0000-000000000047', 'ESTIMATED', null, '21 points',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Insights UI Component (11110000-0000-0000-0000-000000000048) - TODO
('00000000-0048-0000-0000-111100000001', '11110000-0000-0000-0000-000000000048', 'CREATED', null,
 'Insights UI Component', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0048-0000-0000-111100000002', '11110000-0000-0000-0000-000000000048', 'ASSIGNED', null, 'eve-345',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0048-0000-0000-111100000003', '11110000-0000-0000-0000-000000000048', 'ESTIMATED', null, '8 points',
 'eve-345', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Recommendation Engine (11110000-0000-0000-0000-000000000049) - TODO
('00000000-0049-0000-0000-111100000001', '11110000-0000-0000-0000-000000000049', 'CREATED', null,
 'Recommendation Engine', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0049-0000-0000-111100000002', '11110000-0000-0000-0000-000000000049', 'ASSIGNED', null, 'david-012',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0049-0000-0000-111100000003', '11110000-0000-0000-0000-000000000049', 'ESTIMATED', null, '21 points',
 'david-012', CURRENT_DATE - INTERVAL '4 DAY'),

-- Task: Insights API Integration (11110000-0000-0000-0000-000000000050) - TODO
('00000000-0050-0000-0000-111100000001', '11110000-0000-0000-0000-000000000050', 'CREATED', null,
 'Insights API Integration', 'alice-123', CURRENT_DATE - INTERVAL '5 DAY'),
('00000000-0050-0000-0000-111100000002', '11110000-0000-0000-0000-000000000050', 'ASSIGNED', null, 'alice-123',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY'),
('00000000-0050-0000-0000-111100000003', '11110000-0000-0000-0000-000000000050', 'ESTIMATED', null, '13 points',
 'alice-123', CURRENT_DATE - INTERVAL '4 DAY');