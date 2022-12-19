package neuefische.de.neuefischetodoaufgaberecap.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public Todo create (@RequestBody Todo todo) {
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public Todo update (@PathVariable String id, @RequestBody Todo todo) {
        todo.setId(id);
        return todoService.update(todo);
    }

    @GetMapping
    public List<Todo> getAllTodos () {
        return todoService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById (@PathVariable String id) {
        todoService.deleteById(id);
    }
}
