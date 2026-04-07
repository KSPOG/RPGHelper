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
    private static final String ENERGY_REGION = "raid.region.energy";
    private static final String SILVER_REGION = "raid.region.silver";
    private static final String GEMS_REGION = "raid.region.gems";
    private static final String BLUE_SHARD_REGION = "raid.region.blueShard";
    private static final String VOID_SHARD_REGION = "raid.region.voidShard";
    private static final String SACRED_SHARD_REGION = "raid.region.sacredShard";
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
            settings.setEnergyRegion(ScreenRegion.parse(properties.getProperty(ENERGY_REGION, "")));
            settings.setSilverRegion(ScreenRegion.parse(properties.getProperty(SILVER_REGION, "")));
            settings.setGemsRegion(ScreenRegion.parse(properties.getProperty(GEMS_REGION, "")));
            settings.setBlueShardRegion(ScreenRegion.parse(properties.getProperty(BLUE_SHARD_REGION, "")));
            settings.setVoidShardRegion(ScreenRegion.parse(properties.getProperty(VOID_SHARD_REGION, "")));
            settings.setSacredShardRegion(ScreenRegion.parse(properties.getProperty(SACRED_SHARD_REGION, "")));
        } catch (IOException ignored) {
        }

        return settings;
    }

    public void save(AppSettings settings) throws IOException {
        Properties properties = new Properties();
        properties.setProperty(RAID_EXECUTABLE_PATH, settings.getRaidExecutablePath());
        properties.setProperty(AUTO_LAUNCH_ON_STARTUP, Boolean.toString(settings.isAutoLaunchRaidOnStartup()));
        properties.setProperty(ENERGY_REGION, serializeRegion(settings.getEnergyRegion()));
        properties.setProperty(SILVER_REGION, serializeRegion(settings.getSilverRegion()));
        properties.setProperty(GEMS_REGION, serializeRegion(settings.getGemsRegion()));
        properties.setProperty(BLUE_SHARD_REGION, serializeRegion(settings.getBlueShardRegion()));
        properties.setProperty(VOID_SHARD_REGION, serializeRegion(settings.getVoidShardRegion()));
        properties.setProperty(SACRED_SHARD_REGION, serializeRegion(settings.getSacredShardRegion()));
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

    private String serializeRegion(ScreenRegion region) {
        return region == null ? "" : region.serialize();
    }
}
