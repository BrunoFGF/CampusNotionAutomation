import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotionAPI implements INotionTaskRepository {
    private static final String NOTION_TOKEN = Config.getProperty("notion.token");
    private static final String DATABASE_ID = Config.getProperty("notion.database.id");
    private static final String NOTION_VERSION = Config.getProperty("notion.version");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public int addTaskToNotion(Task task) throws IOException {
        String createUrl = "https://api.notion.com/v1/pages";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(createUrl);

        httpPost.setHeader("Authorization", "Bearer " + NOTION_TOKEN);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Notion-Version", NOTION_VERSION);

        Map<String, Object> newPageData = new HashMap<>();
        newPageData.put("parent", Map.of("database_id", DATABASE_ID));

        Map<String, Object> properties = new HashMap<>();
        properties.put("Name", Map.of("title", List.of(Map.of("text", Map.of("content", task.getName())))));
        properties.put("Subject", Map.of("rich_text", List.of(Map.of("text", Map.of("content", MethodCapitalizeWord.CapitalizeWords(task.getSubject()))))));
        properties.put("Due Date", Map.of("type", "date", "date", Map.of("start", task.getDueDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))));
        properties.put("Link", Map.of("url", task.getUrl()));
        properties.put("Completed", Map.of("checkbox", false));

        newPageData.put("properties", properties);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(newPageData);
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);

        try (var response = httpClient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Body: " + new String(response.getEntity().getContent().readAllBytes()));
            return statusCode;
        }
    }

    public List<Map<String, Object>> getExistingTasks() throws IOException {
        String queryUrl = "https://api.notion.com/v1/databases/" + DATABASE_ID + "/query";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(queryUrl);

        httpPost.setHeader("Authorization", "Bearer " + NOTION_TOKEN);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Notion-Version", NOTION_VERSION);

        try (var response = httpClient.execute(httpPost)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());

            List<Map<String, Object>> tasks = new ArrayList<>();
            JsonNode results = jsonResponse.get("results");
            if (results.isArray()) {
                for (JsonNode result : results) {
                    Map<String, Object> task = objectMapper.convertValue(result, Map.class);
                    tasks.add(task);
                }
            }
            return tasks;
        }
    }

    public int updateTaskInNotion(String pageId, Task task) throws IOException {
        String updateUrl = "https://api.notion.com/v1/pages/" + pageId;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(updateUrl);

        httpPatch.setHeader("Authorization", "Bearer " + NOTION_TOKEN);
        httpPatch.setHeader("Content-Type", "application/json");
        httpPatch.setHeader("Notion-Version", NOTION_VERSION);

        Map<String, Object> properties = new HashMap<>();

        LocalDateTime localDateTime = task.getDueDateTime();
        String dueDateTimeUTC = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        properties.put("Due Date", Map.of("date", Map.of("start", dueDateTimeUTC)));

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("properties", properties);

        String json = objectMapper.writeValueAsString(updateData);
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPatch.setEntity(entity);

        try (var response = httpClient.execute(httpPatch)) {
            return response.getStatusLine().getStatusCode();
        }
    }
}
