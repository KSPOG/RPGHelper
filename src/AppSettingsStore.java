import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppSettingsStore {

    private static final String SETTINGS_FILE = System.getProperty("user.home") + "\\.rpg-helper.properties";
    private static final String RAID_EXECUTABLE_PATH = "raid.executable.path";
    private static final String AUTO_LAUNCH_ON_STARTUP = "raid.auto.launch.on.startup";

    public AppSettings load() {
        AppSettings settings = new AppSettings();
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(SETTINGS_FILE)) {
            properties.load(inputStream);
            settings.setRaidExecutablePath(properties.getProperty(RAID_EXECUTABLE_PATH, ""));
            settings.setAutoLaunchRaidOnStartup(Boolean.parseBoolean(properties.getProperty(AUTO_LAUNCH_ON_STARTUP, "false")));
        } catch (IOException ignored) {
        }

        return settings;
    }

    public void save(AppSettings settings) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(RAID_EXECUTABLE_PATH, settings.getRaidExecutablePath());
        properties.setProperty(AUTO_LAUNCH_ON_STARTUP, Boolean.toString(settings.isAutoLaunchRaidOnStartup()));

        try (FileOutputStream outputStream = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(outputStream, "RPG Helper settings");
        }
    }
}
