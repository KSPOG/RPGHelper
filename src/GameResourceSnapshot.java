public class GameResourceSnapshot {

    private final String energy;
    private final String blueShards;
    private final String voidShards;
    private final String sacredShards;
    private final String silver;
    private final String gems;
    private final String source;

    public GameResourceSnapshot(
            String energy,
            String blueShards,
            String voidShards,
            String sacredShards,
            String silver,
            String gems,
            String source
    ) {
        this.energy = energy;
        this.blueShards = blueShards;
        this.voidShards = voidShards;
        this.sacredShards = sacredShards;
        this.silver = silver;
        this.gems = gems;
        this.source = source;
    }

    public String getEnergy() {
        return energy;
    }

    public String getBlueShards() {
        return blueShards;
    }

    public String getVoidShards() {
        return voidShards;
    }

    public String getSacredShards() {
        return sacredShards;
    }

    public String getSilver() {
        return silver;
    }

    public String getGems() {
        return gems;
    }

    public String getSource() {
        return source;
    }

    public static GameResourceSnapshot demo() {
        return new GameResourceSnapshot("845 / 130", "9", "3", "25", "27,480,000", "1,320", "Demo values");
    }
}
