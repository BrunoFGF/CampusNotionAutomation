import java.time.LocalDateTime;

public class Task {
    private String name;
    private LocalDateTime dueDateTime;
    private String subject;
    private String url;

    public Task(String name, LocalDateTime dueDateTime, String subject, String url) {
        this.name = name;
        this.dueDateTime = dueDateTime;
        this.subject = subject;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueDate=" + dueDateTime +
                ", subject='" + subject + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
