import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppLogService {

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final List<LogEntry> entries = new ArrayList<>();
    private final List<AppLogListener> listeners = new ArrayList<>();

    public void log(String message) {
        entries.add(0, new LogEntry(timeFormat.format(new Date()), message));
        for (AppLogListener listener : listeners) {
            listener.onLogChanged();
        }
    }

    public List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addListener(AppLogListener listener) {
        listeners.add(listener);
    }
}
