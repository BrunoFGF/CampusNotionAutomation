import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        CampusScraper scraper = new CampusScraper();
        List<Task> tasks = scraper.scrapeTasks();
        NotionAPI notionAPI = new NotionAPI();

        try {
            List<Map<String, Object>> existingTasks = notionAPI.getExistingTasks();
            Map<String, Map<String, Object>> existingTaskMap = new HashMap<>();

            for (Map<String, Object> task : existingTasks) {
                System.out.println("Existing Task: " + task);
                String url = (String) ((Map<String, Object>) ((Map<String, Object>) task.get("properties")).get("Link")).get("url");
                existingTaskMap.put(url, task);
            }

            for (Task task : tasks) {
                Map<String, Object> existingTask = existingTaskMap.get(task.getUrl());

                if (existingTask != null) {
                    Map<String, Object> dueDateMap = (Map<String, Object>) ((Map<String, Object>) existingTask.get("properties")).get("Due Date");
                    Map<String, Object> dateMap = (Map<String, Object>) dueDateMap.get("date");
                    String existingDueDate = (String) dateMap.get("start");

                    if (existingDueDate != null) {
                        String newDueDate = task.getDueDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        String existingDueDateSimplified = existingDueDate.substring(0, 19);
                        String newDueDateSimplified = newDueDate.substring(0, 19);
                        System.out.println("Comparing existing due date: " + existingDueDateSimplified + " with new due date: " + newDueDateSimplified);

                        if (!existingDueDateSimplified.equals(newDueDateSimplified)) {
                            String pageId = (String) existingTask.get("id");
                            int statusCode = notionAPI.updateTaskInNotion(pageId, task);
                            System.out.println("Task updated in Notion with status code: " + statusCode);
                        } else {
                            System.out.println("No update needed for task: " + task.getName());
                        }
                    } else {
                        System.out.println("Existing task does not have a due date: " + task.getName());
                    }

                } else {
                    int statusCode = notionAPI.addTaskToNotion(task);
                    System.out.println("Task added to Notion with status code: " + statusCode);
                }
            }

        } catch (IOException e) {
            System.err.println("Error interacting with Notion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
