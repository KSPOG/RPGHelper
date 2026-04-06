public class ScreenCaptureGameResourceReader implements GameResourceReader {

    private final GameResourceReader fallbackReader;

    public ScreenCaptureGameResourceReader(GameResourceReader fallbackReader) {
        this.fallbackReader = fallbackReader;
    }

    @Override
    public GameResourceSnapshot readSnapshot() {
        GameResourceSnapshot fallback = fallbackReader.readSnapshot();
        return new GameResourceSnapshot(
                fallback.getEnergy(),
                fallback.getBlueShards(),
                fallback.getVoidShards(),
                fallback.getSacredShards(),
                fallback.getSilver(),
                fallback.getGems(),
                "Prepared for game window capture/OCR"
        );
    }

    @Override
    public String getReaderName() {
        return "Screen Capture Reader";
    }
}
