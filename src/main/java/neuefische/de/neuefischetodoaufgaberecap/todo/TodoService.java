package neuefische.de.neuefischetodoaufgaberecap.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private int id = 0;

    public Todo create(Todo todo) {
        todo.setId(String.valueOf(this.getNextId()));
        return todoRepository.save(todo);
    }

    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public void deleteById(String id) {
        todoRepository.deleteById(id);
    }

    private int getNextId() {
        return ++id;
    }
}
