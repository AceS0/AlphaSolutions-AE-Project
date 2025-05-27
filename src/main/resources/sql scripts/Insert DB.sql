-- Use the kanban_db
USE kanban_db;

-- Insert users
INSERT INTO user (email, username, password, role) VALUES
('abse@alpha.dk', 'Abdul Sekerci', 'alpha123', 'PM'),
('enfi@alpha.dk', 'Enes Filikci', 'alpha123', 'Employee'),
('iake@alpha.dk', 'Ian Kea', 'alpha123', 'Admin'),
('make@alpha.dk', 'Mads Kea', 'alpha123', 'Admin'),
('roek@alpha.dk', 'Robin Eken', 'alpha123', 'PM');

-- Create projects
INSERT INTO project (title, description, deadline, estDeadline, duration, workHours, createdBy, checked) VALUES
('Frontend CSS', 'Build a beatiful frontend design.', '2025-07-01', '2025-07-04', 30, 48, 1, FALSE),
('React Development', 'Develop an app according to the frontend design.', '2025-08-15', '2025-08-15', 45, 24, 1, FALSE),
('Dashboard UI', 'Build a business dashboard providing real-time data.', '2025-09-01', '2025-09-07', 40, 72, 5, FALSE);

-- Create subprojects
INSERT INTO subproject (projectId, title, priority, deadline, estDeadline, duration, workHours, checked) VALUES
(1, 'Frontend Design', 1, '2025-06-15', '2025-06-18', 10, 28, FALSE),
(1, 'Backend Design', 2, '2025-06-25', '2025-06-26', 15, 20, FALSE),
(2, 'API Integration', 1, '2025-07-30', '2025-07-31', 20, 24, FALSE),
(3, 'UI Class', 1, '2025-08-10', '2025-08-13', 12, 28, FALSE), 
(3, 'Data CLass', 2, '2025-08-25', '2025-08-30', 18, 44, FALSE); 

-- Create tasks
INSERT INTO task (subprojectId, title, description, deadline, estDeadline, duration, workHours, status, priority, checked) VALUES
(1, 'Create the logo', 'Design the logo for the website.', '2025-06-10', '2025-06-12', 3, 12, 'To Do', 'High', FALSE),
(1, 'Design homepage', 'Design the homepage for easier accessibility.', '2025-06-15', '2025-06-17', 4, 16, 'In Progress', 'Medium', FALSE),
(2, 'Redesign login function', 'Improve login backend.', '2025-06-20', '2025-06-23', 5, 20, 'To Do', 'High', FALSE),
(3, 'Connect to external API', 'Connect to external data API.', '2025-07-28', '2025-07-31', 6, 24, 'To Do', 'High', FALSE),
(4, 'Sketch and Design the Dashboard UI', 'Sketch and implement UI views.', '2025-08-08', '2025-08-10', 4, 16, 'To Do', 'High', FALSE), 
(4, 'Design the Layout', 'Code the dashboard layout.', '2025-08-10', '2025-08-12', 3, 12, 'To Do', 'Medium', FALSE),     
(5, 'Setup Database', 'Setup and model the sql schema.', '2025-08-20', '2025-08-23', 5, 20, 'To Do', 'High', FALSE), 
(5, 'Implement Repository JDBC', 'Add data access layer.', '2025-08-23', '2025-08-26', 6, 24, 'To Do', 'Medium', FALSE);     

-- Assign tasks to Enes (user id = 2)
INSERT INTO task_user (task_id, user_id) VALUES
(1, 2),
(2, 2),
(4, 2),
(6, 2), 
(7, 2); 

-- Project-user relationships
INSERT INTO project_user (project_id, user_id) VALUES
(1, 1),
(2, 1), 
(1, 2), 
(2, 2), 
(3, 5), 
(3, 2);
