package com.app.Todo.controller;

import com.app.Todo.models.*;
import com.app.Todo.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String newPassword,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {

        if (newPassword == null || newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password cannot be empty");
            return "redirect:/profile";
        }
        taskService.updateUserPassword(user.getId(), newPassword);
        // 3. Security Measure: Redirect to Login so user logs in with new password
        // The '?updated' parameter can be used in login.html to show a success message
        return "redirect:/login?updated";
    }

    @GetMapping
    public String getTask(@RequestParam(required = false) Status status,
                          @RequestParam(required = false) Priority priority,
                          @RequestParam(required = false) Category category,
                          @RequestParam(required = false) String keyword, // NEW PARAM
                          @RequestParam(required = false) String sort,
                          @AuthenticationPrincipal User user,
                          Model model){

        List<Task> tasks;

        // 1. If user is searching, do that first
        if (keyword != null && !keyword.isEmpty()) {
            tasks = taskService.searchTasks(user, keyword);
        }
        // 2. Otherwise, check standard filters
        else if (status != null) {
            tasks = taskService.getTasksByStatus(user, status);
        } else if (priority != null) {
            tasks = taskService.getTasksByPriority(user, priority);
        } else if (category != null) {
            tasks = taskService.getTasksByCategory(user, category);
        } else if ("date".equals(sort)) {
            tasks = taskService.getTasksSortedByDate(user);
        } else if ("status".equals(sort)) {
            tasks = taskService.getTasksSortedByStatus(user);
        } else {
            tasks = taskService.getAllTasksByUser(user);
        }

        // --- NEW: CALCULATE STATS ---
        long totalTasks = taskService.countByUser(user); // You need this method in Service/Repo
        long completedTasks = taskService.countByUserAndStatus(user, Status.COMPLETED);

        int progressPercent = 0;
        if (totalTasks != 0) {
            progressPercent = (int) ((completedTasks * 100) / totalTasks);
        }

        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("progressPercent", progressPercent);
        // ---------------------------------

        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        model.addAttribute("allStatuses", Status.values());
        model.addAttribute("allPriorities", Priority.values());
        model.addAttribute("allCategories", Category.values());

        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedCategory", category);

        model.addAttribute("searchKeyword", keyword);

        return "tasks";
    }

    @PostMapping
    public String createTask(@Valid @ModelAttribute Task task, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please fill in all fields correctly");
            return "redirect:/tasks";
        }

        // ADD CATEGORY HERE: task.getCategory()
        taskService.createTask(
                task.getTitle(),
                task.getDueDate(),
                task.getStatus(),
                task.getPriority(),
                task.getCategory(),
                user
        );

        redirectAttributes.addFlashAttribute("successMessage", "Task created!");
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User user) {
        Task task = taskService.getTaskById(id);

        // SAFETY CHECK 1: Check if task exists (prevent crash if ID is fake)
        if (task == null) {
            return "redirect:/tasks";
        }

        // SAFETY CHECK 2: Ensure the logged-in user owns this task (prevent hacking)
        if (!task.getUser().getId().equals(user.getId())) {
            return "redirect:/tasks";
        }

        model.addAttribute("task", task);
        model.addAttribute("allStatuses", Status.values());
        model.addAttribute("allPriorities", Priority.values());
        model.addAttribute("allCategories",Category.values());
        return "edit-task";
    }

    @PostMapping("/{id}/update")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user) {
        taskService.updateTask(id, task.getTitle(), task.getDueDate(), task.getStatus(), task.getPriority(),task.getCategory(), user);
        redirectAttributes.addFlashAttribute("successMessage", "Task updated!");
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportToCSV(@AuthenticationPrincipal User user) {
        // 1. Get User's Tasks
        List<Task> tasks = taskService.getAllTasksByUser(user);

        // 2. Build CSV String
        StringBuilder csvBuilder = new StringBuilder();

        // Add CSV Header Row
        csvBuilder.append("ID,Title,Due Date,Status,Priority,Category\n");

        // Add Task Rows
        for (Task task : tasks) {
            csvBuilder.append(task.getId()).append(",");
            csvBuilder.append(task.getTitle()).append(",");
            csvBuilder.append(task.getDueDate()).append(",");
            csvBuilder.append(task.getStatus()).append(",");
            csvBuilder.append(task.getPriority()).append(",");
            csvBuilder.append(task.getCategory()).append("\n");
        }

        // 3. Prepare Response Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/csv"));
        headers.setContentDispositionFormData("attachment", "my_tasks.csv");

        // 4. Return Data
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBuilder.toString());
    }

}