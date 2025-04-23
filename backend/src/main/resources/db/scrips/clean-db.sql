-- This script is used to clean the database and create a new one and should ONLY be used manually in local development.
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS users_projects CASCADE;
DROP TABLE IF EXISTS sprints CASCADE;
DROP TABLE IF EXISTS sprint_columns CASCADE;
DROP TABLE IF EXISTS stories CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS task_assignees CASCADE;
DROP TABLE IF EXISTS subtasks CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
