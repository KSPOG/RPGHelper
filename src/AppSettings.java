public class AppSettings {

    private String raidExecutablePath = "";
    private boolean autoLaunchRaidOnStartup;
    private ScreenRegion energyRegion;
    private ScreenRegion silverRegion;
    private ScreenRegion gemsRegion;

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
}
