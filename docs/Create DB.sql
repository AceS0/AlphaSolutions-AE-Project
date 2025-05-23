-- Create database
CREATE DATABASE IF NOT EXISTS kanban_db;
USE kanban_db;

-- Create User table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'PM', 'Employee') NOT NULL DEFAULT 'Employee'
);

-- Create Project table
CREATE TABLE project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    estDeadline DATE,
    duration INT,
    workHours INT,
    createdBy INT NOT NULL,
    checked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (createdBy) REFERENCES user(id) ON DELETE CASCADE
);

-- Create Subproject (Board) table
CREATE TABLE subproject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    projectId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    priority INT NOT NULL,
    deadline DATE NOT NULL,
	estDeadline DATE,
    duration INT,
	workHours INT,
    checked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (projectId) REFERENCES project(id) ON DELETE CASCADE
);

-- Create Task table
CREATE TABLE task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subprojectId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    deadline DATE NOT NULL,
    estDeadline DATE,
    duration INT,
    workHours INT,
    status VARCHAR(255) NOT NULL DEFAULT 'To Do',
    priority VARCHAR(255) NOT NULL DEFAULT 'Medium',
    checked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (subprojectId) REFERENCES subproject(id) ON DELETE CASCADE
);

-- Create the project_user join table
CREATE TABLE project_user (
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- Create the task_user join table
CREATE TABLE task_user (
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);