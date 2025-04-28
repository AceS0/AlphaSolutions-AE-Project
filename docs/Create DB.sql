-- Create database
CREATE DATABASE IF NOT EXISTS kanban_db;
USE kanban_db;

-- Create User table
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Project Manager', 'Employee') NOT NULL
);

-- Create Project table
CREATE TABLE project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    createdBy INT NOT NULL,
    FOREIGN KEY (createdBy) REFERENCES user(id) ON DELETE CASCADE
);

-- Create Subproject (Board) table
CREATE TABLE subproject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    projectId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    FOREIGN KEY (projectId) REFERENCES project(id) ON DELETE CASCADE
);

-- Create Column table (named column_table because "column" is a reserved word)
CREATE TABLE column_table (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subprojectId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    position INT NOT NULL,
    FOREIGN KEY (subprojectId) REFERENCES subproject(id) ON DELETE CASCADE
);

-- Create Task table
CREATE TABLE task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    columnId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assignedTo INT,
    status ENUM('To Do', 'In Progress', 'Done') DEFAULT 'To Do',
    priority ENUM('Low', 'Medium', 'High') DEFAULT 'Medium',
    attachments TEXT,
    FOREIGN KEY (columnId) REFERENCES column_table(id) ON DELETE CASCADE,
    FOREIGN KEY (assignedTo) REFERENCES user(id) ON DELETE SET NULL
);
