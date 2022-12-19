package neuefische.de.neuefischetodoaufgaberecap.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Todo {
    private String id;
    private String description;
    private String status;
}
