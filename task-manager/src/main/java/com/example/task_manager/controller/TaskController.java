package com.example.task_manager.controller;

import com.example.task_manager.model.Task;
import com.example.task_manager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Task create(@Valid @RequestBody Task task) {
        return repo.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task updated) {
        return repo.findById(id)
                .map(task -> {
                    task.setTitle(updated.getTitle());
                    task.setDescription(updated.getDescription());
                    task.setDone(updated.isDone());
                    return ResponseEntity.ok(repo.save(task));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) { // TODO: Sort this
        return repo.findById(id)
                .map(task -> {
                    repo.delete(task);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
