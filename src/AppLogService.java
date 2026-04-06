import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppLogService {

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final List<LogEntry> entries = new ArrayList<>();

    public void log(String message) {
        entries.add(0, new LogEntry(timeFormat.format(new Date()), message));
    }

    public List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }
}
