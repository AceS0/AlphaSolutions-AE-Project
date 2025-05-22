package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;
import com.example.alphasolutionsaeproject.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SubprojectRepository subprojectRepository;
    @Mock
    private SubprojectService subprojectService;
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskByIdReturnTaskIfIdExists() {
        // Arrange
        Task expectedTask = new Task();
        when(taskRepository.findById(1)).thenReturn(expectedTask);

        // Act
        Task result = taskService.getTaskById(1);

        // Assert
        assertEquals(expectedTask, result);
        verify(taskRepository).findById(1);
    }

    @Test
    void addTaskAndSaveTaskAlsoUpdateWorkHours() {
        // Arrange
        Task task = new Task();
        task.setSubprojectId(1);

        // Act
        taskService.addTask(task, 1);

        // Assert
        verify(taskRepository).save(task, 1);
        verify(subprojectRepository).updateWorkHours(anyInt(), anyInt());
    }

    @Test
    void deleteTaskRemoveTaskAlsoUpdateWorkHoursAndDeadline() {
        // Arrange
        Task task = new Task();
        task.setId(1);
        task.setSubprojectId(10);
        task.setDeadline(LocalDate.now());
        when(taskRepository.findById(1)).thenReturn(task);
        when(subprojectRepository.findProjectIdBySubprojectId(10)).thenReturn(100);
        when(subprojectRepository.findById(10)).thenReturn(new Subproject());
        when(projectRepository.findById(100)).thenReturn(new Project());

        // Act
        taskService.deleteTask(1);

        // Assert
        verify(taskRepository).delete(1);
        verify(taskRepository).updateEstimatedDeadline(eq(1), any(LocalDate.class));
    }

    @Test
    void updateStatusCallUpdateStatusOnRepository() {
        // Arrange
        int taskId = 5;
        String status = "IN_PROGRESS";

        // Act
        taskService.updateStatus(taskId, status);

        // Assert
        verify(taskRepository).updateStatus(taskId, status);
    }

    @Test
    void toggleCheckedUp_UpdateFlagsWhenUnchecked() {
        // Arrange
        Task task = new Task();
        task.setId(1);
        task.setChecked(true); // this means that it is checked
        task.setSubprojectId(10);

        Subproject sub = new Subproject();
        sub.setProjectId(100);
        when(taskRepository.findById(1)).thenReturn(task);
        when(subprojectRepository.findById(10)).thenReturn(sub);

        // Act
        taskService.toggleCheckedAndCascadeUp(1); // This unchecks task, that unchecks subproject and project.

        // Assert
        verify(taskRepository).updateChecked(1, false);
        verify(subprojectRepository).updateChecked(10, false);
        verify(projectRepository).updateChecked(100, false);
    }

    @Test
    void toggleCheckedUp_UpdateFlagsWhenChecked() {
        // Arrange
        Task task = new Task();
        task.setId(1);
        task.setChecked(false);  // this means that it is unchecked
        task.setSubprojectId(10);

        Subproject sub = new Subproject();
        sub.setProjectId(100);
        sub.setChecked(false);  // this means that it is unchecked

        when(taskRepository.findById(1)).thenReturn(task);
        when(subprojectRepository.findById(10)).thenReturn(sub);

        when(taskRepository.allTasksCheckedInSubproject(10)).thenReturn(true);
        when(subprojectRepository.allSubprojectsCheckedInProject(100)).thenReturn(true);

        // Act
        taskService.toggleCheckedAndCascadeUp(1); // This checks task, that checks subproject and project.

        // Assert
        verify(taskRepository).updateChecked(1, true);
        verify(subprojectRepository).updateChecked(10, true);
        verify(projectRepository).updateChecked(100, true);
    }


}
