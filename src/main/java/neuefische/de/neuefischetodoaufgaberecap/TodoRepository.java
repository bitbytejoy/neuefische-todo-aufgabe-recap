package neuefische.de.neuefischetodoaufgaberecap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TodoRepository {
    private final Map<String, Todo> todos = new HashMap<>();

    public Todo save(Todo todo) {
        todos.put(todo.getId(), todo);
        return todos.get(todo.getId());
    }

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    public void deleteById(String id) {
        if (todos.remove(id) == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo does not exist"
            );
        }
    }
}
