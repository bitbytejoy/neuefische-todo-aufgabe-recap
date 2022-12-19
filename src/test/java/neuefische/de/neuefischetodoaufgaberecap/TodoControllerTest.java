package neuefische.de.neuefischetodoaufgaberecap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void create_whenTodo_thenSuccessfullyCreateTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
                """)
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.content().json("""
                {
                    "id": "1",
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
                """,
                true
            )
        );
    }

    @Test
    void update_whenTodoExists_thenSuccessfullyUpdateTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
                """)
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "description": "Lorem ipsum updated",
                        "status": "IN_PROGRESS"
                    }
                """)
            ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json("""
                    {
                        "id": "1",
                        "description": "Lorem ipsum updated",
                        "status": "IN_PROGRESS"
                    }
                """,
                    true
                )
        );
    }

    @Test
    void update_whenTodoDoesntExist_thenCreateTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Hello Neufische",
                    "status": "OPEN"
                }
            """)
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk(),
            MockMvcResultMatchers.content().json("""
                {
                    "id": "1",
                    "description": "Hello Neufische",
                    "status": "OPEN"
                }
            """,
                true
            )
        );
    }

    @Test
    void update_when2Todos_thenUpdateCorrectTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
                """)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "description": "Hello world",
                    "status": "DONE"
                }
                """)
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "description": "Hello Neufische",
                    "status": "OPEN"
                }
            """)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
            .andExpectAll(
                MockMvcResultMatchers.content().json("""
                    [
                        {
                            "id": "1",
                            "description": "Hello Neufische",
                            "status": "OPEN"
                        },
                        {
                            "id": "2",
                            "description": "Hello world",
                            "status": "DONE"
                        }
                    ]
""")
        );
    }

    @Test
    void getAll_whenEmpty_thenReturnEmpty () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
            .andExpectAll(
                MockMvcResultMatchers.content().json("[]")
            );
    }

    @Test
    void getAll_whenOneTodo_thenReturnOneTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
            """)
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
            .andExpectAll(
                MockMvcResultMatchers.content().json("""
                    [
                        {
                            "id": "1",
                            "description": "Lorem ipsum",
                            "status": "OPEN"
                        }
                    ]
                """)
            );
    }

    @Test
    void getAll_whenTwoTodos_thenReturnTowTodos () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
            """)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Hello world",
                    "status": "DONE"
                }
            """)
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
            .andExpectAll(
                MockMvcResultMatchers.content().json("""
                    [
                        {
                            "id": "1",
                            "description": "Lorem ipsum",
                            "status": "OPEN"
                        },
                        {
                            "id": "2",
                            "description": "Hello world",
                            "status": "DONE"
                        }
                    ]
                """)
            );
    }

    @Test
    void deleteById_whenTodoDoesntExist_then404 () throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/todos/1")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteById_when2Todos_thenDeleteCorrectTodo () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Lorem ipsum",
                    "status": "OPEN"
                }
            """)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "description": "Hello world",
                    "status": "DONE"
                }
            """)
        );

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/todos/1")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todos"))
            .andExpectAll(
                MockMvcResultMatchers.content().json("""
                    [
                        {
                            "id": "2",
                            "description": "Hello world",
                            "status": "DONE"
                        }
                    ]
                """)
            );
    }
}