package com.app.Todo.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    @NotNull(message = "Title is required")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    @NotNull(message = "Due date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING) // Stores "IN_PROGRESS" in DB instead of "1"
    @NotNull(message = "Status is required")
    private Status status = Status.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is required")
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    private Category category = Category.OTHER;


    //THE RELATIONSHIP
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

}
