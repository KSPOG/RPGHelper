public class ScreenCaptureGameResourceReader implements GameResourceReader {

    private final GameResourceReader fallbackReader;
    private final AppSettings settings;
    private final RaidClientService raidClientService;

    public ScreenCaptureGameResourceReader(
            GameResourceReader fallbackReader,
            AppSettings settings,
            RaidClientService raidClientService
    ) {
        this.fallbackReader = fallbackReader;
        this.settings = settings;
        this.raidClientService = raidClientService;
    }

    @Override
    public GameResourceSnapshot readSnapshot() {
        GameResourceSnapshot fallback = fallbackReader.readSnapshot();
        String source;

        if (raidClientService.isRaidRunning()) {
            source = "Raid client detected. Next step: capture HUD regions and OCR the values.";
        } else if (settings.getRaidExecutablePath().isEmpty()) {
            source = "Raid executable not configured yet.";
        } else {
            source = "Raid configured, but the client is not running.";
        }

        return new GameResourceSnapshot(
                fallback.getEnergy(),
                fallback.getBlueShards(),
                fallback.getVoidShards(),
                fallback.getSacredShards(),
                fallback.getSilver(),
                fallback.getGems(),
                source
        );
    }

    @Override
    public String getReaderName() {
        return "Screen Capture Reader";
    }
}
