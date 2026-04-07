public class AppSettings {

    private String raidExecutablePath = "";
    private boolean autoLaunchRaidOnStartup;
    private ScreenRegion energyRegion;
    private ScreenRegion silverRegion;
    private ScreenRegion gemsRegion;
    private ScreenRegion blueShardRegion;
    private ScreenRegion voidShardRegion;
    private ScreenRegion sacredShardRegion;

    public String getRaidExecutablePath() {
        return raidExecutablePath;
    }

    public void setRaidExecutablePath(String raidExecutablePath) {
        this.raidExecutablePath = raidExecutablePath == null ? "" : raidExecutablePath.trim();
    }

    public boolean isAutoLaunchRaidOnStartup() {
        return autoLaunchRaidOnStartup;
    }

    public void setAutoLaunchRaidOnStartup(boolean autoLaunchRaidOnStartup) {
        this.autoLaunchRaidOnStartup = autoLaunchRaidOnStartup;
    }

    public ScreenRegion getEnergyRegion() {
        return energyRegion;
    }

    public void setEnergyRegion(ScreenRegion energyRegion) {
        this.energyRegion = energyRegion;
    }

    public ScreenRegion getSilverRegion() {
        return silverRegion;
    }

    public void setSilverRegion(ScreenRegion silverRegion) {
        this.silverRegion = silverRegion;
    }

    public ScreenRegion getGemsRegion() {
        return gemsRegion;
    }

    public void setGemsRegion(ScreenRegion gemsRegion) {
        this.gemsRegion = gemsRegion;
    }

    public ScreenRegion getBlueShardRegion() {
        return blueShardRegion;
    }

    public void setBlueShardRegion(ScreenRegion blueShardRegion) {
        this.blueShardRegion = blueShardRegion;
    }

    public ScreenRegion getVoidShardRegion() {
        return voidShardRegion;
    }

    public void setVoidShardRegion(ScreenRegion voidShardRegion) {
        this.voidShardRegion = voidShardRegion;
    }

    public ScreenRegion getSacredShardRegion() {
        return sacredShardRegion;
    }

    public void setSacredShardRegion(ScreenRegion sacredShardRegion) {
        this.sacredShardRegion = sacredShardRegion;
    }
}
