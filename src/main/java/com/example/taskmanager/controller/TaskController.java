package com.example.taskmanager.controller;


import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskRepository taskRepository;
    public TaskController(TaskRepository taskRepository){
        this.taskRepository= taskRepository;
    }

    @GetMapping
    public List<Task> getAllTaks(){
        return taskRepository.findAll();
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        task.setCompleted(false);
        Task savedTask= taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Task>
    getTaskById(@PathVariable Long id){
       return taskRepository.findById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task>
    updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        return taskRepository.findById(id)
                .map(task->{
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setCompleted(updatedTask.isCompleted());

                    Task savedTask = taskRepository.save(task);
                    return ResponseEntity.ok(savedTask);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteTask(@PathVariable Long id){
        if(!taskRepository.existsById(id)){
            return  ResponseEntity.notFound().build();
        }
      taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
