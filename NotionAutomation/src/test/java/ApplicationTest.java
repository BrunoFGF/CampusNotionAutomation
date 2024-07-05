import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ApplicationTest {
    @Test
    public void testAddFictitiousTasksToNotion() {
        List<Task> fictitiousTasks = Arrays.asList(
                new Task("Tarea 1", LocalDateTime.now().plusDays(1), "Matemáticas", "https://ejemplo.com/tarea1"),
                new Task("Tarea 2", LocalDateTime.now().plusDays(2), "Física", "https://ejemplo.com/tarea2"),
                new Task("Tarea 3", LocalDateTime.now().plusDays(3), "Química", "https://ejemplo.com/tarea3")
        );

        NotionAPI notionAPI = new NotionAPI();

        for (Task task : fictitiousTasks) {
            try {
                int statusCode = notionAPI.addTaskToNotion(task);
                System.out.println("Task added to Notion with status code: " + statusCode);
                assertEquals(200, statusCode, "Error adding fake task to notion: " + task.getName());
            } catch (IOException e) {
                fail("IOException occurred: " + e.getMessage());
            }
        }
    }
}
