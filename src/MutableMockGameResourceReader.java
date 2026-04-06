public class MutableMockGameResourceReader implements GameResourceReader {

    private GameResourceSnapshot snapshot = GameResourceSnapshot.demo();

    @Override
    public GameResourceSnapshot readSnapshot() {
        return snapshot;
    }

    @Override
    public String getReaderName() {
        return "Mutable Mock Reader";
    }

    public void updateSnapshot(
            String energy,
            String blueShards,
            String voidShards,
            String sacredShards,
            String silver,
            String gems,
            String source
    ) {
        snapshot = new GameResourceSnapshot(energy, blueShards, voidShards, sacredShards, silver, gems, source);
    }
}
