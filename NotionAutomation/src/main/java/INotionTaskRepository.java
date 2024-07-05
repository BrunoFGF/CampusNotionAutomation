import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface INotionTaskRepository  {
    List<Map<String, Object>> getExistingTasks() throws IOException;
    int addTaskToNotion(Task task) throws IOException;
    int updateTaskInNotion(String pageId, Task task) throws IOException;
}
