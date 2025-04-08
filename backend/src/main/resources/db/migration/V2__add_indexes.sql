-- Unique indexes
CREATE UNIQUE INDEX idx_users_email ON users(email);

-- Users
CREATE INDEX idx_users_github_username ON users(github_username);

-- Projects
CREATE INDEX idx_projects_created_by ON projects(created_by);

-- Users_Projects
CREATE INDEX idx_users_projects_user ON users_projects(user_id);
CREATE INDEX idx_users_projects_project ON users_projects(project_id);

-- Sprints
CREATE INDEX idx_sprints_project_id ON sprints(project_id);

-- Stories
CREATE INDEX idx_stories_project_id ON stories(project_id);
CREATE INDEX idx_stories_sprint_id ON stories(sprint_id);
CREATE INDEX idx_stories_status ON stories(status);

-- Tasks
CREATE INDEX idx_tasks_story_id ON tasks(story_id);
CREATE INDEX idx_tasks_sprint_id ON tasks(sprint_id);
CREATE INDEX idx_tasks_status ON tasks(status);

-- task_assignees
CREATE INDEX idx_task_assignees_task_id ON task_assignees(task_id);
CREATE INDEX idx_task_assignees_user_id ON task_assignees(user_id);

-- Subtasks
CREATE INDEX idx_subtasks_task_id ON subtasks(task_id);

-- Comments
CREATE INDEX idx_comments_story_id ON comments(story_id);
CREATE INDEX idx_comments_task_id ON comments(task_id);
CREATE INDEX idx_comments_subtask_id ON comments(subtask_id);