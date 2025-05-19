package com.example.alphasolutionsaeproject.repository;

import com.example.alphasolutionsaeproject.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProjectRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdReturnsProject() {
        // Arrange
        Project expected = new Project(1, "Title", "Desc", LocalDate.now(), LocalDate.now(), 5, 10, 3, false);
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1))).thenReturn(expected);

        // Act
        Project result = projectRepository.findById(1);

        // Assert
        assertEquals(expected, result);
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(1));
    }

    @Test
    void saveWillInsertProject() {
        // Arrange
        Project project = new Project();
        project.setTitle("Title");
        project.setDescription("Desc");
        project.setDeadline(LocalDate.of(2025, 5, 20));
        project.setDuration(10);
        project.setCreatedBy(2);
        project.setChecked(true);

        // Act
        projectRepository.save(project);

        // Assert
        verify(jdbcTemplate).update(
                eq("INSERT INTO project (title, description, deadline, duration, createdBy, checked) VALUES (?, ?, ?, ?, ?, ?)"),
                eq("Title"), eq("Desc"), eq(LocalDate.of(2025, 5, 20)), eq(10), eq(2), eq(true));
    }

    @Test
    void updateMethodWillUpdateProject() {
        // Arrange
        Project project = new Project();
        project.setTitle("New Title");
        project.setDescription("New Desc");
        project.setDeadline(LocalDate.of(2025, 6, 1));
        project.setDuration(15);
        project.setCreatedBy(5);
        project.setChecked(false);

        int pid = 1;

        // Act
        projectRepository.update(project, pid);

        // Assert
        verify(jdbcTemplate).update(
                eq("UPDATE project SET title = ?, description = ?, deadline = ?, duration = ?, createdBy = ?, checked = ? WHERE id = ?"),
                eq("New Title"), eq("New Desc"), eq(LocalDate.of(2025, 6, 1)), eq(15), eq(5), eq(false), eq(pid));
    }

    @Test
    void deleteByIdWillDeleteProject() {
        // Arrange
        int pid = 10;

        // Act
        projectRepository.deleteById(pid);

        // Assert
        verify(jdbcTemplate).update(eq("DELETE FROM project WHERE id = ?"), eq(pid));
    }

    @Test
    void updateCheckedWillUpdateCheckedStatus() {
        // Arrange
        int projectId = 5;
        boolean newChecked = true;

        // Act
        projectRepository.updateChecked(projectId, newChecked);

        // Assert
        verify(jdbcTemplate).update(eq("UPDATE project SET checked = ? WHERE id = ?"), eq(newChecked), eq(projectId));
    }
}
