package com.app.Todo.repo;

import com.app.Todo.models.Priority;
import com.app.Todo.models.Category;
import com.app.Todo.models.Status;
import com.app.Todo.models.Task;
import com.app.Todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndStatus(User user, Status status);

    List<Task> findByUserAndPriority(User user, Priority priority);

    List<Task> findByUserAndCategory(User user, Category category);

    List<Task> findByUserOrderByDueDateAsc(User user);

    List<Task> findByUserOrderByStatusAsc(User user);

    List<Task> findByUserAndTitleContaining(User user, String keyword);

    long countByUserAndStatus(User user, Status status);

    long countByUser(User user);


}
