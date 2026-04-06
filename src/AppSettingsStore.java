import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppSettingsStore {

    private static final String RAID_EXECUTABLE_PATH = "raid.executable.path";
    private static final String AUTO_LAUNCH_ON_STARTUP = "raid.auto.launch.on.startup";
    private static final Path LEGACY_SETTINGS_FILE = Paths.get(System.getProperty("user.home"), ".rpg-helper.properties");

    public AppSettings load() {
        AppSettings settings = new AppSettings();
        Properties properties = new Properties();
        Path settingsFile = getSettingsFilePath();

        try (InputStream inputStream = openSettingsInputStream(settingsFile)) {
            if (inputStream == null) {
                return settings;
            }
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
        Path settingsFile = getSettingsFilePath();

        Files.createDirectories(settingsFile.getParent());
        try (OutputStream outputStream = Files.newOutputStream(settingsFile)) {
            properties.store(outputStream, "RPG Helper settings");
        }
    }

    public String getSettingsFileLocation() {
        return getSettingsFilePath().toString();
    }

    private InputStream openSettingsInputStream(Path settingsFile) throws IOException {
        if (Files.exists(settingsFile)) {
            return Files.newInputStream(settingsFile);
        }

        if (Files.exists(LEGACY_SETTINGS_FILE)) {
            return Files.newInputStream(LEGACY_SETTINGS_FILE);
        }

        return null;
    }

    private Path getSettingsFilePath() {
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.trim().isEmpty()) {
            return Paths.get(appData, "RPGHelper", "settings.properties");
        }

        return Paths.get(System.getProperty("user.home"), "AppData", "Roaming", "RPGHelper", "settings.properties");
    }
}
