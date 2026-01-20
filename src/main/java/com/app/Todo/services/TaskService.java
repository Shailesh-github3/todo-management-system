package com.app.Todo.services;

import com.app.Todo.models.*;
import com.app.Todo.repo.TaskRepo;
import com.app.Todo.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Task> getAllTasksByUser(User user) {
        return taskRepo.findByUser(user);
    }

    public void createTask(String title, LocalDate dueDate, Status status, Priority priority, Category category, User user) {
        Task task = new Task();
        task.setTitle(title);
        task.setDueDate(dueDate);
        task.setStatus(status);
        task.setPriority(priority);
        task.setCategory(category);
        task.setUser(user);
        taskRepo.save(task);
    }

    public void deleteTask(Long id,User user) {
        Task task = taskRepo.findById(id).orElse(null);
        if(task != null && task.getUser().getId().equals(user.getId())) {
            taskRepo.deleteById(id);
        }
    }

    public Task getTaskById(Long id){
        return taskRepo.findById(id).orElse(null);
    }

    @Transactional
    public void updateTask(Long id, String title, LocalDate dueDate, Status status, Priority priority,Category category, User user) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        if (task.getUser().getId().equals(user.getId())) {
            task.setTitle(title);
            task.setDueDate(dueDate);
            task.setStatus(status);
            task.setPriority(priority);
            task.setCategory(category);
            taskRepo.save(task);
        }
    }

    public List<Task> getTasksByStatus(User user, Status status) {
        return taskRepo.findByUserAndStatus(user, status);
    }

    public List<Task> getTasksByPriority(User user, Priority priority) {
        return taskRepo.findByUserAndPriority(user, priority);
    }

    public List<Task> getTasksSortedByDate(User user) {
        return taskRepo.findByUserOrderByDueDateAsc(user);
    }

    public List<Task> getTasksSortedByStatus(User user) {
        return taskRepo.findByUserOrderByStatusAsc(user);
    }

    public List<Task> getTasksByCategory(User user, Category category) {
        return taskRepo.findByUserAndCategory(user, category);
    }

    public long countByUserAndStatus(User user, Status status) {
        return taskRepo.countByUserAndStatus(user, status);
    }

    public long countByUser(User user) {
        return taskRepo.countByUser(user);
    }

    public List<Task> searchTasks(User user, String keyword) {
        return taskRepo.findByUserAndTitleContaining(user, keyword);
    }

    @Transactional
    public void updateUserPassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepo.findById(userId);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

}


