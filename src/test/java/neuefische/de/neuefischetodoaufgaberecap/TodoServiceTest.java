package neuefische.de.neuefischetodoaufgaberecap;

import neuefische.de.neuefischetodoaufgaberecap.todo.Todo;
import neuefische.de.neuefischetodoaufgaberecap.todo.TodoStatus;
import neuefische.de.neuefischetodoaufgaberecap.todo.TodoRepository;
import neuefische.de.neuefischetodoaufgaberecap.todo.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

class TodoServiceTest {
    @Test
    void create_whenTodo_thenSuccessfullyCreateTodo () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        Todo todo = new Todo(null, "My first todo", TodoStatus.OPEN);

        // when
        Todo actual = todoService.create(todo);

        // then
        Todo expected = new Todo(
            "1",
            "My first todo",
            TodoStatus.OPEN
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_whenTodoExists_thenSuccessfullyUpdateTodo() {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        Todo todo = new Todo(null,"My first todo", TodoStatus.OPEN);
        todoService.create(todo);
        Todo update = new Todo(
            "1",
            "My first todo updated",
            TodoStatus.IN_PROGRESS
        );

        // when
        Todo actual = todoService.update(update);

        // then
        Todo expected = new Todo(
            "1",
            "My first todo updated",
            TodoStatus.IN_PROGRESS
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_whenTodoDoesntExist_thenCreateTodo () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        Todo todo = new Todo("1", "Hello Neuefische", TodoStatus.DONE);

        // when
        Todo actual = todoService.update(todo);

        // then
        Todo expected = new Todo(
            "1",
            "Hello Neuefische",
            TodoStatus.DONE
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_when2Todos_thenUpdateCorrectTodo () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        Todo todo = new Todo(null, "Hello Neuefische", TodoStatus.DONE);
        Todo todo2 = new Todo(
            null,
            "Was geht Neuefische",
            TodoStatus.IN_PROGRESS
        );
        todoService.create(todo);
        todoService.create(todo2);

        Todo update = new Todo("1", "Updated", TodoStatus.OPEN);

        // when
        todoService.update(update);

        // then
        List<Todo> actual = todoService.getAll();
        List<Todo> expected = new ArrayList<>(
            List.of(
                new Todo("1", "Updated", TodoStatus.OPEN),
                new Todo(
                    "2",
                    "Was geht Neuefische",
                    TodoStatus.IN_PROGRESS
                )
            )
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll_whenEmpty_thenReturnEmpty () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);

        // when
        List<Todo> actual = todoService.getAll();

        // then
        List<Todo> expected = new ArrayList<>();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll_whenOneTodo_thenReturnOneTodo () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        todoService.create(
            new Todo(null, "Hello world", TodoStatus.OPEN)
        );

        // when
        List<Todo> actual = todoService.getAll();

        // then
        List<Todo> expected = new ArrayList<>(
            List.of(new Todo("1", "Hello world", TodoStatus.OPEN))
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAll_whenTwoTodos_thenReturnTowTodos () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        todoService.create(
                new Todo(null, "Hello world", TodoStatus.OPEN)
        );
        todoService.create(
                new Todo(null, "Lorem Ipsum", TodoStatus.IN_PROGRESS)
        );


        // when
        List<Todo> actual = todoService.getAll();

        // then
        List<Todo> expected = new ArrayList<>(
            List.of(
                new Todo("1", "Hello world", TodoStatus.OPEN),
                new Todo("2", "Lorem Ipsum", TodoStatus.IN_PROGRESS)
            )
         );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteById_whenTodoDoesntExist_thenThrowNotFoundException () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);

        // when & then
        try {
            todoService.deleteById("1");
            Assertions.fail();
        } catch (ResponseStatusException e) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void deleteById_when2Todos_thenDeleteCorrectTodo () {
        // given
        TodoRepository todoRepository = new TodoRepository();
        TodoService todoService = new TodoService(todoRepository);
        todoService.create(
                new Todo(null, "Hello world", TodoStatus.OPEN)
        );
        todoService.create(
                new Todo(null, "Lorem Ipsum", TodoStatus.IN_PROGRESS)
        );

        // when
        todoService.deleteById("1");

        // then
        List<Todo> actual = todoService.getAll();
        List<Todo> expected = new ArrayList<>(
            List.of(
                new Todo("2", "Lorem Ipsum", TodoStatus.IN_PROGRESS)
            )
        );

        Assertions.assertEquals(expected, actual);
    }
}